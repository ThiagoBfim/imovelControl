package br.com.imovelcontrol.model;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

/**
 * Created by Thiago on 04/08/2017.
 */
@MappedSuperclass
public abstract class BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long codigo;

    public void setCodigo(Long codigo) {
        this.codigo = codigo;
    }

    public Long getCodigo() {
        return codigo;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (this.getCodigo() != null ? this.getCodigo().hashCode() : 0);

        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object)
            return true;
        if (object == null)
            return false;
        if (getClass() != object.getClass())
            return false;

        BaseEntity other = (BaseEntity) object;
        return !(!Objects.equals(this.getCodigo(), other.getCodigo())
                && (this.getCodigo() == null || !this.getCodigo().equals(other.getCodigo())));
    }

    @Override
    public String toString() {
        return this.getClass().getName() + " [ID=" + getCodigo() + "]";
    }
}
