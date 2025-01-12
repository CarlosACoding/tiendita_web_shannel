package com.TiendaWeb.Channel.Servicio;

import com.TiendaWeb.Channel.Modelo.CarritoCompra;
import com.TiendaWeb.Channel.Modelo.CarritoCompraDto;
import com.TiendaWeb.Channel.Modelo.Producto;
import com.TiendaWeb.Channel.Modelo.Usuario;
import com.TiendaWeb.Channel.Repositorio.CarritoCompraRepository;
import com.TiendaWeb.Channel.Repositorio.CarritoCompraRepository2;
import com.TiendaWeb.Channel.Repositorio.CarritoProductoRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class CarritoCompraService {

    @Autowired
    private CarritoCompraRepository carritoCompraRepository;

    @Autowired
    private CarritoProductoRepositorio carritoProductoRepositorio;

    @Autowired
    private CarritoCompraRepository2 carritoCompraRepository2;

    @Transactional
    public void agregarProductoAlCarrito(Usuario usuario, Producto producto, int cantidad) {
        CarritoCompra carritoProducto = carritoProductoRepositorio.findByUsuarioIdAndProductoId(usuario, producto);

        if (carritoProducto != null) {
            // Si el producto ya está en el carrito, aumentar la cantidad
            carritoProducto.setCantidad(carritoProducto.getCantidad() + cantidad);
        } else {
            // Si el producto no está en el carrito, agregarlo como un nuevo registro
            //carritoProducto = new CarritoCompra(usuario, producto, cantidad);
        }
        //carritoProductoRepositorio.save(carritoProducto);
    }

    public List<CarritoCompra> obtenerCarritoPorUsuario(Long usuarioId) {
        return carritoCompraRepository.findByUsuarioId(usuarioId);

    }

    public List<CarritoCompra> obtenerCarritoPorUsuarioYEstado(Long usuarioId, List<String> estados) {

        return carritoCompraRepository.findByUsuarioIdAndEstadoIn(usuarioId, estados);

    }


    public List<CarritoCompra> obtenerCarritoPorEstadoParaCalcularTotal(String estado) {
        return carritoCompraRepository.findByEstado(estado);

    }

    public List<UsuarioConProductos> obtenerCarritoPorEstado(String estado) {
        // Obtener todos los productos del carrito con el estado especificado
        List<CarritoCompra> productosCarrito = carritoCompraRepository.findByEstado(estado);

        // Agrupar los productos por usuario
        Map<Integer, UsuarioConProductos> usuarioMap = new HashMap<>();
        for (CarritoCompra carrito : productosCarrito) {
            Integer cantidad = carrito.getCantidad();
            Integer usuarioId = carrito.getUsuario().getId();
            Long id = carrito.getId();
            UsuarioConProductos usuarioConProductos = usuarioMap.getOrDefault(usuarioId, new UsuarioConProductos(carrito.getUsuario()));

            // Agregar el producto al carrito del usuario
            usuarioConProductos.addProducto(carrito.getProducto(), cantidad, id);

            usuarioMap.put(usuarioId, usuarioConProductos);
        }

        // Devolver la lista de usuarios con sus productos
        return new ArrayList<>(usuarioMap.values());
    }



    public void actualizarCantidad(Integer usuarioId, String estado) {
        // Buscar todos los carritos asociados al usuario
        List<CarritoCompra> carritos = carritoCompraRepository2.findByUsuarioId(usuarioId);

        // Verificar si se encontraron carritos
        if (carritos != null && !carritos.isEmpty()) {
            // Iterar sobre la lista de carritos y actualizar solo el campo "estado"
            for (CarritoCompra carrito : carritos) {
                carrito.setEstado(estado);  // Actualizamos solo el estado
                carritoCompraRepository2.save(carrito);  // Guardamos el carrito actualizado

            }
        }
    }

    public void actualizarEstadoPorUsuario(Integer usuarioId, String estado) {
        // Buscar todos los carritos asociados al usuario
        List<CarritoCompra> carritos = carritoCompraRepository2.findByUsuarioIdAndEstado(usuarioId, estado);

        // Verificar si se encontraron carritos
        if (carritos != null && !carritos.isEmpty()) {
            // Iterar sobre la lista de carritos y actualizar solo el campo "estado"
            String local = "pedido";
            for (CarritoCompra carrito : carritos) {
                carrito.setEstado(local);  // Actualizamos solo el estado
                carritoCompraRepository2.save(carrito);  // Guardamos el carrito actualizado

            }
        }
    }

    public void agregarAlCarrito(CarritoCompra carritoCompra) {
        // Guardar el carrito en la base de datos
        carritoCompraRepository.save(carritoCompra);
    }

    public void eliminarProductoDeCarrito(Long usuarioId, Long productoId) {
        carritoCompraRepository.deleteByUsuarioIdAndProductoId(usuarioId, productoId);
    }

    public void EliminarCarro(Long id){
        carritoProductoRepositorio.deleteById(id);
    }

    // Método para eliminar productos por lista de IDs
    public void eliminarProductosPorIds(List<Integer> ids) {
        // Realizar la eliminación de productos que coinciden con los IDs
        carritoCompraRepository2.deleteByIdIn(ids);
    }

    // Método para actualizar el estado de los productos por una lista de IDs
    @Transactional
    public void actualizarEstadoPorIds(List<Integer> ids, String estado) {
        carritoCompraRepository2.actualizarEstadoPorIds(estado, ids);
    }

}

