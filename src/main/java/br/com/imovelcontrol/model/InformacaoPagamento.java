package br.com.imovelcontrol.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * Created by Usuario on 09/05/2017.
 */
@Entity
@Table(name = "informacao_pagamento")
public class InformacaoPagamento extends TemplateFormaPagamento {

    private Boolean pago;

    private LocalDate dataMensal;

    @OneToMany(mappedBy = "informacaoPagamento")
    private List<GastoAdicional> gastosAdicionais = new ArrayList<>();


    public Boolean getPago() {
        return pago;
    }

    public void setPago(Boolean pago) {
        this.pago = pago;
    }

    public LocalDate getDataMensal() {
        return dataMensal;
    }

    public void setDataMensal(LocalDate dataMensal) {
        this.dataMensal = dataMensal;
    }

    public List<GastoAdicional> getGastosAdicionais() {
        return gastosAdicionais;
    }

    public void setGastosAdicionais(List<GastoAdicional> gastosAdicionais) {
        this.gastosAdicionais = gastosAdicionais;
    }
}

