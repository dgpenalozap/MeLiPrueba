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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
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
    private final ConcurrentHashMap<String, Product> inMemoryProducts = new ConcurrentHashMap<>();

    @Value("${product.data.json-file}")
    private String jsonFilePath;

    @PostConstruct
    public void init() {
        validateDataSource();
        loadInitialData();
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

    private void loadInitialData() {
        loadProductsFromFile().forEach(dto -> {
            Product product = toDomain(dto);
            if (validateProduct(product)) {
                inMemoryProducts.put(product.getId(), product);
            }
        });
        log.info("Loaded {} products into memory", inMemoryProducts.size());
    }

    @Override
    public List<Product> findAll() {
        return new ArrayList<>(inMemoryProducts.values());
    }

    @Override
    public Optional<Product> findById(String id) {
        if (id == null || id.isBlank()) {
            log.debug("Invalid product ID requested: {}", id);
            return Optional.empty();
        }
        return Optional.ofNullable(inMemoryProducts.get(id));
    }

    @Override
    public Product save(Product product) {
        validateDto(toDto(product));
        if (inMemoryProducts.containsKey(product.getId())) {
            throw new IllegalArgumentException("Product with ID " + product.getId() + " already exists");
        }
        inMemoryProducts.put(product.getId(), product);
        log.info("Product saved: {}", product.getId());
        return product;
    }

    @Override
    public Product update(String id, Product product) {
        if (!inMemoryProducts.containsKey(id)) {
            throw new IllegalArgumentException("Product with ID " + id + " not found");
        }
        Product updatedProduct = product.toBuilder().id(id).build();
        validateDto(toDto(updatedProduct));
        inMemoryProducts.put(id, updatedProduct);
        log.info("Product updated: {}", id);
        return updatedProduct;
    }

    @Override
    public void deleteById(String id) {
        if (!inMemoryProducts.containsKey(id)) {
            throw new IllegalArgumentException("Product with ID " + id + " not found");
        }
        inMemoryProducts.remove(id);
        log.info("Product deleted: {}", id);
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

    private ProductDTO toDto(Product product) {
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
