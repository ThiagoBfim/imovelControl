package br.com.imovelcontrol.repository.helper.aluguel;

import br.com.imovelcontrol.model.Aluguel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AlugueisQuerys {

    Page<Aluguel> filtrarByImovel(Long codigo, Pageable pageable);

    Aluguel findOneWithLocatariosByCodigo(Long codigo);

}
