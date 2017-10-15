package br.com.imovelcontrol.dto;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by marcosfellipec on 14/10/17.
 */
public class GraficoColunaAgrupadorDTO {

    private Set<Integer> meses = new LinkedHashSet<>();
    private Set<String> listaNomeImoveis = new LinkedHashSet<>();
    private List<GraficoColunaImovelDTO> valores = new ArrayList<>();
    private List<String> resultadoGrafico = new ArrayList<>();


    public Set<Integer> getMeses() {
        return meses;
    }

    public void setMeses(Set<Integer> meses) {
        this.meses = meses;
    }

    public Set<String> getListaNomeImoveis() {
        return listaNomeImoveis;
    }

    public void setListaNomeImoveis(Set<String> listaNomeImoveis) {
        this.listaNomeImoveis = listaNomeImoveis;
    }


    public List<GraficoColunaImovelDTO> getValores() {
        return valores;
    }

    public void setValores(List<GraficoColunaImovelDTO> valores) {
        this.valores = valores;
    }

    public List<String> getResultadoGrafico() {
        return resultadoGrafico;
    }

    public void setResultadoGrafico(List<String> resultadoGrafico) {
        this.resultadoGrafico = resultadoGrafico;
    }
}
