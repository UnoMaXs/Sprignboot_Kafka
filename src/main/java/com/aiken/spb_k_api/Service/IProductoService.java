package com.aiken.spb_k_api.Service;

import com.aiken.spb_k_api.Entity.Producto;

import java.util.List;

public interface IProductoService {

    List<Producto> getAllProductos();

    Producto getProductoById(Long Productoid);

    void saveProducto(Producto producto);

    void deleteProducto(Long Productoid);

    void aumentarCantidad(Long Productoid,int cantidad);

    void disminuirCantidad(Long Productoid,int cantidad);

    int getCantidad(Long productoId);

    boolean existsProductoById(Long productoId);

}
