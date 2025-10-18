package com.example.productcomparison.exception.repository;

import com.example.productcomparison.repository.ProductDataSource;
import com.example.productcomparison.repository.ProductRepository;

/**
 * Exception for critical product data access errors in the repository or data source.
 * <p>
 * Used when reading, saving, or initializing product data fails.
 * Typically, wraps lower-level exceptions (I/O, format, permissions).
 * Handled globally and returns HTTP 500.
 *
 * @see ProductDataSource
 * @see ProductRepository
 */
public class ProductDataAccessException extends RuntimeException {

    public ProductDataAccessException(String message, Throwable cause) {
        super(message, cause);
    }

    public ProductDataAccessException(String message) {
        super(message);
    }
}
