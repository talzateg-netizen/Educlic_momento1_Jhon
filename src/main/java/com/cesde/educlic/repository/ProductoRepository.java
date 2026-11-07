package com.cesde.educlic.repository;

import com.cesde.educlic.model.entity.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {
    
    List<Producto> findByCategoriaId(Long categoriaId);

    // Nuevo método para buscar productos por el nombre de la categoría en texto
    List<Producto> findByCategoriaNombreIgnoreCase(String nombreCategoria);
}