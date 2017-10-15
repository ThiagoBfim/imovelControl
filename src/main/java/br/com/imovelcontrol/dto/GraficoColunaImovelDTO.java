package br.com.imovelcontrol.dto;

import br.com.imovelcontrol.model.InformacaoPagamento;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by marcosfellipec on 24/09/17.
 */

public class GraficoColunaImovelDTO {
   private Integer mes;
   private BigDecimal valor;
   private String nome;

    public Integer getMes() {
        return mes;
    }

    public void setMes(Integer data) {
        this.mes = data;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof GraficoColunaImovelDTO)) return false;

        GraficoColunaImovelDTO that = (GraficoColunaImovelDTO) o;

        return mes != null ? mes.equals(that.mes) : that.mes == null;
    }

    @Override
    public int hashCode() {
        return mes != null ? mes.hashCode() : 0;
    }
}
