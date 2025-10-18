package com.example.productcomparison.exception.service;

import lombok.Getter;

/**
 * Exception thrown when a query or search operation returns no results unexpectedly.
 *
 * <p>Use this exception when an operation is expected to return at least one result,
 * but returns an empty collection instead.</p>
 *
 * <p>Typically handled as HTTP 404 Not Found in the controller layer.</p>
 *
 * @see ProductNotFoundException
 * @see CategoryNotFoundException
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