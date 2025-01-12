package com.TiendaWeb.Channel.Controlador;

import java.util.List;

public class IdCarritoRequest {

    private List<Long> idCarrito;  // Lista de IDs

    // Getter y setter
    public List<Long> getIdCarrito() {
        return idCarrito;
    }

    public void setIdCarrito(List<Long> idCarrito) {
        this.idCarrito = idCarrito;
    }
}
