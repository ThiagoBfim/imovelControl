package br.com.bomfim.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.bomfim.model.Imovel;
import br.com.bomfim.repository.Imoveis;
import br.com.bomfim.service.event.ImovelSalvoEvent;

@Service
public class CadastroImovelService {

	@Autowired
	private Imoveis imoveis;

	@Autowired
	private ApplicationEventPublisher publisher;

	@Transactional
	public void salvar(Imovel imovel) {
		imoveis.save(imovel);
		publisher.publishEvent(new ImovelSalvoEvent(imovel));
	}

	@Transactional
	public void excluir(Imovel imovel) {
		imoveis.delete(imovel);
		
	}
}
