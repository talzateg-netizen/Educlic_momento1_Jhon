package com.cesde.educlic.dto.response;

public class CursoResponseDto {
    private Long id;
    private String titulo;
    private String descripcion;
    private CategoriaResponseDto categoria;

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public CategoriaResponseDto getCategoria() { return categoria; }
    public void setCategoria(CategoriaResponseDto categoria) { this.categoria = categoria; }
}