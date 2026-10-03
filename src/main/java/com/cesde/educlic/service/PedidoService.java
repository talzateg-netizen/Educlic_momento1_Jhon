package com.cesde.educlic.service;

import com.cesde.educlic.exception.BusinessException;
import com.cesde.educlic.exception.ResourceNotFoundException;
import com.cesde.educlic.model.entity.DetallePedido;
import com.cesde.educlic.model.enums.EstadoPedido;
import com.cesde.educlic.model.entity.Pedido;
import com.cesde.educlic.model.entity.Usuario;
import com.cesde.educlic.repository.PedidoRepository;
import com.cesde.educlic.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final UsuarioRepository usuarioRepository;

    public PedidoService(PedidoRepository pedidoRepository, UsuarioRepository usuarioRepository) {
        this.pedidoRepository = pedidoRepository;
        this.usuarioRepository = usuarioRepository;
    }

    public List<Pedido> listarPedidos() {
        return pedidoRepository.findAll();
    }

    public List<Pedido> listarPedidosPorUsuario(Long usuarioId) {
        return pedidoRepository.findByUsuarioId(usuarioId);
    }

    public List<Pedido> listarPedidosPorEstado(String estado) {
        return pedidoRepository.findByEstado(estado);
    }

    public Pedido buscarPorId(Long id) {
        return pedidoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pedido no encontrado con ID: " + id));
    }

    @Transactional
    public Pedido crearPedido(Pedido pedido) {

        // -------------------------------------------------------------
        // REGLA DE NEGOCIO 1: NO SE PUEDE HACER PEDIDO SIN LOGUEAR
        // -------------------------------------------------------------
        if (pedido.getUsuario() == null || pedido.getUsuario().getId() == null) {
            throw new BusinessException("No se puede realizar un pedido sin iniciar sesión (Usuario requerido).");
        }

        // Buscar el usuario en BD para obtener sus datos completos
        Usuario usuarioExistente = usuarioRepository.findById(pedido.getUsuario().getId())
                .orElseThrow(() -> new BusinessException("El usuario asociado no existe. Debe iniciar sesión."));


        // -------------------------------------------------------------
        // REGLA DE NEGOCIO 2: NO SE PUEDE HACER PEDIDO SIN CONFIRMAR DIRECCIÓN
        // -------------------------------------------------------------
        if (usuarioExistente.getDireccion() == null || usuarioExistente.getDireccion().trim().isEmpty()) {
            throw new BusinessException("No se puede realizar el pedido sin confirmar una dirección de envío en el perfil.");
        }


        // -------------------------------------------------------------
        // REGLA DE NEGOCIO 3: NO SE DEBE HACER PEDIDO MENOR O IGUAL A 0
        // -------------------------------------------------------------
        if (pedido.getTotal() == null || pedido.getTotal().compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessException("No se puede realizar un pedido con un monto total menor o igual a 0.");
        }

        // Asignar estado PENDIENTE por defecto si no viene especificado
        if (pedido.getEstado() == null) {
            pedido.setEstado(EstadoPedido.PENDIENTE);
        }

        // Enlazar la relación bidireccional de los detalles con el pedido
        if (pedido.getDetalles() != null) {
            for (DetallePedido detalle : pedido.getDetalles()) {
                detalle.setPedido(pedido);
            }
        }

        return pedidoRepository.save(pedido);
    }

    public void eliminarPedido(Long id) {
        Pedido pedido = buscarPorId(id);
        pedidoRepository.delete(pedido);
    }
}