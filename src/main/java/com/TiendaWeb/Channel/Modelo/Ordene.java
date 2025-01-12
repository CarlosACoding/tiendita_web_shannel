package com.TiendaWeb.Channel.Modelo;

import jakarta.persistence.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "ordenes", schema = "tienda_vestimenta_accesorios")
public class Ordene {
    @Id
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "fecha_orden")
    private Instant fechaOrden;

    @Column(name = "total", nullable = false, precision = 10, scale = 2)
    private BigDecimal total;

    // Eliminar @Lob y cambiar el tipo de dato
    @Column(name = "estado", nullable = false, columnDefinition = "VARCHAR(20) DEFAULT 'PENDIENTE'") // Cambiar a VARCHAR
    private String estado;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Instant getFechaOrden() {
        return fechaOrden;
    }

    public void setFechaOrden(Instant fechaOrden) {
        this.fechaOrden = fechaOrden;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Ordene() {
    }
}