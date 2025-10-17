package com.example.productcomparison.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * Domain model representing a product.
 * Using Lombok @Value for immutability and @Builder for flexible construction.
 *
 * @Value makes the class immutable (all fields final, getters auto-generated, equals/hashCode)
 * @Builder provides builder pattern
 * @With allows creating copies with modified fields
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Product entity representing an item available for comparison")
public class Product {

    @Schema(description = "Unique product identifier",
            example = "laptop-001",
            requiredMode = Schema.RequiredMode.REQUIRED)
    String id;

    @Schema(description = "Product name",
            example = "UltraBook Pro 15",
            requiredMode = Schema.RequiredMode.REQUIRED)
    String name;

    @Schema(description = "URL to product image",
            example = "https://example.com/images/ultrabook-pro-15.jpg")
    String imageUrl;

    @Schema(description = "Detailed product description",
            example = "Premium laptop with Intel i7, 16GB RAM, perfect for professionals and content creators.")
    String description;

    @Schema(description = "Product price in USD",
            example = "1299.99",
            requiredMode = Schema.RequiredMode.REQUIRED)
    double price;

    @Schema(description = "Product rating (0.0 - 5.0)",
            example = "4.7",
            minimum = "0",
            maximum = "5",
            requiredMode = Schema.RequiredMode.REQUIRED)
    double rating;

    @Schema(description = "Product specifications as key-value pairs",
            example = """
                    {
                      "processor": "Intel Core i7-12700H",
                      "ram": "16GB DDR5",
                      "storage": "512GB SSD",
                      "screen": "15.6 inch FHD",
                      "category": "Laptops"
                    }
                    """)
    Map<String, String> specifications;
}