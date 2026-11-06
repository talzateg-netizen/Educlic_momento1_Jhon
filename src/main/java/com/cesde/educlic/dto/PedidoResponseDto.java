package com.cesde.educlic.dto.request;

import jakarta.validation.constraints.NotBlank;

public class PerfilRequestDto {
    @NotBlank(message = "El nombre del perfil es obligatorio")
    private String nombre;
    private String descripcion;

    // Getters y Setters
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
}