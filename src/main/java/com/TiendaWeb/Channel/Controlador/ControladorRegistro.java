package com.TiendaWeb.Channel.Controlador;

import com.TiendaWeb.Channel.Modelo.CarritoCompra;
import com.TiendaWeb.Channel.Modelo.Producto;
import com.TiendaWeb.Channel.Modelo.Usuario;
import com.TiendaWeb.Channel.Servicio.CarritoCompraService;
import com.TiendaWeb.Channel.Servicio.ServicioUsuario;
import com.TiendaWeb.Channel.Servicio.UsuarioConProductos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/")
public class ControladorRegistro {

    @Autowired
    private ServicioUsuario servicioUsuario;

    @Autowired
    private CarritoCompraService carritoCompraService;

    @GetMapping("/usuario-registro")
    public String registroUsuario(Model model) {
        // Aquí puedes agregar la lógica para mostrar la página de Hombres


        //capucha usuarios
        model.addAttribute("usuario", new Usuario());
        System.out.println("************LISTO PARA CREAR PRODUCTO*************************" + "************************************************************");

        return "registro";  // Nombre de la plantilla Thymeleaf para hombres
    }
    @PostMapping("/guardarUsuario")
    public String guardarUsuario(@ModelAttribute Usuario usuari) throws IOException {

        System.out.println("*************DEBERIA GUARDAR PRODUCTO************************"+ usuari + "************************************************************");

        servicioUsuario.guardarUsuario(usuari);

        return "redirect:/usuario-registro"; // Redirige a donde necesites después de guardar
    }

    @GetMapping("/usuario-login")
    public String loginUsuario(Model model) {
        // Aquí puedes agregar la lógica para mostrar la página de Hombres
        // Crear un HashMap para las categorías

        return "usuario";  // Nombre de la plantilla Thymeleaf para hombres
    }

// Proceso pasarela de pagos
    @GetMapping("/pedidosClientes")
    public String obtenerPedidosClientes(Model model){
        try {
            // Obtener los productos por estado "pedido"
            List<UsuarioConProductos> usuarioConProductosList = carritoCompraService.obtenerCarritoPorEstado("pedido");

            // Variable para almacenar el total
            BigDecimal totalDefinitivo = BigDecimal.ZERO;

            // Iterar sobre los productos para calcular el total
            for (UsuarioConProductos item : usuarioConProductosList) {

                // Iterar sobre los productos de cada usuario
                for (int i = 0; i < item.getProductos().size(); i++) {
                    Producto item2 = item.getProductos().get(i); // Obtener el producto
                    int cantidad = item.getCantidades().get(i); // Obtener la cantidad correspondiente de la lista

                    // Multiplicar el precio por la cantidad y sumar al total
                    BigDecimal precioUnitario = item2.getPrecio(); // Precio unitario (BigDecimal)
                    BigDecimal subtotal = precioUnitario.multiply(BigDecimal.valueOf(cantidad)); // Subtotal por producto
                    totalDefinitivo = totalDefinitivo.add(subtotal); // Sumar al total
                }
            }

            model.addAttribute("totalCompra", totalDefinitivo); // Añadir el total al modelo
            model.addAttribute("usuarioConProductosList", usuarioConProductosList);

            return "pedidosClientes";  // Nombre de la vista en Thymeleaf
        } catch (Exception e) {
            e.printStackTrace();
            return "error";
        }
    }
    // Actualiza
    @PostMapping("/procesar")
    public ResponseEntity<String> procesarPago(@RequestBody Map<String, Object> datos) {
        // Obtener la lista de IDs del carrito
        Object idCarritoObj = datos.get("idCarrito");

        // Verificar si el objeto es una lista
        if (idCarritoObj instanceof List<?>) {
            // Cast a la lista de enteros
            List<Integer> idCarritoCompras = new ArrayList<>();

            // Convertir los valores de la lista a enteros
            for (Object obj : (List<?>) idCarritoObj) {
                if (obj instanceof Number) {
                    idCarritoCompras.add(((Number) obj).intValue());
                }
            }
            int x = 0;
            // Imprimir los IDs recibidos (opcional, solo para debug)
            System.out.println("Actualizando estado de productos con los siguientes IDs: " + idCarritoCompras);

            // Llamar al servicio para actualizar el estado de los productos
            carritoCompraService.actualizarEstadoPorIds(idCarritoCompras, "pagado");

            // Retorna una respuesta al frontend


            return ResponseEntity.ok("Productos actualizados exitosamente");

        } else {
            // Si los datos no son válidos, retornamos un error
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: 'idCarrito' no es una lista válida.");
        }
    }

