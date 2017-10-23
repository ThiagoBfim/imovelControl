package br.com.imovelcontrol.repository.helper.imovel;

import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import br.com.imovelcontrol.dto.GastosDetalhadoDTO;
import br.com.imovelcontrol.dto.GraficoColunaImovelDTO;
import br.com.imovelcontrol.dto.PeriodoRelatorioDTO;
import br.com.imovelcontrol.dto.RelatorioDetalhadoImovelDTO;
import br.com.imovelcontrol.dto.RelatorioImovelDTO;
import br.com.imovelcontrol.dto.SubRelatorioDetalhadoImovelDTO;
import br.com.imovelcontrol.model.Imovel;
import br.com.imovelcontrol.model.Usuario;
import br.com.imovelcontrol.repository.util.PaginacaoUtil;
import br.com.imovelcontrol.service.UsuarioLogadoService;
import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.hibernate.type.BigDecimalType;
import org.hibernate.type.BooleanType;
import org.hibernate.type.DateType;
import org.hibernate.type.IntegerType;
import org.hibernate.type.LongType;
import org.hibernate.type.StringType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

public class ImoveisImpl implements ImoveisQuerys {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private PaginacaoUtil paginacaoUtil;

    @Autowired
    private UsuarioLogadoService usuarioLogadoService;


    @Transactional(readOnly = true)
    @Override
    public Imovel findOneWithAluguelByUsuarioAndCodigo(Long codigo, Usuario usuario) {
        Criteria criteria = entityManager.unwrap(Session.class).createCriteria(Imovel.class);
        criteria.add(Restrictions.eq("donoImovel", usuario));
        criteria.add(Restrictions.eq("codigo", codigo));

        Imovel imovel = (Imovel) criteria.uniqueResult();
        if(imovel != null) {
            Hibernate.initialize(imovel.getAluguelList());
        }
        return imovel;
    }

    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    @Override
    public Page<Imovel> filtrar(Imovel filtro, Usuario usuario, Pageable pageable) {

        Criteria criteria = entityManager.unwrap(Session.class).createCriteria(Imovel.class);
        adicionarFiltro(filtro, usuario, criteria);
        paginacaoUtil.paginacao(pageable, criteria);
        if(StringUtils.isEmpty(pageable.getSort())){
            criteria.addOrder(Order.asc("excluido"));
            criteria.addOrder(Order.asc("nome"));
        }
        List<Imovel> filtrados = criteria.list();
        filtrados.forEach(u -> Hibernate.initialize(u.getAluguelList()));
        return new PageImpl<>(criteria.list(), pageable, total(filtro, usuario));
    }

    private Long total(Imovel filtro, Usuario usuario) {
        Criteria criteria = entityManager.unwrap(Session.class).createCriteria(Imovel.class);
        adicionarFiltro(filtro, usuario, criteria);
        criteria.setProjection(Projections.rowCount());
        return (Long) criteria.uniqueResult();
    }

    private void adicionarFiltro(Imovel filtro, Usuario usuario, Criteria criteria) {
        if (usuario != null) {
            criteria.add(Restrictions.eq("donoImovel", usuario));
        }
        if (filtro != null) {
            if (!StringUtils.isEmpty(filtro.getNome())) {
                criteria.add(Restrictions.ilike("nome", filtro.getNome(), MatchMode.ANYWHERE));
            }
            if (filtro.getEndereco() != null) {
                if (!StringUtils.isEmpty(filtro.getEndereco().getCep())) {
                    criteria.add(Restrictions.ilike("endereco.cep", "%"
                            + filtro.getEndereco().getCep() + "%"));
                }
                if (!StringUtils.isEmpty(filtro.getEndereco().getCidade())) {
                    criteria.add(Restrictions.ilike("endereco.cidade", "%"
                            + filtro.getEndereco().getCidade() + "%"));
                }
                if (!StringUtils.isEmpty(filtro.getEndereco().getBairro())) {
                    criteria.add(Restrictions.ilike("endereco.bairro", "%"
                            + filtro.getEndereco().getBairro() + "%"));
                }
            }
            if (!filtro.isMostrarExcluidos()) {
                criteria.add(Restrictions.eq("excluido", Boolean.FALSE));
            }
        } else {
            criteria.add(Restrictions.eq("excluido", Boolean.FALSE));
        }
    }

