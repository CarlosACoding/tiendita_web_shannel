package com.TiendaWeb.Channel.Modelo;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "categorias", schema = "tienda_vestimenta_accesorios")
public class Categoria {
    @Id
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "nombre", nullable = false, length = 65)
    private String nombre;

    @ManyToMany(mappedBy = "categorias")
    private Set<Producto> productos = new HashSet<>();

    @Column(name = "imagen")
    private String imagen;

    // Getters y Setters

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public void setProductos(Set<Producto> productos) {
        this.productos = productos;
    }

    public Categoria() {
    }
}