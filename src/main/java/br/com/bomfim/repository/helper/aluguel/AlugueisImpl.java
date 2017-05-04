package br.com.bomfim.repository.helper.aluguel;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import br.com.bomfim.model.tipoimovel.Aluguel;
import br.com.bomfim.repository.util.PaginacaoUtil;

public class AlugueisImpl implements AlugueisQuerys {

	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	private PaginacaoUtil paginacaoUtil;

	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	@Override
	public Page<Aluguel> filtrar(Aluguel aluguel, Pageable pageable) {
		Criteria criteria = entityManager.unwrap(Session.class).createCriteria(Aluguel.class);
		adicionarFiltro(aluguel, criteria);
		paginacaoUtil.paginacao(pageable, criteria);
		return new PageImpl<>(criteria.list(), pageable, total(aluguel));
	}

	private Long total(Aluguel aluguel) {
		Criteria criteria = entityManager.unwrap(Session.class).createCriteria(Aluguel.class);
		adicionarFiltro(aluguel, criteria);
		criteria.setProjection(Projections.rowCount());
		return (Long) criteria.uniqueResult();
	}

	private void adicionarFiltro(Aluguel aluguel, Criteria criteria) {
		if (aluguel.getImovel() != null && aluguel.getImovel().getCodigo() != null) {
			criteria.add(Restrictions.eq("imovel.codigo", aluguel.getImovel().getCodigo()));
		}
		if (aluguel.getCodigo() != null) {
			criteria.add(Restrictions.eq("codigo", aluguel.getCodigo()));
		}
		if (aluguel.getTipoImovel() != null) {
			criteria.add(Restrictions.eq("tipoImovel", aluguel.getTipoImovel()));
		}

	}

	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	@Override
	public Page<Aluguel> filtrarByImovel(Long codigo, Pageable pageable) {
		Criteria criteria = entityManager.unwrap(Session.class).createCriteria(Aluguel.class);
		if (codigo != null) {
			criteria.add(Restrictions.eq("imovel.codigo", codigo));
		}
		
		Long total = retrieveTotalByIdImovel(codigo);
		paginacaoUtil.paginacao(pageable, criteria);
		return new PageImpl<>(criteria.list(), pageable, total);
	}

	private Long retrieveTotalByIdImovel(Long codigo) {
		Criteria criteriaTotal = entityManager.unwrap(Session.class).createCriteria(Aluguel.class);
		if (codigo != null) {
			criteriaTotal.add(Restrictions.eq("imovel.codigo", codigo));
		}
		criteriaTotal.setProjection(Projections.rowCount());
		Long total = (Long) criteriaTotal.uniqueResult();
		return total;
	}

}
