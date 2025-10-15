package com.example.productcomparison.exception;

import lombok.Getter;

/**
 * Exception thrown when price range parameters are invalid.
 * Results in HTTP 400 Bad Request response.
 */
@Getter
public class InvalidPriceRangeException extends RuntimeException {
    
    private final double minPrice;
    private final double maxPrice;
    
    public InvalidPriceRangeException(double minPrice, double maxPrice, String reason) {
        super(String.format("Invalid price range [%.2f, %.2f]: %s", minPrice, maxPrice, reason));
        this.minPrice = minPrice;
        this.maxPrice = maxPrice;
    }

}
