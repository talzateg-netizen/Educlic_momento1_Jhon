package com.cesde.educlic.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.cesde.educlic.exception.ResourceNotFoundException;
import com.cesde.educlic.model.entity.Producto;
import com.cesde.educlic.repository.ProductoRepository;

@Service
public class ProductoService {

    private final ProductoRepository productoRepository;

    public ProductoService(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    public List<Producto> listarProductos() {
        return productoRepository.findAll();
    }

    public List<Producto> listarPorCategoria(Long categoriaId) {
        return productoRepository.findByCategoriaId(categoriaId);
    }

    public Producto buscarPorId(Long id) {
        return productoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con ID: " + id));
    }

    public Producto guardarProducto(Producto producto) {
        return productoRepository.save(producto);
    }

    public Producto actualizarProducto(Long id, Producto producto) {
        buscarPorId(id);
        producto.setId(id);
        return productoRepository.save(producto);
    }

    public void eliminarProducto(Long id) {
        Producto producto = buscarPorId(id);
        productoRepository.delete(producto);
    }
}