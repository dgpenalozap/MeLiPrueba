package com.example.productcomparison.exception;

/**
 * Excepción personalizada para errores de acceso a datos de productos.
 */
public class ProductDataAccessException extends RuntimeException {
    public ProductDataAccessException(String message, Throwable cause) {
        super(message, cause);
    }
    public ProductDataAccessException(String message) {
        super(message);
    }
}

