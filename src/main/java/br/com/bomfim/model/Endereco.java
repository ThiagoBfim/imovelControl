package br.com.bomfim.model;

import java.io.Serializable;

import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotBlank;

import br.com.bomfim.model.enuns.Estado;

@SuppressWarnings("serial")
@Embeddable
public class Endereco implements Serializable {

	@NotBlank(message = "CEP Obrigatório")
	private String cep;
	
	@NotBlank(message = "Cidade Obrigatório")
	private String cidade;


	@NotBlank(message = "Bairro Obrigatório")
	@Size(min = 5, max = 100, message = "O tamanho do bairro deve estar entre 5 e 100")
	private String bairro;
	
	@NotNull(message = "Estado Obrigatório")
	@Enumerated(EnumType.STRING)
	private Estado estado;
	
	@Size(min = 5, max = 100, message = "O tamanho do complemento deve estar entre 5 e 150")
	private String complemento;

	public String getCep() {
		return cep;
	}

	public void setCep(String cep) {
		this.cep = cep;
	}

	public String getCidade() {
		return cidade;
	}

	public void setCidade(String cidade) {
		this.cidade = cidade;
	}

	public String getBairro() {
		return bairro;
	}

	public void setBairro(String bairro) {
		this.bairro = bairro;
	}

	public Estado getEstado() {
		return estado;
	}

	public void setEstado(Estado estado) {
		this.estado = estado;
	}

	public String getComplemento() {
		return complemento;
	}

	public void setComplemento(String complemento) {
		this.complemento = complemento;
	}	
	
	
}
