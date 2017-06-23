package br.com.imovelcontrol.model;

import br.com.imovelcontrol.model.tipoimovel.Aluguel;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.br.CPF;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * Created by marcosfellipec on 18/05/17.
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "locatario")
public class Locatario implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long codigo;
    @Size(max = 100, message = "Nome deve ter no máximo 100 caracteres")
    @NotBlank(message = "Nome Obrigatório")
    private String nome;

    @OneToOne
    @JoinColumn(name = "codigo_aluguel")
    private Aluguel aluguel;

    @CPF
    @Size(max = 11, message = "CPF deve ter no máximo 11 caracteres")
    @NotBlank(message = "CPF Obrigatório")
    private String cpf;

    @Size(max=12, message = "Número de telefone inválido.")
    @NotBlank
    private  String telefone;

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
        String cpfAux = "";
        for (int i = 0; i < cpf.length(); i++ ){
            if (cpf.charAt(i) != '-' && cpf.charAt(i) != '.') {
                StringBuilder stringBuilder  = new StringBuilder();
                stringBuilder.append(cpf.charAt(i));
                cpfAux = cpfAux + stringBuilder;
            }
        }
        this.cpf = cpfAux;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
//String fdfdf = "(";
        this.telefone = telefone.replaceAll("[()-]","");
    }

    public Long getCodigo() {
        return codigo;
    }

    public void setCodigo(Long codigo) {
        this.codigo = codigo;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }


}
