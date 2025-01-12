package com.TiendaWeb.Channel.Controlador;

import com.TiendaWeb.Channel.Modelo.CarritoCompra;
import com.TiendaWeb.Channel.Modelo.Producto;
import com.TiendaWeb.Channel.Modelo.Usuario;
import com.TiendaWeb.Channel.Servicio.CarritoCompraService;
import com.TiendaWeb.Channel.Servicio.ServicioProducto;
import com.TiendaWeb.Channel.Servicio.ServicioUsuario;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


import java.io.IOException;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.*;

@Controller
@RequestMapping("/")
public class ControladorUsuario {
    private ControladorUsuario controladorUsuario;

    @Autowired
    private ServicioUsuario servicioUsuario;

    @Autowired
    private ServicioProducto productoService;

    @Autowired
    private CarritoCompraService carritoCompraService;


    // Metodo para manejar errores
    @ExceptionHandler(Exception.class)
    public String manejarError(Exception ex, Model model) {
        model.addAttribute("mensajeError", ex.getMessage());
        return "error";  // Nombre de la plantilla Thymeleaf personalizada para errores
    }

    /*@PostMapping("/guardarUsuario")
    public String guardarUsuario(@ModelAttribute Usuario usuarioregistrar) throws IOException {

        System.out.println("*************DEBERIA GUARDAR PRODUCTO************************"+ usuarioregistrar + "************************************************************");

        servicioUsuario.guardarUsuario(usuarioregistrar);

        return "redirect:/"; // Redirige a donde necesites después de guardar
    }*/

