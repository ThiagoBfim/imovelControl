package br.com.imovelcontrol.dto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Usuario on 03/09/2017.
 */
public class SubRelatorioDetalhadoImovelDTO {

    private Date dataMensal;
    private Boolean aguaInclusa;
    private Boolean internetInclusa;
    private Boolean iptuIncluso;
    private Boolean possuiCondominio;
    private Boolean luzInclusa;
    private BigDecimal valorAluguel;
    private Long codigoPagamento;
    private List<GastosDetalhadoDTO> listaGastos = new ArrayList<>();


    public Date getDataMensal() {
        return dataMensal;
    }

    public void setDataMensal(Date dataMensal) {
        this.dataMensal = dataMensal;
    }

    public Boolean getInternetInclusa() {
        return internetInclusa;
    }

    public void setInternetInclusa(Boolean internetInclusa) {
        this.internetInclusa = internetInclusa;
    }

    public Boolean getIptuIncluso() {
        return iptuIncluso;
    }

    public void setIptuIncluso(Boolean iptuIncluso) {
        this.iptuIncluso = iptuIncluso;
    }

    public Boolean getPossuiCondominio() {
        return possuiCondominio;
    }

    public void setPossuiCondominio(Boolean possuiCondominio) {
        this.possuiCondominio = possuiCondominio;
    }

    public Boolean getLuzInclusa() {
        return luzInclusa;
    }

    public void setLuzInclusa(Boolean luzInclusa) {
        this.luzInclusa = luzInclusa;
    }

    public BigDecimal getValorAluguel() {
        return valorAluguel;
    }

    public void setValorAluguel(BigDecimal valorAluguel) {
        this.valorAluguel = valorAluguel;
    }

    public Boolean getAguaInclusa() {
        return aguaInclusa;
    }

    public void setAguaInclusa(Boolean aguaInclusa) {
        this.aguaInclusa = aguaInclusa;
    }

    public List<GastosDetalhadoDTO> getListaGastos() {
        return listaGastos;
    }

    public void setListaGastos(List<GastosDetalhadoDTO> listaGastos) {
        this.listaGastos = listaGastos;
    }

    public Long getCodigoPagamento() {
        return codigoPagamento;
    }

    public void setCodigoPagamento(Long codigoPagamento) {
        this.codigoPagamento = codigoPagamento;
    }
}
