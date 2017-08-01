package br.com.imovelcontrol.service;

import java.util.List;
import java.util.Optional;

import br.com.imovelcontrol.model.tipoimovel.Aluguel;
import br.com.imovelcontrol.repository.Alugueis;
import br.com.imovelcontrol.repository.FormasPagamentos;
import br.com.imovelcontrol.repository.Locatarios;
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
    private Locatarios locatarios;

    @Autowired
    private CadastroLocatarioService cadastroLocatarioService;

	@Transactional
	public Aluguel salvar(Aluguel aluguel) {
        Optional<Aluguel> usuarioRetrived = alugueis.findByNome(aluguel.getNome());
        if (usuarioRetrived.isPresent() && !usuarioRetrived.get().equals(aluguel)) {
            throw new NomeAluguelJaCadastradoException("Nome já cadastrado");
        }
		return alugueis.save(aluguel);
	}

	@Transactional
	public void excluir(Aluguel aluguel) {

	    cadastroLocatarioService.deleteByAluguel(aluguel.getCodigo());
        alugueis.delete(aluguel);
        formasPagamentos.delete(aluguel.getFormaPagamento());
	}

    @Transactional
    public List<Aluguel> findByImovel(Long codigo){
        Optional<List<Aluguel>> aluguels = alugueis.findByImovel_Codigo(codigo);

        if (!aluguels.isPresent()){
            throw new AluguelByImovelNaoEncontradoException("Aluguéis não encontrados");
        }

        return  aluguels.get();
    }
}
