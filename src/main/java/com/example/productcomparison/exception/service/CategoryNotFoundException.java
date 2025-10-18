package com.example.productcomparison.exception.service;

import lombok.Getter;

/**
 * Exception thrown when a requested product category does not exist in the system.
 * 
 * <p>This exception is raised when filtering products by a category name that doesn't
 * match any existing category in the product catalog. It helps clients discover
 * valid category names and prevents silent failures.</p>
 * 
 * <h2>Usage Layer:</h2>
 * <ul>
 *   <li><b>Service Layer:</b> Thrown by {@code ProductService.filterByCategory()} when no products exist in the specified category</li>
 *   <li><b>Controller Layer:</b> Handled by {@code GlobalExceptionHandler} and returned as HTTP 404 Not Found</li>
 * </ul>
 * 
 * <h2>HTTP Response:</h2>
 * <ul>
 *   <li><b>Status Code:</b> 404 Not Found</li>
 *   <li><b>Error Code:</b> CATEGORY_NOT_FOUND</li>
 *   <li><b>Response Body:</b> ErrorResponse with category name and link to available categories endpoint</li>
 * </ul>
 * 
 * <h2>Available Categories:</h2>
 * <p>The system supports the following product categories:</p>
 * <ul>
 *   <li>Laptops</li>
 *   <li>Smartphones</li>
 *   <li>Tablets</li>
 *   <li>Monitors</li>
 *   <li>Keyboards</li>
 *   <li>Mice</li>
 *   <li>Headphones</li>
 *   <li>Cameras</li>
 *   <li>Smartwatches</li>
 *   <li>Speakers</li>
 *   <li>Networking</li>
 *   <li>Storage</li>
 *   <li>Printers</li>
 * </ul>
 * 
 * <h2>Common Scenarios:</h2>
 * <ul>
 *   <li>Typo in category name: "Laptop" instead of "Laptops"</li>
 *   <li>Using singular form: "Phone" instead of "Smartphones"</li>
 *   <li>Wrong capitalization: "laptops" instead of "Laptops"</li>
 *   <li>Non-existent category: "Desktops"</li>
 * </ul>
 * 
 * <h2>Example Usage:</h2>
 * <pre>{@code
 * List<Product> results = productRepository.findAll().stream()
 *     .filter(p -> category.equalsIgnoreCase(p.getSpecifications().get("category")))
 *     .toList();
 * 
 * if (results.isEmpty()) {
 *     List<String> existingCategories = getAllCategories();
 *     boolean categoryExists = existingCategories.stream()
 *         .anyMatch(cat -> cat.equalsIgnoreCase(category));
 *     
 *     if (!categoryExists) {
 *         throw new CategoryNotFoundException(category);
 *     }
 * }
 * }</pre>
 * 
 * <h2>Exception vs Empty Result:</h2>
 * <p>This exception is only thrown when the category name itself doesn't exist in the
 * system. If the category exists but has no products currently, an empty list is returned
 * instead.</p>
 * 
 * <h2>Related Endpoints:</h2>
 * <ul>
 *   <li>GET /api/products/categories - List all available categories</li>
 *   <li>GET /api/products/filter/category/{category} - Filter by category</li>
 * </ul>
 * 
 * <h2>Client Actions:</h2>
 * <ul>
 *   <li>Call GET /api/products/categories to get valid category names</li>
 *   <li>Check for typos or case sensitivity issues</li>
 *   <li>Use exact category names as returned by the categories endpoint</li>
 *   <li>Consider using case-insensitive matching on client side</li>
 * </ul>
 * 
 * @see com.example.productcomparison.service.ProductService#filterByCategory(String)
 * @see com.example.productcomparison.service.ProductService#getAllCategories()
 * @see com.example.productcomparison.exception.GlobalExceptionHandler#handleCategoryNotFoundException
 * @since 1.0
 * @author Product Comparison API Team
 */
@Getter
public class CategoryNotFoundException extends RuntimeException {
    
    private final String category;
    
    /**
     * Constructs a new CategoryNotFoundException with the specified category name.
     * 
     * <p>The exception message includes a helpful suggestion to use the categories
     * endpoint to discover valid category names.</p>
     * 
     * @param category the name of the category that was not found
     */
    public CategoryNotFoundException(String category) {
        super(String.format("Category not found: '%s'. Use /api/products/categories to see available categories", category));
        this.category = category;
    }

}
