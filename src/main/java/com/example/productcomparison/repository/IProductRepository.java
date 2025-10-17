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
    
    /**
     * Saves a new product.
     * @param product the product to save
     * @return the saved product
     */
    Product save(Product product);
    
    /**
     * Updates an existing product.
     * @param id the product ID to update
     * @param product the updated product data
     * @return the updated product
     */
    Product update(String id, Product product);
    
    /**
     * Deletes a product by its ID.
     * @param id the product ID to delete
     */
    void deleteById(String id);
}
