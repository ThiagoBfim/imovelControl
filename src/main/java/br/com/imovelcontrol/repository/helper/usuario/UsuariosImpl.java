package br.com.imovelcontrol.repository.helper.usuario;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import br.com.imovelcontrol.model.Usuario;
import br.com.imovelcontrol.model.UsuarioGrupo;
import br.com.imovelcontrol.repository.util.PaginacaoUtil;
import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Subqueries;
import org.hibernate.sql.JoinType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

public class UsuariosImpl implements UsuariosQueries {


    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private PaginacaoUtil paginacaoUtil;

    @Override
    public Optional<Usuario> retrieveLoginAtivo(String email) {
        return entityManager.createQuery("from Usuario where lower(login) =:login and ativo= true", Usuario.class)
                .setParameter("login", email.toLowerCase()).getResultList().stream().findFirst();
    }

    @Override
    public List<String> permissoes(Usuario usuario) {
        return entityManager
                .createQuery("Select distinct(p.nome) " + " FROM Usuario u " + " inner join u.grupos g"
                        + " inner join g.permissoes p" + " where u = :usuario", String.class)
                .setParameter("usuario", usuario).getResultList();
    }

    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    @Override
    public Page<Usuario> filtrar(Usuario filtro, Pageable pageable) {
        Criteria criteria = entityManager.unwrap(Session.class).createCriteria(Usuario.class);
        criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        adicionarFiltro(filtro, criteria);
        paginacaoUtil.paginacao(pageable, criteria);
        List<Usuario> filtrados = criteria.list();
        filtrados.forEach(u -> Hibernate.initialize(u.getGrupos()));
        return new PageImpl<>(criteria.list(), pageable, total(filtro));
    }

    private Long total(Usuario filtro) {
        Criteria criteria = entityManager.unwrap(Session.class).createCriteria(Usuario.class);
        criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        adicionarFiltro(filtro, criteria);
        criteria.setProjection(Projections.rowCount());
        return (Long) criteria.uniqueResult();
    }

    private void adicionarFiltro(Usuario filtro, Criteria criteria) {
        if (filtro != null) {
            if (!StringUtils.isEmpty(filtro.getNome())) {
                criteria.add(Restrictions.ilike("nome", filtro.getNome(), MatchMode.ANYWHERE));
            }
            if (!StringUtils.isEmpty(filtro.getEmail())) {
                criteria.add(Restrictions.ilike("email", filtro.getEmail(), MatchMode.ANYWHERE));
            }
            if (!CollectionUtils.isEmpty(filtro.getGrupos())) {
                List<Criterion> subqueries = new ArrayList<>();
                filtro.getGrupos().forEach(g -> {
                    DetachedCriteria dc = DetachedCriteria.forClass(UsuarioGrupo.class);
                    dc.add(Restrictions.eq("id.grupo.codigo", g.getCodigo()));
                    dc.setProjection(Projections.property("id.usuario"));

                    subqueries.add(Subqueries.propertyIn("codigo", dc));
                });
                Criterion[] criterions = new Criterion[subqueries.size()];
                criteria.add(Restrictions.and(subqueries.toArray(criterions)));
            }
        }
    }

    @Transactional(readOnly = true)
    @Override
    public Usuario buscarComGrupos(Long codigo) {
        Criteria criteria = entityManager.unwrap(Session.class).createCriteria(Usuario.class);
        criteria.createAlias("grupos", "g", JoinType.LEFT_OUTER_JOIN);
        criteria.add(Restrictions.eq("codigo", codigo));
        criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        return (Usuario) criteria.uniqueResult();
    }

    @Transactional(readOnly = true)
    @Override
    public Usuario buscarComGruposByLogin(String login) {
        Criteria criteria = entityManager.unwrap(Session.class).createCriteria(Usuario.class);
        criteria.createAlias("grupos", "g", JoinType.LEFT_OUTER_JOIN);
        criteria.add(Restrictions.eq("login", login));
        criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        return (Usuario) criteria.uniqueResult();
    }

}
