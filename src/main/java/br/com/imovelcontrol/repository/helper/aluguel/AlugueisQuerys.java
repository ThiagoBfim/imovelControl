package br.com.imovelcontrol.repository.helper.aluguel;

import br.com.imovelcontrol.model.tipoimovel.Aluguel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AlugueisQuerys {
	
	public Page<Aluguel> filtrar(Aluguel aluguel, Pageable pageable);
	
	public Page<Aluguel> filtrarByImovel(Long codigo, Pageable pageable);
	
}
