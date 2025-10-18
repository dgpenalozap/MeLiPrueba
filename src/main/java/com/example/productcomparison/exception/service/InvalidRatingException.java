package com.example.productcomparison.exception.service;

import lombok.Getter;

/**
 * Exception thrown when a rating value is outside the valid range (0.0 - 5.0).
 * 
 * <p>This exception is specific to product rating validation in the service layer.
 * The rating system uses a 5-star scale where 0.0 represents the lowest rating
 * and 5.0 represents the highest rating.</p>
 * 
 * <h2>Usage Layer:</h2>
 * <ul>
 *   <li><b>Service Layer:</b> Thrown by {@code ProductService} for rating validation in create, update, and filter operations</li>
 *   <li><b>Controller Layer:</b> Handled by {@code GlobalExceptionHandler} and returned as HTTP 400 Bad Request</li>
 * </ul>
 * 
 * <h2>HTTP Response:</h2>
 * <ul>
 *   <li><b>Status Code:</b> 400 Bad Request</li>
 *   <li><b>Error Code:</b> INVALID_RATING</li>
 *   <li><b>Response Body:</b> ErrorResponse with rating value and valid range information</li>
 * </ul>
 * 
 * <h2>Validation Rules:</h2>
 * <ul>
 *   <li>Rating must be between 0.0 and 5.0 (inclusive)</li>
 *   <li>Negative ratings are not allowed</li>
 *   <li>Ratings above 5.0 are not allowed</li>
 *   <li>Null ratings are allowed (optional field)</li>
 * </ul>
 * 
 * <h2>Common Scenarios:</h2>
 * <ul>
 *   <li>Creating product with negative rating: {@code rating = -1.0}</li>
 *   <li>Updating product with rating above 5: {@code rating = 6.5}</li>
 *   <li>Filtering with invalid minimum rating: {@code minRating = 5.5}</li>
 *   <li>Using percentage values instead of 5-star scale: {@code rating = 85.0}</li>
 * </ul>
 * 
 * <h2>Example Usage:</h2>
 * <pre>{@code
 * // Product creation/update validation
 * if (product.getRating() < 0 || product.getRating() > 5) {
 *     throw new InvalidRatingException(product.getRating());
 * }
 * 
 * // Filter validation
 * if (minRating < 0 || minRating > 5) {
 *     throw new InvalidRatingException(minRating);
 * }
 * }</pre>
 * 
 * <h2>Rating Scale Reference:</h2>
 * <ul>
 *   <li>0.0 - 1.0: Poor</li>
 *   <li>1.0 - 2.0: Below Average</li>
 *   <li>2.0 - 3.0: Average</li>
 *   <li>3.0 - 4.0: Good</li>
 *   <li>4.0 - 5.0: Excellent</li>
 * </ul>
 * 
 * <h2>Related Exceptions:</h2>
 * <ul>
 *   <li>{@link InvalidParameterException} - General parameter validation</li>
 *   <li>{@link InvalidPriceRangeException} - Price-specific validation</li>
 * </ul>
 * 
 * <h2>Client Actions:</h2>
 * <ul>
 *   <li>Ensure rating values are between 0.0 and 5.0</li>
 *   <li>Convert percentage ratings to 5-star scale (divide by 20)</li>
 *   <li>Use decimal precision (e.g., 4.5 instead of 4.5678)</li>
 *   <li>Validate rating before sending to API</li>
 * </ul>
 * 
 * @see com.example.productcomparison.service.ProductService#createProduct(com.example.productcomparison.model.Product)
 * @see com.example.productcomparison.service.ProductService#updateProduct(String, com.example.productcomparison.model.Product)
 * @see com.example.productcomparison.service.ProductService#filterByRating(double)
 * @see com.example.productcomparison.exception.GlobalExceptionHandler#handleInvalidRatingException
 * @since 1.0
 * @author Product Comparison API Team
 */
@Getter
public class InvalidRatingException extends RuntimeException {
    
    private final double rating;
    
    /**
     * Constructs a new InvalidRatingException with the invalid rating value.
     * 
     * <p>The exception message automatically includes the valid range (0.0 - 5.0).</p>
     * 
     * @param rating the invalid rating value that was provided
     */
    public InvalidRatingException(double rating) {
        super(String.format("Invalid rating value: %.1f. Rating must be between 0.0 and 5.0", rating));
        this.rating = rating;
    }

}