    @GetMapping("/carrito/{userId}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> obtenerCarrito(@PathVariable("userId") Long userId) {
        //
        Map<String, Object> response = new HashMap<>();

        List<String> estados = Arrays.asList("pendiente", "pedido");

        try {
            // Llamada al servicio que obtiene los productos en el carrito

            List<CarritoCompra> productosCarrito = carritoCompraService.obtenerCarritoPorUsuarioYEstado(userId, estados);
            // Lista para almacenar los productos en formato JSON
            List<Map<String, Object>> productosJSON = new ArrayList<>();

            // Verificamos si el carrito está vacío
            if (productosCarrito == null || productosCarrito.isEmpty()) {
                response.put("productosCarrito", new ArrayList<>());

            } else {

                // Usando forEach directamente sobre la lista con lambda
                for (CarritoCompra carrito : productosCarrito) {
                    // Crear un Map para almacenar los valores de cada producto
                    Map<String, Object> productoMap = new HashMap<>();
                    // Obtener los valores correctos de cada producto
                    productoMap.put("id", carrito.getId());
                    productoMap.put("nombre", carrito.getProducto().getNombre());
                    productoMap.put("descripcion", carrito.getProducto().getDescripcion());
                    productoMap.put("precio", carrito.getProducto().getPrecio());
                    productoMap.put("cantidad", carrito.getCantidad());
                    productoMap.put("imagen", carrito.getProducto().getImagen1());
                    productoMap.put("estado", carrito.getEstado());

                    // Ahora puedes trabajar con los valores, por ejemplo, imprimirlos o agregarlos a un objeto
                    System.out.println("Nombre: " + productoMap);

                    // Agregar el Map del producto a la lista
                    productosJSON.add(productoMap);
                }
                // Colocar la lista de productos en la respuesta
                response.put("productosCarrito", productosJSON);
            }

            return ResponseEntity.ok(response); // Retornamos los productos en formato JSON

        } catch (Exception e) {
            // Manejo de errores generales
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "Hubo un error al obtener los datos del carrito", "error", e.getMessage()));
        }
    }
    // Proceso pasarela de pagos
    @GetMapping("/pagos")
    public String PasarelaPagos(@RequestParam Long userId, Model model){
        List<String> estados = Arrays.asList("pendiente", "");

        try {

            List<CarritoCompra> productosCarrito = carritoCompraService.obtenerCarritoPorUsuarioYEstado(userId, estados);
            model.addAttribute("productosCarrito", productosCarrito);

            Usuario usuarioId = servicioUsuario.obtenerUsuariosId(userId);
            model.addAttribute("usuario", usuarioId);
            // Calcular el total de la compra
            // Inicializar el total con BigDecimal para mayor precisión
            BigDecimal totalDefinitivo = BigDecimal.ZERO;

            // Iterar sobre los productos para calcular el total
            for (CarritoCompra item : productosCarrito) {
                BigDecimal precioUnitario = item.getProducto().getPrecio(); // Precio unitario (BigDecimal)
                int cantidad = item.getCantidad(); // Cantidad (int)

                // Multiplicar el precio por la cantidad y sumar al total
                BigDecimal subtotal = precioUnitario.multiply(BigDecimal.valueOf(cantidad));
                totalDefinitivo = totalDefinitivo.add(subtotal);
            }

            model.addAttribute("totalCompra", totalDefinitivo); // Añadir el total al modelo
            //System.out.println(totalDefinitivo);

            return "pagos";

        } catch (Exception e) {
            System.out.println(e.getMessage());

        }

        return "pagos";
    }
    //

    @PutMapping("/actualizarYComprar/{userId}")
    public String actualizarYComprar(@PathVariable("userId") Integer  id, @ModelAttribute("usuario") Usuario usuarioUpdate, RedirectAttributes redirectAttributes) {
        try {
            servicioUsuario.ActualizarUsuarioId(usuarioUpdate, id);
            System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!Paso al paso 1" + usuarioUpdate.getNombre());

            carritoCompraService.actualizarEstadoPorUsuario(id, "pendiente");



        } catch (Exception e) {
            System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!NOOOOOOPaso al paso 1");
            System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!NOOOOOOPaso al paso 1 " + usuarioUpdate.getNombre());
            throw new RuntimeException(e);
        }

        redirectAttributes.addFlashAttribute("pedidoEnviado", true);
        return "redirect:/";
    }

    // Proceso pasarela de pagos
    @GetMapping("/actualizarInfo")
    public String actualizarInfo(@RequestParam Long userId, Model model){
        try {
            Usuario usuarioId = servicioUsuario.obtenerUsuariosId(userId);
            model.addAttribute("usuario", usuarioId);

            return "actualizarInfo";

        } catch (Exception e) {
            System.out.println(e.getMessage());

        }
        return "actualizarInfo";
    }

    @PutMapping("/actualizarUsuario/{userId}")
    public String actualizarUsuario(@PathVariable("userId") Integer  id, @ModelAttribute("usuario") Usuario usuarioUpdate, RedirectAttributes redirectAttributes) {
        try {
            servicioUsuario.ActualizarUsuarioId(usuarioUpdate, id);
            System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!Paso al paso 1" + usuarioUpdate.getNombre());

            redirectAttributes.addFlashAttribute("usuarioActualizado", true);
            return "redirect:/actualizarInfo?userId=" + id;  // Usar "redirect:" seguido de la ruta completa

        } catch (Exception e) {
            System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!NOOOOOOPaso al paso 1");
            System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!NOOOOOOPaso al paso 1 " + usuarioUpdate.getNombre());
            throw new RuntimeException(e);
        }

    }
   /* @PostMapping("/carrito/agregar")
    public String agregarProductoAlCarrito(@RequestParam Usuario usuarioId, @RequestParam Producto productoId, @RequestParam int cantidad) {
        carritoCompraService.agregarProductoAlCarrito(usuarioId, productoId, cantidad);
        return "redirect:/carrito?usuarioId=" + usuarioId; // Redirige al carrito del usuario
    }*/

    // Eliminar un producto del carritogg
    /*@PostMapping("/carrito/eliminar")
    public String eliminarProductoDelCarrito(@RequestParam Long productoId, @RequestParam Long usuarioId) {
        carritoCompraService.eliminarProductoDeCarrito(usuarioId, productoId);
        return "redirect:/carrito?usuarioId=" + usuarioId;
    }*/

    // Vista principal para los usuarios
    @GetMapping("/")
    public String mostrarUsuarios(Model model) {
        // Obtener la lista de productos con descuento del 50% solo 3
        List<Producto> productosConDescuento = productoService.obtenerTresProductosConDescuento50(); // Asegúrate de tener este metodo en tu servicio
        model.addAttribute("productosConDescuento", productosConDescuento); // Añadir los productos al modelo

        // Obtener la lista de productos con descuento del 50% solo 2
        List<Producto> productosConDescuento2 = productoService.obtenerDosProductosConDescuento50(); // Asegúrate de tener este metodo en tu servicio
        model.addAttribute("productosConDescuento2", productosConDescuento2); // Añadir los productos al modelo

        // Obtener todos los productos del inventario
        List<Producto> productos = productoService.obtenerProductos(0, 20); // Cargar los primeros 20 productos
        model.addAttribute("productos20", productos);
        // Activacion de enlance
        model.addAttribute("isActive", true);
        // No loggin
        model.addAttribute("isUserLoggedIn", false);

        //Obtener Usuarios
        List<Usuario> usuarios = servicioUsuario.obtenerUsuarios();
        model.addAttribute("usuarios", usuarios);


        return "index";  // Nombre de la plantilla Thymeleaf (index.html)
    }
    // Vista principal para los usuarios
    @GetMapping("/session")
    public String InicioSession(@RequestParam Long usuarioId, Model model) {
        // Obtener la lista de productos con descuento del 50% solo 3
        List<Producto> productosConDescuento = productoService.obtenerTresProductosConDescuento50(); // Asegúrate de tener este metodo en tu servicio
        model.addAttribute("productosConDescuento", productosConDescuento); // Añadir los productos al modelo

        // Obtener la lista de productos con descuento del 50% solo 2
        List<Producto> productosConDescuento2 = productoService.obtenerDosProductosConDescuento50(); // Asegúrate de tener este metodo en tu servicio
        model.addAttribute("productosConDescuento2", productosConDescuento2); // Añadir los productos al modelo

        // Obtener todos los productos del inventario
        List<Producto> productos = productoService.obtenerProductos(0, 20); // Cargar los primeros 20 productos
        model.addAttribute("productos20", productos);
        // Activacion de enlance
        model.addAttribute("isActive", true);

        //Obtener Usuarios
        List<Usuario> usuarios = servicioUsuario.obtenerUsuarios();
        model.addAttribute("usuarios", usuarios);
        // Proceso de activacion de carrito de compras
        if (usuarioId != 0){
            List<CarritoCompra> productosCarrito = carritoCompraService.obtenerCarritoPorUsuario(usuarioId);
            model.addAttribute("productosCarrito", productosCarrito);

            for (CarritoCompra i : productosCarrito){
               Long a = i.getId();
                System.out.println("id!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!id" +a);
            }


            model.addAttribute("isUserLoggedIn", true);
        }else {
            model.addAttribute("isUserLoggedIn", false);
        }

        return "index";  // Nombre de la plantilla Thymeleaf (index.html)
    }

    @GetMapping("/hombres")
    public String mostrarVistaHombres(@RequestParam("categoria") String nombreCategoria, Model model) {
        // Aquí puedes agregar la lógica para mostrar la página de Hombres
        // Crear un HashMap para las categorías
        List<Producto> productos = productoService.obtenerProductosPorCategoria(nombreCategoria);
        model.addAttribute("productos", productos);
        model.addAttribute("categoria", nombreCategoria);
        // Activacion de enlance
        model.addAttribute("isActiveHombres", true);

        return "hombres";  // Nombre de la plantilla Thymeleaf para hombres
    }
    @GetMapping("/mujeres")
    public String mostrarVistaMujeres(@RequestParam("categoria") String nombreCategoria, Model model) {
        // Aquí puedes agregar la lógica para mostrar la página de Hombres
        // Crear un HashMap para las categorías
        List<Producto> productos = productoService.obtenerProductosPorCategoria(nombreCategoria);
        model.addAttribute("productos", productos);
        model.addAttribute("categoria", nombreCategoria);
        // Activacion de enlance
        model.addAttribute("isActiveMujeres", true);

        return "mujeres";  // Nombre de la plantilla Thymeleaf para hombres
    }

    @GetMapping("/ninos")
    public String mostrarVistaNinos(@RequestParam("categoria") String nombreCategoria, Model model) {
        // Aquí puedes agregar la lógica para mostrar la página de Hombres
        // Crear un HashMap para las categorías
        List<Producto> productos = productoService.obtenerProductosPorCategoria(nombreCategoria);
        model.addAttribute("productos", productos);
        model.addAttribute("categoria", nombreCategoria);
        // Activacion de enlance
        model.addAttribute("isActiveNinos", true);

        return "ninos";  // Nombre de la plantilla Thymeleaf para hombres
    }
    @GetMapping("/ninas")
    public String mostrarVistaNinas(@RequestParam("categoria") String nombreCategoria, Model model) {
        // Aquí puedes agregar la lógica para mostrar la página de Hombres
        // Crear un HashMap para las categorías
        List<Producto> productos = productoService.obtenerProductosPorCategoria(nombreCategoria);
        model.addAttribute("productos", productos);
        model.addAttribute("categoria", nombreCategoria);
        // Activacion de enlance
        model.addAttribute("isActiveNinas", true);

        return "ninas";  // Nombre de la plantilla Thymeleaf para hombres
    }
    @GetMapping("/accesorios")
    public String mostrarVistaAccesorios(@RequestParam("categoria") String nombreCategoria, Model model) {
        // Aquí puedes agregar la lógica para mostrar la página de Hombres
        // Crear un HashMap para las categorías
        List<Producto> productos = productoService.obtenerProductosPorCategoria(nombreCategoria);
        model.addAttribute("productos", productos);
        model.addAttribute("categoria", nombreCategoria);
        // Activacion de enlance
        model.addAttribute("isActiveAccesorios", true);

        return "accesorios";  // Nombre de la plantilla Thymeleaf para hombres
    }

    @GetMapping("/cosmeticosyBelleza")
    public String mostrarVistaComesticosyBelleza(@RequestParam("categoria") String nombreCategoria, Model model) {
        // Aquí puedes agregar la lógica para mostrar la página de Hombres
        // Crear un HashMap para las categorías
        List<Producto> productos = productoService.obtenerProductosPorCategoria(nombreCategoria);
        model.addAttribute("productos", productos);
        model.addAttribute("categoria", nombreCategoria);
        // Activacion de enlance
        model.addAttribute("isActivecosmeticosyBelleza", true);

        return "cosmeticosyBelleza";  // Nombre de la plantilla Thymeleaf para hombres
    }
    /* *******************Proceso de login de usuarios********************** */
    @GetMapping("/usuarioInicio")
    public String loginUsuario(Model model) {
        // Aquí puedes agregar la lógica para mostrar la página de Hombres
        // Crear un HashMap para las categorías
        model.addAttribute("usuario", new Usuario());
        return "usuario";  // Nombre de la plantilla Thymeleaf para hombres
    }

    @PostMapping("/loginUser")
    public String guardarProducto(@ModelAttribute Usuario usuario, RedirectAttributes redirectAttributes) {
        String email = usuario.getEmail();
        String password = usuario.getClave();

        System.out.println("email: " + email + "/n" + "password: " + password);
        // Verificar las credenciales
        boolean validUser = servicioUsuario.validarUsuario(email, password);
        // Llamamos al mét odo y obtenemos el arreglo con ID del usuario y del rol
        int[] identificacionUsuario = servicioUsuario.validarUsuarioYObtenerId(email, password);
        // Accedemos al ID del usuario (primer elemento del arreglo)
        int idUser = identificacionUsuario[0];
        // Accedemos al ID del rol (segundo elemento del arreglo)
        int rolId = identificacionUsuario[1];

        System.out.println("email: " + email + "/n" + "password: " + password + " id: " + idUser + " rolId: " + rolId);

        if (validUser) {
            redirectAttributes.addFlashAttribute("idUsuarioUnico", idUser);
            redirectAttributes.addFlashAttribute("idUsuarioRol", rolId);
            redirectAttributes.addFlashAttribute("emailUsuario", email);

            return "redirect:/"; // Redirecciona al home si las credenciales son correctas

        } else {
            // Si el usuario no existe o la contraseña es incorrecta, agregar un mensaje de error
            redirectAttributes.addFlashAttribute("error", true);
            return "redirect:usuarioInicio"; // Redirige a la página de login
        }
    }

    @GetMapping("/admin")
    public String mostrarProductos(Model model) {
        // Aquí puedes agregar la lógica para mostrar la página de Hombres
        // Crear un HashMap para las categorías

        return "admin";  // Nombre de la plantilla Thymeleaf para hombres
    }

    @GetMapping("/registroInicio")
    public String registrarUsuario(Model model) {
        // Aquí puedes agregar la lógica para mostrar la página de Hombres
        // Crear un HashMap para las categorías
        model.addAttribute("usuarioNuevo", new Usuario());

        return "registro";  // Nombre de la plantilla Thymeleaf para hombres
    }

    @PostMapping("/creacionUsuario")
    public String creacionUsuario(@ModelAttribute("usuarioNuevo") Usuario usuario, RedirectAttributes redirectAttributes) {
        try {
            // Intentamos guardar al usuario
            servicioUsuario.guardarUsuario(usuario);
            // Si la creación es exitosa, redirigimos al login
            redirectAttributes.addFlashAttribute("usuarioRegistrado", true);
            return "redirect:/usuarioInicio";  // Cambiar la URL de redirección si es necesario

        } catch (RuntimeException e) {
            // Si ocurre un error, mostrar un mensaje y regresar a la vista de registro

            System.out.println(e.getMessage());
            return "redirect:registroInicio";  // Volver a la página de registro si hubo un error
        }
    }
}

