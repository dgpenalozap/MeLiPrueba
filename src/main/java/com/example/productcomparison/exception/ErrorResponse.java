package com.example.productcomparison.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Standard error response returned for all API errors")
public class ErrorResponse {

    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

    @Schema(description = "Timestamp when the error occurred",
            example = "2024-01-15T10:30:00")
    private String timestamp;

    @Schema(description = "HTTP status code",
            example = "404")
    private int status;

    @Schema(description = "HTTP status reason phrase",
            example = "Not Found")
    private String error;

    @Schema(description = "Error message describing what went wrong",
            example = "Product not found: laptop-999")
    private String message;

    @Schema(description = "API endpoint path where the error occurred",
            example = "/api/products/laptop-999")
    private String path;

    @Schema(description = "Detailed error information (optional)",
            example = "The requested product with ID 'laptop-999' does not exist in the database")
    private String details;

    @Schema(description = "List of validation errors (for 400 Bad Request)",
            example = "[\"minPrice must be positive\", \"maxPrice must be greater than minPrice\"]")
    private List<String> validationErrors;

    @Schema(description = "Error code for client-side handling",
            example = "PRODUCT_NOT_FOUND")
    private String errorCode;

    // Método helper para crear el timestamp
    public static ErrorResponseBuilder builderWithTimestamp() {
        return ErrorResponse.builder()
                .timestamp(LocalDateTime.now().format(FORMATTER));
    }
}