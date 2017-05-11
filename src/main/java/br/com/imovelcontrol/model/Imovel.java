package br.com.imovelcontrol.model;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.Valid;

import org.hibernate.validator.constraints.NotBlank;
import org.springframework.util.StringUtils;

@SuppressWarnings("serial")
@Entity
@Table(name = "imovel")
public class Imovel implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long codigo;

	@NotBlank(message = "Nome Obrigatório")
	private String nome;

	@Embedded
	@Valid
	private Endereco endereco;

	private String foto;

	@Column(name = "content_type")
	private String contentType;
	
	public Long getCodigo() {
		return codigo;
	}

	public void setCodigo(Long codigo) {
		this.codigo = codigo;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getFoto() {
		return foto;
	}

	public String getFotoOrMock() {
		return !StringUtils.isEmpty(foto) ? foto : "casa.png";
	}

	public void setFoto(String foto) {
		this.foto = foto;
	}

	public Endereco getEndereco() {
		return endereco;
	}

	public void setEndereco(Endereco endereco) {
		this.endereco = endereco;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public boolean isNovo() {
		return getCodigo() == null;
	}

}
