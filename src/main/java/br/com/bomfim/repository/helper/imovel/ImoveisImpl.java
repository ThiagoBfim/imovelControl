package br.com.bomfim.repository.helper.imovel;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import br.com.bomfim.model.Imovel;
import br.com.bomfim.repository.util.PaginacaoUtil;

public class ImoveisImpl implements ImoveisQuerys {

	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	private PaginacaoUtil paginacaoUtil;

	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	@Override
	public Page<Imovel> filtrar(Imovel filtro, Pageable pageable) {
		Criteria criteria = entityManager.unwrap(Session.class).createCriteria(Imovel.class);

		adicionarFiltro(filtro, criteria);
		paginacaoUtil.paginacao(pageable, criteria);
		return new PageImpl<>(criteria.list(), pageable, total(filtro));
	}

	private Long total(Imovel filtro) {
		Criteria criteria = entityManager.unwrap(Session.class).createCriteria(Imovel.class);
		adicionarFiltro(filtro, criteria);
		criteria.setProjection(Projections.rowCount());
		return (Long) criteria.uniqueResult();
	}

	private void adicionarFiltro(Imovel filtro, Criteria criteria) {
		if (filtro != null) {
			if (!StringUtils.isEmpty(filtro.getNome())) {
				criteria.add(Restrictions.ilike("nome", filtro.getNome(), MatchMode.ANYWHERE));
			}
			if (filtro.getEndereco() != null) {
				if (filtro.getEndereco().getCidade() != null) {
					criteria.add(Restrictions.eq("endereco.cidade", filtro.getEndereco().getCidade()));
				}
				if (filtro.getEndereco().getBairro() != null) {
					criteria.add(Restrictions.eq("endereco.bairro", filtro.getEndereco().getBairro()));
				}
			}
		}
	}

}
