package br.com.imovelcontrol.repository.helper.imovel;

import br.com.imovelcontrol.model.Imovel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ImoveisQuerys {

	public Page<Imovel> filtrar(Imovel filtro, Pageable pageable);
	
}
