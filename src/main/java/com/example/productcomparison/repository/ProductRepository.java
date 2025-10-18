package com.example.productcomparison.repository;

import com.example.productcomparison.exception.repository.*;
import com.example.productcomparison.exception.service.ProductNotFoundException;
import com.example.productcomparison.model.Product;
import com.example.productcomparison.model.ProductDTO;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Product repository that manages in-memory storage of products.
 * <p>
 * This implementation uses a {@link ConcurrentHashMap} to store products in memory,
 * providing thread-safe operations for concurrent access.
 * </p>
 *
 * <h2>Main functionalities:</h2>
 * <ul>
 *   <li>Initial loading of products from a JSON file on initialization</li>
 *   <li>CRUD operations (Create, Read, Update, Delete) on products</li>
 *   <li>Product validation through {@link ProductMapper}</li>
 *   <li>Thread-safe in-memory storage</li>
 * </ul>
 *
 * <h2>Required configuration:</h2>
 * <p>
 * The {@code product.data.json-file} property must be defined in the Spring Boot
 * configuration file (application.properties or application.yml) specifying the path
 * to the JSON file with initial product data.
 * </p>
 *
 * <h2>Dependencies:</h2>
 * <ul>
 *   <li>{@link ProductDataSource} - Data source to load products from JSON</li>
 *   <li>{@link ProductMapper} - Mapper between DTOs and domain entities</li>
 * </ul>
 *
 * <h2>Exception handling:</h2>
 * <ul>
 *   <li>{@link ProductDataAccessException} - Thrown when data loading fails</li>
 *   <li>{@link IllegalArgumentException} - Thrown on invalid operations (duplicates, not found)</li>
 * </ul>
 *
 * @see Product
 * @see ProductDTO
 * @see IProductRepository
 * @see ProductDataSource
 * @see ProductMapper
 */
@Repository
@RequiredArgsConstructor
@Slf4j
public class ProductRepository implements IProductRepository {

    @Value("${product.data.json-file}")
    private String jsonFilePath;

    private final ProductDataSource productDataSource;
    private final ProductMapper productMapper;
    private final ConcurrentHashMap<String, Product> inMemoryProducts = new ConcurrentHashMap<>();

    private static final String ERROR_LOAD_PRODUCTS = "Failed to load products from ";
    private static final String ERROR_PRODUCT_EXISTS = "Product with ID %s already exists";
    private static final String ERROR_PRODUCT_NOT_FOUND = "Product with ID %s not found";
    private static final String ERROR_LOG_MESSAGE_FORMAT = "{} for product: {}";


    @PostConstruct
    public void init() {
        loadInitialData();
    }

    private List<ProductDTO> loadProductsFromFile() {
        try {
            List<ProductDTO> dtos = productDataSource.loadProductsFromJson(jsonFilePath);
            if (dtos == null || dtos.isEmpty()) {
                return List.of();
            }
            return dtos;
        } catch (DataSourceInitializationException e) {
            String errorMessage = ERROR_LOAD_PRODUCTS + jsonFilePath;
            log.error(errorMessage, e);
            throw new ProductDataAccessException(errorMessage, e);
        }
    }

    private void loadInitialData() {
        try {
            List<ProductDTO> dtos = loadProductsFromFile();
            dtos.forEach(dto -> {
                try {
                    Product product = productMapper.toDomain(dto);
                    if (productMapper.validateProduct(product)) {
                        inMemoryProducts.put(product.getId(), product);
                    } else {
                        log.warn("Product {} ignored: validation failed", product.getId());
                    }
                } catch (IllegalArgumentException e) {
                    log.warn("Invalid product data from source, skipping product with ID: {}",
                            dto.getId(), e);
                }
            });
            log.info("Successfully loaded {} products into memory", inMemoryProducts.size());
        } catch (ProductDataAccessException e) {
            log.error("Failed to load initial product data", e);
            throw e;
        } catch (Exception e) {
            String errorMessage = "Unexpected error during initial data load";
            log.error(errorMessage, e);
            throw new ProductDataAccessException(errorMessage, e);
        }
    }


    @Override
    public List<Product> findAll() {
        return new ArrayList<>(inMemoryProducts.values());
    }

    @Override
    public Optional<Product> findById(String id) {
        if (id == null || id.isBlank()) {
            return Optional.empty();
        }
        return Optional.ofNullable(inMemoryProducts.get(id));
    }

    @Override
    public Product save(Product product) {
        try {
            productMapper.validateDto(productMapper.toDto(product));

            if (inMemoryProducts.containsKey(product.getId())) {
                String errorMessage = String.format(ERROR_PRODUCT_EXISTS, product.getId());
                log.error(errorMessage);
                throw new ProductAlreadyExistsException(product.getId());
            }

            inMemoryProducts.put(product.getId(), product);
            log.info("Product saved successfully: {}", product.getId());
            return product;
        } catch (ProductAlreadyExistsException e) {
            throw e;
        } catch (IllegalArgumentException e) {
            log.error("Validation failed for product: {}", product.getId(), e);
            throw new ProductValidationException("Product validation failed: " + e.getMessage(), e);
        } catch (Exception e) {
            String errorMessage = "Unexpected error during product save";
            log.error(ERROR_LOG_MESSAGE_FORMAT, errorMessage, product.getId(), e);
            throw new ProductSaveException(product.getId(), errorMessage, e);
        }
    }

    @Override
    public Product update(String id, Product product) {
        try {
            if (!inMemoryProducts.containsKey(id)) {
                String errorMessage = String.format(ERROR_PRODUCT_NOT_FOUND, id);
                log.error(errorMessage);
                throw new ProductNotFoundException(id);
            }

            Product updatedProduct = product.toBuilder().id(id).build();
            productMapper.validateDto(productMapper.toDto(updatedProduct));

            inMemoryProducts.put(id, updatedProduct);
            log.info("Product updated successfully: {}", id);
            return updatedProduct;
        } catch (ProductNotFoundException e) {
            throw e;
        } catch (IllegalArgumentException e) {
            log.error("Validation failed for product update: {}", id, e);
            throw new ProductValidationException("Product validation failed: " + e.getMessage(), e);
        } catch (Exception e) {
            String errorMessage = "Unexpected error during product update";
            log.error(ERROR_LOG_MESSAGE_FORMAT, errorMessage, id, e);
            throw new ProductUpdateException(id, errorMessage, e);
        }
    }

    @Override
    public void deleteById(String id) {
        try {
            if (!inMemoryProducts.containsKey(id)) {
                String errorMessage = String.format(ERROR_PRODUCT_NOT_FOUND, id);
                log.error(errorMessage);
                throw new ProductNotFoundException(id);
            }

            inMemoryProducts.remove(id);
            log.info("Product deleted successfully: {}", id);
        } catch (ProductNotFoundException e) {
            throw e;
        } catch (Exception e) {
            String errorMessage = "Unexpected error during product deletion";
            log.error(ERROR_LOG_MESSAGE_FORMAT, errorMessage, id, e);
            throw new ProductDeleteException(id, errorMessage, e);
        }
    }

}