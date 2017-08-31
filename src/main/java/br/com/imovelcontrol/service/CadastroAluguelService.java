package br.com.imovelcontrol.service;

import java.util.List;
import java.util.Optional;

import br.com.imovelcontrol.model.Aluguel;
import br.com.imovelcontrol.repository.Alugueis;
import br.com.imovelcontrol.repository.FormasPagamentos;
import br.com.imovelcontrol.service.exception.AluguelByImovelNaoEncontradoException;
import br.com.imovelcontrol.service.exception.NomeAluguelJaCadastradoException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CadastroAluguelService {

    @Autowired
    private Alugueis alugueis;

    @Autowired
    private FormasPagamentos formasPagamentos;

    @Autowired
    private CadastroLocatarioService cadastroLocatarioService;

    @Transactional
    public Aluguel salvar(Aluguel aluguel) {
        Optional<Aluguel> usuarioRetrived = alugueis.findByNomeAndImovel_Codigo(aluguel.getNome(),
                aluguel.getImovel().getCodigo());
        if (usuarioRetrived.isPresent() && !usuarioRetrived.get().equals(aluguel)
                && usuarioRetrived.get().getImovel().equals(aluguel.getImovel())) {
            throw new NomeAluguelJaCadastradoException("Nome já cadastrado");
        }
        return alugueis.save(aluguel);
    }

    @Transactional
    public void excluirLogicamente(Aluguel aluguel) {

        cadastroLocatarioService.deleteByAluguel(aluguel);
        aluguel.setExcluido(Boolean.TRUE);
        alugueis.save(aluguel);
    }

    @Transactional
    public List<Aluguel> findByImovel(Long codigo) {
        Optional<List<Aluguel>> aluguels = alugueis.findByImovel_Codigo(codigo);

        if (!aluguels.isPresent()) {
            throw new AluguelByImovelNaoEncontradoException("Aluguéis não encontrados");
        }

        return aluguels.get();
    }
}
