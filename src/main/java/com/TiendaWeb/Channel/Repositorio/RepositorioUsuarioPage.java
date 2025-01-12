package com.TiendaWeb.Channel.Repositorio;

import com.TiendaWeb.Channel.Modelo.Usuario;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;


public interface RepositorioUsuarioPage extends PagingAndSortingRepository<Usuario, Integer> {
}
