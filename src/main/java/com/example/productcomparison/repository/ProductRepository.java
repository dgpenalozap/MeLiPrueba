package com.example.productcomparison.repository;

import com.example.productcomparison.exception.DataSourceInitializationException;
import com.example.productcomparison.exception.ProductDataAccessException;
import com.example.productcomparison.model.Product;
import com.example.productcomparison.model.ProductDTO;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Repository implementation for product management using JSON file storage.
 * This implementation reads directly from the JSON file for each operation
 * to ensure data consistency.
 *
 * @see IProductRepository
 * @see Product
 */

@Repository
@RequiredArgsConstructor
@Slf4j
public class ProductRepository implements IProductRepository {

    private final ProductDataSource productDataSource;

    @Value("${product.data.json-file}")
    private String jsonFilePath;

    @PostConstruct
    public void init() {
        validateDataSource();
    }

    private void validateDataSource() {
        try {
            List<ProductDTO> products = loadProductsFromFile();
            log.info("Data source validation successful. Found {} products.", products.size());
        } catch (DataSourceInitializationException e) {
            String errorMessage = "Failed to validate data source: " + jsonFilePath;
            log.error(errorMessage, e);
            throw new ProductDataAccessException(errorMessage, e);
        }
    }

    @Override
    public List<Product> findAll() {
        return loadProductsFromFile().stream()
                .map(this::toDomain)
                .filter(this::validateProduct)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Product> findById(String id) {
        if (id == null || id.isBlank()) {
            log.debug("Invalid product ID requested: {}", id);
            return Optional.empty();
        }

        return loadProductsFromFile().stream()
                .map(this::toDomain)
                .filter(this::validateProduct)
                .filter(product -> product.getId().equals(id))
                .findFirst();
    }

    private List<ProductDTO> loadProductsFromFile() {
        try {
            log.debug("Loading products from: {}", jsonFilePath);
            List<ProductDTO> dtos = productDataSource.loadProductsFromJson(jsonFilePath);

            if (dtos == null || dtos.isEmpty()) {
                log.warn("No products found in {}", jsonFilePath);
                return List.of();
            }

            return dtos;
        } catch (DataSourceInitializationException e) {
            String errorMessage = "Failed to load products from " + jsonFilePath;
            log.error(errorMessage, e);
            throw new ProductDataAccessException(errorMessage, e);
        }
    }

    private Product toDomain(ProductDTO dto) {
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

    private void validateDto(ProductDTO dto) {
        Assert.notNull(dto, "ProductDTO cannot be null");
        Assert.hasText(dto.getId(), "Product ID cannot be null or empty");
        Assert.hasText(dto.getName(), "Product name cannot be null or empty");
    }

    private boolean validateProduct(Product product) {
        if (!isValidPrice(product.getPrice())) {
            log.warn("Product {} ignored: invalid price", product.getId());
            return false;
        }
        if (!isValidRating(product.getRating())) {
            log.warn("Product {} ignored: rating out of range", product.getId());
            return false;
        }
        return true;
    }

    private boolean isValidPrice(Double price) {
        return price != null && price >= 0;
    }

    private boolean isValidRating(Double rating) {
        return rating == null || (rating >= 0 && rating <= 5);
    }
}
