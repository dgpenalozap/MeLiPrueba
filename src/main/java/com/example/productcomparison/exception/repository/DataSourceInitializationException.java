package com.example.productcomparison.exception.repository;

import com.example.productcomparison.repository.ProductDataSource;

/**
 * Exception thrown when the product data source cannot be initialized or loaded.
 * <p>
 * Used in {@link ProductDataSource} for JSON file read or format errors.
 *
 * @see ProductDataSource
 * @see ProductDataAccessException
 */
public class DataSourceInitializationException extends RuntimeException {

    /**
     * Constructor with error message.
     *
     * @param message Description of the initialization error.
     */
    public DataSourceInitializationException(String message) {
        super(message);
    }

    /**
     * Constructor with error message and cause.
     *
     * @param message Description of the initialization error.
     * @param cause   Original exception that caused the error.
     */
    public DataSourceInitializationException(String message, Throwable cause) {
        super(message, cause);
    }
}