package br.com.imovelcontrol.dto;

/**
 * Created by Thiago on 01/06/2017.
 */
public class AluguelGraficoDTO {

    private String nomeImovel;
    private double rendimento;

    public double getRendimento() {
        return rendimento;
    }

    public void setRendimento(double rendimento) {
        this.rendimento = rendimento;
    }

    public String getNomeImovel() {
        return nomeImovel;
    }

    public void setNomeImovel(String nomeImovel) {
        this.nomeImovel = nomeImovel;
    }
}
