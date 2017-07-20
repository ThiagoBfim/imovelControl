package br.com.imovelcontrol.service;

import java.util.Optional;

import br.com.imovelcontrol.controller.converter.FormatUtil;
import br.com.imovelcontrol.model.Locatario;
import br.com.imovelcontrol.model.tipoimovel.Aluguel;
import br.com.imovelcontrol.repository.Locatarios;
import br.com.imovelcontrol.service.exception.CpfLocatarioJaCadastradoException;
import br.com.imovelcontrol.service.exception.TelefoneLocatarioJaCadastradoException;
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
    public Locatario salvar(Locatario locatario) {
        if (locatarios.findByCpf(FormatUtil.removerMascara(locatario.getCpf())).isPresent()){
            throw new CpfLocatarioJaCadastradoException("Já existe um Locatário cadastrado com esse CPF");
        }else if (locatarios.findByTelefone(FormatUtil.removerMascara(locatario.getTelefone())).isPresent()){
            throw  new TelefoneLocatarioJaCadastradoException("Já existe um locatário cadastrado com esse telefone");
        }
        return locatarios.save(locatario);
    }

    @Transactional
    public void excluir(Locatario locatario) {
        locatarios.delete(locatario);
    }

    @Transactional
    public Optional<Locatario> retrieveByAluguel(String codigo) {
        Aluguel aluguel = new Aluguel();
        aluguel.setCodigo(Long.parseLong(codigo));
        return locatarios.findByAluguel(aluguel);
    }

    @Transactional
    public Locatario retrieveById(Long codigo) {
        return locatarios.findOne(codigo);
    }
}











