package com.cesde.educlic.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.cesde.educlic.entity.Categoria;
import com.cesde.educlic.entity.Producto;
import com.cesde.educlic.exception.BusinessException;
import com.cesde.educlic.exception.ResourceNotFoundException;
import com.cesde.educlic.repository.CategoriaRepository;
import com.cesde.educlic.repository.ProductoRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductoService {

    private final ProductoRepository productoRepository;
    private final CategoriaRepository categoriaRepository;

    /** Usado por productos.html: catalogo completo de productos activos. */
    public List<Producto> listarActivos() {
        return productoRepository.findByActivoTrue();
    }

    public List<Producto> listarPorCategoria(Long categoriaId) {
        return productoRepository.findByCategoriaIdAndActivoTrue(categoriaId);
    }

    public List<Producto> buscar(String nombre) {
        return productoRepository.findByNombreContainingIgnoreCaseAndActivoTrue(nombre);
    }

    /** Usado por producto.html: detalle de un solo producto. */
    public Producto obtenerPorId(Long id) {
        return buscarEntidad(id);
    }

    public Producto crear(Producto producto) {
        Categoria categoria = resolverCategoria(producto);
        producto.setId(null);
        producto.setCategoria(categoria);
        producto.setActivo(true);
        return productoRepository.save(producto);
    }

    public Producto actualizar(Long id, Producto cambios) {
        Producto producto = buscarEntidad(id);
        Categoria categoria = resolverCategoria(cambios);

        producto.setNombre(cambios.getNombre());
        producto.setDescripcion(cambios.getDescripcion());
        producto.setPrecio(cambios.getPrecio());
        producto.setStock(cambios.getStock());
        producto.setImagenUrl(cambios.getImagenUrl());
        producto.setCategoria(categoria);

        return productoRepository.save(producto);
    }

    public void desactivar(Long id) {
        Producto producto = buscarEntidad(id);
        producto.setActivo(false);
        productoRepository.save(producto);
    }

    public void eliminar(Long id) {
        if (!productoRepository.existsById(id)) {
            throw new ResourceNotFoundException("Producto no encontrado con id " + id);
        }
        productoRepository.deleteById(id);
    }

    private Producto buscarEntidad(Long id) {
        return productoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con id " + id));
    }

    /**
     * El JSON de entrada trae "categoria": { "id": N } con los demas campos
     * en null. Aqui se busca la categoria real y completa en la BD para no
     * guardar un producto apuntando a una categoria "vacia".
     */
    private Categoria resolverCategoria(Producto producto) {
        if (producto.getCategoria() == null || producto.getCategoria().getId() == null) {
            throw new BusinessException("Debe indicar la categoria (\"categoria\": { \"id\": N })");
        }
        Long categoriaId = producto.getCategoria().getId();
        return categoriaRepository.findById(categoriaId)
                .orElseThrow(() -> new ResourceNotFoundException("Categoria no encontrada con id " + categoriaId));
    }
}