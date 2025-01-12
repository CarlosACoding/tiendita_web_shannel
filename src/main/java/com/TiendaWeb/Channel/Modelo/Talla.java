package com.TiendaWeb.Channel.Modelo;

import jakarta.persistence.*;

@Entity
@Table(name = "tallas", schema = "tienda_vestimenta_accesorios")
public class Talla {
    @Id
    @Column(name = "id", nullable = false)
    private Integer id;

    @Lob
    @Column(name = "tipo", nullable = false)
    private String tipo;

    @Column(name = "talla", nullable = false, length = 10)
    private String talla;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getTalla() {
        return talla;
    }

    public void setTalla(String talla) {
        this.talla = talla;
    }

    public Talla() {
    }
}