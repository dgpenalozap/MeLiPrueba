package com.example.productcomparison.exception.repository;

import com.example.productcomparison.exception.GlobalExceptionHandler;
import com.example.productcomparison.repository.ProductRepository;
import lombok.Getter;

/**
 * Exception thrown when a product update fails in the repository.
 * <p>
 * Used to indicate unexpected errors during product modification,
 * excluding validation and not found errors.
 * <p>
 * Typically handled as HTTP 500 Internal Server Error.
 *
 * @see ProductRepository
 * @see GlobalExceptionHandler
 */
@Getter
public class ProductUpdateException extends RuntimeException {

    private final String productId;

    public ProductUpdateException(String productId, String message) {
        super(String.format("Failed to update product with ID '%s': %s", productId, message));
        this.productId = productId;
    }

    public ProductUpdateException(String productId, String message, Throwable cause) {
        super(String.format("Failed to update product with ID '%s': %s", productId, message), cause);
        this.productId = productId;
    }

}