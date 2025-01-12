package com.TiendaWeb.Channel.Servicio;

import com.TiendaWeb.Channel.Modelo.Categoria;
import com.TiendaWeb.Channel.Modelo.Producto;
import com.TiendaWeb.Channel.Repositorio.RepositorioCategoria;
import com.TiendaWeb.Channel.Repositorio.RepositorioProducto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;


import java.util.Base64;
import java.util.List;
import java.util.Optional;

@Service
public class ServicioProducto {
    @Autowired
    private RepositorioProducto productoRepository; // Esto es incorrecto

    @Autowired
    private RepositorioCategoria repositorioCategoria;

    @Value("${imgbb.api.key}")
    private String apiKey;

    private static final String IMG_BB_API_URL = "https://api.imgbb.com/1/upload";

    // Obtener todos los productos
    public List<Producto> obtenerTodosLosProductos() {

        return productoRepository.findAll();
    }

    // Crear o actualizar Producto
    public void guardarProducto(Producto producto) {
        productoRepository.save(producto); // Aquí falla porque 'productoRepository' no es un repositorio
    }
    // Proceso guardado producto y proceso para guardar imagen a imggbb y db
    public Producto guardarProductoYDevolverId(MultipartFile file, Producto producto) {
        if (file == null) {
            throw new IllegalArgumentException("Debe cargar una imagen.");
        }

        try {
            // Subir la imagen y guardarla en ImgCar
            // Transformar imagen a Base64
            String encodedImage = encodeToBase64(file);
            String imageUrl = uploadToImgBB(encodedImage); // subir a imgbb y devolver ruta
            System.out.println("url imagen::::::::::::::::::::::::::::::::: " + imageUrl);

            if (imageUrl != null) {
                // Limpiar la URL
                String cleanedUrl = imageUrl.replace("\\/", "/");

                producto.setImagen1(cleanedUrl);



            }
            return productoRepository.save(producto);

        } catch(Exception e)  {
            System.out.println(e.getMessage());
        }
        return productoRepository.save(producto); // Aquí falla porque 'productoRepository' no es un repositorio
    }
    // Méto do para codificar la imagen en Base64
    private String encodeToBase64(MultipartFile file) throws Exception {
        byte[] bytes = file.getBytes();
        return Base64.getEncoder().encodeToString(bytes);
    }
    // Subir imagen al repositorio de imagenes ImgBB y obtener la URL de cada imagen
    private String uploadToImgBB(String encodedImage) { // Imagen o archivo en Codigo Base64 como parametro
        RestTemplate restTemplate = new RestTemplate(); // Objeto encargado de enviar una solicitud post a imgBB
        // Preprar el cuerpo que se enviara en la solicutd http
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("key", apiKey);
        body.add("image", encodedImage);
        // Configuracion de encabezados de la solicitud http
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA); // Establece de que tipo de contenido es la solicitud, MULTIPART_FROM_DATA
        // Creacion de la entidad de la solicitud http que contendra el cuerpo y el encabezado
        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);
        // Ejecutar la solocitud post. Envia y recibe una respuesta
        ResponseEntity<String> response = restTemplate.exchange(IMG_BB_API_URL, HttpMethod.POST, requestEntity, String.class);
        // Procesar la respuesta recibida de la API imgBB
        String responseBody = response.getBody(); // Informacion en formato json sobre la imagen
        if (responseBody != null && responseBody.contains("url")) { // busca la url de la imgen en el json
            int start = responseBody.indexOf("\"url\":\"") + 7; // "  "url:" "  extraer el numero de posicion donde empieza la url
            int end = responseBody.indexOf("\"", start); // extraer el numero de posicion donde termina la url
            return responseBody.substring(start, end); // estra url http:// imagen.jpg
        }

        return null;
    }

    public Producto actualizarProductoImgbb(MultipartFile file, Producto producto, Long id) {
        // Verificar si el producto existe antes de actualizarlo
        Producto productoExistente = productoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado"));

        // Si se ha cargado una nueva imagen, la actualizamos
        if (file != null && !file.isEmpty()) {
            try {
                // Subir la nueva imagen y obtener la URL
                String encodedImage = encodeToBase64(file);
                String imageUrl = uploadToImgBB(encodedImage);

                if (imageUrl != null) {
                    // Limpiar la URL y actualizar la imagen
                    String cleanedUrl = imageUrl.replace("\\/", "/");
                    productoExistente.setImagen1(cleanedUrl);
                }
            } catch (Exception e) {
                System.out.println("Error al subir la imagen: " + e.getMessage());
            }
        }

        // Actualizar los demás campos del producto
        productoExistente.setNombre(producto.getNombre());
        productoExistente.setDescripcion(producto.getDescripcion());
        productoExistente.setPrecio(producto.getPrecio());
        productoExistente.setStock(producto.getStock());
        productoExistente.setDescuento(producto.getDescuento());

        // Guardar el producto actualizado
        return productoRepository.save(productoExistente);
    }
    // Méto do para actualizar la imagen
    public Producto updateImage(Long productoId, MultipartFile file) {
        try {
            // Buscar la imagen o el objeto producto q tiene la imagen existente en la base de datos
            Producto imgCar = productoRepository.findById(productoId)
                    .orElseThrow(() -> new IllegalArgumentException("Imagen no encontrada"));

            // Codificar la nueva imagen en base64
            String encodedImage = encodeToBase64(file);

            // Subir la nueva imagen a ImgBB
            String imageUrl = uploadToImgBB(encodedImage);

            // Actualizar la URL de la imagen
            imgCar.setImagen1(imageUrl);

            // Guardar la imagen actualizada en la base de datos
            productoRepository.save(imgCar);

            return imgCar; // Retorna la imagen actualizada
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    // Obtener 1  productos por su ID
    public Producto obtenerProductoPorId(Long id) {
        return productoRepository.findById(id).orElse(null);
    }

    // Eliminar un producto por ID
    public void eliminarProducto(Long id) {
        productoRepository.deleteById(id);
    }
    // Obtener un producto por ID

    // Producto por descuento 50%
    public List<Producto> obtenerProductosConDescuento50() {
        return productoRepository.findByDescuento(50);
    }
    // Producto por descuento 50% solo 3
    public List<Producto> obtenerTresProductosConDescuento50() {
        return productoRepository.findByDescuento(50, PageRequest.of(0, 3)); // 0 para la primera página, 3 para el tamaño de la página
    }
    // Producto por descuento 30% solo 2
    public List<Producto> obtenerDosProductosConDescuento50() {
        return productoRepository.findByDescuento(30, PageRequest.of(0, 2)); // 0 para la primera página, 3 para el tamaño de la página
    }
    //
    public List<Producto> obtenerProductos(int page, int size) {
        return productoRepository.findAll(PageRequest.of(page, size)).getContent();
    }
    //
    public List<Producto> obtenerProductosPorCategoria(String nombreCategoria) {
        return productoRepository.findProductosByNombreCategoria(nombreCategoria);
    }
    // Retornar todos los nombres de categoria
    public List<Categoria> obtenerNombresCategoria(){
        return repositorioCategoria.findAll();
    }

    //Méto do para buscar productos por nombre o descripción
    public List<Producto> buscarProductos(String query) {
        // Realiza la búsqueda utilizando el repositorio
        return productoRepository.buscarPorNombreODescripcion(query);
    }

}


