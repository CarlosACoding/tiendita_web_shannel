package com.TiendaWeb.Channel.Repositorio;

import com.TiendaWeb.Channel.Modelo.CarritoCompra;
import com.TiendaWeb.Channel.Modelo.Producto;
import com.TiendaWeb.Channel.Modelo.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CarritoProductoRepositorio extends JpaRepository<CarritoCompra, Long> {

    CarritoCompra findByUsuarioIdAndProductoId(Usuario usuarioId, Producto productoId);
}