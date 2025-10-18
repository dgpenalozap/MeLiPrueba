package com.example.productcomparison.exception.repository;

/**
 * Exception thrown when attempting to save a product that already exists in the repository.
 * 
 * <p>This exception is raised during create operations when a product with the same ID 
 * is already present in the data store. It prevents duplicate entries and maintains 
 * data integrity.</p>
 * 
 * <h2>Usage Layer:</h2>
 * <ul>
 *   <li><b>Repository Layer:</b> Thrown by {@code ProductRepository.save()} when detecting duplicate product IDs</li>
 *   <li><b>Service Layer:</b> Propagated through {@code ProductService.createProduct()}</li>
 *   <li><b>Controller Layer:</b> Handled by {@code GlobalExceptionHandler} and returned as HTTP 409 Conflict</li>
 * </ul>
 * 
 * <h2>HTTP Response:</h2>
 * <ul>
 *   <li><b>Status Code:</b> 409 Conflict</li>
 *   <li><b>Error Code:</b> PRODUCT_ALREADY_EXISTS</li>
 *   <li><b>Response Body:</b> ErrorResponse with product ID and descriptive message</li>
 * </ul>
 * 
 * <h2>Example Usage:</h2>
 * <pre>{@code
 * if (inMemoryProducts.containsKey(product.getId())) {
 *     throw new ProductAlreadyExistsException(product.getId());
 * }
 * }</pre>
 * 
 * <h2>Client Impact:</h2>
 * <p>Clients should handle this exception by either:</p>
 * <ul>
 *   <li>Using a different product ID</li>
 *   <li>Calling the update endpoint instead of create</li>
 *   <li>Checking if the product exists before attempting to create it</li>
 * </ul>
 * 
 * @see com.example.productcomparison.repository.ProductRepository#save(com.example.productcomparison.model.Product)
 * @see com.example.productcomparison.exception.GlobalExceptionHandler#handleProductAlreadyExistsException
 * @since 1.0
 * @author Product Comparison API Team
 */
public class ProductAlreadyExistsException extends RuntimeException {

    private final String productId;

    /**
     * Constructs a new ProductAlreadyExistsException with the specified product ID.
     * 
     * @param productId the ID of the product that already exists in the repository
     */
    public ProductAlreadyExistsException(String productId) {
        super(String.format("Product with ID '%s' already exists", productId));
        this.productId = productId;
    }

    /**
     * Constructs a new ProductAlreadyExistsException with the specified product ID and cause.
     * 
     * @param productId the ID of the product that already exists in the repository
     * @param cause the underlying cause of this exception
     */
    public ProductAlreadyExistsException(String productId, Throwable cause) {
        super(String.format("Product with ID '%s' already exists", productId), cause);
        this.productId = productId;
    }

    /**
     * Returns the ID of the product that already exists.
     * 
     * @return the product ID that caused the conflict
     */
    public String getProductId() {
        return productId;
    }
}
