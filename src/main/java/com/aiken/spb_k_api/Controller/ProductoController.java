package com.aiken.spb_k_api.Controller;

import com.aiken.spb_k_api.Entity.Producto;
import com.aiken.spb_k_api.Service.IProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

import java.util.Collections;
import java.util.List;

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

    @GetMapping("/{productoId}")
    public ResponseEntity<Object> getProductosById(@PathVariable Long productoId) {
        try {
            Producto producto = productoService.getProductoById(productoId);
            return ResponseEntity.ok(producto);
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Product not found with id " + productoId);
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

    @PutMapping("/{productoId}")
    public ResponseEntity<String> updateProducto(@PathVariable Long productoId, @RequestBody Producto producto){
        try {
            productoService.saveProducto(producto);
            return ResponseEntity.ok("Product updated successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Error updating product");
        }

    }

    @DeleteMapping("/{productoId}")
    public ResponseEntity<String> deleteProducto(@PathVariable Long productoId){
        try {
            productoService.deleteProducto(productoId);
            return ResponseEntity.ok("Product deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Error deleting product");
        }

    }

    @PutMapping("/aumentar")
    public ResponseEntity<String> aumentarCantidad(
            @RequestParam(name = "productoId") Long productoId,
            @RequestParam(name = "cantidad") int cantidad) {
        try {
            productoService.aumentarCantidad(productoId, cantidad);
            return ResponseEntity.ok("The number of products have been updated to: " + productoService.getCantidad(productoId));
        } catch (HttpClientErrorException.NotFound e) {
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
        try{
            productoService.disminuirCantidad(productoId, cantidad);
            return ResponseEntity.ok("The number of products have been updated to: " + productoService.getCantidad(productoId));
        } catch (HttpClientErrorException.NotFound e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Error: Product not found with ID " + productoId);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Internal error while trying to add products");
        }
    }

}
