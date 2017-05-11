package br.com.imovelcontrol.model;

import java.math.BigDecimal;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "template_forma_pagamento")
public class FormaPagamento extends TemplateFormaPagamento {

    //TODO VERIFICAR SE VAI FUNCIONAR.
	private Long codigo;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long getCodigo() {
		return codigo;
	}

	public void setCodigo(Long codigo) {
		this.codigo = codigo;
	}

    @NotNull(message = "Valor do aluguel é obrigatório")
    @Override
    public BigDecimal getValor() {
        return super.getValor();
    }

}
