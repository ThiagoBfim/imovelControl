package br.com.imovelcontrol.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import br.com.imovelcontrol.model.enuns.TipoForro;
import br.com.imovelcontrol.model.enuns.TipoImovel;
import br.com.imovelcontrol.model.enuns.TipoPiso;
import org.hibernate.validator.constraints.NotBlank;

@Entity
public class Aluguel extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long codigo;

    @NotBlank(message = "Nome Obrigatório")
    private String nome;

    private String complemento;

    @ManyToOne
    @JoinColumn(name = "codigo_imovel")
    private Imovel imovel;

    private Boolean excluido;

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

    @Column(name = "quantidade_quartos")
    private Integer quantidadeQuartos;

    @Column(name = "quantidade_banheiros")
    private Integer quantidadeBanheiros;

    @Column(name = "suites")
    private Integer suites;

    @Column(name = "vagas_garagem")
    private Integer vagasGaragem;

    private Integer tamanhoArea;

    public Long getCodigo() {
        return codigo;
    }

    public void setCodigo(Long codigo) {
        this.codigo = codigo;
    }

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
}
