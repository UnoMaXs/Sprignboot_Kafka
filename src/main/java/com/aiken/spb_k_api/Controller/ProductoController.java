package com.aiken.spb_k_api.Controller;

import com.aiken.spb_k_api.Entity.Producto;
import com.aiken.spb_k_api.Service.IProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/v1/productos")
public class ProductoController {

    private IProductoService productoService;
    private KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    public ProductoController(KafkaTemplate<String, String> kafkaTemplate,IProductoService iProductoService) {
        this.productoService = iProductoService;
        this.kafkaTemplate = kafkaTemplate;

    }

    @GetMapping
    public ResponseEntity<List> getAllProductos() {
        try {
            List<Producto> productos = productoService.getAllProductos();
            kafkaTemplate.send("inventoryTopic", productos.isEmpty() ? "No products found" : "Showing all the products");
            return ResponseEntity.ok(productos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonList("Error retrieving products"));
        }
    }

    @GetMapping("/{productoId}")
    public ResponseEntity<Object> getProductosById(@PathVariable Long productoId) {
        try {
            Producto producto = productoService.getProductoById(productoId);
            kafkaTemplate.send("inventoryTopic", producto != null ? "Product found: " + producto : "Product not found with id " + productoId);
            return ResponseEntity.ok(producto);
        } catch (Exception e) {
            kafkaTemplate.send("inventoryTopic", "Error retrieving product with id " + productoId);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error retrieving product with id " + productoId);
        }
    }

    @PostMapping()
    public ResponseEntity<String> createProducto(@RequestBody Producto producto) {
        try {
            productoService.saveProducto(producto);
            String productInfo = "Product ID: " + producto.getId() + ", Product Name: " + producto.getNombre();
            kafkaTemplate.send("inventoryTopic", "Product created successfully: " + productInfo);
            return ResponseEntity.ok("Product created successfully");
        } catch (Exception e) {
            kafkaTemplate.send("inventoryTopic", "Error creating product: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error creating Product");
        }
    }
@PutMapping("/{productoId}")
public ResponseEntity<String> updateProducto(@PathVariable Long productoId, @RequestBody Producto producto){
    try {
        productoService.saveProducto(producto);
        kafkaTemplate.send("inventoryTopic", "inventory modification: Product ID: " + productoId + " Was updated successfully");
        return ResponseEntity.ok("Product updated successfully");
    } catch (EntityNotFoundException e) {
        kafkaTemplate.send("inventoryTopic", "Product update failed: Product with ID " + productoId + " not found");
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("Error updating product: Product with ID " + productoId + " not found");
    } catch (Exception e) {
        kafkaTemplate.send("inventoryTopic", "inventory modification: Product ID: " + productoId + " Wasn't updated successfully");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error updating product");
    }
}

@DeleteMapping("/{productoId}")
public ResponseEntity<String> deleteProducto(@PathVariable Long productoId){
    try {
        productoService.deleteProducto(productoId);
        kafkaTemplate.send("inventoryTopic", "inventory modification: Product ID: " + productoId + " Was deleted");
        return ResponseEntity.ok("Product deleted successfully");
    } catch (EntityNotFoundException e) {
        kafkaTemplate.send("inventoryTopic", "Product deleting failed: Product with ID " + productoId + " not found");
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("Error deleting product: Product with ID " + productoId + " not found");
    } catch (Exception e) {
        kafkaTemplate.send("inventoryTopic", "inventory modification: Product ID: " + productoId + " Wasn't deleted successfully");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error deleting product");
    }
}


    @PutMapping("/aumentar")
    public ResponseEntity<String> aumentarCantidad(
            @RequestParam(name = "productoId") Long productoId,
            @RequestParam(name = "cantidad") int cantidad) {
        try {
            productoService.aumentarCantidad(productoId, cantidad);
            int nuevaCantidad = productoService.getCantidad(productoId);
            kafkaTemplate.send("inventoryTopic", "inventory modification: Product ID: " + productoId + " - New quantity: " + nuevaCantidad);
            return ResponseEntity.ok("The number of products have been updated to: " + nuevaCantidad);
        } catch (HttpClientErrorException.NotFound e) {
            kafkaTemplate.send("inventoryTopic", "inventory modification: Product ID: " + productoId + " Wasn't found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Error: Product not found with ID " + productoId);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Internal error while trying to add products");
        }
    }

    @PutMapping("/disminuir")
    public ResponseEntity<String> disminuirCantidad(
            @RequestParam(name = "productoId") Long productoId,
            @RequestParam(name = "cantidad") int cantidad) {
        try {
            productoService.disminuirCantidad(productoId, cantidad);
            int nuevaCantidad = productoService.getCantidad(productoId);
            kafkaTemplate.send("inventoryTopic", "inventory modification: Product ID: " + productoId + " - New quantity: " + nuevaCantidad);
            return ResponseEntity.ok("The number of products have been updated to: " + nuevaCantidad);
        } catch (HttpClientErrorException.NotFound e) {
            kafkaTemplate.send("inventoryTopic", "inventory modification: Product ID: " + productoId + " Wasn't found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Error: Product not found with ID " + productoId);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Internal error while trying to add products");
        }
    }


}
