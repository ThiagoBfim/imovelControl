package br.com.imovelcontrol.model;

import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;

/**
 * Created by marcosfellipec on 18/05/17.
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "locatario")
public class Locatario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long codigo;

    @NotBlank(message = "Nome Obrigatório")
    private String nome;

    @NotBlank(message = "CPF Obrigatório")
    private String cpf;

    private String fotoComprovanteRenda;

    private String contentTypeComprovanteRenda;

    private String fotoContrato;

    private String contentTypeContrato;


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

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getFotoComprovanteRenda() {
        return fotoComprovanteRenda;
    }

    public void setFotoComprovanteRenda(String fotoComprovanteRenda) {
        this.fotoComprovanteRenda = fotoComprovanteRenda;
    }

    public String getContentTypeComprovanteRenda() {
        return contentTypeComprovanteRenda;
    }

    public void setContentTypeComprovanteRenda(String contentTypeComprovanteRenda) {
        this.contentTypeComprovanteRenda = contentTypeComprovanteRenda;
    }

    public String getFotoContrato() {
        return fotoContrato;
    }

    public void setFotoContrato(String fotoContrato) {
        this.fotoContrato = fotoContrato;
    }

    public String getContentTypeContrato() {
        return contentTypeContrato;
    }

    public void setContentTypeContrato(String contentTypeContrato) {
        this.contentTypeContrato = contentTypeContrato;
    }
}
