package com.TiendaWeb.Channel.Modelo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.beans.factory.annotation.Value;

@Entity
@Table(name = "carrito_compras", schema = "tienda_vestimenta_accesorios")
public class CarritoCompra {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "usuario_id")
    @JsonIgnore // Evita serializar la relación Usuario
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "producto_id")
    @JsonIgnore // Evita serializar la relación Producto
    private Producto producto;

    @ColumnDefault("1")
    @Column(name = "cantidad", nullable = true)
    private Integer cantidad;

    @Column(name = "talla", nullable = true)
    private String talla;

    @Column(name = "color", nullable = true)
    private String color;

    @Column(name = "estado", nullable = true)
    private String estado = "pendiente";

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public String getTalla() {
        return talla;
    }

    public void setTalla(String talla) {
        this.talla = talla;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public CarritoCompra(Long id, Usuario usuario, Producto producto, String talla, Integer cantidad, String color, String estado) {
        this.id = id;
        this.usuario = usuario;
        this.producto = producto;
        this.talla = talla;
        this.cantidad = cantidad;
        this.color = color;
        this.estado = estado;
    }

    public CarritoCompra() {
    }
}