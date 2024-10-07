package com.aiken.spb_k_api.Controller;

import com.aiken.spb_k_api.Entity.Producto;
import com.aiken.spb_k_api.Service.IProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/productos")
public class ProductoController {

    private final IProductoService productoService;
    private final KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    public ProductoController(KafkaTemplate<String, String> kafkaTemplate, IProductoService productoService) {
        this.productoService = productoService;
        this.kafkaTemplate = kafkaTemplate;
    }

    @GetMapping
    public ResponseEntity<List<Producto>> getAllProductos() {
        List<Producto> productos = productoService.getAllProductos();
        kafkaTemplate.send("inventoryTopic", productos.isEmpty() ? "No products found" : "Showing all products");
        return ResponseEntity.ok(productos);
    }

    @GetMapping("/{productoId}")
    public ResponseEntity<Producto> getProductosById(@PathVariable Long productoId) {
        Producto producto = productoService.getProductoById(productoId);
        kafkaTemplate.send("inventoryTopic", "Product found: " + producto);
        return ResponseEntity.ok(producto);
    }

    @PostMapping
    public ResponseEntity<String> createProducto(@RequestBody Producto producto) {
        productoService.saveProducto(producto);
        kafkaTemplate.send("inventoryTopic", "Product created: " + producto);
        return ResponseEntity.status(HttpStatus.CREATED).body("Product created successfully");
    }

    @PutMapping("/{productoId}")
    public ResponseEntity<String> updateProducto(@PathVariable Long productoId, @RequestBody Producto producto) {
        productoService.updateProducto(productoId, producto);
        kafkaTemplate.send("inventoryTopic", "Product updated: " + producto);
        return ResponseEntity.ok("Product updated successfully");
    }

    @DeleteMapping("/{productoId}")
    public ResponseEntity<String> deleteProducto(@PathVariable Long productoId) {
        productoService.deleteProducto(productoId);
        kafkaTemplate.send("inventoryTopic", "Product deleted with ID: " + productoId);
        return ResponseEntity.ok("Product deleted successfully");
    }

    @PutMapping("/aumentar")
    public ResponseEntity<String> aumentarCantidad(@RequestParam Long productoId, @RequestParam int cantidad) {
        productoService.aumentarCantidad(productoId, cantidad);
        int nuevaCantidad = productoService.getCantidad(productoId);
        kafkaTemplate.send("inventoryTopic", "Increased quantity for Product ID: " + productoId + " to " + nuevaCantidad);
        return ResponseEntity.ok("Product quantity updated to: " + nuevaCantidad);
    }

    @PutMapping("/disminuir")
    public ResponseEntity<String> disminuirCantidad(@RequestParam Long productoId, @RequestParam int cantidad) {
        productoService.disminuirCantidad(productoId, cantidad);
        int nuevaCantidad = productoService.getCantidad(productoId);
        kafkaTemplate.send("inventoryTopic", "Decreased quantity for Product ID: " + productoId + " to " + nuevaCantidad);
        return ResponseEntity.ok("Product quantity updated to: " + nuevaCantidad);
    }
}
