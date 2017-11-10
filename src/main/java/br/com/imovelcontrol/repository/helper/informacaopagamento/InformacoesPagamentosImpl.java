package br.com.imovelcontrol.repository.helper.informacaopagamento;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import br.com.imovelcontrol.model.InformacaoPagamento;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by Usuario on 08/11/2017.
 */
public class InformacoesPagamentosImpl implements InformacoesPagamentosQuerys {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional(readOnly = true)
    @Override
    public List<InformacaoPagamento> retrieveInformacaoPagamentoVencidoByAluguel(Long codAluguel) {
        return entityManager.createQuery("SELECT informacaoPgt FROM " + InformacaoPagamento.class.getName()
                        + " informacaoPgt "
                        + " JOIN informacaoPgt.aluguel aluguel "
                        + " LEFT JOIN aluguel.locatarios locatario "
                        + " WHERE informacaoPgt.pago = false AND  aluguel.codigo =:codAluguel"
                        + " AND locatario.dataInicio <= informacaoPgt.dataMensal"
                        + " AND locatario.dataFim >= informacaoPgt.dataMensal"
                        + " ORDER BY informacaoPgt.dataMensal",
                InformacaoPagamento.class)
                .setParameter("codAluguel", codAluguel)
                .getResultList();
    }

}
