package br.com.imovelcontrol.model;

import java.time.LocalDate;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.Size;

import br.com.imovelcontrol.controller.converter.FormatUtil;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.br.CPF;

/**
 * Created by marcosfellipec on 18/05/17.
 */
@Entity
@Table(name = "locatario")
public class Locatario extends BaseEntity {


    @Size(max = 100, message = "Nome deve ter no máximo 100 caracteres")
    @NotBlank(message = "Nome Obrigatório")
    private String nome;

    @CPF(message = "CPF Inválido")
    @Size(max = 11, message = "CPF deve ter no máximo 11 caracteres")
    @NotBlank(message = "CPF Obrigatório")
    private String cpf;

    @Size(min = 10, max = 11, message = "Número de telefone inválido.")
    @NotBlank(message = "Telefone Obrigatório")
    private String telefone;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "codigo_aluguel")
    private Aluguel aluguel = new Aluguel();

    private Boolean excluido = Boolean.FALSE;

    private LocalDate dataInicio;

    private LocalDate dataFim;

    @Transient
    private String dataInicioJson;

    @PrePersist
    @PreUpdate
    private void prePersistUpdate() {
        this.setCpf(FormatUtil.removerMascara(this.getCpf()));
        this.setTelefone(FormatUtil.removerMascara(this.getTelefone()));
    }

    public Aluguel getAluguel() {
        return aluguel;
    }

    public void setAluguel(Aluguel aluguel) {
        this.aluguel = aluguel;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Boolean getExcluido() {
        return excluido;
    }

    public void setExcluido(Boolean excluido) {
        this.excluido = excluido;
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

    public String getDataInicioJson() {
        return dataInicioJson;
    }

    public void setDataInicioJson(String dataInicioJson) {
        this.dataInicioJson = dataInicioJson;
    }
}
