package br.com.imovelcontrol.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Usuario on 01/06/2017.
 */
public class RelatorioDetalhadoImovelDTO {

    private String nome;
    private String cep;
    private String nomeAluguel;
    private Long codigoAluguel;
    private List<SubRelatorioDetalhadoImovelDTO> subRelatorioDetalhadoImovelDTOS = new ArrayList<>();


    public RelatorioDetalhadoImovelDTO() {
        //Construtor Vazio
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public String getNomeAluguel() {
        return nomeAluguel;
    }

    public void setNomeAluguel(String nomeAluguel) {
        this.nomeAluguel = nomeAluguel;
    }

    public List<SubRelatorioDetalhadoImovelDTO> getSubRelatorioDetalhadoImovelDTOS() {
        return subRelatorioDetalhadoImovelDTOS;
    }

    public void setSubRelatorioDetalhadoImovelDTOS(List<SubRelatorioDetalhadoImovelDTO> subRelatorioDetalhadoImovelDTOS) {
        this.subRelatorioDetalhadoImovelDTOS = subRelatorioDetalhadoImovelDTOS;
    }

    public Long getCodigoAluguel() {
        return codigoAluguel;
    }

    public void setCodigoAluguel(Long codigoAluguel) {
        this.codigoAluguel = codigoAluguel;
    }
}
