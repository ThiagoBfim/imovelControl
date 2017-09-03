package br.com.imovelcontrol.repository.helper.imovel;

import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import br.com.imovelcontrol.dto.PeriodoRelatorioDTO;
import br.com.imovelcontrol.dto.RelatorioImovelDTO;
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
import org.hibernate.type.StringType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
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
                + "               GROUP BY gastoAdicional.codPagamento"
                + "            ) as tabelaGastos on tabelaGastos.codPagamento = informacaoPagamento.codigo "
                + " WHERE imovel.codigo_usuario = :donoImovel "
        );

        sql.append(" AND  informacaoPagamento.dataMensal >= :dataInicio ");
        sql.append("  AND  informacaoPagamento.dataMensal <= :dataFim ");

        if (!StringUtils.isEmpty(periodoRelatorioDTO.getNomeImovel())) {
            sql.append(" AND  imovel.nome like :nome");
        }


        sql.append("  GROUP BY  imovel.nome, imovel.cep, imovel.codigo_usuario ");

        SQLQuery sqlQuery = entityManager.createNativeQuery(sql.toString()).unwrap(SQLQuery.class);
        sqlQuery.setParameter("donoImovel", usuarioLogadoService.getUsuario());
        sqlQuery.setParameter("dataFim", periodoRelatorioDTO.getDataFim());
        sqlQuery.setParameter("dataInicio", periodoRelatorioDTO.getDataInicio());

        if (!StringUtils.isEmpty(periodoRelatorioDTO.getNomeImovel())) {
            sqlQuery.setParameter("nome", '%' + periodoRelatorioDTO.getNomeImovel() + '%');
        }

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

}
