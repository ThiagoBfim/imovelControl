package br.com.imovelcontrol.service;

import br.com.imovelcontrol.model.Imovel;
import br.com.imovelcontrol.repository.Imoveis;
import br.com.imovelcontrol.service.event.ImovelSalvoEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
