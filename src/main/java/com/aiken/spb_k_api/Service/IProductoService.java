package com.aiken.spb_k_api.Service;

import com.aiken.spb_k_api.Entity.Producto;

import java.util.List;
import java.util.Optional;

public interface IProductoService {

    List<Producto> getAllProductos();

    Producto getProductoById(Long id);

    void saveProducto(Producto producto);

    void deleteProducto(Long id);
}
