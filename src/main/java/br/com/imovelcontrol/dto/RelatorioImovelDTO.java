package br.com.imovelcontrol.dto;

import java.math.BigDecimal;

import br.com.imovelcontrol.controller.converter.FormatUtil;

/**
 * Created by Usuario on 01/06/2017.
 */
public class RelatorioImovelDTO {

    private String nome;
    private String cep;
    private BigDecimal recebimento = BigDecimal.ZERO;
    private BigDecimal gastos = BigDecimal.ZERO;

    public RelatorioImovelDTO() {
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

    public BigDecimal getRecebimento() {
        return recebimento;
    }

    public void setRecebimento(BigDecimal recebimento) {
        this.recebimento = recebimento;
    }

    public BigDecimal getGastos() {
        if (gastos == null) {
            gastos = BigDecimal.ZERO;
        }
        return gastos;
    }

    public void setGastos(BigDecimal gastos) {
        this.gastos = gastos;
    }

    public BigDecimal getSomaTotal() {
        return recebimento.subtract(getGastos());
    }

    public String getRecebimentoFormat() {
        return FormatUtil.formatBigDecimal(recebimento);
    }

    public String getGastosFormat() {
        return FormatUtil.formatBigDecimal(gastos);
    }
}
