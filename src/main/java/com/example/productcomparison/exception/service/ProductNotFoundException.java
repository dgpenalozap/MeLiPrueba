package com.example.productcomparison.exception.service;

import com.example.productcomparison.exception.GlobalExceptionHandler;
import com.example.productcomparison.repository.ProductRepository;
import com.example.productcomparison.service.ProductService;
import lombok.Getter;

/**
 * Exception thrown when a requested product cannot be found in the repository.
 * <p>
 * This exception is raised when attempting to retrieve, update, or delete a product
 * that does not exist in the data store.
 *
 * @see ProductService
 * @see ProductRepository
 * @see GlobalExceptionHandler
 */
@Getter
public class ProductNotFoundException extends RuntimeException {

    private final String productId;

    /**
     * Constructs a new ProductNotFoundException with the specified product ID.
     *
     * @param productId the ID of the product that was not found
     */
    public ProductNotFoundException(String productId) {
        super(String.format("Product not found: %s", productId));
        this.productId = productId;
    }

    /**
     * Constructs a new ProductNotFoundException with the product ID and additional context.
     *
     * @param productId      the ID of the product that was not found
     * @param additionalInfo additional context for debugging
     */
    public ProductNotFoundException(String productId, String additionalInfo) {
        super(String.format("Product not found: %s. %s", productId, additionalInfo));
        this.productId = productId;
    }
}