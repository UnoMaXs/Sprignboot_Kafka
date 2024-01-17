package com.aiken.spb_k_api.Controller;

import com.aiken.spb_k_api.Entity.Producto;
import com.aiken.spb_k_api.Service.IProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/v1/productos")
public class ProductoController {


    @Autowired
    private IProductoService productoService;

    @Autowired
    public ProductoController(IProductoService iProductoService) {
        this.productoService = iProductoService;
    }

    @GetMapping
    public ResponseEntity<List> getAllProductos() {
        try {
            List<Producto> productos = productoService.getAllProductos();
            return ResponseEntity.ok(productos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Collections.singletonList("No products found"));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getProductosById(@PathVariable Long id) {
        try {
            Producto producto = productoService.getProductoById(id);
            return ResponseEntity.ok(producto);
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Product not found with id " + id);
        }
    }

    @PostMapping()
    public ResponseEntity<String> createProducto(@RequestBody Producto producto) {
        try {
            productoService.saveProducto(producto);
            return ResponseEntity.ok("Product created successfully");
        } catch (Exception e) {
         return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                 .body("Error creating Product");
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateProducto(@PathVariable Long id, @RequestBody Producto producto){
        try {
            productoService.saveProducto(producto);
            return ResponseEntity.ok("Product updated successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Error updating product");
        }

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProducto(@PathVariable Long id){
        try {
            productoService.deleteProducto(id);
            return ResponseEntity.ok("Product deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Error deleting product");
        }

    }

}
