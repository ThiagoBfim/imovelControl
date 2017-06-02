package br.com.imovelcontrol.dto;

import java.math.BigDecimal;

/**
 * Created by Usuario on 01/06/2017.
 */
public class RelatorioImovelDTO {

    private String nome;
    private String cep;
    private BigDecimal recimento;
    private BigDecimal gastos;
    private BigDecimal valorTotal;

    public RelatorioImovelDTO(String nome, String cep) {
        this.nome = nome;
        this.cep = cep;
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

    public BigDecimal getRecimento() {
        return recimento;
    }

    public void setRecimento(BigDecimal recimento) {
        this.recimento = recimento;
    }

    public BigDecimal getGastos() {
        return gastos;
    }

    public void setGastos(BigDecimal gastos) {
        this.gastos = gastos;
    }

    public BigDecimal getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(BigDecimal valorTotal) {
        this.valorTotal = valorTotal;
    }
}
