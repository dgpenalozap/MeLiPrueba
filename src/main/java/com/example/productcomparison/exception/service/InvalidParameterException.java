package com.example.productcomparison.exception.service;

import com.example.productcomparison.exception.GlobalExceptionHandler;
import com.example.productcomparison.exception.repository.ProductValidationException;
import com.example.productcomparison.service.ProductService;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * Exception thrown when request parameters fail business validation.
 *
 * Used in the service layer for invalid or missing parameters.
 * Handled by the global exception handler and returns HTTP 400 Bad Request.
 *
 * @see ProductService
 * @see GlobalExceptionHandler
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
