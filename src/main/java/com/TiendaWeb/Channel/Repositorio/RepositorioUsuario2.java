package com.TiendaWeb.Channel.Repositorio;

import com.TiendaWeb.Channel.Modelo.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RepositorioUsuario2 extends JpaRepository<Usuario, Integer> {
    // Puedes agregar métodos personalizados si es necesario
    // Méto do para buscar un usuario por su correo electrónico


}


