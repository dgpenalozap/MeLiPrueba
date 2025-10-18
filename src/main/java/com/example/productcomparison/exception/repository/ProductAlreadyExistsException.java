package com.example.productcomparison.exception.repository;

import com.example.productcomparison.repository.ProductRepository;
import lombok.Getter;

/**
 * Exception thrown when trying to save a product that already exists.
 *
 * <p>Used in the repository layer to prevent duplicate product IDs.</p>
 *
 * @see ProductRepository
 */
@Getter
public class ProductAlreadyExistsException extends RuntimeException {

    private final String productId;

    public ProductAlreadyExistsException(String productId) {
        super(String.format("Product with ID '%s' already exists", productId));
        this.productId = productId;
    }

    public ProductAlreadyExistsException(String productId, Throwable cause) {
        super(String.format("Product with ID '%s' already exists", productId), cause);
        this.productId = productId;
    }

}