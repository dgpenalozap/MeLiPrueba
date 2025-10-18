package com.example.productcomparison.exception;

import com.example.productcomparison.exception.repository.*;
import com.example.productcomparison.exception.service.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

/**
 * Global exception handler for consistent error responses across the API.
 * Catches all exceptions and returns standardized ErrorResponse objects.
 */
@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * Handle ProductAlreadyExistsException - Returns 409 Conflict
     */
    @ExceptionHandler(ProductAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleProductAlreadyExistsException(
            ProductAlreadyExistsException ex, HttpServletRequest request) {
        
        log.warn("Product already exists: {}", ex.getMessage());
        
        ErrorResponse errorResponse = ErrorResponse.builderWithTimestamp()
                .status(HttpStatus.CONFLICT.value())
                .error(HttpStatus.CONFLICT.getReasonPhrase())
                .message(ex.getMessage())
                .path(request.getRequestURI())
                .errorCode("PRODUCT_ALREADY_EXISTS")
                .build();
        
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
    }

    /**
     * Handle ProductSaveException - Returns 500 Internal Server Error
     */
    @ExceptionHandler(ProductSaveException.class)
    public ResponseEntity<ErrorResponse> handleProductSaveException(
            ProductSaveException ex, HttpServletRequest request) {
        
        log.error("Failed to save product: {}", ex.getMessage(), ex);
        
        ErrorResponse errorResponse = ErrorResponse.builderWithTimestamp()
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .error(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase())
                .message(ex.getMessage())
                .path(request.getRequestURI())
                .errorCode("PRODUCT_SAVE_ERROR")
                .build();
        
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }

    /**
     * Handle ProductUpdateException - Returns 500 Internal Server Error
     */
    @ExceptionHandler(ProductUpdateException.class)
    public ResponseEntity<ErrorResponse> handleProductUpdateException(
            ProductUpdateException ex, HttpServletRequest request) {
        
        log.error("Failed to update product: {}", ex.getMessage(), ex);
        
        ErrorResponse errorResponse = ErrorResponse.builderWithTimestamp()
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .error(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase())
                .message(ex.getMessage())
                .path(request.getRequestURI())
                .errorCode("PRODUCT_UPDATE_ERROR")
                .build();
        
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }

    /**
     * Handle ProductDeleteException - Returns 500 Internal Server Error
     */
    @ExceptionHandler(ProductDeleteException.class)
    public ResponseEntity<ErrorResponse> handleProductDeleteException(
            ProductDeleteException ex, HttpServletRequest request) {
        
        log.error("Failed to delete product: {}", ex.getMessage(), ex);
        
        ErrorResponse errorResponse = ErrorResponse.builderWithTimestamp()
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .error(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase())
                .message(ex.getMessage())
                .path(request.getRequestURI())
                .errorCode("PRODUCT_DELETE_ERROR")
                .build();
        
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }

    /**
     * Handle ProductValidationException - Returns 400 Bad Request
     */
    @ExceptionHandler(ProductValidationException.class)
    public ResponseEntity<ErrorResponse> handleProductValidationException(
            ProductValidationException ex, HttpServletRequest request) {
        
        log.warn("Product validation failed: {}", ex.getMessage());
        
        ErrorResponse errorResponse = ErrorResponse.builderWithTimestamp()
                .status(HttpStatus.BAD_REQUEST.value())
                .error(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .message(ex.getMessage())
                .path(request.getRequestURI())
                .errorCode("PRODUCT_VALIDATION_ERROR")
                .build();
        
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    /**
     * Handle ProductNotFoundException - Returns 404 Not Found
     */
    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleProductNotFoundException(
            ProductNotFoundException ex, HttpServletRequest request) {
        
        log.warn("Product not found: {}", ex.getMessage());
        
        ErrorResponse errorResponse = ErrorResponse.builderWithTimestamp()
                .status(HttpStatus.NOT_FOUND.value())
                .error(HttpStatus.NOT_FOUND.getReasonPhrase())
                .message(ex.getMessage())
                .path(request.getRequestURI())
                .errorCode("PRODUCT_NOT_FOUND")
                .build();
        
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    /**
     * Handle CategoryNotFoundException - Returns 404 Not Found
     */
    @ExceptionHandler(CategoryNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleCategoryNotFoundException(
            CategoryNotFoundException ex, HttpServletRequest request) {
        
        log.warn("Category not found: {}", ex.getMessage());
        
        ErrorResponse errorResponse = ErrorResponse.builderWithTimestamp()
                .status(HttpStatus.NOT_FOUND.value())
                .error(HttpStatus.NOT_FOUND.getReasonPhrase())
                .message(ex.getMessage())
                .path(request.getRequestURI())
                .errorCode("CATEGORY_NOT_FOUND")
                .build();
        
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    /**
     * Handle InvalidParameterException - Returns 400 Bad Request
     */
    @ExceptionHandler(InvalidParameterException.class)
    public ResponseEntity<ErrorResponse> handleInvalidParameterException(
            InvalidParameterException ex, HttpServletRequest request) {
        
        log.warn("Invalid parameter: {}", ex.getMessage());
        
        ErrorResponse errorResponse = ErrorResponse.builderWithTimestamp()
                .status(HttpStatus.BAD_REQUEST.value())
                .error(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .message(ex.getMessage())
                .path(request.getRequestURI())
                .errorCode("INVALID_PARAMETER")
                .validationErrors(ex.getValidationErrors())
                .build();
        
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    /**
     * Handle InvalidPriceRangeException - Returns 400 Bad Request
     */
    @ExceptionHandler(InvalidPriceRangeException.class)
    public ResponseEntity<ErrorResponse> handleInvalidPriceRangeException(
            InvalidPriceRangeException ex, HttpServletRequest request) {
        
        log.warn("Invalid price range: {}", ex.getMessage());
        
        ErrorResponse errorResponse = ErrorResponse.builderWithTimestamp()
                .status(HttpStatus.BAD_REQUEST.value())
                .error(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .message(ex.getMessage())
                .path(request.getRequestURI())
                .errorCode("INVALID_PRICE_RANGE")
                .build();
        
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    /**
     * Handle InvalidRatingException - Returns 400 Bad Request
     */
    @ExceptionHandler(InvalidRatingException.class)
    public ResponseEntity<ErrorResponse> handleInvalidRatingException(
            InvalidRatingException ex, HttpServletRequest request) {
        
        log.warn("Invalid rating: {}", ex.getMessage());
        
        ErrorResponse errorResponse = ErrorResponse.builderWithTimestamp()
                .status(HttpStatus.BAD_REQUEST.value())
                .error(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .message(ex.getMessage())
                .path(request.getRequestURI())
                .errorCode("INVALID_RATING")
                .build();
        
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    /**
     * Handle EmptyResultException - Returns 404 Not Found
     */
    @ExceptionHandler(EmptyResultException.class)
    public ResponseEntity<ErrorResponse> handleEmptyResultException(
            EmptyResultException ex, HttpServletRequest request) {
        
        log.info("Empty result: {}", ex.getMessage());
        
        ErrorResponse errorResponse = ErrorResponse.builderWithTimestamp()
                .status(HttpStatus.NOT_FOUND.value())
                .error(HttpStatus.NOT_FOUND.getReasonPhrase())
                .message(ex.getMessage())
                .path(request.getRequestURI())
                .errorCode("EMPTY_RESULT")
                .build();
        
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    /**
     * Handle ProductDataAccessException - Returns 500 Internal Server Error
     */
    @ExceptionHandler(ProductDataAccessException.class)
    public ResponseEntity<ErrorResponse> handleProductDataAccessException(
            ProductDataAccessException ex, HttpServletRequest request) {
        
        log.error("Data access error: {}", ex.getMessage(), ex);
        
        ErrorResponse errorResponse = ErrorResponse.builderWithTimestamp()
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .error(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase())
                .message("Failed to access product data")
                .path(request.getRequestURI())
                .errorCode("DATA_ACCESS_ERROR")
                .build();
        
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }

    /**
     * Handle MissingServletRequestParameterException - Returns 400 Bad Request
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ErrorResponse> handleMissingServletRequestParameter(
            MissingServletRequestParameterException ex, HttpServletRequest request) {
        
        String message = String.format("Required parameter '%s' is missing", ex.getParameterName());
        log.warn("Missing request parameter: {}", message);
        
        ErrorResponse errorResponse = ErrorResponse.builderWithTimestamp()
                .status(HttpStatus.BAD_REQUEST.value())
                .error(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .message(message)
                .path(request.getRequestURI())
                .errorCode("MISSING_PARAMETER")
                .build();
        
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    /**
     * Handle MethodArgumentTypeMismatchException - Returns 400 Bad Request
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentTypeMismatch(
            MethodArgumentTypeMismatchException ex, HttpServletRequest request) {
        
        String expectedType = ex.getRequiredType() != null ? ex.getRequiredType().getSimpleName() : "unknown";
        String message = String.format("Invalid value '%s' for parameter '%s'. Expected type: %s", 
                ex.getValue(), ex.getName(), expectedType);
        log.warn("Type mismatch for parameter: {}", message);
        
        ErrorResponse errorResponse = ErrorResponse.builderWithTimestamp()
                .status(HttpStatus.BAD_REQUEST.value())
                .error(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .message(message)
                .path(request.getRequestURI())
                .errorCode("TYPE_MISMATCH")
                .build();
        
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    /**
     * Handle NoHandlerFoundException - Returns 404 Not Found
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ErrorResponse> handleNoHandlerFoundException(
            NoHandlerFoundException ex, HttpServletRequest request) {
        
        String message = String.format("Endpoint not found: %s %s", ex.getHttpMethod(), ex.getRequestURL());
        log.warn("No handler found for request: {}", message);
        
        ErrorResponse errorResponse = ErrorResponse.builderWithTimestamp()
                .status(HttpStatus.NOT_FOUND.value())
                .error(HttpStatus.NOT_FOUND.getReasonPhrase())
                .message(message)
                .path(request.getRequestURI())
                .errorCode("ENDPOINT_NOT_FOUND")
                .build();
        
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    /**
     * Handle IllegalArgumentException - Returns 400 Bad Request
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(
            IllegalArgumentException ex, HttpServletRequest request) {
        
        log.warn("Illegal argument: {}", ex.getMessage());
        
        ErrorResponse errorResponse = ErrorResponse.builderWithTimestamp()
                .status(HttpStatus.BAD_REQUEST.value())
                .error(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .message(ex.getMessage())
                .path(request.getRequestURI())
                .errorCode("ILLEGAL_ARGUMENT")
                .build();
        
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    /**
     * Handle all other exceptions - Returns 500 Internal Server Error
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(
            Exception ex, HttpServletRequest request) {
        
        log.error("Unexpected error occurred: {}", ex.getMessage(), ex);
        
        ErrorResponse errorResponse = ErrorResponse.builderWithTimestamp()
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .error(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase())
                .message("An unexpected error occurred")
                .path(request.getRequestURI())
                .errorCode("INTERNAL_SERVER_ERROR")
                .build();
        
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }
}