package com.aiken.spb_k_api.Service;

import com.aiken.spb_k_api.Entity.Producto;
import com.aiken.spb_k_api.Repository.ProductoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.hibernate.service.spi.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProductoService implements IProductoService {

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    public ProductoService(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    @Override
    public List<Producto> getAllProductos() {
        try{
            return productoRepository.findAll();
        } catch (Exception e) {
            throw new ServiceException("Error al obtener todo los productos");
        }
    }

    @Override
    public Producto getProductoById(Long id) {
        return productoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Producto no encontrado con ID: " + id));
    }

    @Transactional
    @Override
    public void saveProducto(Producto producto) {
        if (producto == null || producto.getNombre() == null || producto.getNombre().isEmpty()) {
            throw new IllegalArgumentException("El producto debe tener un nombre valido");
        }
        productoRepository.save(producto);
    }

    @Override
    public void deleteProducto(Long id) {
        try {
            productoRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new EntityNotFoundException("Producto no encontrado con ID: " + id);
        } catch (Exception e) {
            throw new ServiceException("Error al eliminar el producto con ID: " + id);
        }
    }
}
