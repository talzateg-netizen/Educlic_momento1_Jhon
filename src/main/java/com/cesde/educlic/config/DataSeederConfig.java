package com.cesde.educlic.config;

import com.cesde.educlic.model.entity.*;
import com.cesde.educlic.model.enums.EstadoPedido;
import com.cesde.educlic.model.enums.Rol;
import com.cesde.educlic.repository.CategoriaRepository;
import com.cesde.educlic.repository.PedidoRepository;
import com.cesde.educlic.repository.ProductoRepository;
import com.cesde.educlic.repository.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Configuration
public class DataSeederConfig {

    @Bean
    CommandLineRunner initDatabase(
            CategoriaRepository categoriaRepository,
            ProductoRepository productoRepository,
            UsuarioRepository usuarioRepository,
            PedidoRepository pedidoRepository) {
        return args -> {

            // 1. Sembrar Categorías
            if (categoriaRepository.count() == 0) {
                crearCategoria(categoriaRepository, "Tenis", "Calzado urbano y de alto rendimiento");
                crearCategoria(categoriaRepository, "Gorras", "Estilo y protección para la calle");
                crearCategoria(categoriaRepository, "Camisetas", "Diseños exclusivos y minimalistas");
                crearCategoria(categoriaRepository, "Hoodies", "Comodidad y abrigo con actitud");
                crearCategoria(categoriaRepository, "Accesorios", "Complementos para tu día a día");
            }

            // 2. Sembrar Usuarios (3 Administradores y 15 Clientes)
            if (usuarioRepository.count() == 0) {
                // Crear 3 Administradores
                for (int i = 1; i <= 3; i++) {
                    Usuario admin = Usuario.builder()
                            .nombre("Admin " + i)
                            .email("admin" + i + "@tiendacreativa.com")
                            .password("$2a$10$dummyEncryptedPasswordForTest123")
                            .rol(Rol.ADMIN)
                            .build();
                    usuarioRepository.save(admin);
                }

                // Crear 15 Clientes
                for (int i = 1; i <= 15; i++) {
                    Usuario cliente = Usuario.builder()
                            .nombre("Cliente " + i)
                            .email("cliente" + i + "@correo.com")
                            .password("$2a$10$dummyEncryptedPasswordForTest123")
                            .rol(Rol.CLIENTE)
                            .build();
                    usuarioRepository.save(cliente);
                }
            }

            // 3. Sembrar Productos (15 por cada categoría con sus tallas y colores)
            if (productoRepository.count() == 0) {
                List<Categoria> categorias = categoriaRepository.findAll();

                for (Categoria cat : categorias) {
                    for (int i = 1; i <= 15; i++) {
                        Producto p = new Producto();
                        p.setNombre(cat.getNombre() + " Edición " + i);
                        p.setDescripcion("Artículo exclusivo de alta calidad perteneciente a la línea de " + cat.getNombre().toLowerCase() + ".");
                        p.setPrecio(BigDecimal.valueOf(60000.0 + (i * 15000.0)));
                        p.setStock(30);
                        p.setCategoria(cat);
                        p.setActivo(true);
                        p.setFechaCreacion(LocalDateTime.now());

                        // Asignar tallas y colores según la categoría (Hibernate llenará producto_tallas y producto_colores)
                        if (cat.getNombre().equalsIgnoreCase("Tenis")) {
                            p.setTallas(Arrays.asList("37", "38", "39", "40", "41", "42"));
                            p.setColores(Arrays.asList("Negro", "Blanco", "Multicolor"));
                            p.setImagenUrl("https://images.unsplash.com/photo-1595341888016-a392ef81b7de?q=80");
                        } else if (cat.getNombre().equalsIgnoreCase("Gorras")) {
                            p.setTallas(Arrays.asList("Única"));
                            p.setColores(Arrays.asList("Negro", "Azul", "Rojo"));
                            p.setImagenUrl("https://images.unsplash.com/photo-1588850561407-ed78c282e89b?q=80");
                        } else if (cat.getNombre().equalsIgnoreCase("Camisetas") || cat.getNombre().equalsIgnoreCase("Hoodies")) {
                            p.setTallas(Arrays.asList("S", "M", "L", "XL"));
                            p.setColores(Arrays.asList("Negro", "Blanco", "Gris"));
                            p.setImagenUrl(cat.getNombre().equalsIgnoreCase("Camisetas") ?
                                    "https://images.unsplash.com/photo-1521572267360-ee0c2909d518?q=80" :
                                    "https://images.unsplash.com/photo-1556905055-8f358a7a47b2?q=80");
                        } else {
                            p.setTallas(Arrays.asList("Estándar"));
                            p.setColores(Arrays.asList("Dorado", "Plateado", "Negro"));
                            p.setImagenUrl("https://images.unsplash.com/photo-1523293182086-7651a899d37f?q=80");
                        }

                        productoRepository.save(p);
                    }
                }
            }

            // 4. Sembrar 15 Pedidos de prueba y sus Detalles
            if (pedidoRepository.count() == 0) {
                List<Usuario> usuarios = usuarioRepository.findAll();
                List<Producto> productos = productoRepository.findAll();

                if (!usuarios.isEmpty() && !productos.isEmpty()) {
                    EstadoPedido[] estados = {EstadoPedido.PENDIENTE, EstadoPedido.PROCESANDO, EstadoPedido.ENVIADO, EstadoPedido.ENTREGADO};

                    for (int i = 1; i <= 15; i++) {
                        // Asignamos intercalando entre los usuarios disponibles
                        Usuario clientePedido = usuarios.get(i % usuarios.size());

                        Pedido pedido = new Pedido();
                        pedido.setUsuario(clientePedido);
                        pedido.setEstado(estados[i % estados.length]);
                        pedido.setFechaCreacion(LocalDateTime.now().minusDays(15 - i));

                        // Seleccionamos un producto para el detalle
                        Producto prodSeleccionado = productos.get((i * 3) % productos.size());
                        int cantidad = (i % 3) + 1;

                        BigDecimal precioUnitario = prodSeleccionado.getPrecio();
                        BigDecimal totalPedido = precioUnitario.multiply(BigDecimal.valueOf(cantidad));

                        pedido.setTotal(totalPedido);

                        DetallePedido detalle = new DetallePedido();
                        detalle.setPedido(pedido);
                        detalle.setProducto(prodSeleccionado);
                        detalle.setCantidad(cantidad);
                        detalle.setPrecioUnitario(precioUnitario);

                        pedido.setDetalles(Arrays.asList(detalle));
                        pedidoRepository.save(pedido);
                    }
                }
            }

            System.out.println("¡Base de datos poblada al 100%: Categorías, Usuarios, Productos, Tallas, Colores, Pedidos y Detalles listos!");
        };
    }

    private void crearCategoria(CategoriaRepository repo, String nombre, String descripcion) {
        Categoria cat = new Categoria();
        cat.setNombre(nombre);
        cat.setDescripcion(descripcion);
        repo.save(cat);
    }
}