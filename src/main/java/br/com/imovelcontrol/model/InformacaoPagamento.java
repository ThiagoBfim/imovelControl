package br.com.imovelcontrol.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * Created by Thiago on 09/05/2017.
 */
@Entity
@Table(name = "INFORMACAO_PAGAMENTO")
public class InformacaoPagamento extends TemplateFormaPagamento {

    private Boolean pago;

    private LocalDate dataMensal;

    private LocalDate dataPagamento;

    private BigDecimal multa = BigDecimal.ZERO;

    private Boolean atrasado = Boolean.FALSE;

    @Transient
    private Locatario locatario = new Locatario();

    @OneToOne
    @JoinColumn(name = "codigo_aluguel")
    private Aluguel aluguel = new Aluguel();

    @Transient
    private GastoAdicional gastoAdicional;

    @Transient
    private boolean estaAlugado;

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

    public boolean isEstaAlugado() {
        return estaAlugado;
    }

    public void setEstaAlugado(boolean estaAlugado) {
        this.estaAlugado = estaAlugado;
    }

    public LocalDate getDataPagamento() {
        return dataPagamento;
    }

    public void setDataPagamento(LocalDate dataPagamento) {
        this.dataPagamento = dataPagamento;
    }

    public BigDecimal getMulta() {
        return multa;
    }

    public void setMulta(BigDecimal multa) {
        this.multa = multa;
    }

    public Boolean getAtrasado() {
        return atrasado;
    }

    public void setAtrasado(Boolean atrasado) {
        this.atrasado = atrasado;
    }

    public Locatario getLocatario() {
        return locatario;
    }

    public void setLocatario(Locatario locatario) {
        this.locatario = locatario;
    }
}

