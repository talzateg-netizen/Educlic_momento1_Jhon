package com.cesde.educlic.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cesde.educlic.model.entity.Pedido;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {
    List<Pedido> findByUsuarioId(Long usuarioId);
    // 1. Buscar pedidos por cualquier estado (ej. "PENDIENTE", "COMPLETADO", "CANCELADO")
    List<Pedido> findByEstado(String estado);

    // 2. Método rápido para obtener solo los pedidos pendientes
    List<Pedido> findByEstadoEqualsIgnoreCase(String estado);
}