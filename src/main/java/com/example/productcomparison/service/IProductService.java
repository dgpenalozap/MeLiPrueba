package com.example.productcomparison.service;

import com.example.productcomparison.model.Product;

import java.util.List;

/**
 * Service interface for product business logic.
 * Following the Interface Segregation Principle (ISP) and 
 * Dependency Inversion Principle (DIP).
 */
public interface IProductService {
    
    List<Product> getAllProducts();

    Product createProduct(Product product);

    Product generateRandomProduct();

    Product updateProduct(String id, Product product);

    void deleteProduct(String id);

    Product getProductById(String id);

    List<Product> searchByName(String query);

    List<Product> filterByPriceRange(double minPrice, double maxPrice);

    List<Product> filterByRating(double minRating);

    List<Product> filterByCategory(String category);

    List<String> getAllCategories();

    List<Product> compareProducts(List<String> productIds);

    List<Product> sortByPrice(boolean ascending);

    List<Product> sortByRating(boolean ascending);

    List<Product> getTopRatedProducts(int limit);

    List<Product> findBySpecification(String specKey, String specValue);
}
