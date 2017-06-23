package br.com.imovelcontrol.service;

import br.com.imovelcontrol.model.Locatario;
import br.com.imovelcontrol.model.tipoimovel.Aluguel;
import br.com.imovelcontrol.repository.Locatarios;
import br.com.imovelcontrol.service.event.ImovelSalvoEvent;
import br.com.imovelcontrol.service.event.LocatarioSalvoEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by marcosfellipec on 18/05/17.
 */

@Service
public class CadastroLocatarioService {


    @Autowired
    private Locatarios locatarios;

    @Autowired
    private ApplicationEventPublisher publisher;
    @Transactional
    public void salvar(Locatario locatario){
        locatarios.save(locatario);
        //publisher.publishEvent(new LocatarioSalvoEvent(locatario));
    }

    @Transactional
    public void excluir(Locatario locatario){
        locatarios.delete(locatario);
    }



}











