package br.com.imovelcontrol.model;

import java.time.LocalDate;
import java.time.Month;
import java.util.Date;
import java.util.List;

/**
 * Created by marcosfellipec on 24/09/17.
 */
//FIXME QUE MERDA É ESSA??? TIRA ISSO DAQUI, COLOCA NA PASTA DTO.
public class GraficoColunaImovelDTO {
    private LocalDate data;

    public LocalDate getData() {
        return data;
    }
    public String nome;

    public double valor;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

}
