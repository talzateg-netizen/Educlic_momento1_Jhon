package com.cesde.educlic.dto.response;

public class DetallePedidoResponseDto {
    private Long id;
    private ProductoResponseDto producto;
    private Integer cantidad;
    private Double subtotal;

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public ProductoResponseDto getProducto() { return producto; }
    public void setProducto(ProductoResponseDto producto) { this.producto = producto; }
    public Integer getCantidad() { return cantidad; }
    public void setCantidad(Integer cantidad) { this.cantidad = cantidad; }
    public Double getSubtotal() { return subtotal; }
    public void setSubtotal(Double subtotal) { this.subtotal = subtotal; }
}