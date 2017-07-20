package br.com.imovelcontrol.service;

import br.com.imovelcontrol.model.Imovel;
import br.com.imovelcontrol.repository.Imoveis;
import br.com.imovelcontrol.service.event.ImovelSalvoEvent;
import br.com.imovelcontrol.service.exception.CepImovelJaCadastradoException;
import br.com.imovelcontrol.service.exception.NomeImovelJaCadastradoException;
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
	public Imovel salvar(Imovel imovel) {

		if(imoveis.findByCep(imovel.getEndereco().getCep()).get().size() > 0
				&& !(imoveis.findByUsuario(imovel.getDonoImovel().getCodigo()).get().size() > 0)) {

			throw  new CepImovelJaCadastradoException("Já existe um imóvel cadastrado com este CEP");
		}else if (imoveis.findByNome(imovel.getNome()).isPresent()
				&& !(imoveis.findByUsuario(imovel.getDonoImovel().getCodigo()).get().size() > 0)){

			throw  new NomeImovelJaCadastradoException("Já existe um imóvel cadastrado com este Nome");
		}
		publisher.publishEvent(new ImovelSalvoEvent(imovel));
		return imoveis.save(imovel);
	}


	@Transactional
	public void excluir(Imovel imovel) {
		imoveis.delete(imovel);
		
	}
}
