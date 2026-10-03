package com.cesde.educlic.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.cesde.educlic.exception.ResourceNotFoundException;
import com.cesde.educlic.model.entity.Categoria;
import com.cesde.educlic.repository.CategoriaRepository;

@Service
public class CategoriaService {

    private final CategoriaRepository categoriaRepository;

    public CategoriaService(CategoriaRepository categoriaRepository) {
        this.categoriaRepository = categoriaRepository;
    }

    public List<Categoria> listarCategorias() {
        return categoriaRepository.findAll();
    }

    public Categoria buscarPorId(Long id) {
        return categoriaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Categoría no encontrada con ID: " + id));
    }

    public Categoria guardarCategoria(Categoria categoria) {
        return categoriaRepository.save(categoria);
    }

    public void eliminarCategoria(Long id) {
        Categoria categoria = buscarPorId(id);
        categoriaRepository.delete(categoria);
    }
}