package com.example.productcomparison.exception.repository;

/**
 * Exception thrown when product validation fails during repository operations.
 * 
 * <p>This exception is raised when a product doesn't meet the required validation
 * constraints. It can indicate issues with required fields, invalid data formats,
 * or business rule violations at the data layer. This is different from service-layer
 * validation exceptions which handle business logic validation.</p>
 * 
 * <h2>Usage Layer:</h2>
 * <ul>
 *   <li><b>Repository Layer:</b> Thrown by {@code ProductRepository} when {@code ProductMapper} validation fails</li>
 *   <li><b>Mapper Layer:</b> Validation errors from {@code ProductMapper.validateDto()} are wrapped in this exception</li>
 *   <li><b>Service Layer:</b> Propagated through service methods when repository validation fails</li>
 *   <li><b>Controller Layer:</b> Handled by {@code GlobalExceptionHandler} and returned as HTTP 400 Bad Request</li>
 * </ul>
 * 
 * <h2>HTTP Response:</h2>
 * <ul>
 *   <li><b>Status Code:</b> 400 Bad Request</li>
 *   <li><b>Error Code:</b> PRODUCT_VALIDATION_ERROR</li>
 *   <li><b>Response Body:</b> ErrorResponse with validation failure details</li>
 * </ul>
 * 
 * <h2>Validation Rules:</h2>
 * <ul>
 *   <li>Product ID must not be null or empty</li>
 *   <li>Product name must not be null or empty</li>
 *   <li>Price must be non-negative</li>
 *   <li>Rating must be between 0.0 and 5.0 (if provided)</li>
 * </ul>
 * 
 * <h2>Example Usage:</h2>
 * <pre>{@code
 * // Repository layer validation
 * try {
 *     productMapper.validateDto(productDto);
 * } catch (IllegalArgumentException e) {
 *     throw new ProductValidationException("Validation failed: " + e.getMessage(), e);
 * }
 * 
 * // Specific field validation
 * if (price < 0) {
 *     throw new ProductValidationException(productId, "price", "Price cannot be negative");
 * }
 * }</pre>
 * 
 * <h2>Distinction from Service Validation:</h2>
 * <p>This exception is for data-layer validation (DTO validation, data integrity).
 * Service-layer validation uses {@code InvalidParameterException}, {@code InvalidPriceRangeException},
 * and {@code InvalidRatingException} for business logic validation.</p>
 * 
 * <h2>Client Actions:</h2>
 * <ul>
 *   <li>Review the error message for specific field failures</li>
 *   <li>Correct the invalid data and retry the request</li>
 *   <li>Ensure all required fields are provided</li>
 *   <li>Verify data types and formats match API specifications</li>
 * </ul>
 * 
 * @see com.example.productcomparison.repository.ProductMapper#validateDto(com.example.productcomparison.model.ProductDTO)
 * @see com.example.productcomparison.repository.ProductRepository
 * @see com.example.productcomparison.exception.GlobalExceptionHandler#handleProductValidationException
 * @since 1.0
 * @author Product Comparison API Team
 */
public class ProductValidationException extends RuntimeException {

    private final String productId;
    private final String field;

    /**
     * Constructs a new ProductValidationException with specific product ID, field, and message.
     * 
     * <p>Use this constructor when validation fails for a specific field of a known product.</p>
     * 
     * @param productId the ID of the product that failed validation
     * @param field the name of the field that failed validation
     * @param message a descriptive message explaining the validation failure
     */
    public ProductValidationException(String productId, String field, String message) {
        super(String.format("Validation failed for product '%s', field '%s': %s", productId, field, message));
        this.productId = productId;
        this.field = field;
    }

    /**
     * Constructs a new ProductValidationException with a general validation message.
     * 
     * <p>Use this constructor when validation fails but specific product ID or field
     * information is not available or not applicable.</p>
     * 
     * @param message a descriptive message explaining the validation failure
     */
    public ProductValidationException(String message) {
        super(message);
        this.productId = null;
        this.field = null;
    }

    /**
     * Constructs a new ProductValidationException with a message and underlying cause.
     * 
     * <p>Use this constructor to wrap validation exceptions from underlying frameworks
     * or libraries (e.g., Spring's Assert utilities).</p>
     * 
     * @param message a descriptive message explaining the validation failure
     * @param cause the underlying exception that triggered this validation failure
     */
    public ProductValidationException(String message, Throwable cause) {
        super(message, cause);
        this.productId = null;
        this.field = null;
    }

    /**
     * Returns the ID of the product that failed validation.
     * 
     * @return the product ID, or {@code null} if not available
     */
    public String getProductId() {
        return productId;
    }

    /**
     * Returns the name of the field that failed validation.
     * 
     * @return the field name, or {@code null} if not specific to a field
     */
    public String getField() {
        return field;
    }
}
