package br.com.imovelcontrol.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import br.com.imovelcontrol.model.tipoimovel.Aluguel;

/**
 * Created by Usuario on 09/05/2017.
 */
@Entity
@Table(name = "informacao_pagamento")
public class InformacaoPagamento extends TemplateFormaPagamento {

    private Boolean pago;

    private LocalDate dataMensal;

    @OneToOne
    @JoinColumn(name = "codigo_aluguel")
    private Aluguel aluguel;

    @OneToMany(mappedBy = "informacaoPagamento")
    private List<GastoAdicional> gastosAdicionais = new ArrayList<>();

    @Transient
    private GastoAdicional gastoAdicional;

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

    public GastoAdicional getGastoAdicional() {
        return gastoAdicional;
    }

    public void setGastoAdicional(GastoAdicional gastoAdicional) {
        this.gastoAdicional = gastoAdicional;
    }

    public Aluguel getAluguel() {
        return aluguel;
    }

    public void setAluguel(Aluguel aluguel) {
        this.aluguel = aluguel;
    }
}

