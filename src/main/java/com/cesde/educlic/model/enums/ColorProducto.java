package com.cesde.educlic.model.enums;

public enum ColorProducto {
    NEGRO("Negro"),
    BLANCO("Blanco"),
    GRIS("Gris"),
    AZUL("Azul"),
    ROJO("Rojo");

    private final String valor;

    ColorProducto(String valor) {
        this.valor = valor;
    }

    public String getValor() {
        return valor;
    }
}