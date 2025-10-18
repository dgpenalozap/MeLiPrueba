package com.example.productcomparison.exception.service;

import com.example.productcomparison.exception.repository.ProductValidationException;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * Exception thrown when request parameters fail business logic validation.
 * 
 * <p>This exception is raised when input parameters do not meet the business rules
 * or constraints defined in the service layer. It covers a wide range of validation
 * failures including null/empty values, invalid formats, and business rule violations.</p>
 * 
 * <h2>Usage Layer:</h2>
 * <ul>
 *   <li><b>Service Layer:</b> Primary layer - thrown by {@code ProductService} for business logic validation</li>
 *   <li><b>Controller Layer:</b> Handled by {@code GlobalExceptionHandler} and returned as HTTP 400 Bad Request</li>
 * </ul>
 * 
 * <h2>HTTP Response:</h2>
 * <ul>
 *   <li><b>Status Code:</b> 400 Bad Request</li>
 *   <li><b>Error Code:</b> INVALID_PARAMETER</li>
 *   <li><b>Response Body:</b> ErrorResponse with parameter details and validation errors</li>
 * </ul>
 * 
 * <h2>Common Validation Failures:</h2>
 * <ul>
 *   <li>Empty or whitespace-only product IDs</li>
 *   <li>Null or empty product names</li>
 *   <li>Empty search queries</li>
 *   <li>Empty category names</li>
 *   <li>Empty specification keys or values</li>
 *   <li>Invalid limit values (negative, zero, or exceeding maximum)</li>
 *   <li>Empty or oversized product ID lists for comparison</li>
 * </ul>
 * 
 * <h2>Example Usage:</h2>
 * <pre>{@code
 * // Single parameter validation
 * if (id == null || id.trim().isEmpty()) {
 *     throw new InvalidParameterException("id", id, "Product ID cannot be empty");
 * }
 * 
 * // Multiple validation errors
 * List<String> errors = new ArrayList<>();
 * errors.add("Name is required");
 * errors.add("Price must be positive");
 * throw new InvalidParameterException("Validation failed", errors);
 * }</pre>
 * 
 * <h2>Distinction from Other Exceptions:</h2>
 * <ul>
 *   <li>{@link InvalidPriceRangeException} - Specific to price range validation</li>
 *   <li>{@link InvalidRatingException} - Specific to rating value validation</li>
 *   <li>{@link ProductValidationException} - Data layer validation (DTO/entity)</li>
 * </ul>
 * 
 * <h2>Client Actions:</h2>
 * <ul>
 *   <li>Review the validation error messages in the response</li>
 *   <li>Correct the invalid parameters and retry</li>
 *   <li>Check API documentation for parameter requirements</li>
 *   <li>Ensure all required parameters are provided</li>
 * </ul>
 * 
 * @see com.example.productcomparison.service.ProductService
 * @see com.example.productcomparison.exception.GlobalExceptionHandler#handleInvalidParameterException
 * @since 1.0
 * @author Product Comparison API Team
 */
@Getter
public class InvalidParameterException extends RuntimeException {
    
    private final String parameterName;
    private final Object parameterValue;
    private final List<String> validationErrors;
    
    /**
     * Constructs a new InvalidParameterException for a specific parameter.
     * 
     * @param parameterName the name of the invalid parameter
     * @param parameterValue the invalid value that was provided
     * @param reason explanation of why the parameter value is invalid
     */
    public InvalidParameterException(String parameterName, Object parameterValue, String reason) {
        super(String.format("Invalid parameter '%s' with value '%s': %s", 
                parameterName, parameterValue, reason));
        this.parameterName = parameterName;
        this.parameterValue = parameterValue;
        this.validationErrors = new ArrayList<>();
        this.validationErrors.add(reason);
    }
    
    /**
     * Constructs a new InvalidParameterException with a general validation message.
     * 
     * <p>Use this constructor when the error is not specific to a single parameter.</p>
     * 
     * @param message a descriptive message explaining the validation failure
     */
    public InvalidParameterException(String message) {
        super(message);
        this.parameterName = null;
        this.parameterValue = null;
        this.validationErrors = new ArrayList<>();
    }
    
    /**
     * Constructs a new InvalidParameterException with multiple validation errors.
     * 
     * <p>Use this constructor when multiple validation failures need to be reported together.</p>
     * 
     * @param message a general message describing the validation context
     * @param validationErrors list of specific validation error messages
     */
    public InvalidParameterException(String message, List<String> validationErrors) {
        super(message);
        this.parameterName = null;
        this.parameterValue = null;
        this.validationErrors = validationErrors;
    }

}
