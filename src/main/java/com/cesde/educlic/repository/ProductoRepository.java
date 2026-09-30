package com.cesde.educlic.repository;

import com.cesde.educlic.model.entity.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {
    // Útil para filtrar productos por categoría si el front lo necesita
    List<Producto> findByCategoriaId(Long categoriaId);
}