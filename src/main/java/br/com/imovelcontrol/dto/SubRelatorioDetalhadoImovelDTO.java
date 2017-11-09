package br.com.imovelcontrol.dto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.util.CollectionUtils;

/**
 * Created by Thiago on 03/09/2017.
 */
public class SubRelatorioDetalhadoImovelDTO {

    private Date dataMensal;
    private Date dataPagamento;
    private Boolean aguaInclusa;
    private Boolean internetInclusa;
    private Boolean iptuIncluso;
    private Boolean possuiCondominio;
    private Boolean luzInclusa;
    private BigDecimal valorAluguel;
    private BigDecimal multa;
    private Long codigoPagamento;
    private Boolean pago;
    private String nome;
    private List<GastosDetalhadoDTO> listaGastos = new ArrayList<>();

    public BigDecimal getLucroMensal(){
        BigDecimal lucro = BigDecimal.ZERO;
        if(pago != null && pago){
            lucro = lucro.add(valorAluguel);
        }
        lucro = lucro.add(multa);
        if(!CollectionUtils.isEmpty(listaGastos)) {
            for (GastosDetalhadoDTO gasto : listaGastos) {
                if(gasto.getGasto() != null) {
                    lucro = lucro.subtract(gasto.getGasto());
                }
            }
        }
        return lucro;
    }

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

    public Boolean getPago() {
        return pago;
    }

    public void setPago(Boolean pago) {
        this.pago = pago;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public BigDecimal getMulta() {
        return multa;
    }

    public void setMulta(BigDecimal multa) {
        this.multa = multa;
    }

    public Date getDataPagamento() {
        return dataPagamento;
    }

    public void setDataPagamento(Date dataPagamento) {
        this.dataPagamento = dataPagamento;
    }
}
