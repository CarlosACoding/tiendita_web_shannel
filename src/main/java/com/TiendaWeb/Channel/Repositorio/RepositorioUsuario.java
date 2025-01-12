package com.TiendaWeb.Channel.Repositorio;

import com.TiendaWeb.Channel.Modelo.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface RepositorioUsuario extends JpaRepository<Usuario, Long> {
    // Puedes agregar métodos personalizados si es necesario
    // Méto do para buscar un usuario por su correo electrónico
    Usuario findByEmail(String email);
    Usuario findById(long id);

}

