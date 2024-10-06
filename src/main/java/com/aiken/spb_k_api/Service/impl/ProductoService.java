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
import java.util.Optional;

@Service
public class ProductoServiceImpl implements IProductoService {

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    public ProductoService(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    @Override
    public List<Producto> getAllProductos() {
        List<Producto> productos = productoRepository.findAll();
        if (productos == null || productos.isEmpty()) {
            throw new ServiceException("No hay productos disponibles.");
        }
        return productos;
    }

    @Override
    public Producto getProductoById(Long productoId) {
        if (productoId == null || productoId <= 0) {
            throw new IllegalArgumentException("El ID del producto debe ser un valor válido.");
        }
        return productoRepository.findById(productoId)
                .orElseThrow(() -> new EntityNotFoundException("Producto no encontrado con ID: " + productoId));
    }

    @Transactional
    @Override
    public void saveProducto(Producto producto) {
        if (producto == null || producto.getNombre() == null || producto.getNombre().isEmpty()) {
            throw new IllegalArgumentException("El producto debe tener un nombre válido");
        }
        productoRepository.save(producto);
    }

    @Override
    @Transactional
    public void deleteProducto(Long productoId) {
        if (productoId == null || productoId <= 0) {
            throw new IllegalArgumentException("El ID del producto debe ser un valor válido.");
        }

        try {
            productoRepository.deleteById(productoId);
        } catch (EmptyResultDataAccessException e) {
            throw new EntityNotFoundException("Producto no encontrado con ID: " + productoId);
        } catch (Exception e) {
            throw new ServiceException("Error al eliminar el producto con ID: " + productoId);
        }
    }


    @Override
    @Transactional
    public void aumentarCantidad(Long productoId, int cantidad) {
        if (productoId == null || productoId <= 0 || cantidad <= 0) {
            throw new IllegalArgumentException("ID del producto y cantidad deben ser valores válidos.");
        }

        Optional<Producto> optionalProducto = productoRepository.findById(productoId);

        if (optionalProducto.isPresent()) {
            Producto producto = optionalProducto.get();
            producto.setCantidad(producto.getCantidad() + cantidad);
            productoRepository.save(producto);
        } else {
            throw new EntityNotFoundException("Producto no encontrado con ID: " + productoId);
        }
    }

    @Override
    @Transactional
    public void disminuirCantidad(Long productoId, int cantidad) {
        if (productoId == null || productoId <= 0 || cantidad <= 0) {
            throw new IllegalArgumentException("ID del producto y cantidad deben ser valores válidos.");
        }

        Optional<Producto> optionalProducto = productoRepository.findById(productoId);

        if (optionalProducto.isPresent()) {
            Producto producto = optionalProducto.get();
            int cantidadActual = producto.getCantidad();

            if (cantidadActual >= cantidad) {
                producto.setCantidad(cantidadActual - cantidad);
                productoRepository.save(producto);
            } else {
                throw new ServiceException("No se puede eliminar más cantidad de la existente para el producto con ID: " + productoId);
            }
        } else {
            throw new EntityNotFoundException("Producto no encontrado con ID: " + productoId);
        }
    }

    @Override
    public int getCantidad(Long productoId) {
        if (productoId == null || productoId <= 0) {
            throw new IllegalArgumentException("El ID del producto debe ser un valor válido.");
        }

        Optional<Producto> productoOptional = productoRepository.findById(productoId);
        if (productoOptional.isPresent()) {
            Producto producto = productoOptional.get();
            return producto.getCantidad();
        } else {
            throw new EntityNotFoundException("Producto no encontrado con ID: " + productoId);
        }
    }

        @Override
    public boolean existsProductoById(Long productoId) {
        if (productoId == null || productoId <= 0) {
            throw new IllegalArgumentException("El ID del producto debe ser un valor válido.");
        }

        Optional<Producto> productoOptional = productoRepository.findById(productoId);
        return productoOptional.isPresent();
    }

}
