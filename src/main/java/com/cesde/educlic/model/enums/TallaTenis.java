package com.cesde.educlic.model.enums;

public enum TallaTenis {
    T_37("37"),
    T_38("38"),
    T_39("39"),
    T_40("40"),
    T_41("41"),
    T_42("42");

    private final String valor;

    TallaTenis(String valor) {
        this.valor = valor;
    }

    public String getValor() {
        return valor;
    }
}
