package br.com.imovelcontrol.service;

import java.util.Optional;

import br.com.imovelcontrol.model.Aluguel;
import br.com.imovelcontrol.model.Locatario;
import br.com.imovelcontrol.repository.Locatarios;
import br.com.imovelcontrol.service.exception.BusinessException;
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
        if (locatarios.findByCpfAndExcluido(locatario.getCpf(), Boolean.FALSE).isPresent()
                && locatario.getCodigo() == null) {
            throw new BusinessException("Já existe um Locatário cadastrado com esse CPF", "cpf");
        } else if (locatarios.findByTelefoneAndExcluido(locatario.getTelefone(), Boolean.FALSE).isPresent()
                && locatario.getCodigo() == null) {
            throw new BusinessException("Já existe um locatário cadastrado com esse telefone", "telefone");
        }
        return locatarios.save(locatario);
    }

    @Transactional
    public void excluirLogicamente(Locatario locatario) {
        locatario.setExcluido(Boolean.TRUE);
        locatarios.save(locatario);
    }

    @Transactional
    public Optional<Locatario> retrieveByAluguel(String codigo) {
        Aluguel aluguel = new Aluguel();
        aluguel.setCodigo(Long.parseLong(codigo));

        return locatarios.findByAluguelAndExcluido(aluguel, Boolean.FALSE);
    }

    @Transactional
    public Locatario retrieveById(Long codigo) {
        return locatarios.findOne(codigo);
    }

    @Transactional
    public void deleteByAluguel(Aluguel aluguel) {

        Optional<Locatario> locatario = locatarios.findByAluguelAndExcluido(aluguel, Boolean.FALSE);
        if (!locatario.isPresent()) {
            return;
        }
        locatario.get().setExcluido(Boolean.TRUE);
        locatarios.save(locatario.get());

    }

}











