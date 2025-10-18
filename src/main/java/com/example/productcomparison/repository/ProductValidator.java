package com.example.productcomparison.repository;

import com.example.productcomparison.model.Product;
import com.example.productcomparison.model.ProductDTO;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

@Component
public class ProductValidator {

    public void validateDto(ProductDTO dto) {
        Assert.notNull(dto, "ProductDTO cannot be null");
        Assert.hasText(dto.getId(), "Product ID cannot be null or empty");
        Assert.hasText(dto.getName(), "Product name cannot be null or empty");
    }

    public boolean validateProduct(Product product) {
        if (!isValidPrice(product.getPrice())) {
            return false;
        }
        return isValidRating(product.getRating());
    }

    public boolean isValidPrice(Double price) {
        return price != null && price >= 0;
    }

    public boolean isValidRating(Double rating) {
        return rating == null || (rating >= 0 && rating <= 5);
    }
}
