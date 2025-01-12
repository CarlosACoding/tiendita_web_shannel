package com.TiendaWeb.Channel.Servicio;

import com.TiendaWeb.Channel.Modelo.Producto;
import com.TiendaWeb.Channel.Modelo.Role;
import com.TiendaWeb.Channel.Modelo.Usuario;
import com.TiendaWeb.Channel.Repositorio.RepositorioUsuario;
import com.TiendaWeb.Channel.Repositorio.RepositorioUsuario2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class ServicioUsuario {

    @Autowired
    private RepositorioUsuario repositorioUsuario;

    @Autowired
    private RepositorioUsuario2 repositorioUsuario2;

    public List<Usuario> obtenerUsuarios() {

        return repositorioUsuario.findAll();  // No necesitas escribir SQL aquí

    }

    public Usuario obtenerUsuariosId(long userId) {

        return repositorioUsuario.findById(userId);  // No necesitas escribir SQL aquí

    }

    public void  ActualizarUsuarioId(Usuario usuarioUpdate, Integer id){

        // Asegúrate de que el usuario exista antes de intentar actualizar
        Optional<Usuario> usuarioExistente = repositorioUsuario2.findById(id);

        if (usuarioExistente.isPresent()) {

            Usuario usuario = usuarioExistente.get();
            // Aquí se actualizan el rol id por defecto
            usuario.setRol(usuarioExistente.get().getRol());

            // Solo actualizamos los campos que no son nulos o vacíos del formulario
            if (usuarioUpdate.getEmail() != null && !usuarioUpdate.getEmail().isEmpty()) {
                usuario.setEmail(usuarioUpdate.getEmail());
            }

            if (usuarioUpdate.getNombre() != null && !usuarioUpdate.getNombre().isEmpty()) {
                usuario.setNombre(usuarioUpdate.getNombre());
            }

            if (usuarioUpdate.getApellidos() != null && !usuarioUpdate.getApellidos().isEmpty()) {
                usuario.setApellidos(usuarioUpdate.getApellidos());
            }

            if (usuarioUpdate.getCedula() != null && usuarioUpdate.getCedula() != 0) {
                usuario.setCedula(usuarioUpdate.getCedula());
            }

            if (usuarioUpdate.getTelefono() != null && !usuarioUpdate.getTelefono().isEmpty()) {
                usuario.setTelefono(usuarioUpdate.getTelefono());
            }

            if (usuarioUpdate.getDireccion() != null && !usuarioUpdate.getDireccion().isEmpty()) {
                usuario.setDireccion(usuarioUpdate.getDireccion());
            }

            if (usuarioUpdate.getClave() != null && !usuarioUpdate.getClave().isEmpty()) {
                usuario.setClave(usuarioUpdate.getClave());
            }
            // etc.

            repositorioUsuario2.save(usuario);  // Guarda el usuario actualizado

        } else {
            throw new RuntimeException("El usuario con ID " + id + " no existe.");
        }

    }

    public void guardarUsuario(Usuario usuario) {
        // Crear un objeto Rol con el id = 1
        Role rol = new Role();
        rol.setId(1);  // Establecer el id del rol

        // Asignar el rol al usuario
        usuario.setRol(rol);

        // Guardar el usuario en el repositorio
        repositorioUsuario.save(usuario);
    }
    // Mét odo para verificar las credenciales del usuario
    public boolean validarUsuario(String email, String clave) {
        Usuario usuario = repositorioUsuario.findByEmail(email);

        if (usuario != null && usuario.getClave().equals(clave)) {
            return true;
        }else {
            return false;
        }
    }
    // Mét odo para verificar las credenciales del usuario y devolver id
    public int[] validarUsuarioYObtenerId(String email, String clave) {
        Usuario usuario = repositorioUsuario.findByEmail(email);

        // Inicializamos un arreglo de tamaño 2, uno para ID y otro para Rol
        int[] identificacionUsuario = new int[2];

        if (usuario != null && usuario.getClave().equals(clave)) {
            identificacionUsuario[0] = usuario.getId(); // ID del usuario
            identificacionUsuario[1] = usuario.getRol().getId(); // Rol del usuario (asumimos que es un número)
            return identificacionUsuario;
        }else {
            return new int[] {0, 0}; // Podrías usar otro valor si lo prefieres
        }
    }
}

