package com.example.productcomparison.exception;

public class DataSourceInitializationException extends RuntimeException {
    public DataSourceInitializationException(String message) {
        super(message);
    }

    public DataSourceInitializationException(String message, Throwable cause) {
        super(message, cause);
    }
}
