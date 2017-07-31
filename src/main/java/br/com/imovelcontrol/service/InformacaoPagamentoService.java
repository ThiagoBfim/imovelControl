package br.com.imovelcontrol.service;

import java.time.LocalDate;
import java.util.Optional;

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
        if (informacaoPagamento.getCodigo() == null) {
            informacaoPagamento.setDataMensal(LocalDate.now());
        }
        if (informacaoPagamento.getGastoAdicional() != null
                && informacaoPagamento.getGastoAdicional().getValorGasto() != null) {
            informacaoPagamento.getGastoAdicional().setDataMensal(informacaoPagamento.getDataMensal());
            informacaoPagamento.getGastoAdicional().setInformacaoPagamento(informacaoPagamento);
            gastosAdicionais.save(informacaoPagamento.getGastoAdicional());
        }
        informacaoPagamento.setGastoAdicional(null);
        return informacaoPagamentos.save(informacaoPagamento);
    }

    @Transactional
    public Optional<InformacaoPagamento> retrieveByAluguel(String codigo) {
        Aluguel aluguel = new Aluguel();
        aluguel.setCodigo(Long.parseLong(codigo));
        return informacaoPagamentos.findByAluguel(aluguel);
    }

}
