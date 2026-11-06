package com.cesde.educlic.service;

import com.cesde.educlic.dto.PedidoRequestDto;
import com.cesde.educlic.model.entity.Pedido;
import com.cesde.educlic.model.entity.DetallePedido;
import com.cesde.educlic.model.entity.Producto;
import com.cesde.educlic.model.entity.Usuario;
import com.cesde.educlic.model.enums.EstadoPedido;
import com.cesde.educlic.repository.PedidoRepository;
import com.cesde.educlic.repository.ProductoRepository;
import com.cesde.educlic.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PedidoService {

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ProductoRepository productoRepository;

    public List<Pedido> listarPedidos() {
        return pedidoRepository.findAll();
    }

    public List<Pedido> listarPedidosPorUsuario(Long usuarioId) {
        return pedidoRepository.findByUsuarioId(usuarioId);
    }

    public Optional<Pedido> buscarPorId(Long id) {
        return pedidoRepository.findById(id);
    }

    @Transactional
    public Pedido crearPedido(PedidoRequestDto dto) {
        Usuario usuario = usuarioRepository.findById(dto.getUsuarioId())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + dto.getUsuarioId()));

        List<DetallePedido> detalles = null;
        if (dto.getDetalles() != null) {
            detalles = dto.getDetalles().stream().map(detDto -> {
                Producto producto = productoRepository.findById(detDto.getProductoId())
                        .orElseThrow(() -> new RuntimeException("Producto no encontrado con ID: " + detDto.getProductoId()));

                BigDecimal precioUnit = producto.getPrecio();

                return DetallePedido.builder()
                        .producto(producto)
                        .cantidad(detDto.getCantidad())
                        .precioUnitario(precioUnit)
                        .build();
            }).collect(Collectors.toList());
        }

        BigDecimal totalGeneral = BigDecimal.ZERO;
        if (detalles != null) {
            for (DetallePedido d : detalles) {
                BigDecimal subtotal = d.getPrecioUnitario().multiply(BigDecimal.valueOf(d.getCantidad()));
                totalGeneral = totalGeneral.add(subtotal);
            }
        }

        Pedido pedido = Pedido.builder()
                .usuario(usuario)
                .detalles(detalles)
                .total(totalGeneral)
                .estado(EstadoPedido.PENDIENTE)
                .build();

        if (pedido.getDetalles() != null) {
            for (DetallePedido detalle : pedido.getDetalles()) {
                detalle.setPedido(pedido);
            }
        }

        return pedidoRepository.save(pedido);
    }

    @Transactional
    public Pedido actualizarPedido(Long id, PedidoRequestDto dto) {
        return pedidoRepository.findById(id).map(pedidoExistente -> {
            Usuario usuario = usuarioRepository.findById(dto.getUsuarioId())
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + dto.getUsuarioId()));

            pedidoExistente.setUsuario(usuario);

            if (pedidoExistente.getDetalles() != null) {
                pedidoExistente.getDetalles().clear();
            }

            if (dto.getDetalles() != null) {
                List<DetallePedido> nuevosDetalles = dto.getDetalles().stream().map(detDto -> {
                    Producto producto = productoRepository.findById(detDto.getProductoId())
                            .orElseThrow(() -> new RuntimeException("Producto no encontrado con ID: " + detDto.getProductoId()));

                    return DetallePedido.builder()
                            .pedido(pedidoExistente)
                            .producto(producto)
                            .cantidad(detDto.getCantidad())
                            .precioUnitario(producto.getPrecio())
                            .build();
                }).collect(Collectors.toList());

                pedidoExistente.getDetalles().addAll(nuevosDetalles);
            }

            BigDecimal totalGeneral = BigDecimal.ZERO;
            if (pedidoExistente.getDetalles() != null) {
                for (DetallePedido d : pedidoExistente.getDetalles()) {
                    BigDecimal subtotal = d.getPrecioUnitario().multiply(BigDecimal.valueOf(d.getCantidad()));
                    totalGeneral = totalGeneral.add(subtotal);
                }
            }
            pedidoExistente.setTotal(totalGeneral);

            return pedidoRepository.save(pedidoExistente);
        }).orElseThrow(() -> new RuntimeException("Pedido no encontrado con el ID: " + id));
    }

    public void eliminarPedido(Long id) {
        pedidoRepository.deleteById(id);
    }
}