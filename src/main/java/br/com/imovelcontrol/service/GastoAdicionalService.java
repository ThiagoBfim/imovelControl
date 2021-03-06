package br.com.imovelcontrol.service;

import java.util.List;
import java.util.Optional;

import br.com.imovelcontrol.model.GastoAdicional;
import br.com.imovelcontrol.model.InformacaoPagamento;
import br.com.imovelcontrol.repository.GastosAdicionais;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by marcosfellipec on 01/10/17.
 */

@Service
public class GastoAdicionalService {

    @Autowired
    private GastosAdicionais gastosAdicionais;

    @Transactional
    public Optional<List<GastoAdicional>> findByInformaçãoPagamento(InformacaoPagamento informacaoPagamento){
        return gastosAdicionais.findByInformacaoPagamento(informacaoPagamento);

    }


}
