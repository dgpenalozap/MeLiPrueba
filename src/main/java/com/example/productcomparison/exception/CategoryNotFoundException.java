package com.example.productcomparison.exception;

import lombok.Getter;

/**
 * Exception thrown when a requested category does not exist.
 * Results in HTTP 404 Not Found response.
 */
@Getter
public class CategoryNotFoundException extends RuntimeException {
    
    private final String category;
    
    public CategoryNotFoundException(String category) {
        super(String.format("Category not found: '%s'. Use /api/products/categories to see available categories", category));
        this.category = category;
    }

}
