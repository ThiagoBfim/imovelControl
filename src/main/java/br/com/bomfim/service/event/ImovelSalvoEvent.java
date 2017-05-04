package br.com.bomfim.service.event;

import org.springframework.util.StringUtils;

import br.com.bomfim.model.Imovel;

public class ImovelSalvoEvent {
	private Imovel imovel;

	public ImovelSalvoEvent(Imovel imovel) {
		this.imovel = imovel;
	}

	public Imovel getCerveja() {
		return imovel;
	}

	public void setCerveja(Imovel imovel) {
		this.imovel = imovel;
	}

	public boolean isFoto() {
		return !StringUtils.isEmpty(imovel.getFoto());
	}

}
