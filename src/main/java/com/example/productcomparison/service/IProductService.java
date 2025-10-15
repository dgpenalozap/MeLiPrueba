package com.example.productcomparison.service;

import com.example.productcomparison.model.Product;

import java.util.List;

/**
 * Service interface for product business logic.
 * Following the Interface Segregation Principle (ISP) and 
 * Dependency Inversion Principle (DIP).
 */
public interface IProductService {
    
    /**
     * Retrieves all available products.
     * @return list of products
     */
    List<Product> getAllProducts();
    
    /**
     * Retrieves a specific product by its ID.
     * @param id the product ID
     * @return the product
     * @throws com.example.productcomparison.exception.ProductNotFoundException if product not found
     */
    Product getProductById(String id);
    
    /**
     * Search products by name (case-insensitive, partial match).
     * @param query search query
     * @return list of matching products
     */
    List<Product> searchByName(String query);
    
    /**
     * Filter products by price range.
     * @param minPrice minimum price (inclusive)
     * @param maxPrice maximum price (inclusive)
     * @return list of products within price range
     */
    List<Product> filterByPriceRange(double minPrice, double maxPrice);
    
    /**
     * Filter products by minimum rating.
     * @param minRating minimum rating (inclusive)
     * @return list of products with rating >= minRating
     */
    List<Product> filterByRating(double minRating);
    
    /**
     * Filter products by category.
     * @param category product category
     * @return list of products in the category
     */
    List<Product> filterByCategory(String category);
    
    /**
     * Get all unique categories.
     * @return list of unique categories
     */
    List<String> getAllCategories();
    
    /**
     * Compare multiple products by their IDs.
     * @param productIds list of product IDs to compare
     * @return list of products for comparison
     */
    List<Product> compareProducts(List<String> productIds);
    
    /**
     * Sort products by price.
     * @param ascending true for ascending, false for descending
     * @return sorted list of products
     */
    List<Product> sortByPrice(boolean ascending);
    
    /**
     * Sort products by rating.
     * @param ascending true for ascending, false for descending
     * @return sorted list of products
     */
    List<Product> sortByRating(boolean ascending);
    
    /**
     * Get top N products by rating.
     * @param limit number of products to return
     * @return list of top-rated products
     */
    List<Product> getTopRatedProducts(int limit);
    
    /**
     * Find products within a specification criteria.
     * @param specKey specification key to search
     * @param specValue specification value to match
     * @return list of matching products
     */
    List<Product> findBySpecification(String specKey, String specValue);
}
