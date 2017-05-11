package br.com.imovelcontrol.service;

import br.com.imovelcontrol.model.FormaPagamento;
import br.com.imovelcontrol.repository.FormasPagamentos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FormaPagamentoService {

	@Autowired
	private FormasPagamentos formasPagamentos;

	@Transactional
	public FormaPagamento salvar(FormaPagamento formaPagamento) {
		return formasPagamentos.save(formaPagamento);
	}

}
