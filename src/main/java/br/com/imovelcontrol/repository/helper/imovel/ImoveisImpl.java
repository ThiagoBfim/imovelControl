package br.com.imovelcontrol.repository.helper.imovel;

import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import br.com.imovelcontrol.dto.GastosDetalhadoDTO;
import br.com.imovelcontrol.dto.PeriodoRelatorioDTO;
import br.com.imovelcontrol.dto.RelatorioDetalhadoImovelDTO;
import br.com.imovelcontrol.dto.RelatorioImovelDTO;
import br.com.imovelcontrol.dto.SubRelatorioDetalhadoImovelDTO;
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
                + " FROM  informacao_pagamento informacaoPagamento"
                + " INNER JOIN aluguel aluguel"
                + " on informacaoPagamento.codigo_aluguel = aluguel.codigo"
                + " INNER JOIN imovel imovel "
                + " on aluguel.codigo_imovel = imovel.codigo "
                + " LEFT JOIN (select gastoAdicional.codPagamento as codPagamento, "
                + "            gastoAdicional.valorGasto as valorGasto"
                + "               FROM gasto_adicional gastoAdicional"
                + "               INNER JOIN informacao_pagamento pagamento"
                + "               ON gastoAdicional.codPagamento = pagamento.codigo"
                + "               GROUP BY gastoAdicional.codPagamento, gastoAdicional.valorGasto"
                + "            ) as tabelaGastos on tabelaGastos.codPagamento = informacaoPagamento.codigo "
                + " WHERE imovel.codigo_usuario = :donoImovel "
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

        StringBuilder sql = new StringBuilder("SELECT MIN(informacaoPagamento.dataMensal) "
                + " FROM  informacao_pagamento informacaoPagamento"
                + " INNER JOIN aluguel aluguel"
                + " on informacaoPagamento.codigo_aluguel = aluguel.codigo"
                + " INNER JOIN imovel imovel "
                + " on aluguel.codigo_imovel = imovel.codigo "
                + " WHERE imovel.codigo_usuario = :donoImovel "
        );

        SQLQuery sqlQuery = entityManager.createNativeQuery(sql.toString()).unwrap(SQLQuery.class);
        sqlQuery.setParameter("donoImovel", usuarioLogadoService.getUsuario());
        Long time = ((java.sql.Date) sqlQuery.uniqueResult()).getTime();
        return new Date(time);
    }


    @Transactional(readOnly = true)
    @Override
    public List<RelatorioDetalhadoImovelDTO> retrieveRelatorioDetalhadoImovelDTO(PeriodoRelatorioDTO periodoRelatorioDTO) {

        StringBuilder sql = new StringBuilder("SELECT imovel.nome as nome, imovel.cep as cep, "
                + " aluguel.nome as nomeAluguel, aluguel.codigo as codigoAluguel, loc.excluido as estaAlugado"
                + " FROM  imovel imovel"
                + " INNER JOIN aluguel aluguel on aluguel.codigo_imovel = imovel.codigo  "
                + " LEFT JOIN locatario loc on loc.codigo_aluguel = aluguel.codigo "
                + " WHERE imovel.codigo_usuario = :donoImovel "
                + " AND imovel.codigo =:imovel ");
        SQLQuery sqlQuery = entityManager.createNativeQuery(sql.toString()).unwrap(SQLQuery.class);
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
                + " FROM  informacao_pagamento informacaoPagamento"
                + " INNER JOIN aluguel aluguel on informacaoPagamento.codigo_aluguel = aluguel.codigo  "
                + " INNER JOIN imovel imovel  on aluguel.codigo_imovel = imovel.codigo"
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
        StringBuilder sql = new StringBuilder("SELECT gasto.valorGasto as gasto, gasto.comentarioGasto as descricao "
                + " FROM gasto_adicional gasto"
                + " WHERE gasto.codPagamento = :codigoPagamento");
        SQLQuery sqlQuery = entityManager.createNativeQuery(sql.toString()).unwrap(SQLQuery.class);
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

}
