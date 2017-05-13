package br.com.imovelcontrol.model;

import java.math.BigDecimal;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "template_forma_pagamento")
public class FormaPagamento extends TemplateFormaPagamento {


    @NotNull(message = "Valor do aluguel é obrigatório")
    @Override
    public BigDecimal getValor() {
        return super.getValor();
    }

}
