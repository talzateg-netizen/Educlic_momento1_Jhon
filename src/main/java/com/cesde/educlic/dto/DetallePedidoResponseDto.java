package com.cesde.educlic.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DetallePedidoResponseDto {
    private Long id;
    private String nombreProducto;
    private Integer cantidad;
    private Double subtotal;
}