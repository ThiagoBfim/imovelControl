package br.com.imovelcontrol.dto;

import java.util.ArrayList;
import java.util.List;

import br.com.imovelcontrol.model.InformacaoPagamento;

/**
 * Created by Usuario on 08/11/2017.
 */
public class InformacaoPagamentoModalDTO {

    private List<InformacaoPagamento> informacaoPagamentoList = new ArrayList<>();
    private InformacaoPagamento informacaoPagamento = new InformacaoPagamento();

    public List<InformacaoPagamento> getInformacaoPagamentoList() {
        return informacaoPagamentoList;
    }

    public void setInformacaoPagamentoList(List<InformacaoPagamento> informacaoPagamentoList) {
        this.informacaoPagamentoList = informacaoPagamentoList;
    }

    public InformacaoPagamento getInformacaoPagamento() {
        return informacaoPagamento;
    }

    public void setInformacaoPagamento(InformacaoPagamento informacaoPagamento) {
        this.informacaoPagamento = informacaoPagamento;
    }
}