    @Transactional(readOnly = true)
    @Override
    public List<RelatorioImovelDTO> retrieveRelatorioImovelDTO(PeriodoRelatorioDTO periodoRelatorioDTO) {


        StringBuilder sql = new StringBuilder("SELECT imovel.nome as nome, imovel.cep as cep, "
                + " SUM(CASE WHEN informacaoPagamento.pago = 1 then informacaoPagamento.valor else 0 end) as recebimento,"
                + " SUM(tabelaGastos.valorGasto) as gastos , imovel.excluido as excluido"
                + " FROM  INFORMACAO_PAGAMENTO informacaoPagamento"
                + " INNER JOIN ALUGUEL aluguel"
                + " on informacaoPagamento.codigo_aluguel = aluguel.codigo"
                + " INNER JOIN IMOVEL imovel "
                + " on aluguel.codigo_imovel = imovel.codigo "
                + " LEFT JOIN (select gastoAdicional.codPagamento as codPagamento, "
                + "            SUM(gastoAdicional.valorGasto) as valorGasto"
                + "               FROM GASTO_ADICIONAL gastoAdicional"
                + "               INNER JOIN INFORMACAO_PAGAMENTO pagamento"
                + "               ON gastoAdicional.codPagamento = pagamento.codigo"
                + "               GROUP BY gastoAdicional.codPagamento"
                + "            ) as tabelaGastos on tabelaGastos.codPagamento = informacaoPagamento.codigo "
                + " WHERE imovel.codigo_usuario = :donoImovel "
        );

        SQLQuery sqlQuery = appendFiltros(periodoRelatorioDTO, sql, false);

        sqlQuery.setResultTransformer(Transformers.aliasToBean(RelatorioImovelDTO.class));
        sqlQuery.addScalar("nome", StringType.INSTANCE)
                .addScalar("cep", StringType.INSTANCE)
                .addScalar("recebimento", BigDecimalType.INSTANCE)
                .addScalar("gastos", BigDecimalType.INSTANCE)
                .addScalar("excluido", BooleanType.INSTANCE);
        return sqlQuery.list();
    }

    @Transactional(readOnly = true)
    @Override
    public Date retrieveMinDataMensalPagamento() {

        String sql = "SELECT MIN(informacaoPagamento.dataMensal) "
                + " FROM  INFORMACAO_PAGAMENTO informacaoPagamento"
                + " INNER JOIN ALUGUEL aluguel"
                + " on informacaoPagamento.codigo_aluguel = aluguel.codigo"
                + " INNER JOIN IMOVEL imovel "
                + " on aluguel.codigo_imovel = imovel.codigo "
                + " WHERE imovel.codigo_usuario = :donoImovel ";

        SQLQuery sqlQuery = entityManager.createNativeQuery(sql).unwrap(SQLQuery.class);
        sqlQuery.setParameter("donoImovel", usuarioLogadoService.getUsuario());
        return sqlQuery.uniqueResult() != null ? new Date(((java.sql.Date) sqlQuery.uniqueResult()).getTime())
                : null;
    }


    @Transactional(readOnly = true)
    @Override
    public List<RelatorioDetalhadoImovelDTO> retrieveRelatorioDetalhadoImovelDTO(PeriodoRelatorioDTO periodoRelatorioDTO) {

        String sql = "SELECT DISTINCT imovel.nome as nome, imovel.cep as cep, "
                + " aluguel.nome as nomeAluguel, aluguel.codigo as codigoAluguel,"
                + " locatarioTemp.excluido as estaDesalugado, aluguel.excluido as excluido"
                + " FROM  IMOVEL imovel"
                + " INNER JOIN ALUGUEL aluguel on aluguel.codigo_imovel = imovel.codigo  "
                + " LEFT JOIN (Select loc.excluido, loc.codigo_aluguel from locatario loc "
                + " where loc.dataFim is null group by loc.codigo_aluguel) "
                + " as locatarioTemp on locatarioTemp.codigo_aluguel = aluguel.codigo "
                + " WHERE imovel.codigo_usuario = :donoImovel "
                + " AND imovel.codigo =:imovel ";
        SQLQuery sqlQuery = entityManager.createNativeQuery(sql).unwrap(SQLQuery.class);
        sqlQuery.setParameter("donoImovel", usuarioLogadoService.getUsuario());
        sqlQuery.setParameter("imovel", periodoRelatorioDTO.getImovel());

        sqlQuery.setResultTransformer(Transformers.aliasToBean(RelatorioDetalhadoImovelDTO.class));
        sqlQuery.addScalar("nome", StringType.INSTANCE)
                .addScalar("cep", StringType.INSTANCE)
                .addScalar("codigoAluguel", LongType.INSTANCE)
                .addScalar("nomeAluguel", StringType.INSTANCE)
                .addScalar("excluido", BooleanType.INSTANCE)
                .addScalar("estaDesalugado", BooleanType.INSTANCE);

        List<RelatorioDetalhadoImovelDTO> listaDetalhadoImovelDTOS = sqlQuery.list();
        if (!CollectionUtils.isEmpty(listaDetalhadoImovelDTOS)) {
            listaDetalhadoImovelDTOS.forEach(l ->
                    l.setSubRelatorioDetalhadoImovelDTOS(retrieveSubRelatorioDetalhado(periodoRelatorioDTO,
                            l.getCodigoAluguel())));
        }
        return listaDetalhadoImovelDTOS;
    }

