package com.cesde.educlic.service;

import com.cesde.educlic.dto.CategoriaRequestDto;
import com.cesde.educlic.model.entity.Categoria;
import com.cesde.educlic.repository.CategoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoriaService {

    @Autowired
    private CategoriaRepository categoriaRepository;

    public List<Categoria> listarCategorias() {
        return categoriaRepository.findAll();
    }

    public Categoria buscarPorId(Long id) {
        return categoriaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("La categoría con el ID " + id + " no existe en el sistema."));
    }

    public Categoria guardarCategoria(CategoriaRequestDto dto) {
        if (dto.getNombre() == null || dto.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre de la categoría es obligatorio y no puede estar vacío.");
        }

        if (dto.getNombre().length() < 2 || dto.getNombre().length() > 50) {
            throw new IllegalArgumentException("El nombre de la categoría debe tener entre 2 y 50 caracteres.");
        }

        Categoria categoria = Categoria.builder()
                .nombre(dto.getNombre())
                .build();
        
        return categoriaRepository.save(categoria);
    }

    public Categoria actualizarCategoria(Long id, CategoriaRequestDto dto) {
        Categoria categoriaExistente = buscarPorId(id);

        if (dto.getNombre() == null || dto.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre de la categoría es obligatorio.");
        }

        if (dto.getNombre().length() < 2 || dto.getNombre().length() > 50) {
            throw new IllegalArgumentException("El nombre de la categoría debe tener entre 2 y 50 caracteres.");
        }

        categoriaExistente.setNombre(dto.getNombre());
        return categoriaRepository.save(categoriaExistente);
    }

    public void eliminarCategoria(Long id) {
        Categoria categoria = buscarPorId(id);
        categoriaRepository.delete(categoria);
    }
}