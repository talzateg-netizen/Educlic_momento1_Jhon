package com.cesde.educlic.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.cesde.educlic.entity.Categoria;
import com.cesde.educlic.exception.BusinessException;
import com.cesde.educlic.exception.ResourceNotFoundException;
import com.cesde.educlic.repository.CategoriaRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CategoriaService {

    private final CategoriaRepository categoriaRepository;

    public List<Categoria> listar() {
        return categoriaRepository.findAll();
    }

    public Categoria obtenerPorId(Long id) {
        return buscarEntidad(id);
    }

    public Categoria crear(Categoria categoria) {
        if (categoriaRepository.existsByNombreIgnoreCase(categoria.getNombre())) {
            throw new BusinessException("Ya existe una categoria con ese nombre");
        }
        categoria.setId(null);
        return categoriaRepository.save(categoria);
    }

    public Categoria actualizar(Long id, Categoria cambios) {
        Categoria categoria = buscarEntidad(id);
        categoria.setNombre(cambios.getNombre());
        categoria.setDescripcion(cambios.getDescripcion());
        return categoriaRepository.save(categoria);
    }

    public void eliminar(Long id) {
        if (!categoriaRepository.existsById(id)) {
            throw new ResourceNotFoundException("Categoria no encontrada con id " + id);
        }
        categoriaRepository.deleteById(id);
    }

    private Categoria buscarEntidad(Long id) {
        return categoriaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Categoria no encontrada con id " + id));
    }
}