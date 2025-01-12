package com.TiendaWeb.Channel.Modelo;

import jakarta.persistence.*;

@Entity
@Table(name = "roles", schema = "tienda_vestimenta_accesorios")
public class Role {
    @Id
    @Column(name = "id", nullable = false)
    private Integer id;

    @Lob
    @Column(name = "nombre", nullable = false)
    private String nombre;

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

    public Role() {
    }
}