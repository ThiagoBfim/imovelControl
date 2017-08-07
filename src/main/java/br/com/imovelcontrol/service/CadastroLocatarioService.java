package br.com.imovelcontrol.service;

import java.util.List;
import java.util.Optional;

import br.com.imovelcontrol.controller.converter.FormatUtil;
import br.com.imovelcontrol.model.Locatario;
import br.com.imovelcontrol.model.tipoimovel.Aluguel;
import br.com.imovelcontrol.repository.Locatarios;
import br.com.imovelcontrol.service.exception.CpfLocatarioInvalidoException;
import br.com.imovelcontrol.service.exception.CpfLocatarioJaCadastradoException;
import br.com.imovelcontrol.service.exception.NomeLocatarioInvalidoException;
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
        } else if( FormatUtil.removerMascara(locatario.getCpf()).length() < 11){
            throw new CpfLocatarioInvalidoException("Cpf Inválido!");

        }else if(FormatUtil.removerMascara(locatario.getTelefone()).length() < 10 ){
            throw new TelefoneLocatarioJaCadastradoException("Telefone Inválido!");
        }else if(locatario.getNome().length() < 1){
            throw new NomeLocatarioInvalidoException("Nome inválido");
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

    @Transactional
    public void deleteByAluguel(Long codigo){
         locatarios.deleteByAluguel_Codigo(codigo);
    }

}











