package br.com.imovelcontrol.repository.helper.aluguel;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import br.com.imovelcontrol.model.Aluguel;
import br.com.imovelcontrol.repository.util.PaginacaoUtil;
import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

public class AlugueisImpl implements AlugueisQuerys {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private PaginacaoUtil paginacaoUtil;

    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    @Override
    public Page<Aluguel> filtrarByImovel(Long codigo, Pageable pageable) {
        Criteria criteria = entityManager.unwrap(Session.class).createCriteria(Aluguel.class);
        if (pageable.getSort() == null) {
            criteria.addOrder(Order.asc("nome"));
        }
        if (codigo != null) {
            criteria.add(Restrictions.eq("imovel.codigo", codigo));
        }
        criteria.add(Restrictions.eq("excluido", Boolean.FALSE));


        Long total = retrieveTotalByIdImovel(codigo);
        paginacaoUtil.paginacao(pageable, criteria);

        List<Aluguel> filtrados = criteria.list();
        filtrados.forEach(u -> Hibernate.initialize(u.getLocatarios()));
        filtrados.forEach(u -> Hibernate.initialize(u.getInformacaoPagamentoList()));
        return new PageImpl<>(criteria.list(), pageable, total);
    }

    private Long retrieveTotalByIdImovel(Long codigo) {
        Criteria criteriaTotal = entityManager.unwrap(Session.class).createCriteria(Aluguel.class);
        if (codigo != null) {
            criteriaTotal.add(Restrictions.eq("imovel.codigo", codigo));
        }

        criteriaTotal.add(Restrictions.eq("excluido", Boolean.FALSE));
        criteriaTotal.setProjection(Projections.rowCount());
        return (Long) criteriaTotal.uniqueResult();
    }

}
