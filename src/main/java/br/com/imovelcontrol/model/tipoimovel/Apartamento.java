package br.com.imovelcontrol.model.tipoimovel;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@SuppressWarnings("serial")
@Entity
@DiscriminatorValue(value = "AP")
public class Apartamento extends Aluguel {
	
	@Column(name="quantidade_quartos")
	private Integer quantidadeQuartos;
	
	@Column(name="quantidade_banheiros")
	private Integer quantidadeBanheiros;

	public Integer getQuantidadeQuartos() {
		return quantidadeQuartos;
	}

	public void setQuantidadeQuartos(Integer quantidadeQuartos) {
		this.quantidadeQuartos = quantidadeQuartos;
	}

	public Integer getQuantidadeBanheiros() {
		return quantidadeBanheiros;
	}

	public void setQuantidadeBanheiros(Integer quantidadeBanheiros) {
		this.quantidadeBanheiros = quantidadeBanheiros;
	}

	
}
