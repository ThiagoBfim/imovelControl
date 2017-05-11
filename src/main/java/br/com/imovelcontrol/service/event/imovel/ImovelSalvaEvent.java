package br.com.imovelcontrol.service.event.imovel;

import br.com.imovelcontrol.model.Imovel;
import org.springframework.util.StringUtils;

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
