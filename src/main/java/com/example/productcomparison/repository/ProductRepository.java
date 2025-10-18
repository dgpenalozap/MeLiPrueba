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
 * @see Product
 * @see ProductDTO
 * @see IProductRepository
 * @see ProductDataSource
 * @see ProductMapper
 */
@Repository
@RequiredArgsConstructor
public class ProductRepository implements IProductRepository {

    @Value("${product.data.json-file}")
    private String jsonFilePath;

    private final ProductDataSource productDataSource;
    private final ProductMapper productMapper;
    private final ConcurrentHashMap<String, Product> inMemoryProducts = new ConcurrentHashMap<>();

    private static final String ERROR_LOAD_PRODUCTS = "Failed to load products from ";
    private static final String ERROR_PRODUCT_EXISTS = "Product with ID %s already exists";
    private static final String ERROR_PRODUCT_NOT_FOUND = "Product with ID %s not found";

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
            throw new ProductDataAccessException(ERROR_LOAD_PRODUCTS + jsonFilePath, e);
        }
    }

    private void loadInitialData() {
        loadProductsFromFile().forEach(dto -> {
            Product product = productMapper.toDomain(dto);
            if (productMapper.validateProduct(product)) {
                inMemoryProducts.put(product.getId(), product);
            }
        });
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
        productMapper.validateDto(productMapper.toDto(product));
        if (inMemoryProducts.containsKey(product.getId())) {
            throw new IllegalArgumentException(String.format(ERROR_PRODUCT_EXISTS, product.getId()));
        }
        inMemoryProducts.put(product.getId(), product);
        return product;
    }

    @Override
    public Product update(String id, Product product) {
        if (!inMemoryProducts.containsKey(id)) {
            throw new IllegalArgumentException(String.format(ERROR_PRODUCT_NOT_FOUND, id));
        }
        Product updatedProduct = product.toBuilder().id(id).build();
        productMapper.validateDto(productMapper.toDto(updatedProduct));
        inMemoryProducts.put(id, updatedProduct);
        return updatedProduct;
    }

    @Override
    public void deleteById(String id) {
        if (!inMemoryProducts.containsKey(id)) {
            throw new IllegalArgumentException(String.format(ERROR_PRODUCT_NOT_FOUND, id));
        }
        inMemoryProducts.remove(id);
    }

}