package com.TiendaWeb.Channel.Modelo;

import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Table(name = "productos_categorias")
public class ProductosCategoria {

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "id_productos", nullable = false)
    private Producto idProductos;


    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_categorias", nullable = false)
    private Categoria idCategorias;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Producto getIdProductos() {
        return idProductos;
    }

    public void setIdProductos(Producto idProductos) {
        this.idProductos = idProductos;
    }

    public Categoria getIdCategorias() {
        return idCategorias;
    }

    public void setIdCategorias(Categoria idCategorias) {
        this.idCategorias = idCategorias;
    }

    public ProductosCategoria() {
    }
}