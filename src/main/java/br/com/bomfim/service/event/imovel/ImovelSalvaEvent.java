package br.com.bomfim.service.event.imovel;

import org.springframework.util.StringUtils;

import br.com.bomfim.model.Imovel;

public class ImovelSalvaEvent {

	private Imovel imovel;

	public ImovelSalvaEvent(Imovel imovel) {
		this.imovel = imovel;
	}

	public Imovel getImovel() {
		return imovel;
	}

	public void setImovel(Imovel imovel) {
		this.imovel = imovel;
	}

	public boolean isFoto() {
		return !StringUtils.isEmpty(imovel.getFoto());
	}

}
