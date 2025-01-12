package com.TiendaWeb.Channel.Repositorio;

import com.TiendaWeb.Channel.Modelo.CarritoCompra;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CarritoCompraRepository2 extends JpaRepository<CarritoCompra, Integer> {
    List<CarritoCompra> findByUsuarioId(Integer usuarioId);

    List<CarritoCompra> findByUsuarioIdAndEstado(Integer usuarioId, String estado);

    List<CarritoCompra> findByEstado(String estado);

    // Eliminar productos por una lista de IDs
    void deleteByIdIn(List<Integer> ids);

    // Méto do para actualizar el estado a "pagado" para un conjunto de IDs
    @Modifying
    @Query("UPDATE CarritoCompra p SET p.estado = :estado WHERE p.id IN :ids")
    void actualizarEstadoPorIds(@Param("estado") String estado, @Param("ids") List<Integer> ids);


}
