package br.com.bomfim.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.bomfim.model.FormaPagamento;

public interface FormasPagamentos extends JpaRepository<FormaPagamento, Long> {

}
