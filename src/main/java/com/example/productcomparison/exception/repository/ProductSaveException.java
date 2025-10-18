package com.example.productcomparison.exception.repository;

import com.example.productcomparison.exception.GlobalExceptionHandler;
import com.example.productcomparison.repository.ProductRepository;
import lombok.Getter;

/**
 * Exception thrown when a product cannot be saved in the repository.
 *
 * <p>Used to indicate unexpected errors during product persistence,
 * excluding validation and duplicate ID errors.</p>
 *
 * @see ProductRepository
 * @see GlobalExceptionHandler
 */
@Getter
public class ProductSaveException extends RuntimeException {

    private final String productId;

    public ProductSaveException(String productId, String message) {
        super(String.format("Failed to save product with ID '%s': %s", productId, message));
        this.productId = productId;
    }

    public ProductSaveException(String productId, String message, Throwable cause) {
        super(String.format("Failed to save product with ID '%s': %s", productId, message), cause);
        this.productId = productId;
    }

}
