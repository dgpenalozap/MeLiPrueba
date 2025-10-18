package com.example.productcomparison.repository;

import com.example.productcomparison.model.Product;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for Product domain objects.
 * This interface defines the contract for product data access.
 */
public interface IProductRepository {

    List<Product> findAll();

    Optional<Product> findById(String id);

    Product save(Product product);

    Product update(String id, Product product);

    void deleteById(String id);

}