    private List<GastosDetalhadoDTO> retrieveGastosByCodigoPagamento(Long codigoPagamento) {
        String sql = "SELECT gasto.valorGasto as gasto, gasto.comentarioGasto as descricao "
                + " FROM GASTO_ADICIONAL gasto"
                + " WHERE gasto.codPagamento = :codigoPagamento";
        SQLQuery sqlQuery = entityManager.createNativeQuery(sql).unwrap(SQLQuery.class);
        sqlQuery.setParameter("codigoPagamento", codigoPagamento);
        sqlQuery.setResultTransformer(Transformers.aliasToBean(GastosDetalhadoDTO.class));
        sqlQuery.addScalar("gasto", BigDecimalType.INSTANCE)
                .addScalar("descricao", StringType.INSTANCE);
        return sqlQuery.list();
    }

    private List<SubRelatorioDetalhadoImovelDTO> retrieveSubRelatorioDetalhado(PeriodoRelatorioDTO periodoRelatorioDTO,
            Long codigoAluguel) {
        StringBuilder sql = new StringBuilder("SELECT informacaoPagamento.aguaInclusa as aguaInclusa, "
                + " informacaoPagamento.internetInclusa as internetInclusa, informacaoPagamento.pago as pago, "
                + " locatario.nome as nome, "
                + " informacaoPagamento.codigo as codigoPagamento, "
                + " informacaoPagamento.iptuIncluso as iptuIncluso, "
                + " informacaoPagamento.possuiCondominio as possuiCondominio, "
                + " informacaoPagamento.luzInclusa as luzInclusa, "
                + " informacaoPagamento.valor as valorAluguel "
                + " ,informacaoPagamento.dataMensal as dataMensal"
                + " FROM  INFORMACAO_PAGAMENTO informacaoPagamento"
                + " INNER JOIN ALUGUEL aluguel ON informacaoPagamento.codigo_aluguel = aluguel.codigo  "
                + " INNER JOIN IMOVEL imovel  ON aluguel.codigo_imovel = imovel.codigo"
                + " LEFT JOIN locatario locatario ON locatario.codigo_aluguel = aluguel.codigo "
                + " AND informacaoPagamento.dataMensal BETWEEN  locatario.dataInicio "
                + " AND CASE WHEN locatario.dataFim IS NULL THEN NOW() else locatario.dataFim END"
                + " WHERE imovel.codigo_usuario = :donoImovel "
                + " AND aluguel.codigo = :codigoAluguel"
        );

        periodoRelatorioDTO.setMostrarExcluidos(Boolean.TRUE);
        SQLQuery sqlQuery = appendFiltros(periodoRelatorioDTO, sql, true);
        sqlQuery.setParameter("codigoAluguel", codigoAluguel);

        sqlQuery.setResultTransformer(Transformers.aliasToBean(SubRelatorioDetalhadoImovelDTO.class));
        sqlQuery.addScalar("aguaInclusa", BooleanType.INSTANCE)
                .addScalar("internetInclusa", BooleanType.INSTANCE)
                .addScalar("iptuIncluso", BooleanType.INSTANCE)
                .addScalar("possuiCondominio", BooleanType.INSTANCE)
                .addScalar("luzInclusa", BooleanType.INSTANCE)
                .addScalar("dataMensal", DateType.INSTANCE)
                .addScalar("valorAluguel", BigDecimalType.INSTANCE)
                .addScalar("codigoPagamento", LongType.INSTANCE)
                .addScalar("pago", BooleanType.INSTANCE)
                .addScalar("nome", StringType.INSTANCE);
        List<SubRelatorioDetalhadoImovelDTO> subRelatorioDetalhadoImovelDTOS = sqlQuery.list();

        if (!CollectionUtils.isEmpty(subRelatorioDetalhadoImovelDTOS)) {
            subRelatorioDetalhadoImovelDTOS.forEach(sreport -> {
                sreport.setListaGastos(retrieveGastosByCodigoPagamento(sreport.getCodigoPagamento()));

                //Logica para adicionar um elemento vazio no relatorio.
                if (CollectionUtils.isEmpty(sreport.getListaGastos())) {
                    sreport.getListaGastos().add(new GastosDetalhadoDTO());
                }
            });

        }

        return subRelatorioDetalhadoImovelDTOS;
    }


