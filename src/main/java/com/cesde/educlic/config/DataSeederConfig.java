package com.cesde.educlic.config;

import com.cesde.educlic.model.entity.Usuario;
import com.cesde.educlic.model.entity.Categoria;
import com.cesde.educlic.repository.CategoriaRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataSeederConfig {

    @Bean
    CommandLineRunner initDatabase(CategoriaRepository categoriaRepository) {
        return args -> {
            if (categoriaRepository.count() == 0) {
                Categoria cat1 = new Categoria();
                cat1.setNombre("Tenis Deportivos");
                cat1.setDescripcion("Calzado especializado para running y training");
                categoriaRepository.save(cat1);

                Categoria cat2 = new Categoria();
                cat2.setNombre("Tenis Urbanos");
                cat2.setDescripcion("Calzado casual para el día a día");
                categoriaRepository.save(cat2);
            }
        };
    }
}