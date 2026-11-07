package com.cesde.educlic.config;

import com.cesde.educlic.model.entity.Categoria;
import com.cesde.educlic.model.entity.Producto;
import com.cesde.educlic.repository.CategoriaRepository;
import com.cesde.educlic.repository.ProductoRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;
import java.util.List;

@Configuration
public class DataSeederConfig {

    @Bean
    CommandLineRunner initDatabase(CategoriaRepository categoriaRepository, ProductoRepository productoRepository) {
        return args -> {
            // 1. Sembrar Categorías si está vacío
            if (categoriaRepository.count() == 0) {
                crearCategoria(categoriaRepository, "Tenis", "Calzado urbano y de alto rendimiento");
                crearCategoria(categoriaRepository, "Gorras", "Estilo y protección para la calle");
                crearCategoria(categoriaRepository, "Camisetas", "Diseños exclusivos y minimalistas");
                crearCategoria(categoriaRepository, "Hoodies", "Comodidad y abrigo con actitud");
                crearCategoria(categoriaRepository, "Accesorios", "Complementos para tu día a día");
            }

            // 2. Sembrar Productos si está vacío (creando múltiples por categoría)
            if (productoRepository.count() == 0) {
                List<Categoria> categorias = categoriaRepository.findAll();

                for (Categoria cat : categorias) {
                    for (int i = 1; i <= 15; i++) {
                        Producto p = new Producto();
                        p.setNombre(cat.getNombre() + " Modelo " + i);
                        p.setDescripcion("Descripción detallada para el producto " + i + " de la categoría " + cat.getNombre());
                        p.setPrecio(BigDecimal.valueOf(50000.0 + (i * 12000.0)));
                        p.setStock(25);
                        p.setCategoria(cat);
                        p.setActivo(true);

                        // Asignar imágenes de prueba según la categoría
                        if (cat.getNombre().equalsIgnoreCase("Tenis")) {
                            p.setImagenUrl("https://images.unsplash.com/photo-1595341888016-a392ef81b7de?q=80");
                        } else if (cat.getNombre().equalsIgnoreCase("Gorras")) {
                            p.setImagenUrl("https://images.unsplash.com/photo-1588850561407-ed78c282e89b?q=80");
                        } else if (cat.getNombre().equalsIgnoreCase("Camisetas")) {
                            p.setImagenUrl("https://images.unsplash.com/photo-1521572267360-ee0c2909d518?q=80");
                        } else if (cat.getNombre().equalsIgnoreCase("Hoodies")) {
                            p.setImagenUrl("https://images.unsplash.com/photo-1556905055-8f358a7a47b2?q=80");
                        } else {
                            p.setImagenUrl("https://images.unsplash.com/photo-1523293182086-7651a899d37f?q=80");
                        }

                        productoRepository.save(p);
                    }
                }
                System.out.println("¡Base de datos poblada con 15 productos por cada categoría exitosamente!");
            }
        };
    }

    private void crearCategoria(CategoriaRepository repo, String nombre, String descripcion) {
        Categoria cat = new Categoria();
        cat.setNombre(nombre);
        cat.setDescripcion(descripcion);
        repo.save(cat);
    }
}