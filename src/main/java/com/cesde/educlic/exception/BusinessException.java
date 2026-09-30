package com.cesde.educlic.exception;

/**
 * Se lanza para reglas de negocio incumplidas (ej: stock insuficiente,
 * credenciales invalidas, email ya registrado).
 */
public class BusinessException extends RuntimeException {
    public BusinessException(String message) {
        super(message);
    }
}