package br.com.imovelcontrol.dto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Thiago on 01/06/2017.
 */
public class RelatorioDetalhadoImovelDTO {

    private String nome;
    private String cep;
    private String nomeAluguel;
    private Long codigoAluguel;
    private List<SubRelatorioDetalhadoImovelDTO> subRelatorioDetalhadoImovelDTOS = new ArrayList<>();
    private Boolean estaDesalugado = Boolean.TRUE;
    private Boolean excluido = Boolean.TRUE;
    private BigDecimal lucro = BigDecimal.ZERO;

    /*Método chamado pelo relatorio, para obter o lucroTotal dealhado.*/
    public BigDecimal getLucroTotal() {
        subRelatorioDetalhadoImovelDTOS.stream().forEach(s -> {
            if (s.getPago() != null && s.getPago() != Boolean.FALSE) {
                lucro = lucro.add(s.getValorAluguel());
            }
            s.getListaGastos().stream().filter(g -> g.getGasto() != null)
                    .forEach(g -> lucro = lucro.subtract(g.getGasto()));
        });
        return lucro;
    }

    public RelatorioDetalhadoImovelDTO() {
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

    public String getNomeAluguel() {
        return nomeAluguel;
    }

    public void setNomeAluguel(String nomeAluguel) {
        this.nomeAluguel = nomeAluguel;
    }

    public List<SubRelatorioDetalhadoImovelDTO> getSubRelatorioDetalhadoImovelDTOS() {
        return subRelatorioDetalhadoImovelDTOS;
    }

    public void setSubRelatorioDetalhadoImovelDTOS(List<SubRelatorioDetalhadoImovelDTO> subRelatorioDetalhadoImovelDTOS) {
        this.subRelatorioDetalhadoImovelDTOS = subRelatorioDetalhadoImovelDTOS;
    }

    public Long getCodigoAluguel() {
        return codigoAluguel;
    }

    public void setCodigoAluguel(Long codigoAluguel) {
        this.codigoAluguel = codigoAluguel;
    }

    public Boolean getEstaDesalugado() {
        return estaDesalugado;
    }

    public void setEstaDesalugado(Boolean estaDesalugado) {
        this.estaDesalugado = estaDesalugado;
    }

    public Boolean getExcluido() {
        return excluido;
    }

    public void setExcluido(Boolean excluido) {
        this.excluido = excluido;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RelatorioDetalhadoImovelDTO)) return false;

        RelatorioDetalhadoImovelDTO that = (RelatorioDetalhadoImovelDTO) o;

        return getNomeAluguel().equals(that.getNomeAluguel());
    }

    @Override
    public int hashCode() {
        return getNomeAluguel().hashCode();
    }
}
