package com.cesde.educlic.service;

import com.cesde.educlic.dto.ProductoRequestDto;
import com.cesde.educlic.model.entity.Categoria;
import com.cesde.educlic.model.entity.Producto;
import com.cesde.educlic.repository.CategoriaRepository;
import com.cesde.educlic.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class ProductoService {

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;
    
    public List<Producto> listarPorNombreCategoria(String nombreCategoria) {
        return productoRepository.findByCategoriaNombreIgnoreCase(nombreCategoria);
    }

    public List<Producto> listarProductos() {
        return productoRepository.findAll();
    }

    public List<Producto> listarPorCategoria(Long categoriaId) {
        return productoRepository.findByCategoriaId(categoriaId);
    }

    public Optional<Producto> buscarPorId(Long id) {
        return productoRepository.findById(id);
    }

    public Producto guardarProducto(ProductoRequestDto dto) {
        Categoria categoria = categoriaRepository.findById(dto.getCategoriaId())
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada con el ID: " + dto.getCategoriaId()));

        Producto producto = new Producto();
        producto.setNombre(dto.getNombre());
        producto.setDescripcion(dto.getDescripcion());
        producto.setPrecio(BigDecimal.valueOf(dto.getPrecio()));
        producto.setStock(dto.getStock());
        producto.setCategoria(categoria);

        return productoRepository.save(producto);
    }

    public Producto actualizarProducto(Long id, ProductoRequestDto dto) {
        return productoRepository.findById(id).map(productoExistente -> {
            Categoria categoria = categoriaRepository.findById(dto.getCategoriaId())
                    .orElseThrow(() -> new RuntimeException("Categoría no encontrada con el ID: " + dto.getCategoriaId()));

            productoExistente.setNombre(dto.getNombre());
            productoExistente.setDescripcion(dto.getDescripcion());
            productoExistente.setPrecio(BigDecimal.valueOf(dto.getPrecio()));
            productoExistente.setStock(dto.getStock());
            productoExistente.setCategoria(categoria);

            return productoRepository.save(productoExistente);
        }).orElseThrow(() -> new RuntimeException("Producto no encontrado con el ID: " + id));
    }

    public void eliminarProducto(Long id) {
        productoRepository.deleteById(id);
    }
}