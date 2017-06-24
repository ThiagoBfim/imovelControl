package br.com.imovelcontrol.service;

import br.com.imovelcontrol.model.Locatario;
import br.com.imovelcontrol.model.tipoimovel.Aluguel;
import br.com.imovelcontrol.repository.Locatarios;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by marcosfellipec on 18/05/17.
 */

@Service
public class CadastroLocatarioService {


    @Autowired
    private Locatarios locatarios;

    @Transactional
    public void salvar(Locatario locatario) {
        locatarios.save(locatario);
    }

    @Transactional
    public void excluir(Locatario locatario) {
        locatarios.delete(locatario);
    }

    @Transactional
    public Locatario retrieveByAluguel(String codigo) {
        Aluguel aluguel = new Aluguel();
        aluguel.setCodigo(Long.parseLong(codigo));
        return locatarios.findByAluguel(aluguel).get();
    }

    @Transactional
    public Locatario retrieveById(Long codigo) {
        return locatarios.findOne(codigo);
    }
}











