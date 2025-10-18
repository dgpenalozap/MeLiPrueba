package com.example.productcomparison.repository;

import com.example.productcomparison.model.Product;
import com.example.productcomparison.model.ProductDTO;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

/**
 * Mapper class responsible for converting between Product domain objects and ProductDTO.
 * Centralizes mapping logic and validation for product data transformations.
 */
@Component
public class ProductMapper {

    /**
     * Converts a ProductDTO to a Product domain object.
     *
     * @param dto the ProductDTO to convert
     * @return the corresponding Product domain object
     * @throws IllegalArgumentException if dto is invalid
     */
    public Product toDomain(ProductDTO dto) {
        validateDto(dto);
        return Product.builder()
                .id(dto.getId())
                .name(dto.getName())
                .imageUrl(dto.getImageUrl())
                .description(dto.getDescription())
                .price(dto.getPrice())
                .rating(dto.getRating())
                .specifications(dto.getSpecifications())
                .build();
    }

    /**
     * Converts a Product domain object to a ProductDTO.
     *
     * @param product the Product to convert
     * @return the corresponding ProductDTO
     */
    public ProductDTO toDto(Product product) {
        return ProductDTO.builder()
                .id(product.getId())
                .name(product.getName())
                .imageUrl(product.getImageUrl())
                .description(product.getDescription())
                .price(product.getPrice())
                .rating(product.getRating())
                .specifications(product.getSpecifications())
                .build();
    }

    /**
     * Validates a ProductDTO to ensure required fields are present.
     *
     * @param dto the ProductDTO to validate
     * @throws IllegalArgumentException if validation fails
     */
    public void validateDto(ProductDTO dto) {
        Assert.notNull(dto, "ProductDTO cannot be null");
        Assert.hasText(dto.getId(), "Product ID cannot be null or empty");
        Assert.hasText(dto.getName(), "Product name cannot be null or empty");
    }

    /**
     * Validates a Product domain object.
     *
     * @param product the Product to validate
     * @return true if the product is valid, false otherwise
     */
    public boolean validateProduct(Product product) {
        if (!isValidPrice(product.getPrice())) {
            return false;
        }
        return isValidRating(product.getRating());
    }

    private boolean isValidPrice(Double price) {
        return price != null && price >= 0;
    }

    private boolean isValidRating(Double rating) {
        return rating == null || (rating >= 0 && rating <= 5);
    }
}
