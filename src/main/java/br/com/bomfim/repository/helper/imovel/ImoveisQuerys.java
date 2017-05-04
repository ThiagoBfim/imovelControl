package br.com.bomfim.repository.helper.imovel;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import br.com.bomfim.model.Imovel;

public interface ImoveisQuerys {

	public Page<Imovel> filtrar(Imovel filtro, Pageable pageable);
	
}
