package com.cesde.educlic.dto.request;

import jakarta.validation.constraints.NotBlank;

public class CategoriaRequestDto {
    @NotBlank(message = "El nombre de la categoría es obligatorio")
    private String nombre;
    private String descripcion;

    // Getters y Setters
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
}