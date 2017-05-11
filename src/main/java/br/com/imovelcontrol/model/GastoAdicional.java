package br.com.imovelcontrol.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Created by Usuario on 09/05/2017.
 */
@Entity
@Table(name = "gasto_adicional")
public class GastoAdicional implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long codigo;

    private LocalDate dataMensal;

    private String comentarioGasto;

    private BigDecimal valorGasto;

    @ManyToOne
    @JoinColumn(name="codPagamento")
    private InformacaoPagamento informacaoPagamento;

    public Long getCodigo() {
        return codigo;
    }

    public void setCodigo(Long codigo) {
        this.codigo = codigo;
    }

    public LocalDate getDataMensal() {
        return dataMensal;
    }

    public void setDataMensal(LocalDate dataMensal) {
        this.dataMensal = dataMensal;
    }

    public String getComentarioGasto() {
        return comentarioGasto;
    }

    public void setComentarioGasto(String comentarioGasto) {
        this.comentarioGasto = comentarioGasto;
    }

    public BigDecimal getValorGasto() {
        return valorGasto;
    }

    public void setValorGasto(BigDecimal valorGasto) {
        this.valorGasto = valorGasto;
    }

    public InformacaoPagamento getInformacaoPagamento() {
        return informacaoPagamento;
    }

    public void setInformacaoPagamento(InformacaoPagamento informacaoPagamento) {
        this.informacaoPagamento = informacaoPagamento;
    }
}
