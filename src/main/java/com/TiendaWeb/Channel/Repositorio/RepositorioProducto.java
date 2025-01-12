package com.TiendaWeb.Channel.Repositorio;

import com.TiendaWeb.Channel.Modelo.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RepositorioProducto extends JpaRepository<Producto, Long> {
    List<Producto> findByDescuento(int descuento);

    List<Producto> findByDescuento(int descuento, Pageable pageable);

    @Query("SELECT p FROM Producto p JOIN p.categorias c WHERE c.nombre = :nombreCategoria")
    List<Producto> findProductosByNombreCategoria(@Param("nombreCategoria") String nombreCategoria);

    @Query("SELECT p FROM Producto p WHERE p.nombre LIKE CONCAT('%', :query, '%') OR p.descripcion LIKE CONCAT('%', :query, '%')")
    List<Producto> buscarPorNombreODescripcion(@Param("query") String query);


}
