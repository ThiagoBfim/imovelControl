package br.com.imovelcontrol.repository;

import br.com.imovelcontrol.model.FormaPagamento;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FormasPagamentos extends JpaRepository<FormaPagamento, Long> {

}
