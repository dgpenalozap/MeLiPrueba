package com.example.productcomparison.exception.repository;

/**
 * Exception thrown when a product delete operation fails unexpectedly in the repository.
 * 
 * <p>This exception wraps underlying exceptions that occur during product removal
 * operations. It indicates an unexpected error that prevented the successful deletion
 * of an existing product, excluding "not found" scenarios which have their own specific
 * exception.</p>
 * 
 * <h2>Usage Layer:</h2>
 * <ul>
 *   <li><b>Repository Layer:</b> Thrown by {@code ProductRepository.deleteById()} when an unexpected error occurs during deletion</li>
 *   <li><b>Service Layer:</b> May be thrown by {@code ProductService.deleteProduct()} when repository operations fail</li>
 *   <li><b>Controller Layer:</b> Handled by {@code GlobalExceptionHandler} and returned as HTTP 500 Internal Server Error</li>
 * </ul>
 * 
 * <h2>HTTP Response:</h2>
 * <ul>
 *   <li><b>Status Code:</b> 500 Internal Server Error</li>
 *   <li><b>Error Code:</b> PRODUCT_DELETE_ERROR</li>
 *   <li><b>Response Body:</b> ErrorResponse with product ID and error details</li>
 * </ul>
 * 
 * <h2>Common Causes:</h2>
 * <ul>
 *   <li>Concurrent modification during deletion</li>
 *   <li>Lock contention in multi-threaded scenarios</li>
 *   <li>Unexpected runtime exceptions during remove operations</li>
 *   <li>Data structure corruption or inconsistencies</li>
 * </ul>
 * 
 * <h2>Example Usage:</h2>
 * <pre>{@code
 * try {
 *     inMemoryProducts.remove(id);
 * } catch (Exception e) {
 *     throw new ProductDeleteException(id, "Unexpected error during deletion", e);
 * }
 * }</pre>
 * 
 * <h2>Recovery Actions:</h2>
 * <ul>
 *   <li>Verify the product still exists and retry</li>
 *   <li>Check system logs for concurrent access issues</li>
 *   <li>Ensure no other operations are accessing the same product</li>
 *   <li>Verify data integrity of the storage mechanism</li>
 * </ul>
 * 
 * <h2>Data Integrity:</h2>
 * <p>When this exception occurs, the product's state is uncertain. Clients should
 * verify whether the deletion succeeded by attempting to retrieve the product before
 * retrying the deletion operation.</p>
 * 
 * @see com.example.productcomparison.repository.ProductRepository#deleteById(String)
 * @see com.example.productcomparison.exception.GlobalExceptionHandler#handleProductDeleteException
 * @since 1.0
 * @author Product Comparison API Team
 */
public class ProductDeleteException extends RuntimeException {

    private final String productId;

    /**
     * Constructs a new ProductDeleteException with the specified product ID and error message.
     * 
     * @param productId the ID of the product that failed to delete
     * @param message a descriptive message explaining the deletion failure
     */
    public ProductDeleteException(String productId, String message) {
        super(String.format("Failed to delete product with ID '%s': %s", productId, message));
        this.productId = productId;
    }

    /**
     * Constructs a new ProductDeleteException with the specified product ID, message, and cause.
     * 
     * @param productId the ID of the product that failed to delete
     * @param message a descriptive message explaining the deletion failure
     * @param cause the underlying cause of the deletion failure
     */
    public ProductDeleteException(String productId, String message, Throwable cause) {
        super(String.format("Failed to delete product with ID '%s': %s", productId, message), cause);
        this.productId = productId;
    }

    /**
     * Returns the ID of the product that failed to delete.
     * 
     * @return the product ID associated with the failed deletion operation
     */
    public String getProductId() {
        return productId;
    }
}
