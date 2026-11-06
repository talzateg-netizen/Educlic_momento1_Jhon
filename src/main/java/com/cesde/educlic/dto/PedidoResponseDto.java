package com.cesde.educlic.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PedidoResponseDto {
    private Long id;
    private LocalDateTime fecha;
    private String estado;
    private Double total;
    private List<DetallePedidoResponseDto> detalles;
}