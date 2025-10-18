package com.example.productcomparison.exception.repository;

import com.example.productcomparison.repository.ProductMapper;
import lombok.Getter;

/**
 * Exception thrown when product validation fails in the repository layer.
 * <p>
 * Used to indicate invalid product data, such as missing fields or incorrect values.
 *
 * @see ProductMapper
 */
@Getter
public class ProductValidationException extends RuntimeException {

    private final String productId;
    private final String field;

    public ProductValidationException(String productId, String field, String message) {
        super(String.format("Validation failed for product '%s', field '%s': %s", productId, field, message));
        this.productId = productId;
        this.field = field;
    }

    public ProductValidationException(String message) {
        super(message);
        this.productId = null;
        this.field = null;
    }

    public ProductValidationException(String message, Throwable cause) {
        super(message, cause);
        this.productId = null;
        this.field = null;
    }

}