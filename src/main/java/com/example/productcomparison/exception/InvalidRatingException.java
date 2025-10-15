package com.example.productcomparison.exception;

import lombok.Getter;

/**
 * Exception thrown when rating parameter is out of valid range (0.0 - 5.0).
 * Results in HTTP 400 Bad Request response.
 */
@Getter
public class InvalidRatingException extends RuntimeException {
    
    private final double rating;
    
    public InvalidRatingException(double rating) {
        super(String.format("Invalid rating value: %.1f. Rating must be between 0.0 and 5.0", rating));
        this.rating = rating;
    }

}
