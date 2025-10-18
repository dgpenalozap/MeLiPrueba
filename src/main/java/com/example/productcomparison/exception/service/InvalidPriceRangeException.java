package com.example.productcomparison.exception.service;

import lombok.Getter;

/**
 * Exception thrown when price range parameters are invalid or violate business rules.
 * 
 * <p>This exception is specific to price-related validation in the service layer.
 * It ensures that price values and ranges meet business requirements such as
 * non-negative values and logical min/max relationships.</p>
 * 
 * <h2>Usage Layer:</h2>
 * <ul>
 *   <li><b>Service Layer:</b> Thrown by {@code ProductService} for price validation in create, update, and filter operations</li>
 *   <li><b>Controller Layer:</b> Handled by {@code GlobalExceptionHandler} and returned as HTTP 400 Bad Request</li>
 * </ul>
 * 
 * <h2>HTTP Response:</h2>
 * <ul>
 *   <li><b>Status Code:</b> 400 Bad Request</li>
 *   <li><b>Error Code:</b> INVALID_PRICE_RANGE</li>
 *   <li><b>Response Body:</b> ErrorResponse with price range details and violation reason</li>
 * </ul>
 * 
 * <h2>Validation Rules:</h2>
 * <ul>
 *   <li>Product price must be non-negative (>= 0)</li>
 *   <li>Minimum price in range filters must be non-negative</li>
 *   <li>Maximum price in range filters must be non-negative</li>
 *   <li>Minimum price must not exceed maximum price in range filters</li>
 * </ul>
 * 
 * <h2>Common Scenarios:</h2>
 * <ul>
 *   <li>Creating product with negative price: {@code price = -10.50}</li>
 *   <li>Updating product with negative price: {@code price = -5.00}</li>
 *   <li>Filtering with negative minimum: {@code minPrice = -100.00}</li>
 *   <li>Filtering with negative maximum: {@code maxPrice = -50.00}</li>
 *   <li>Filtering with inverted range: {@code minPrice = 500.00, maxPrice = 100.00}</li>
 * </ul>
 * 
 * <h2>Example Usage:</h2>
 * <pre>{@code
 * // Single price validation
 * if (product.getPrice() < 0) {
 *     throw new InvalidPriceRangeException(product.getPrice(), 0, 
 *         "Product price cannot be negative");
 * }
 * 
 * // Range validation
 * if (minPrice > maxPrice) {
 *     throw new InvalidPriceRangeException(minPrice, maxPrice,
 *         "Minimum price cannot be greater than maximum price");
 * }
 * }</pre>
 * 
 * <h2>Related Exceptions:</h2>
 * <ul>
 *   <li>{@link InvalidParameterException} - General parameter validation</li>
 *   <li>{@link InvalidRatingException} - Rating-specific validation</li>
 * </ul>
 * 
 * <h2>Client Actions:</h2>
 * <ul>
 *   <li>Ensure price values are non-negative</li>
 *   <li>Verify minPrice <= maxPrice in range queries</li>
 *   <li>Check that currency formatting doesn't include negative signs</li>
 *   <li>Use decimal format with 2 decimal places for prices</li>
 * </ul>
 * 
 * @see com.example.productcomparison.service.ProductService#createProduct(com.example.productcomparison.model.Product)
 * @see com.example.productcomparison.service.ProductService#updateProduct(String, com.example.productcomparison.model.Product)
 * @see com.example.productcomparison.service.ProductService#filterByPriceRange(double, double)
 * @see com.example.productcomparison.exception.GlobalExceptionHandler#handleInvalidPriceRangeException
 * @since 1.0
 * @author Product Comparison API Team
 */
@Getter
public class InvalidPriceRangeException extends RuntimeException {
    
    private final double minPrice;
    private final double maxPrice;
    
    /**
     * Constructs a new InvalidPriceRangeException with price values and reason.
     * 
     * @param minPrice the minimum price value (or single price in non-range validations)
     * @param maxPrice the maximum price value (or 0 in non-range validations)
     * @param reason explanation of why the price range is invalid
     */
    public InvalidPriceRangeException(double minPrice, double maxPrice, String reason) {
        super(String.format("Invalid price range [%.2f, %.2f]: %s", minPrice, maxPrice, reason));
        this.minPrice = minPrice;
        this.maxPrice = maxPrice;
    }

}
