package br.com.bomfim.repository.helper.aluguel;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import br.com.bomfim.model.tipoimovel.Aluguel;

public interface AlugueisQuerys {
	
	public Page<Aluguel> filtrar(Aluguel aluguel, Pageable pageable);
	
	public Page<Aluguel> filtrarByImovel(Long codigo, Pageable pageable);
	
}
