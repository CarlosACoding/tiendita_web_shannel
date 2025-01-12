package com.TiendaWeb.Channel.Repositorio;

import com.TiendaWeb.Channel.Modelo.Producto;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface PersonaRepository extends PagingAndSortingRepository<Producto, Integer> {

}