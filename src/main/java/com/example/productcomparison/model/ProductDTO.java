package com.example.productcomparison.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * Data Transfer Object for Product.
 * Used for JSON deserialization, separating persistence concerns from domain model.
 * 
 * @Data generates getters, setters, toString, equals, hashCode
 * @NoArgsConstructor generates default constructor for Jackson
 */
@Data
@NoArgsConstructor
public class ProductDTO {
    private String id;
    private String name;
    private String imageUrl;
    private String description;
    private double price;
    private double rating;
    private Map<String, String> specifications;
}
