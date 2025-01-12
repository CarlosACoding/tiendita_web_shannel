package com.TiendaWeb.Channel.Controlador;

import com.TiendaWeb.Channel.Modelo.CarritoCompra;
import com.TiendaWeb.Channel.Modelo.CarritoCompraDto;
import com.TiendaWeb.Channel.Modelo.Producto;
import com.TiendaWeb.Channel.Modelo.Usuario;
import com.TiendaWeb.Channel.Repositorio.RepositorioProducto;
import com.TiendaWeb.Channel.Repositorio.RepositorioUsuario;
import com.TiendaWeb.Channel.Servicio.CarritoCompraService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/carrito")
public class CarritoController {

    @Autowired
    private CarritoCompraService carritoService;

    @Autowired
    private RepositorioUsuario usuarioRepository;

    @Autowired
    private RepositorioProducto productoRepository;

    @Autowired
    private CarritoCompraService carritoCompraService;

    @PostMapping("/agregar")
    public ResponseEntity<Void> agregarAlCarrito(@RequestBody CarritoCompraDto carritoCompraDto) {
        System.out.println("Datos recibidos: ");
        System.out.println("Usuario ID: " + carritoCompraDto.getUsuarioId());
        System.out.println("Producto ID: " + carritoCompraDto.getProductoId());
        System.out.println("Cantidad: " + carritoCompraDto.getCantidad());

        // Obtener las instancias de Usuario y Producto
        Usuario usuario = usuarioRepository.findById(carritoCompraDto.getUsuarioId())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Producto producto = productoRepository.findById(carritoCompraDto.getProductoId())
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        // Crear la entidad CarritoCompra
        CarritoCompra carritoCompra = new CarritoCompra();
        carritoCompra.setUsuario(usuario);   // Establecer el objeto Usuario
        carritoCompra.setProducto(producto); // Establecer el objeto Producto
        carritoCompra.setCantidad(carritoCompraDto.getCantidad());

        // Guardar el carrito en la base de datos
        carritoService.agregarAlCarrito(carritoCompra);

        // Solo retornamos un código HTTP 200 (OK), sin respuesta de contenido
        return ResponseEntity.ok().build();
    }
    // Eliminar registro carro guardado AJAX
    @DeleteMapping("/eliminar/{id}")
    @ResponseBody // Indica que la respuesta será manejada sin redirigir
    public ResponseEntity<Map<String, String>> EliminarCarro(@PathVariable("id") Long id) {
        System.out.println("id carrito !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!" + id);
        try {
            carritoService.EliminarCarro(id); // Llama al servicio para eliminar el carro

            Map<String, String> response = new HashMap<>();
            response.put("mensaje", "Producto Eliminado con éxito.");

            return ResponseEntity.ok(response); // Responde con éxito

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); // Responde con error
        }
    }
}