    // todas las ventas d clientes
    @GetMapping("/ventasClientes")
    public String ventasClientes(Model model){
        try {
            // Obtener los productos por estado "pedido"
            List<UsuarioConProductos> usuarioConProductosList = carritoCompraService.obtenerCarritoPorEstado("pagado");

            // Variable para almacenar el total
            BigDecimal totalDefinitivo = BigDecimal.ZERO;

            // Iterar sobre los productos para calcular el total
            for (UsuarioConProductos item : usuarioConProductosList) {

                // Iterar sobre los productos de cada usuario
                for (int i = 0; i < item.getProductos().size(); i++) {
                    Producto item2 = item.getProductos().get(i); // Obtener el producto
                    int cantidad = item.getCantidades().get(i); // Obtener la cantidad correspondiente de la lista

                    // Multiplicar el precio por la cantidad y sumar al total
                    BigDecimal precioUnitario = item2.getPrecio(); // Precio unitario (BigDecimal)
                    BigDecimal subtotal = precioUnitario.multiply(BigDecimal.valueOf(cantidad)); // Subtotal por producto
                    totalDefinitivo = totalDefinitivo.add(subtotal); // Sumar al total
                }
            }

            model.addAttribute("totalCompra", totalDefinitivo); // Añadir el total al modelo
            model.addAttribute("usuarioConProductosList", usuarioConProductosList);

            return "ventasClientes";  // Nombre de la vista en Thymeleaf
        } catch (Exception e) {
            e.printStackTrace();
            return "error";
        }
    }

    @GetMapping("/acerca_nosotros")
    public String acercaNosotros() {
        return "acerca_nosotros"; // Nombre del archivo .html en templates
    }

    @GetMapping("/condiciones")
    public String condiciones() {
        return "condiciones"; // Nombre del archivo .html en templates
    }

    @GetMapping("/nuestro_proyecto")
    public String nuestroProyecto() {
        return "nuestro_proyecto"; // Nombre del archivo .html en templates
    }

    @GetMapping("/carrera")
    public String carrera() {
        return "carrera"; // Nombre del archivo .html en templates
    }

    @GetMapping("/soporte")
    public String soporte() {
        return "soporte"; // Nombre del archivo .html en templates
    }

    @GetMapping("/mas")
    public String mas() {
        return "mas"; // Nombre del archivo .html en templates
    }

    @GetMapping("/faq")
    public String faq() {
        return "faq"; // Nombre del archivo .html en templates
    }

    @GetMapping("/contactos")
    public String contactos() {
        return "contactos"; // Nombre del archivo .html en templates
    }

    @GetMapping("/politica-privacidad")
    public String politicaPrivacidad() {
        return "politica-privacidad"; // Nombre del archivo .html en templates
    }

    @GetMapping("/chats")
    public String chats() {
        return "chats"; // Nombre del archivo .html en templates
    }

    @GetMapping("/guias")
    public String guias() {
        return "guias"; // Nombre del archivo .html en templates
    }

    @GetMapping("/informacion-envios")
    public String informacionEnvios() {
        return "informacion-envios"; // Nombre del archivo .html en templates
    }
}


