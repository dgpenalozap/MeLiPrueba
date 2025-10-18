package com.example.productcomparison.exception.repository;

/**
 * Exception thrown when a product save operation fails unexpectedly in the repository.
 * 
 * <p>This exception wraps underlying exceptions that occur during product persistence
 * operations. It indicates an unexpected error that prevented the successful creation
 * of a new product, excluding validation errors and duplicate ID conflicts which have
 * their own specific exceptions.</p>
 * 
 * <h2>Usage Layer:</h2>
 * <ul>
 *   <li><b>Repository Layer:</b> Thrown by {@code ProductRepository.save()} when an unexpected error occurs during save</li>
 *   <li><b>Service Layer:</b> May be thrown by {@code ProductService.createProduct()} when repository operations fail</li>
 *   <li><b>Controller Layer:</b> Handled by {@code GlobalExceptionHandler} and returned as HTTP 500 Internal Server Error</li>
 * </ul>
 * 
 * <h2>HTTP Response:</h2>
 * <ul>
 *   <li><b>Status Code:</b> 500 Internal Server Error</li>
 *   <li><b>Error Code:</b> PRODUCT_SAVE_ERROR</li>
 *   <li><b>Response Body:</b> ErrorResponse with product ID and error details</li>
 * </ul>
 * 
 * <h2>Common Causes:</h2>
 * <ul>
 *   <li>Concurrent modification exceptions</li>
 *   <li>Memory allocation failures</li>
 *   <li>Unexpected runtime exceptions during save operations</li>
 *   <li>System resource limitations</li>
 * </ul>
 * 
 * <h2>Example Usage:</h2>
 * <pre>{@code
 * try {
 *     inMemoryProducts.put(product.getId(), product);
 * } catch (Exception e) {
 *     throw new ProductSaveException(product.getId(), "Unexpected error", e);
 * }
 * }</pre>
 * 
 * <h2>Recovery Actions:</h2>
 * <ul>
 *   <li>Check system logs for underlying cause</li>
 *   <li>Retry the operation after a brief delay</li>
 *   <li>Verify system resources are available</li>
 *   <li>Contact system administrators if issue persists</li>
 * </ul>
 * 
 * @see com.example.productcomparison.repository.ProductRepository#save(com.example.productcomparison.model.Product)
 * @see com.example.productcomparison.exception.GlobalExceptionHandler#handleProductSaveException
 * @since 1.0
 * @author Product Comparison API Team
 */
public class ProductSaveException extends RuntimeException {

    private final String productId;

    /**
     * Constructs a new ProductSaveException with the specified product ID and error message.
     * 
     * @param productId the ID of the product that failed to save
     * @param message a descriptive message explaining the save failure
     */
    public ProductSaveException(String productId, String message) {
        super(String.format("Failed to save product with ID '%s': %s", productId, message));
        this.productId = productId;
    }

    /**
     * Constructs a new ProductSaveException with the specified product ID, message, and cause.
     * 
     * @param productId the ID of the product that failed to save
     * @param message a descriptive message explaining the save failure
     * @param cause the underlying cause of the save failure
     */
    public ProductSaveException(String productId, String message, Throwable cause) {
        super(String.format("Failed to save product with ID '%s': %s", productId, message), cause);
        this.productId = productId;
    }

    /**
     * Returns the ID of the product that failed to save.
     * 
     * @return the product ID associated with the failed save operation
     */
    public String getProductId() {
        return productId;
    }
}
