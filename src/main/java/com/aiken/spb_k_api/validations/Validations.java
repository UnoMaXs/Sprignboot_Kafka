package com.aiken.spb_k_api.Validations;

import com.aiken.spb_k_api.Entity.Producto;
import jakarta.persistence.EntityNotFoundException;

public class Validations {

    public static void validateProductoId(Long productoId) {
        if (productoId == null || productoId <= 0) {
            throw new IllegalArgumentException("El ID del producto debe ser un valor válido.");
        }
    }

    public static void validateProducto(Producto producto) {
        if (producto == null || producto.getNombre() == null || producto.getNombre().isEmpty()) {
            throw new IllegalArgumentException("El producto debe tener un nombre válido.");
        }
    }

    public static void validateCantidad(int cantidad) {
        if (cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor a 0.");
        }
    }
}
