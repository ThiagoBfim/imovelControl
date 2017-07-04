package br.com.imovelcontrol.service;

import java.util.Optional;

import br.com.imovelcontrol.model.FormaPagamento;
import br.com.imovelcontrol.model.InformacaoPagamento;
import br.com.imovelcontrol.model.Locatario;
import br.com.imovelcontrol.model.tipoimovel.Aluguel;
import br.com.imovelcontrol.repository.FormasPagamentos;
import br.com.imovelcontrol.repository.InformacaoPagamentos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class InformacaoPagamentoService {

    @Autowired
    private InformacaoPagamentos informacaoPagamentos;

    @Transactional
    public InformacaoPagamento salvar(InformacaoPagamento informacaoPagamento) {
        return informacaoPagamentos.save(informacaoPagamento);
    }

    @Transactional
    public Optional<InformacaoPagamento> retrieveByAluguel(String codigo) {
        Aluguel aluguel = new Aluguel();
        aluguel.setCodigo(Long.parseLong(codigo));
        return informacaoPagamentos.findByAluguel(aluguel);
    }

}
