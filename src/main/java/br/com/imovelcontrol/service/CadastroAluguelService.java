package br.com.imovelcontrol.service;

import java.util.Optional;

import br.com.imovelcontrol.model.tipoimovel.Aluguel;
import br.com.imovelcontrol.repository.Alugueis;
import br.com.imovelcontrol.repository.FormasPagamentos;
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

	@Transactional
	public Aluguel salvar(Aluguel aluguel) {
        Optional<Aluguel> usuarioRetrived = alugueis.findByNome(aluguel.getNome());
        if (usuarioRetrived.isPresent() && !usuarioRetrived.get().equals(aluguel)
                && usuarioRetrived.get().getImovel().getCodigo()
                .equals(aluguel.getImovel().getCodigo())) {
            throw new NomeAluguelJaCadastradoException("Nome já cadastrado");
        }
		return alugueis.save(aluguel);
	}

	@Transactional
	public void excluir(Aluguel aluguel) {
        alugueis.delete(aluguel);
        formasPagamentos.delete(aluguel.getFormaPagamento());
	}
}
