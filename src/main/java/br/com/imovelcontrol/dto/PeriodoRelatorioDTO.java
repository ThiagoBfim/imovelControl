package br.com.imovelcontrol.dto;

import java.time.LocalDate;

import br.com.imovelcontrol.model.Imovel;

public class PeriodoRelatorioDTO {

    private Imovel imovel;
    private LocalDate dataInicio;
    private LocalDate dataFim;

    public Imovel getImovel() {
        return imovel;
    }

    public void setImovel(Imovel imovel) {
        this.imovel = imovel;
    }

    public LocalDate getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(LocalDate dataInicio) {
        this.dataInicio = dataInicio;
    }

    public LocalDate getDataFim() {
        return dataFim;
    }

    public void setDataFim(LocalDate dataFim) {
        this.dataFim = dataFim;
    }
}
