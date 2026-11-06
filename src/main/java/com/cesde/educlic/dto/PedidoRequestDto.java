package com.cesde.educlic.dto.request;

import jakarta.validation.constraints.NotNull;
import java.util.List;

public class PedidoRequestDto {
    @NotNull(message = "El usuario es obligatorio")
    private Long usuarioId;

    @NotNull(message = "El pedido debe contener detalles de productos")
    private List<DetallePedidoRequestDto> detalles;

    // Getters y Setters
    public Long getUsuarioId() { return usuarioId; }
    public void setUsuarioId(Long usuarioId) { this.usuarioId = usuarioId; }
    public List<DetallePedidoRequestDto> getDetalles() { return detalles; }
    public void setDetalles(List<DetallePedidoRequestDto> detalles) { this.detalles = detalles; }
}