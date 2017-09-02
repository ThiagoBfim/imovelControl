package br.com.imovelcontrol.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.Size;

import br.com.imovelcontrol.validation.AtributoConfirmacao;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

@AtributoConfirmacao(atributo = "senha", atributoConfirmacao = "confirmacaoSenha", message = "Confirmação da senha não confere")
@Entity
@Table(name = "usuario")
public class Usuario extends BaseEntity {

    @NotBlank(message = "Login Obrigatório")
    @Size(max = 100, message = "Login deve ter no máximo 100 caracteres")
    private String login;

    @NotBlank(message = "Nome Obrigatório")
    @Size(max = 100, message = "Nome deve ter no máximo 100 caracteres")
    private String nome;

    @Email
    @NotBlank(message = "E-mail Obrigatório")
    @Size(max = 100, message = "Nome deve ter no máximo 100 caracteres")
    private String email;

    @Size(min = 6, max = 120, message = "Senha deve ter no máximo 30 caracteres e no mínimo 6")
    private String senha;

    @Transient
    private String confirmacaoSenha;

    private Boolean ativo = Boolean.TRUE;

    @Column(name = "data_nascimento")
    private LocalDate dataNascimento;

    @Size(min = 1, message = "Selecione pelo menos um grupo")
    @ManyToMany
    @JoinTable(name = "usuario_grupo",
            joinColumns = @JoinColumn(name = "codigo_usuario"),
            inverseJoinColumns = @JoinColumn(name = "codigo_grupo"))
    private List<Grupo> grupos;

    @NotBlank(message = "Código Verificador Obrigatório")
    @Size(max = 20, message = "Código Verificador deve ter no máximo 20 caracteres")
    private String codigoVerificador;

    @NotBlank(message = "Descrição do Código Verificador Obrigatório")
    @Size(max = 100, message = "A descrição do codigo deve ter no máximo 100 caracteres")
    private String descricaoVerificador;

    //Essa váriavel é utilizada para armazenar o código verificador apenas de forma temporaria.
    @Transient
    private String codigoVerificadorTemp;

    @PreUpdate
    private void preUpdate() {
        this.confirmacaoSenha = senha;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public Boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }

    public LocalDate getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(LocalDate dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public List<Grupo> getGrupos() {
        if (grupos == null) {
            grupos = new ArrayList<>();
        }
        return grupos;
    }

    public void setGrupos(List<Grupo> grupos) {
        this.grupos = grupos;
    }

    public String getConfirmacaoSenha() {
        return confirmacaoSenha;
    }

    public void setConfirmacaoSenha(String confirmacaoSenha) {
        this.confirmacaoSenha = confirmacaoSenha;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public boolean isNovo() {
        return getCodigo() == null;
    }

    public String getCodigoVerificador() {
        return codigoVerificador;
    }

    public void setCodigoVerificador(String codigoVerificador) {
        this.codigoVerificador = codigoVerificador;
    }

    public String getDescricaoVerificador() {
        return descricaoVerificador;
    }

    public void setDescricaoVerificador(String descricaoVerificador) {
        this.descricaoVerificador = descricaoVerificador;
    }

    public String getCodigoVerificadorTemp() {
        return codigoVerificadorTemp;
    }

    public void setCodigoVerificadorTemp(String codigoVerificadorTemp) {
        this.codigoVerificadorTemp = codigoVerificadorTemp;
    }
}
