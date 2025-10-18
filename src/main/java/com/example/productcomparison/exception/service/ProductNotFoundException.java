package com.example.productcomparison.exception.service;

import lombok.Getter;

/**
 * Exception thrown when a requested product cannot be found in the repository.
 * 
 * <p>This exception is raised when attempting to retrieve, update, or delete a product
 * that does not exist in the data store. It indicates that the specified product ID
 * does not match any existing product records.</p>
 * 
 * <h2>Usage Layer:</h2>
 * <ul>
 *   <li><b>Repository Layer:</b> Thrown by {@code ProductRepository} when product lookup fails in update/delete operations</li>
 *   <li><b>Service Layer:</b> Thrown by {@code ProductService.getProductById()} when product is not found</li>
 *   <li><b>Service Layer:</b> Propagated through update and delete service methods</li>
 *   <li><b>Controller Layer:</b> Handled by {@code GlobalExceptionHandler} and returned as HTTP 404 Not Found</li>
 * </ul>
 * 
 * <h2>HTTP Response:</h2>
 * <ul>
 *   <li><b>Status Code:</b> 404 Not Found</li>
 *   <li><b>Error Code:</b> PRODUCT_NOT_FOUND</li>
 *   <li><b>Response Body:</b> ErrorResponse with product ID and helpful message</li>
 * </ul>
 * 
 * <h2>Common Scenarios:</h2>
 * <ul>
 *   <li>GET request for a non-existent product ID</li>
 *   <li>PUT request attempting to update a deleted product</li>
 *   <li>DELETE request for an already deleted product</li>
 *   <li>Product ID typo in the request</li>
 *   <li>Product was deleted by another client</li>
 * </ul>
 * 
 * <h2>Example Usage:</h2>
 * <pre>{@code
 * // In repository
 * public Product update(String id, Product product) {
 *     if (!inMemoryProducts.containsKey(id)) {
 *         throw new ProductNotFoundException(id);
 *     }
 *     // ... update logic
 * }
 * 
 * // In service
 * public Product getProductById(String id) {
 *     return productRepository.findById(id)
 *         .orElseThrow(() -> new ProductNotFoundException(id));
 * }
 * }</pre>
 * 
 * <h2>Client Actions:</h2>
 * <ul>
 *   <li>Verify the product ID is correct</li>
 *   <li>Use GET /api/products to list all available products</li>
 *   <li>Check if the product was recently deleted</li>
 *   <li>For update operations, consider using POST to create a new product instead</li>
 * </ul>
 * 
 * @see com.example.productcomparison.repository.ProductRepository#findById(String)
 * @see com.example.productcomparison.repository.ProductRepository#update(String, com.example.productcomparison.model.Product)
 * @see com.example.productcomparison.repository.ProductRepository#deleteById(String)
 * @see com.example.productcomparison.service.ProductService#getProductById(String)
 * @see com.example.productcomparison.exception.GlobalExceptionHandler#handleProductNotFoundException
 * @since 1.0
 * @author Product Comparison API Team
 */
@Getter
public class ProductNotFoundException extends RuntimeException {
    
    private final String productId;
    
    /**
     * Constructs a new ProductNotFoundException with the specified product ID.
     * 
     * @param productId the ID of the product that was not found
     */
    public ProductNotFoundException(String productId) {
        super(String.format("Product not found: %s", productId));
        this.productId = productId;
    }
    
    /**
     * Constructs a new ProductNotFoundException with the product ID and additional context.
     * 
     * @param productId the ID of the product that was not found
     * @param additionalInfo additional context or helpful information for debugging
     */
    public ProductNotFoundException(String productId, String additionalInfo) {
        super(String.format("Product not found: %s. %s", productId, additionalInfo));
        this.productId = productId;
    }

}
