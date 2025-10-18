package com.example.productcomparison.exception.repository;

import com.example.productcomparison.repository.ProductRepository;
import lombok.Getter;

/**
 * Exception thrown when a product cannot be deleted from the repository.
 *
 * <p>Used to indicate unexpected errors during product deletion, except for "not found" cases.</p>
 *
 * @see ProductRepository
 */
@Getter
public class ProductDeleteException extends RuntimeException {

    private final String productId;

    public ProductDeleteException(String productId, String message) {
        super(String.format("Failed to delete product with ID '%s': %s", productId, message));
        this.productId = productId;
    }

    public ProductDeleteException(String productId, String message, Throwable cause) {
        super(String.format("Failed to delete product with ID '%s': %s", productId, message), cause);
        this.productId = productId;
    }

}