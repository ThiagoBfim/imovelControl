package br.com.imovelcontrol.model;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import br.com.imovelcontrol.model.enuns.TipoForro;
import br.com.imovelcontrol.model.enuns.TipoImovel;
import br.com.imovelcontrol.model.enuns.TipoPiso;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.util.CollectionUtils;

@Entity
@Table(name = "ALUGUEL")
public class Aluguel extends BaseEntity {

    @NotBlank(message = "Nome Obrigatório")
    @Size(max = 100, message = "Nome deve ter no máximo 100 caracteres")
    private String nome;

    @Size(max = 200, message = "Nome deve ter no máximo 200 caracteres")
    private String complemento;

    @ManyToOne
    @JoinColumn(name = "codigo_imovel")
    private Imovel imovel;

    private Boolean excluido = Boolean.FALSE;

    @Valid
    @ManyToOne
    @JoinColumn(name = "codigo_forma_pagamento")
    private FormaPagamento formaPagamento;

    @Column(name = "tipo_imovel")
    @NotNull(message = "Tipo do Imovel Obrigatório")
    @Enumerated(EnumType.STRING)
    private TipoImovel tipoImovel;

    @Column(name = "tipo_forro")
    @Enumerated(EnumType.STRING)
    private TipoForro tipoForro;

    @Column(name = "tipo_piso")
    @Enumerated(EnumType.STRING)
    private TipoPiso tipoPiso;

    @Max(value = 99, message = "Quantidade máxima de quartos é 99")
    @Column(name = "quantidade_quartos")
    private Integer quantidadeQuartos;

    @Max(value = 99, message = "Quantidade máxima de banheiros é 99")
    @Column(name = "quantidade_banheiros")
    private Integer quantidadeBanheiros;

    @Max(value = 99, message = "Quantidade máxima de suítes é 99")
    @Column(name = "suites")
    private Integer suites;

    @Max(value = 99, message = "Quantidade máxima de vagas na garagem é 99")
    @Column(name = "vagas_garagem")
    private Integer vagasGaragem;

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "aluguel")
    private Locatario locatario;

    @OneToMany(mappedBy = "aluguel")
    private List<InformacaoPagamento> informacaoPagamentoList = new ArrayList<>();

    private Integer tamanhoArea;

    public String getComplemento() {
        return complemento;
    }

    public void setComplemento(String complemento) {
        this.complemento = complemento;
    }

    public Imovel getImovel() {
        return imovel;
    }

    public void setImovel(Imovel imovel) {
        this.imovel = imovel;
    }

    public FormaPagamento getFormaPagamento() {
        return formaPagamento;
    }

    public void setFormaPagamento(FormaPagamento formaPagamento) {
        this.formaPagamento = formaPagamento;
    }

    public TipoImovel getTipoImovel() {
        return tipoImovel;
    }

    public void setTipoImovel(TipoImovel tipoImovel) {
        this.tipoImovel = tipoImovel;
    }

    public Integer getTamanhoArea() {
        return tamanhoArea;
    }

    public void setTamanhoArea(Integer tamanhoArea) {
        this.tamanhoArea = tamanhoArea;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public boolean isNovo() {
        return getCodigo() == null;
    }

    public TipoForro getTipoForro() {
        return tipoForro;
    }

    public void setTipoForro(TipoForro tipoForro) {
        this.tipoForro = tipoForro;
    }

    public TipoPiso getTipoPiso() {
        return tipoPiso;
    }

    public void setTipoPiso(TipoPiso tipoPiso) {
        this.tipoPiso = tipoPiso;
    }

    public Integer getQuantidadeQuartos() {
        return quantidadeQuartos;
    }

    public void setQuantidadeQuartos(Integer quantidadeQuartos) {
        this.quantidadeQuartos = quantidadeQuartos;
    }

    public Integer getQuantidadeBanheiros() {
        return quantidadeBanheiros;
    }

    public void setQuantidadeBanheiros(Integer quantidadeBanheiros) {
        this.quantidadeBanheiros = quantidadeBanheiros;
    }

    public Integer getSuites() {
        return suites;
    }

    public void setSuites(Integer suites) {
        this.suites = suites;
    }

    public Integer getVagasGaragem() {
        return vagasGaragem;
    }

    public void setVagasGaragem(Integer vagasGaragem) {
        this.vagasGaragem = vagasGaragem;
    }

    public Boolean getExcluido() {
        return excluido;
    }

    public void setExcluido(Boolean excluido) {
        this.excluido = excluido;
    }

    public boolean isAlugado() {
        return this.locatario != null;
    }

    public Locatario getLocatario() {
        return locatario;
    }

    public void setLocatario(Locatario locatario) {
        this.locatario = locatario;
    }

    public List<InformacaoPagamento> getInformacaoPagamentoList() {
        return informacaoPagamentoList;
    }

    public void setInformacaoPagamentoList(List<InformacaoPagamento> informacaoPagamentoList) {
        this.informacaoPagamentoList = informacaoPagamentoList;
    }

    public boolean isPago() {
        if (!CollectionUtils.isEmpty(getInformacaoPagamentoList())) {
            InformacaoPagamento informacaoPagamento = getInformacaoPagamentoList().get(getInformacaoPagamentoList().size() - 1);
            if (informacaoPagamento.getPago() != null) {
                return informacaoPagamento.getPago();
            }
        }
        return false;
    }
}
