package br.com.imovelcontrol.dto;

import java.math.BigDecimal;

/**
 * Created by Thiago on 03/09/2017.
 */
public class GastosDetalhadoDTO {

    private BigDecimal gasto;
    private String descricao;

    public BigDecimal getGasto() {
        return gasto;
    }

    public void setGasto(BigDecimal gasto) {
        this.gasto = gasto;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
}
