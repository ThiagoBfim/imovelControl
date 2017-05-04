package br.com.bomfim.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.bomfim.model.tipoimovel.Aluguel;
import br.com.bomfim.repository.Alugueis;

@Service
public class CadastroAluguelService {

	@Autowired
	private Alugueis alugueis;

	@Transactional
	public Aluguel salvar(Aluguel aluguel) {
		return alugueis.save(aluguel);
	}

	@Transactional
	public void excluir(Aluguel aluguel) {
		alugueis.delete(aluguel);
	}
}
