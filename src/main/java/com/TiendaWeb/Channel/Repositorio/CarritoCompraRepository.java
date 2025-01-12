package com.TiendaWeb.Channel.Repositorio;

import com.TiendaWeb.Channel.Modelo.CarritoCompra;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CarritoCompraRepository extends JpaRepository<CarritoCompra, Long> {

    List<CarritoCompra> findByUsuarioId(Long usuarioId);

    List<CarritoCompra> findByUsuarioIdAndEstado(Long usuarioId, String estado);

    List<CarritoCompra> findByEstado(String estado);

    List<CarritoCompra> findByUsuarioIdAndEstadoIn(Long usuarioId, List<String> estados);

    //List<CarritoCompra> findByUsuarioIdAndEstado(Long usuarioId, String estado);

    void deleteByUsuarioIdAndProductoId(Long usuarioId, Long productoId);
}