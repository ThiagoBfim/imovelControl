package br.com.imovelcontrol.repository.helper.imovel;

import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import br.com.imovelcontrol.dto.*;
import br.com.imovelcontrol.model.Imovel;
import br.com.imovelcontrol.model.Usuario;
import br.com.imovelcontrol.repository.util.PaginacaoUtil;
import br.com.imovelcontrol.service.UsuarioLogadoService;
import org.hibernate.Criteria;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.hibernate.type.BigDecimalType;
import org.hibernate.type.BooleanType;
import org.hibernate.type.DateType;
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

    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    @Override
    public Page<Imovel> filtrar(Imovel filtro, Usuario usuario, Pageable pageable) {
        Criteria criteria = entityManager.unwrap(Session.class).createCriteria(Imovel.class);
        adicionarFiltro(filtro, usuario, criteria);
        paginacaoUtil.paginacao(pageable, criteria);
        return new PageImpl<>(criteria.list(), pageable, total(filtro, usuario));
    }

    private Long total(Imovel filtro, Usuario usuario) {
        Criteria criteria = entityManager.unwrap(Session.class).createCriteria(Imovel.class);
        adicionarFiltro(filtro, usuario, criteria);
        criteria.setProjection(Projections.rowCount());
        return (Long) criteria.uniqueResult();
    }

    private void adicionarFiltro(Imovel filtro, Usuario usuario, Criteria criteria) {
        criteria.add(Restrictions.eq("excluido", Boolean.FALSE));
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
        }
    }

    @Transactional(readOnly = true)
    @Override
    public List<RelatorioImovelDTO> retrieveRelatorioImovelDTO(PeriodoRelatorioDTO periodoRelatorioDTO) {


        StringBuilder sql = new StringBuilder("SELECT imovel.nome as nome, imovel.cep as cep, "
                + " SUM(CASE WHEN informacaoPagamento.pago = 1 then informacaoPagamento.valor else 0 end) as recebimento,"
                + " SUM(tabelaGastos.valorGasto) as gastos "
                + " FROM  INFORMACAO_PAGAMENTO informacaoPagamento"
                + " INNER JOIN ALUGUEL aluguel"
                + " on informacaoPagamento.codigo_aluguel = aluguel.codigo"
                + " INNER JOIN IMOVEL imovel "
                + " on aluguel.codigo_imovel = imovel.codigo "
                + " LEFT JOIN (select gastoAdicional.codPagamento as codPagamento, "
                + "            gastoAdicional.valorGasto as valorGasto"
                + "               FROM GASTO_ADICIONAL gastoAdicional"
                + "               INNER JOIN INFORMACAO_PAGAMENTO pagamento"
                + "               ON gastoAdicional.codPagamento = pagamento.codigo"
                + "               GROUP BY gastoAdicional.codPagamento, gastoAdicional.valorGasto"
                + "            ) as tabelaGastos on tabelaGastos.codPagamento = informacaoPagamento.codigo "
                + " WHERE imovel.codigo_usuario = :donoImovel "
                + " AND imovel.excluido = 0 "
                + " AND aluguel.excluido = 0 "
        );

        SQLQuery sqlQuery = appendFiltros(periodoRelatorioDTO, sql, false);

        sqlQuery.setResultTransformer(Transformers.aliasToBean(RelatorioImovelDTO.class));
        sqlQuery.addScalar("nome", StringType.INSTANCE)
                .addScalar("cep", StringType.INSTANCE)
                .addScalar("recebimento", BigDecimalType.INSTANCE)
                .addScalar("gastos", BigDecimalType.INSTANCE);
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
                + " WHERE imovel.codigo_usuario = :donoImovel "
                + " AND imovel.excluido = 0 "
                + " AND aluguel.excluido = 0 ";

        SQLQuery sqlQuery = entityManager.createNativeQuery(sql).unwrap(SQLQuery.class);
        sqlQuery.setParameter("donoImovel", usuarioLogadoService.getUsuario());
        return sqlQuery.uniqueResult() != null ? new Date(((java.sql.Date) sqlQuery.uniqueResult()).getTime())
                : null;
    }


    @Transactional(readOnly = true)
    @Override
    public List<RelatorioDetalhadoImovelDTO> retrieveRelatorioDetalhadoImovelDTO(PeriodoRelatorioDTO periodoRelatorioDTO) {

        String sql = "SELECT imovel.nome as nome, imovel.cep as cep, "
                + " aluguel.nome as nomeAluguel, aluguel.codigo as codigoAluguel, loc.excluido as estaAlugado"
                + " FROM  IMOVEL imovel"
                + " INNER JOIN ALUGUEL aluguel on aluguel.codigo_imovel = imovel.codigo  "
                + " LEFT JOIN locatario loc on loc.codigo_aluguel = aluguel.codigo "
                + " WHERE imovel.codigo_usuario = :donoImovel "
                + " AND imovel.codigo =:imovel "
                + " AND imovel.excluido = 0"
                + " AND aluguel.excluido = 0 ";
        SQLQuery sqlQuery = entityManager.createNativeQuery(sql).unwrap(SQLQuery.class);
        sqlQuery.setParameter("donoImovel", usuarioLogadoService.getUsuario());
        sqlQuery.setParameter("imovel", periodoRelatorioDTO.getImovel());

        sqlQuery.setResultTransformer(Transformers.aliasToBean(RelatorioDetalhadoImovelDTO.class));
        sqlQuery.addScalar("nome", StringType.INSTANCE)
                .addScalar("cep", StringType.INSTANCE)
                .addScalar("codigoAluguel", LongType.INSTANCE)
                .addScalar("nomeAluguel", StringType.INSTANCE)
                .addScalar("estaAlugado", BooleanType.INSTANCE);

        List<RelatorioDetalhadoImovelDTO> listaDetalhadoImovelDTOS = sqlQuery.list();
        if (!CollectionUtils.isEmpty(listaDetalhadoImovelDTOS)) {
            listaDetalhadoImovelDTOS.forEach(l ->
                    l.setSubRelatorioDetalhadoImovelDTOS(retrieveSubRelatorioDetalhado(periodoRelatorioDTO,
                            l.getCodigoAluguel())));
        }
        return listaDetalhadoImovelDTOS;
    }

    private List<SubRelatorioDetalhadoImovelDTO> retrieveSubRelatorioDetalhado(PeriodoRelatorioDTO periodoRelatorioDTO,
            Long codigoAluguel) {
        StringBuilder sql = new StringBuilder("SELECT informacaoPagamento.aguaInclusa as aguaInclusa, "
                + " informacaoPagamento.internetInclusa as internetInclusa, informacaoPagamento.pago as pago, "
                + " informacaoPagamento.codigo as codigoPagamento, "
                + " informacaoPagamento.iptuIncluso as iptuIncluso, "
                + " informacaoPagamento.possuiCondominio as possuiCondominio, "
                + " informacaoPagamento.luzInclusa as luzInclusa, "
                + " informacaoPagamento.valor as valorAluguel "
                + " ,informacaoPagamento.dataMensal as dataMensal"
                + " FROM  INFORMACAO_PAGAMENTO informacaoPagamento"
                + " INNER JOIN ALUGUEL aluguel on informacaoPagamento.codigo_aluguel = aluguel.codigo  "
                + " INNER JOIN IMOVEL imovel  on aluguel.codigo_imovel = imovel.codigo"
                + " WHERE imovel.codigo_usuario = :donoImovel "
                + " AND aluguel.codigo = :codigoAluguel"
        );

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
                .addScalar("pago", BooleanType.INSTANCE);
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


    private SQLQuery appendFiltros(PeriodoRelatorioDTO periodoRelatorioDTO, StringBuilder sql, boolean detalhar) {
        sql.append(" AND  informacaoPagamento.dataMensal >= :dataInicio ");
        sql.append("  AND  informacaoPagamento.dataMensal <= :dataFim ");

        if (periodoRelatorioDTO.getImovel() != null) {
            sql.append(" AND  imovel.nome like :nome");
        }

        sql.append("  GROUP BY  imovel.nome, imovel.cep, imovel.codigo_usuario ");
        if (detalhar) {
            sql.append(" , informacaoPagamento.dataMensal , informacaoPagamento.codigo "
                    + " ORDER BY imovel.nome, aluguel.nome, informacaoPagamento.dataMensal");
        }

        SQLQuery sqlQuery = entityManager.createNativeQuery(sql.toString()).unwrap(SQLQuery.class);
        sqlQuery.setParameter("donoImovel", usuarioLogadoService.getUsuario());
        sqlQuery.setParameter("dataFim", periodoRelatorioDTO.getDataFim());
        sqlQuery.setParameter("dataInicio", periodoRelatorioDTO.getDataInicio());

        if (periodoRelatorioDTO.getImovel() != null) {
            sqlQuery.setParameter("nome", '%' + periodoRelatorioDTO.getImovel().getNome() + '%');
        }
        return sqlQuery;
    }

    /**
     *
     * @return
     */
    @Transactional(readOnly = true)
    @Override
    public List<GraficoColunaImovelDTO> retrieveGraficoColunaDTO() {

        StringBuilder sql = new StringBuilder("SELECT  imovel.nome as nome," +
                " tabela.mes as mes," +
                " SUM(tabela.valor) as valor" +
                " FROM usuario u, imovel imovel" +
                " INNER JOIN(" +
                "   SELECT  i.valor - case when valorPG.valor is null then 0 else valorPG.valor end as valor," +
                "   MONTH(i.dataMensal) as mes, YEAR(i.dataMensal),  a.codigo_imovel as codigo_imovel" +
                "   FROM informacao_pagamento i" +
                "   INNER JOIN aluguel a" +
                "   ON a.codigo = i.codigo_aluguel" +
                "   LEFT JOIN (" +
                "       SELECT  SUM(g.valorGasto) valor, ia.codigo " +
                "       FROM gasto_adicional g" +
                "       LEFT JOIN informacao_pagamento ia " +
                "       ON g.codPagamento = ia.codigo" +
                "       LEFT JOIN aluguel a2" +
                "       ON a2.codigo = ia.codigo_aluguel" +
                "       GROUP BY  ia.codigo, MONTH(ia.dataMensal)" +
                "   ) valorPG " +
                "   ON valorPG.codigo = i.codigo" +
                "   GROUP BY MONTH(i.dataMensal), i.valor, valorPG.valor,  a.codigo_imovel, i.dataMensal" +
                " ) as tabela" +
                " ON tabela.codigo_imovel = imovel.codigo AND imovel.codigo_usuario = :donoImovel" +
                " GROUP BY tabela.mes, imovel.nome" +
                " ORDER BY tabela.mes;"
        );

        SQLQuery sqlQuery = entityManager.createNativeQuery(sql.toString()).unwrap(SQLQuery.class);
        sqlQuery.setParameter("donoImovel", usuarioLogadoService.getUsuario());

        sqlQuery.setResultTransformer(Transformers.aliasToBean(GraficoColunaImovelDTO.class));
        sqlQuery.addScalar("nome", StringType.INSTANCE)
                .addScalar("mes", StringType.INSTANCE)
                .addScalar("valor", BigDecimalType.INSTANCE);
        return sqlQuery.list();
    }



}