    private SQLQuery appendFiltros(PeriodoRelatorioDTO periodoRelatorioDTO, StringBuilder sql, boolean detalhar) {
        sql.append(" AND  informacaoPagamento.dataMensal >= :dataInicio ");
        sql.append("  AND  informacaoPagamento.dataMensal <= :dataFim ");

        if (periodoRelatorioDTO.getImovel() != null) {
            sql.append(" AND  imovel.codigo = :id");
        }
        if (!periodoRelatorioDTO.isMostrarExcluidos()) {
            sql.append(" AND imovel.excluido = 0 AND aluguel.excluido = 0 ");
        }
        sql.append("  GROUP BY  imovel.nome, imovel.cep, imovel.codigo_usuario, imovel.excluido ");
        if (detalhar) {
            sql.append(" ,locatario.nome, informacaoPagamento.dataMensal , informacaoPagamento.codigo "
                    + " ORDER BY imovel.nome, aluguel.nome, informacaoPagamento.dataMensal");
        }


        SQLQuery sqlQuery = entityManager.createNativeQuery(sql.toString()).unwrap(SQLQuery.class);
        sqlQuery.setParameter("donoImovel", usuarioLogadoService.getUsuario());
        sqlQuery.setParameter("dataFim", periodoRelatorioDTO.getDataFim());
        sqlQuery.setParameter("dataInicio", periodoRelatorioDTO.getDataInicio());

        if (periodoRelatorioDTO.getImovel() != null) {
            sqlQuery.setParameter("id", periodoRelatorioDTO.getImovel().getCodigo());
        }
        return sqlQuery;
    }

    /**
     * @return
     */
    @Transactional(readOnly = true)
    @Override
    public List<GraficoColunaImovelDTO> retrieveGraficoColunaDTO() {

        StringBuilder sql = new StringBuilder("SELECT  imovel.nome as nome,"
                + " tabela.mes as mes,"
                + " tabela.valor as valor"
                + " FROM IMOVEL imovel"
                + " INNER JOIN("
                + "   SELECT  SUM(CASE WHEN i.pago = 1  THEN (i.valor - case when valorPG.valor "
                + "   is null then 0 else valorPG.valor END)"
                + "   ELSE - case when valorPG.valor is null then 0 else valorPG.valor END END) as valor,"
                + "   MONTH(i.dataMensal) as mes,  a.codigo_imovel as codigo_imovel"
                + "   FROM INFORMACAO_PAGAMENTO i"
                + "   INNER JOIN ALUGUEL a"
                + "   ON a.codigo = i.codigo_aluguel"
                + "   LEFT JOIN ("
                + "       SELECT  SUM(g.valorGasto) valor, ia.codigo as codigo,  ia.codigo_aluguel as codigoAluguel"
                + "       FROM GASTO_ADICIONAL g"
                + "       LEFT JOIN INFORMACAO_PAGAMENTO ia "
                + "       ON g.codPagamento = ia.codigo"
                + "       GROUP BY  ia.codigo, MONTH(ia.dataMensal),  ia.codigo_aluguel"
                + "   ) valorPG "
                + "   ON valorPG.codigo = i.codigo AND valorPG.codigoAluguel = i.codigo_aluguel"
                + "   WHERE a.excluido = 0"
                + "   GROUP BY MONTH(i.dataMensal), a.codigo_imovel"
                + " ) as tabela"
                + " ON tabela.codigo_imovel = imovel.codigo"
                + " WHERE  mes >= (MONTH(now()) - 4)"
                + " AND imovel.excluido = 0 "
                + " AND imovel.codigo_usuario = :donoImovel"
                + " GROUP BY tabela.mes, imovel.nome, tabela.valor"
                + " ORDER BY tabela.mes;"
        );

        SQLQuery sqlQuery = entityManager.createNativeQuery(sql.toString()).unwrap(SQLQuery.class);
        sqlQuery.setParameter("donoImovel", usuarioLogadoService.getUsuario());

        sqlQuery.setResultTransformer(Transformers.aliasToBean(GraficoColunaImovelDTO.class));
        sqlQuery.addScalar("nome", StringType.INSTANCE)
                .addScalar("mes", IntegerType.INSTANCE)
                .addScalar("valor", BigDecimalType.INSTANCE);
        return sqlQuery.list();
    }


}
