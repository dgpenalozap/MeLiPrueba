package com.example.productcomparison.repository;

import com.example.productcomparison.model.Product;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for Product domain objects.
 * This interface defines the contract for product data access.
 * Following the Dependency Inversion Principle (DIP) - high-level modules
 * (services) depend on this abstraction, not on concrete implementations.
 */
public interface IProductRepository {
    
    /**
     * Retrieves all products.
     * @return list of all products
     */
    List<Product> findAll();
    
    /**
     * Finds a product by its ID.
     * @param id the product ID
     * @return an Optional containing the product if found
     */
    Optional<Product> findById(String id);
}
