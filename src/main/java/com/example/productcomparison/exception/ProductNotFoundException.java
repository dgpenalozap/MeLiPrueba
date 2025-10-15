package com.example.productcomparison.exception;

import lombok.Getter;

/**
 * Exception thrown when a requested product is not found.
 * Results in HTTP 404 Not Found response.
 */
@Getter
public class ProductNotFoundException extends RuntimeException {
    
    private final String productId;
    
    public ProductNotFoundException(String productId) {
        super(String.format("Product not found: %s", productId));
        this.productId = productId;
    }
    
    public ProductNotFoundException(String productId, String additionalInfo) {
        super(String.format("Product not found: %s. %s", productId, additionalInfo));
        this.productId = productId;
    }

}
