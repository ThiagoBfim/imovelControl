package br.com.imovelcontrol.model;

import java.io.Serializable;
import javax.persistence.Embeddable;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotBlank;

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


    @NotBlank(message = "Rua Obrigatório")
    @Size(min = 5, max = 60, message = "O tamanho do bairro deve estar entre 5 e 60")
    private String rua;

    @NotBlank(message = "Estado Obrigatório")
    @Size(min = 2, max = 2, message = "O tamanho do Estado deve ser 2 caracteres ")
    private String uf;

    @Size(max = 150, message = "O tamanho máximo do complemento é de 150 caracteres")
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

    public String getRua() {
        return rua;
    }

    public void setRua(String rua) {
        this.rua = rua;
    }

    public String getUf() {
        return uf;
    }

    public void setUf(String uf) {
        this.uf = uf;
    }

    public String getComplemento() {
        return complemento;
    }

    public void setComplemento(String complemento) {
        this.complemento = complemento;
    }


}
