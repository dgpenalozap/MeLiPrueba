package com.example.productcomparison.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * DTO for creating new products.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Data transfer object for creating a new product")
public class CreateProductRequest {

    @Schema(description = "Unique product identifier",
            example = "laptop-999",
            requiredMode = Schema.RequiredMode.REQUIRED)
    private String id;

    @Schema(description = "Product name",
            example = "UltraBook Pro 15",
            requiredMode = Schema.RequiredMode.REQUIRED)
    private String name;

    @Schema(description = "URL to product image",
            example = "https://example.com/images/ultrabook-pro-15.jpg")
    private String imageUrl;

    @Schema(description = "Detailed product description",
            example = "Premium laptop with Intel i7, 16GB RAM, perfect for professionals and content creators.")
    private String description;

    @Schema(description = "Product price in USD",
            example = "1299.99",
            requiredMode = Schema.RequiredMode.REQUIRED)
    private double price;

    @Schema(description = "Product rating (0.0 - 5.0)",
            example = "4.7",
            minimum = "0",
            maximum = "5",
            requiredMode = Schema.RequiredMode.REQUIRED)
    private double rating;

    @Schema(description = "Product specifications as key-value pairs")
    private Map<String, String> specifications;
}
