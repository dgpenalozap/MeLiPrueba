package com.example.productcomparison.exception;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * Exception thrown when request parameters are invalid.
 * Results in HTTP 400 Bad Request response.
 */
@Getter
public class InvalidParameterException extends RuntimeException {
    
    private final String parameterName;
    private final Object parameterValue;
    private final List<String> validationErrors;
    
    public InvalidParameterException(String parameterName, Object parameterValue, String reason) {
        super(String.format("Invalid parameter '%s' with value '%s': %s", 
                parameterName, parameterValue, reason));
        this.parameterName = parameterName;
        this.parameterValue = parameterValue;
        this.validationErrors = new ArrayList<>();
        this.validationErrors.add(reason);
    }
    
    public InvalidParameterException(String message) {
        super(message);
        this.parameterName = null;
        this.parameterValue = null;
        this.validationErrors = new ArrayList<>();
    }
    
    public InvalidParameterException(String message, List<String> validationErrors) {
        super(message);
        this.parameterName = null;
        this.parameterValue = null;
        this.validationErrors = validationErrors;
    }

}
