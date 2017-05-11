package br.com.imovelcontrol.model;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by Usuario on 09/05/2017.
 */
public class TemplateFormaPagamento implements Serializable {

    private BigDecimal valor;

    private Boolean aguaInclusa;

    private Boolean luzInclusa;

    private Boolean internetInclusa;

    private Boolean iptuIncluso;

    private Boolean possuiCondominio;

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public Boolean getAguaInclusa() {
        return aguaInclusa;
    }

    public void setAguaInclusa(Boolean aguaInclusa) {
        this.aguaInclusa = aguaInclusa;
    }

    public Boolean getLuzInclusa() {
        return luzInclusa;
    }

    public void setLuzInclusa(Boolean luzInclusa) {
        this.luzInclusa = luzInclusa;
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
}
