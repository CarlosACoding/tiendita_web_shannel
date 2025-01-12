package com.TiendaWeb.Channel.Servicio;

import com.TiendaWeb.Channel.Modelo.Producto;
import com.TiendaWeb.Channel.Modelo.Usuario;

import java.util.ArrayList;
import java.util.List;

public class UsuarioConProductos {
    private List<Long> id;
    private Usuario usuario;
    private List<Producto> productos;
    private List<Integer> cantidades;

    // Constructor
    public UsuarioConProductos(Usuario usuario) {
        this.usuario = usuario;
        this.productos = new ArrayList<>();
        this.cantidades = new ArrayList<>();  // Asegúrate de inicializar la lista de cantidades
        this.id = new ArrayList<>();
    }

    // Getters y Setters
    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public List<Producto> getProductos() {
        return productos;
    }

    public void setProductos(List<Producto> productos) {
        this.productos = productos;
    }

    public List<Integer> getCantidades() {
        return cantidades;
    }

    public void setCantidades(List<Integer> cantidades) {
        this.cantidades = cantidades;
    }

    public List<Long> getId() {
        return id;
    }

    public void setId(List<Long> id) {
        this.id = id;
    }

    // Agregar un producto
    public void addProducto(Producto producto, Integer cantidad, long id) {
        this.productos.add(producto);
        this.cantidades.add(cantidad);
        this.id.add(id);
    }
}
