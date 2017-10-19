package br.com.imovelcontrol.model;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.Valid;
import javax.validation.constraints.Size;

import br.com.imovelcontrol.service.event.imovel.ImovelListener;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

@EntityListeners(ImovelListener.class)
@Entity
@Table(name = "IMOVEL")
public class Imovel extends BaseEntity {

    @NotBlank(message = "Nome Obrigatório")
    @Size(max = 100, message = "Nome deve ter no máximo 100 caracteres")
    private String nome;

    @Embedded
    @Valid
    private Endereco endereco;

    @Size(max = 100, message = "Nome da foto deve ter no máximo 100 caracteres")
    private String foto;

    @Column(name = "content_type")
    private String contentType;

    @ManyToOne
    @JoinColumn(name = "codigo_usuario")
    private Usuario donoImovel;

    private Boolean excluido = Boolean.FALSE;

    @OneToMany(mappedBy = "imovel")
    private List<Aluguel> aluguelList = new ArrayList<>();

    @Transient
    private String urlFoto;

    @Transient
    private String urlThumbnailFoto;

    @Transient
    private boolean novaFoto;

    @Transient
    private boolean mostrarExcluidos;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getFoto() {
        return foto;
    }

    public String getFotoOrMock() {
        return !StringUtils.isEmpty(foto) ? foto : "casa.png";
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public Endereco getEndereco() {
        return endereco;
    }

    public void setEndereco(Endereco endereco) {
        this.endereco = endereco;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public boolean isNovo() {
        return getCodigo() == null;
    }

    public Usuario getDonoImovel() {
        return donoImovel;
    }

    public void setDonoImovel(Usuario donoImovel) {
        this.donoImovel = donoImovel;
    }

    public String getUrlFoto() {
        return urlFoto;
    }

    public void setUrlFoto(String urlFoto) {
        this.urlFoto = urlFoto;
    }

    public String getUrlThumbnailFoto() {
        return urlThumbnailFoto;
    }

    public void setUrlThumbnailFoto(String urlThumbnailFoto) {
        this.urlThumbnailFoto = urlThumbnailFoto;
    }

    public boolean isNovaFoto() {
        return novaFoto;
    }

    public void setNovaFoto(boolean novaFoto) {
        this.novaFoto = novaFoto;
    }

    public Boolean getExcluido() {
        return excluido;
    }

    public void setExcluido(Boolean excluido) {
        this.excluido = excluido;
    }

    public List<Aluguel> getAluguelList() {
        return aluguelList;
    }

    public void setAluguelList(List<Aluguel> aluguelList) {
        this.aluguelList = aluguelList;
    }

    public boolean containsAluguel(){
        return CollectionUtils.isEmpty(getAluguelList());
    }

    public boolean isMostrarExcluidos() {
        return mostrarExcluidos;
    }

    public void setMostrarExcluidos(boolean mostrarExcluidos) {
        this.mostrarExcluidos = mostrarExcluidos;
    }
}
