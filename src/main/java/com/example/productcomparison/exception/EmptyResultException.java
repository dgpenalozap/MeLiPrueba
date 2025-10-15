package com.example.productcomparison.exception;

import lombok.Getter;

/**
 * Exception thrown when a query returns no results but was expected to return at least one.
 * Results in HTTP 404 Not Found response.
 */
@Getter
public class EmptyResultException extends RuntimeException {
    
    private final String query;
    
    public EmptyResultException(String message) {
        super(message);
        this.query = null;
    }
    
    public EmptyResultException(String message, String query) {
        super(message);
        this.query = query;
    }

}
