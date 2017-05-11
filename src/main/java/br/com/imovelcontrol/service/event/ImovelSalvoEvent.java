package br.com.imovelcontrol.service.event;

import br.com.imovelcontrol.model.Imovel;
import org.springframework.util.StringUtils;

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
