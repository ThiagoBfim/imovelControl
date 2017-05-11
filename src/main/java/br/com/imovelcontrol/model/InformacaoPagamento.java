package br.com.imovelcontrol.model;

import java.time.LocalDate;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * Created by Usuario on 09/05/2017.
 */
@Entity
@Table(name = "informacao_pagamento")
public class InformacaoPagamento extends TemplateFormaPagamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long codigo;

    private Boolean pago;

    private LocalDate dataMensal;

    @OneToMany(mappedBy = "informacaoPagamento")
    private List<GastoAdicional> gastosAdicionais;

    public Long getCodigo() {
        return codigo;
    }

    public void setCodigo(Long codigo) {
        this.codigo = codigo;
    }

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

