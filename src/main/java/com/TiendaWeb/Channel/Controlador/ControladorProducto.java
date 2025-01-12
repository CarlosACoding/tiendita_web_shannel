package com.TiendaWeb.Channel.Controlador;

import com.TiendaWeb.Channel.Modelo.*;
import com.TiendaWeb.Channel.Repositorio.*;
import com.TiendaWeb.Channel.Servicio.CarritoCompraService;
import com.TiendaWeb.Channel.Servicio.ServicioProducto;
import com.mysql.cj.xdevapi.Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Controller
public class ControladorProducto {
    @Autowired
    private ServicioProducto productoService;

    @Autowired
    private PersonaRepository personaRepository;

    @Autowired
    private CarritoCompraService carritoService;

    @Autowired
    private RepositorioProducto productoRepository;

    @Autowired
    private RepositorioProducto2 repositorioProducto2;

    @Autowired
    private RepositorioCategoria repositorioCategoria;

    @Autowired
    private RepositorioProductosCategoria repositorioProductosCategoria;

    @Autowired
    private RepositorioUsuarioPage repositorioUsuarioPage;
    //
    @GetMapping(value = "/productosTabla")
    public String findAll(@PageableDefault(size = 10, page = 0) Pageable pageable, Model model) {
        Page<Producto> page = personaRepository
                .findAll(PageRequest.of(pageable.getPageNumber(), pageable.getPageSize()));

        model.addAttribute("page", page);
        var totalPages = page.getTotalPages();
        var currentPage = page.getNumber();

        var start = Math.max(1, currentPage);
        var end = Math.min(currentPage + 5, totalPages);

        if (totalPages > 0) {
            List<Integer> pageNumbers = new ArrayList<>();
            for (int i = start; i <= end; i++) {
                pageNumbers.add(i);
            }

            model.addAttribute("pageNumbers", pageNumbers);
        }


        List<Integer> pageSizeOptions = Arrays.asList(10,20, 50, 100);
        model.addAttribute("pageSizeOptions", pageSizeOptions);


        return "productotabla";
    }

    //
    @GetMapping(value = "/clientesTabla")
    public String findAllClientes(@PageableDefault(size = 10, page = 0) Pageable pageable, Model model) {
        Page<Usuario> page = repositorioUsuarioPage
                .findAll(PageRequest.of(pageable.getPageNumber(), pageable.getPageSize()));

        System.out.println("usuario lista!!!!!!!!!!!!!!!!!!!!!!!!!!!!! " + page);

        model.addAttribute("page", page);
        var totalPages = page.getTotalPages();
        var currentPage = page.getNumber();

        var start = Math.max(1, currentPage);
        var end = Math.min(currentPage + 5, totalPages);

        if (totalPages > 0) {
            List<Integer> pageNumbers = new ArrayList<>();
            for (int i = start; i <= end; i++) {
                pageNumbers.add(i);
            }

            model.addAttribute("pageNumbers", pageNumbers);
        }


        List<Integer> pageSizeOptions = Arrays.asList(10,20, 50, 100);
        model.addAttribute("pageSizeOptions", pageSizeOptions);


        return "clientesTabla";
    }

    @GetMapping("/crear-producto")
    public String crearProductoForm(Model model) {

        List<Categoria> categoriaList = productoService.obtenerNombresCategoria();

        model.addAttribute("producto", new Producto());
        model.addAttribute("nombresCategoria", categoriaList);
        System.out.println("************LISTO PARA CREAR PRODUCTO*************************" + "************************************************************");

        return "crear_nuevo_producto";
    }

    @PostMapping("/guardarProducto")
    public String guardarProducto(@ModelAttribute Producto productos,  @RequestParam("idCategoria") Long  idCategoria, @RequestParam("imagenImgbb") MultipartFile imagen1) throws IOException {
        System.out.println("ver si los paramentos estan siendo pasados :::::::::::::::::: " +productos + "  !!!!!!!"
            + " idCategoria: " + idCategoria + " imagen: " + imagen1);

        // Primero guardar el nuevo producto
        productos.setFechaCreacion(Instant.now());
        Producto productoConId =  productoService.guardarProductoYDevolverId(imagen1, productos);

        if (productoConId != null){
            // Una ves guardado el producto, se le tendria que haber creado un id automatico en la base datos. Mostrar datos por consola
            System.out.println("Id generado mysql de producto:::::::::: " +  productoConId.getId());
            // Obtener las instancias de Categoria y Producto
            Categoria categoriaI = repositorioCategoria.findById(idCategoria)
                    .orElseThrow(() -> new RuntimeException("categoria no encontrado"));

            Producto productoI = repositorioProducto2.findById(productoConId.getId())
                    .orElseThrow(() -> new RuntimeException("producto no encontrado"));
            // Ver si la nueva instancia de categoria esta en memoria
            System.out.println("id de la categoría seleccionada::::: " + categoriaI);
            System.out.println("id de la categoría seleccionada::::: " + productoI);
            // Guardar los objetos de categoria, producto en la tabla intermedia o relacion manytomany
            ProductosCategoria productosCategoria = new ProductosCategoria();
            productosCategoria.setIdProductos(productoI);
            productosCategoria.setIdCategorias(categoriaI);
            repositorioProductosCategoria.save(productosCategoria);
            // Imprimir id cateigura obtenida desde desde la vista
            System.out.println("id de la categoría seleccionada:::::: " + idCategoria);

            System.out.println("*************DEBERIA GUARDAR PRODUCTO************************"+ productos + "************************************************************");
            return "redirect:/crear-producto"; // Redirige a donde necesites después de guardar

        } else {
            return "error"; // O una página de error si algo salió mal
        }

        //productoService.guardarProducto(productos);
    }

    @GetMapping("/editarProducto/{id}")
    public String editarProducto(@PathVariable Long id, Model model) {
        // Obtener el producto por su ID
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado"));

        // Agregar el producto al modelo para que esté disponible en el formulario
        model.addAttribute("producto", producto);

        // Devolver la vista del formulario de edición
        return "actualizarProducto"; // Nombre de la vista de tu formulario de edición
    }

    @PutMapping("/actualizarProducto/{id}")
    public String actualizarProducto(@ModelAttribute Producto producto,
                                     @RequestParam("imagen1") MultipartFile imagen1,
                                     @PathVariable Long id) throws IOException {

        // Llamar al servicio para actualizar el producto
        Producto productoActualizado = productoService.actualizarProductoImgbb(imagen1, producto, id);

        if (productoActualizado != null) {
            return "redirect:/productosTabla"; // Redirige a la lista de productos (o donde desees)
        }

        return "error"; // Si algo sale mal, puedes redirigir a una página de error
    }


    @GetMapping("/detalleProducto/{id}")
    public String obtenerUsuarioPorId(@PathVariable Long id, Model model) {

        Producto producto = productoService.obtenerProductoPorId(id);

        if (producto != null) {
            model.addAttribute("productoIdDetalle", producto);
                return "detalleProducto"; // Nombre de la plantilla HTML

        } else {
            return "error"; // Puedes tener una página 404 para manejar productos no encontrados
        }

    }
    // funcion de buscador
    @GetMapping("/buscarProductos")
    public String buscarProductos(@RequestParam("query") String query, Model model) {
        // Buscar productos por nombre o descripción
        List<Producto> productosFiltrados = productoService.buscarProductos(query);

        // Agregar los productos filtrados al modelo
        model.addAttribute("productos", productosFiltrados);

        // Redirigir a la vista donde se mostrarán los productos
        return "productosBuscador"; // El nombre de la vista donde se muestran los productos
    }



}


