package br.com.imovelcontrol.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import br.com.imovelcontrol.model.GastoAdicional;
import br.com.imovelcontrol.model.InformacaoPagamento;
import br.com.imovelcontrol.model.tipoimovel.Aluguel;
import br.com.imovelcontrol.repository.GastosAdicionais;
import br.com.imovelcontrol.repository.InformacaoPagamentos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class InformacaoPagamentoService {

    @Autowired
    private InformacaoPagamentos informacaoPagamentos;

    @Autowired
    private GastosAdicionais gastosAdicionais;

    @Transactional
    public InformacaoPagamento salvar(InformacaoPagamento informacaoPagamento) {
        GastoAdicional gastoAdicional = informacaoPagamento.getGastoAdicional();
        if (informacaoPagamento.getCodigo() == null) {
            informacaoPagamento.setDataMensal(LocalDate.now());
            informacaoPagamento.setPago(Boolean.FALSE);
        } else {
            /*Logica para garantir que o sistema continue com os dados de forma integra, ou seja,
            * não permitir que o usuario burle o sistema com JavaScript. */
            InformacaoPagamento informacaoPagamentoRetrived = informacaoPagamentos.findOne(informacaoPagamento.getCodigo());
            caseTrueChangeValue(informacaoPagamento, informacaoPagamentoRetrived);
            informacaoPagamento = informacaoPagamentoRetrived;

        }
        InformacaoPagamento informacaoPagamentoRetrived = informacaoPagamentos.save(informacaoPagamento);
        if (gastoAdicional != null && gastoAdicional.getValorGasto() != null) {
            gastoAdicional.setInformacaoPagamento(informacaoPagamentoRetrived);
            gastoAdicional.setDataMensal(LocalDate.now());
            gastosAdicionais.save(gastoAdicional);
        }

        return informacaoPagamentoRetrived;
    }

    /**
     * Logica para alterar os valores de pagamento apenas no caso do usuario selecionar como pago(TRUE).
     *
     * @param informacaoPagamento         Informação de pagamento provinda da tela.
     * @param informacaoPagamentoRetrived Informação de pagamento no banco de dados.
     */
    private void caseTrueChangeValue(InformacaoPagamento informacaoPagamento,
            InformacaoPagamento informacaoPagamentoRetrived) {
        if (informacaoPagamento.getPago().equals(Boolean.TRUE)) {
            informacaoPagamentoRetrived.setPago(Boolean.TRUE);
        }
        if (informacaoPagamento.getAguaInclusa().equals(Boolean.TRUE)) {
            informacaoPagamentoRetrived.setAguaInclusa(Boolean.TRUE);
        }
        if (informacaoPagamento.getInternetInclusa().equals(Boolean.TRUE)) {
            informacaoPagamentoRetrived.setInternetInclusa(Boolean.TRUE);
        }
        if (informacaoPagamento.getIptuIncluso().equals(Boolean.TRUE)) {
            informacaoPagamentoRetrived.setIptuIncluso(Boolean.TRUE);
        }
        if (informacaoPagamento.getLuzInclusa().equals(Boolean.TRUE)) {
            informacaoPagamentoRetrived.setLuzInclusa(Boolean.TRUE);
        }
        if (informacaoPagamento.getPossuiCondominio().equals(Boolean.TRUE)) {
            informacaoPagamentoRetrived.setPossuiCondominio(Boolean.TRUE);
        }
    }

    @Transactional
    public Optional<InformacaoPagamento> retrieveByAluguelAndData(String codigo) {
        Aluguel aluguel = new Aluguel();

        aluguel.setCodigo(Long.parseLong(codigo));
        List<InformacaoPagamento> informacaoPagamento = informacaoPagamentos.findByAluguel(aluguel);
        return informacaoPagamento.stream().filter(pag -> pag.getDataMensal().getMonth()
                .equals(LocalDate.now().getMonth())).findAny();
    }

}
