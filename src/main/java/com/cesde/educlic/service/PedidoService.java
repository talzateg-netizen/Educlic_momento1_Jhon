package com.cesde.educlic.service;

import com.cesde.educlic.model.enums.EstadoPedido;
import com.cesde.educlic.exception.BusinessException;
import com.cesde.educlic.exception.ResourceNotFoundException;
import com.cesde.educlic.repository.PedidoRepository;
import com.cesde.educlic.repository.ProductoRepository;
import com.cesde.educlic.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final ProductoRepository productoRepository;
    private final UsuarioRepository usuarioRepository;

    /**
     * Confirma el checkout. El JSON de entrada luce asi (sin DTOs):
     * {
     *   "usuario": { "id": 1 },
     *   "nombreDestinatario": "...", "telefonoContacto": "...",
     *   "direccion": "...", "ciudad": "...", "codigoPostal": "...", "notas": "...",
     *   "detalles": [ { "producto": { "id": 5 }, "cantidad": 2 } ]
     * }
     * El service valida stock, calcula precios/totales y descuenta inventario;
     * el cliente NO envia precioUnitario, subtotal ni pedido dentro de detalles.
     */
    @Transactional
    public Pedido crear(Pedido pedidoRequest) {
        if (pedidoRequest.getUsuario() == null || pedidoRequest.getUsuario().getId() == null) {
            throw new BusinessException("Debe indicar el usuario (\"usuario\": { \"id\": N })");
        }
        Usuario usuario = usuarioRepository.findById(pedidoRequest.getUsuario().getId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Usuario no encontrado con id " + pedidoRequest.getUsuario().getId()));

        if (pedidoRequest.getDetalles() == null || pedidoRequest.getDetalles().isEmpty()) {
            throw new BusinessException("El pedido debe tener al menos un producto");
        }

        Pedido pedido = Pedido.builder()
                .usuario(usuario)
                .estado(EstadoPedido.PENDIENTE)
                .nombreDestinatario(pedidoRequest.getNombreDestinatario())
                .telefonoContacto(pedidoRequest.getTelefonoContacto())
                .direccion(pedidoRequest.getDireccion())
                .ciudad(pedidoRequest.getCiudad())
                .codigoPostal(pedidoRequest.getCodigoPostal())
                .notas(pedidoRequest.getNotas())
                .build();

        BigDecimal total = BigDecimal.ZERO;

        for (DetallePedido item : pedidoRequest.getDetalles()) {
            if (item.getProducto() == null || item.getProducto().getId() == null) {
                throw new BusinessException("Cada detalle debe indicar el producto (\"producto\": { \"id\": N })");
            }
            if (item.getCantidad() == null || item.getCantidad() < 1) {
                throw new BusinessException("La cantidad debe ser al menos 1");
            }

            Producto producto = productoRepository.findById(item.getProducto().getId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Producto no encontrado con id " + item.getProducto().getId()));

            if (producto.getStock() < item.getCantidad()) {
                throw new BusinessException(
                        "Stock insuficiente para \"" + producto.getNombre() + "\". Disponible: " + producto.getStock());
            }

            BigDecimal subtotal = producto.getPrecio().multiply(BigDecimal.valueOf(item.getCantidad()));

            DetallePedido detalle = DetallePedido.builder()
                    .producto(producto)
                    .cantidad(item.getCantidad())
                    .precioUnitario(producto.getPrecio())
                    .subtotal(subtotal)
                    .build();

            pedido.agregarDetalle(detalle);
            total = total.add(subtotal);

            producto.setStock(producto.getStock() - item.getCantidad());
            productoRepository.save(producto);
        }

        pedido.setTotal(total);

        return pedidoRepository.save(pedido);
    }

    /** Usado en perfil.html para el historial de compras del usuario. */
    public List<Pedido> listarPorUsuario(Long usuarioId) {
        return pedidoRepository.findByUsuarioIdOrderByFechaPedidoDesc(usuarioId);
    }

    /** Usado por un ADMIN para ver todos los pedidos. */
    public List<Pedido> listarTodos() {
        return pedidoRepository.findAllByOrderByFechaPedidoDesc();
    }

    public Pedido obtenerPorId(Long id) {
        return buscarEntidad(id);
    }

    public Pedido cambiarEstado(Long id, EstadoPedido nuevoEstado) {
        Pedido pedido = buscarEntidad(id);
        pedido.setEstado(nuevoEstado);
        return pedidoRepository.save(pedido);
    }

    private Pedido buscarEntidad(Long id) {
        return pedidoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pedido no encontrado con id " + id));
    }
}