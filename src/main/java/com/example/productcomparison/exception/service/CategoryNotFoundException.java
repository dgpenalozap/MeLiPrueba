package com.example.productcomparison.exception.service;

import com.example.productcomparison.exception.GlobalExceptionHandler;
import com.example.productcomparison.service.ProductService;
import lombok.Getter;
/**
 * Exception thrown when a requested product category does not exist.
 *
 * <p>Used in the service layer when filtering products by a category name that is not found.
 * Handled in the controller layer and returned as HTTP 404 Not Found.</p>
 *
 * @see ProductService
 * @see GlobalExceptionHandler
 */
@Getter
public class CategoryNotFoundException extends RuntimeException {

    private final String category;

    public CategoryNotFoundException(String category) {
        super(String.format("Category not found: '%s'. Use /api/products/categories to see available categories", category));
        this.category = category;
    }

}
