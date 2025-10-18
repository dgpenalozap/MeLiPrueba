package com.example.productcomparison.exception.repository;

/**
 * Exception thrown when a product update operation fails unexpectedly in the repository.
 * 
 * <p>This exception wraps underlying exceptions that occur during product modification
 * operations. It indicates an unexpected error that prevented the successful update
 * of an existing product, excluding validation errors and "not found" scenarios which
 * have their own specific exceptions.</p>
 * 
 * <h2>Usage Layer:</h2>
 * <ul>
 *   <li><b>Repository Layer:</b> Thrown by {@code ProductRepository.update()} when an unexpected error occurs during update</li>
 *   <li><b>Service Layer:</b> May be thrown by {@code ProductService.updateProduct()} when repository operations fail</li>
 *   <li><b>Controller Layer:</b> Handled by {@code GlobalExceptionHandler} and returned as HTTP 500 Internal Server Error</li>
 * </ul>
 * 
 * <h2>HTTP Response:</h2>
 * <ul>
 *   <li><b>Status Code:</b> 500 Internal Server Error</li>
 *   <li><b>Error Code:</b> PRODUCT_UPDATE_ERROR</li>
 *   <li><b>Response Body:</b> ErrorResponse with product ID and error details</li>
 * </ul>
 * 
 * <h2>Common Causes:</h2>
 * <ul>
 *   <li>Concurrent modification conflicts</li>
 *   <li>Race conditions in multi-threaded environments</li>
 *   <li>Unexpected runtime exceptions during update operations</li>
 *   <li>System state inconsistencies</li>
 * </ul>
 * 
 * <h2>Example Usage:</h2>
 * <pre>{@code
 * try {
 *     inMemoryProducts.put(id, updatedProduct);
 * } catch (Exception e) {
 *     throw new ProductUpdateException(id, "Unexpected error during update", e);
 * }
 * }</pre>
 * 
 * <h2>Recovery Actions:</h2>
 * <ul>
 *   <li>Verify the product still exists before retrying</li>
 *   <li>Check system logs for concurrency issues</li>
 *   <li>Retry the operation with exponential backoff</li>
 *   <li>Investigate if multiple clients are modifying the same product</li>
 * </ul>
 * 
 * @see com.example.productcomparison.repository.ProductRepository#update(String, com.example.productcomparison.model.Product)
 * @see com.example.productcomparison.exception.GlobalExceptionHandler#handleProductUpdateException
 * @since 1.0
 * @author Product Comparison API Team
 */
public class ProductUpdateException extends RuntimeException {

    private final String productId;

    /**
     * Constructs a new ProductUpdateException with the specified product ID and error message.
     * 
     * @param productId the ID of the product that failed to update
     * @param message a descriptive message explaining the update failure
     */
    public ProductUpdateException(String productId, String message) {
        super(String.format("Failed to update product with ID '%s': %s", productId, message));
        this.productId = productId;
    }

    /**
     * Constructs a new ProductUpdateException with the specified product ID, message, and cause.
     * 
     * @param productId the ID of the product that failed to update
     * @param message a descriptive message explaining the update failure
     * @param cause the underlying cause of the update failure
     */
    public ProductUpdateException(String productId, String message, Throwable cause) {
        super(String.format("Failed to update product with ID '%s': %s", productId, message), cause);
        this.productId = productId;
    }

    /**
     * Returns the ID of the product that failed to update.
     * 
     * @return the product ID associated with the failed update operation
     */
    public String getProductId() {
        return productId;
    }
}
