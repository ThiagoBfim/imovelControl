package br.com.imovelcontrol.model.tipoimovel;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import br.com.imovelcontrol.model.FormaPagamento;
import br.com.imovelcontrol.model.Imovel;
import br.com.imovelcontrol.model.enuns.TipoImovel;
import org.hibernate.validator.constraints.NotBlank;

@SuppressWarnings("serial")
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "tipo", length = 2, discriminatorType = DiscriminatorType.STRING)
@DiscriminatorValue("AL")
public class Aluguel implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long codigo;

	@NotBlank(message = "Nome Obrigatório")
	private String nome;

	private String complemento;

	@ManyToOne
	@JoinColumn(name = "codigo_imovel")
	private Imovel imovel;

	@Valid
	@ManyToOne
	@JoinColumn(name = "codigo_forma_pagamento")
	private FormaPagamento formaPagamento;

	@Column(name = "tipo_imovel")
	@NotNull(message = "Tipo do Imovel Obrigatório")
	@Enumerated(EnumType.STRING)
	private TipoImovel tipoImovel;

	private Long tamanhoArea;

	public Long getCodigo() {
		return codigo;
	}

	public void setCodigo(Long codigo) {
		this.codigo = codigo;
	}

	public String getComplemento() {
		return complemento;
	}

	public void setComplemento(String complemento) {
		this.complemento = complemento;
	}

	public Imovel getImovel() {
		return imovel;
	}

	public void setImovel(Imovel imovel) {
		this.imovel = imovel;
	}

	public FormaPagamento getFormaPagamento() {
		return formaPagamento;
	}

	public void setFormaPagamento(FormaPagamento formaPagamento) {
		this.formaPagamento = formaPagamento;
	}

	public TipoImovel getTipoImovel() {
		return tipoImovel;
	}

	public void setTipoImovel(TipoImovel tipoImovel) {
		this.tipoImovel = tipoImovel;
	}

	public Long getTamanhoArea() {
		return tamanhoArea;
	}

	public void setTamanhoArea(Long tamanhoArea) {
		this.tamanhoArea = tamanhoArea;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public boolean isNovo() {
		return getCodigo() == null;
	}

}
