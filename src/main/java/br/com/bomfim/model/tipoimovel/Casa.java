package br.com.bomfim.model.tipoimovel;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import br.com.bomfim.model.tipoimovel.enuns.TipoForro;
import br.com.bomfim.model.tipoimovel.enuns.TipoPiso;

@Entity
@DiscriminatorValue(value = "CS")
public class Casa extends Aluguel {

	@Column(name = "tipo_forro")
	@Enumerated(EnumType.STRING)
	private TipoForro tipoForro;
	
	@Column(name = "tipo_piso")
	@Enumerated(EnumType.STRING)
	private TipoPiso tipoPiso;
	
	@Column(name="quantidade_quartos")
	private Integer quantidadeQuartos;
	
	@Column(name="quantidade_banheiros")
	private Integer quantidadeBanheiros;
	
	@Column(name="suites")
	private Integer suites;
	
	@Column(name="vagas_garagem")
	private Integer vagasGaragem;

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

	public TipoForro getTipoForro() {
		return tipoForro;
	}

	public void setTipoForro(TipoForro tipoForro) {
		this.tipoForro = tipoForro;
	}

	public TipoPiso getTipoPiso() {
		return tipoPiso;
	}

	public void setTipoPiso(TipoPiso tipoPiso) {
		this.tipoPiso = tipoPiso;
	}

	public Integer getSuites() {
		return suites;
	}

	public void setSuites(Integer suites) {
		this.suites = suites;
	}

	public Integer getVagasGaragem() {
		return vagasGaragem;
	}

	public void setVagasGaragem(Integer vagasGaragem) {
		this.vagasGaragem = vagasGaragem;
	}

	


}
