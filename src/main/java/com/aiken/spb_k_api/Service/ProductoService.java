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
public class ProductoService implements IProductoService {

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    public ProductoService(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }
    @Override
    public List<Producto> getAllProductos() {
        List<Producto> productos = productoRepository.findAll();
        if (productos == null) {
            throw new ServiceException("Error al obtener todos los productos");
        }
        return productos;
    }

    @Override
    public Producto getProductoById(Long productoId) {
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
    public void deleteProducto(Long productoId) {
        try {
            productoRepository.deleteById(productoId);
        } catch (EmptyResultDataAccessException e) {
            throw new EntityNotFoundException("Producto no encontrado con ID: " + productoId);
        } catch (Exception e) {
            throw new ServiceException("Error al eliminar el producto con ID: " + productoId);
        }
    }


    @Override
    public void aumentarCantidad(Long productoId,int cantidad) {
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
    public void disminuirCantidad(Long productoId, int cantidad) {
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
        Optional<Producto> productoOptional = productoRepository.findById(productoId);
        return productoOptional.isPresent();
    }



}
