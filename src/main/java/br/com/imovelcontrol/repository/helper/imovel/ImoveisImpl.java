package br.com.imovelcontrol.repository.helper.imovel;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import br.com.imovelcontrol.dto.PeriodoRelatorioDTO;
import br.com.imovelcontrol.dto.RelatorioImovelDTO;
import br.com.imovelcontrol.model.Imovel;
import br.com.imovelcontrol.model.Usuario;
import br.com.imovelcontrol.repository.util.PaginacaoUtil;
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

public class ImoveisImpl implements ImoveisQuerys {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private PaginacaoUtil paginacaoUtil;

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
    public List<RelatorioImovelDTO> retrieveRelatorioImovelDTO(PeriodoRelatorioDTO periodoRelatorioDTO) {

        String sql = "SELECT new " + RelatorioImovelDTO.class.getName() + " (imovel.nome, imovel.endereco.cep)"
                + " FROM " + Imovel.class.getCanonicalName() + "  imovel"
                + " where  imovel.nome like :nome";
        Query query = entityManager.createQuery(sql);
        query.setParameter("nome", '%' + periodoRelatorioDTO.getNomeImovel() + '%');
        List<RelatorioImovelDTO> relatorioImovelDTOs = query.getResultList();


        //     .addScalar("id", new LongType())
        //          .addScalar("message", new StringType())
        //           .setResultTransformer(Transformers.aliasToBean(MessageExtDto.class));
        return relatorioImovelDTOs;
    }

}
