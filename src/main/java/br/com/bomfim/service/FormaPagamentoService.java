package br.com.bomfim.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.bomfim.model.FormaPagamento;
import br.com.bomfim.repository.FormasPagamentos;

@Service
public class FormaPagamentoService {

	@Autowired
	private FormasPagamentos formasPagamentos;

	@Transactional
	public FormaPagamento salvar(FormaPagamento formaPagamento) {
		return formasPagamentos.save(formaPagamento);
	}

}
