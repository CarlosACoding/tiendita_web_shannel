package com.TiendaWeb.Channel.Modelo;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import org.hibernate.Hibernate;

import java.util.Objects;

@Embeddable
public class ProductosCategoriaId implements java.io.Serializable {
    private static final long serialVersionUID = 103229706814394061L;
    @Column(name = "id_productos", nullable = false)
    private Integer idProductos;

    @Column(name = "id_categorias", nullable = false)
    private Integer idCategorias;

    public Integer getIdProductos() {
        return idProductos;
    }

    public void setIdProductos(Integer idProductos) {
        this.idProductos = idProductos;
    }

    public Integer getIdCategorias() {
        return idCategorias;
    }

    public void setIdCategorias(Integer idCategorias) {
        this.idCategorias = idCategorias;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        ProductosCategoriaId entity = (ProductosCategoriaId) o;
        return Objects.equals(this.idCategorias, entity.idCategorias) &&
                Objects.equals(this.idProductos, entity.idProductos);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idCategorias, idProductos);
    }

    public ProductosCategoriaId() {
    }
}