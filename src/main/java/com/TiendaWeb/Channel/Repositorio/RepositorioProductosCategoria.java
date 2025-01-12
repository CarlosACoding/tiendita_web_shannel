package com.TiendaWeb.Channel.Repositorio;

import com.TiendaWeb.Channel.Modelo.ProductosCategoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RepositorioProductosCategoria extends JpaRepository<ProductosCategoria, Long> {

}
