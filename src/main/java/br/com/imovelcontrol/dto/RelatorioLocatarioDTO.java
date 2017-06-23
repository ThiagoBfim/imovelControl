package br.com.imovelcontrol.dto;

/**
 * Created by marcosfellipec on 15/06/17.
 */
public class RelatorioLocatarioDTO {

    private String nome;
    private String cpf;

    public RelatorioLocatarioDTO (String nome, String cpf){
        this.nome = nome;
        this.cpf = cpf;
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
}
