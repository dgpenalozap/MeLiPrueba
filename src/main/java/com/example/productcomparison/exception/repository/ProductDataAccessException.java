package com.example.productcomparison.exception.repository;

/**
 * Exception thrown when data access operations fail at the repository or data source level.
 * 
 * <p>This exception represents critical failures in data persistence or retrieval operations
 * that prevent the application from accessing or modifying product data. It typically wraps
 * lower-level exceptions from data sources, I/O operations, or data format issues.</p>
 * 
 * <h2>Usage Layer:</h2>
 * <ul>
 *   <li><b>Data Source Layer:</b> Thrown by {@code ProductDataSource} when JSON loading fails</li>
 *   <li><b>Repository Layer:</b> Thrown by {@code ProductRepository} when data initialization or access fails</li>
 *   <li><b>Service Layer:</b> May be thrown when AI product generation and save fails</li>
 *   <li><b>Controller Layer:</b> Handled by {@code GlobalExceptionHandler} and returned as HTTP 500 Internal Server Error</li>
 * </ul>
 * 
 * <h2>HTTP Response:</h2>
 * <ul>
 *   <li><b>Status Code:</b> 500 Internal Server Error</li>
 *   <li><b>Error Code:</b> DATA_ACCESS_ERROR</li>
 *   <li><b>Response Body:</b> ErrorResponse with generic error message (sensitive details logged server-side)</li>
 * </ul>
 * 
 * <h2>Common Causes:</h2>
 * <ul>
 *   <li>JSON file not found or inaccessible</li>
 *   <li>JSON parsing errors (malformed data)</li>
 *   <li>File system permissions issues</li>
 *   <li>I/O errors during file operations</li>
 *   <li>Data source initialization failures</li>
 *   <li>Corrupted or invalid data format</li>
 *   <li>Resource exhaustion (memory, file handles)</li>
 * </ul>
 * 
 * <h2>Example Usage:</h2>
 * <pre>{@code
 * // In ProductDataSource
 * try {
 *     String content = Files.readString(Paths.get(jsonFilePath));
 *     return objectMapper.readValue(content, new TypeReference<List<ProductDTO>>() {});
 * } catch (IOException e) {
 *     throw new DataSourceInitializationException("Failed to load products from: " + jsonFilePath, e);
 * }
 * 
 * // In ProductRepository
 * try {
 *     List<ProductDTO> dtos = productDataSource.loadProductsFromJson(jsonFilePath);
 * } catch (DataSourceInitializationException e) {
 *     throw new ProductDataAccessException("Failed to load products from " + jsonFilePath, e);
 * }
 * 
 * // In ProductService
 * try {
 *     Product randomProduct = aiProductGenerator.generateRandomProduct();
 *     return productRepository.save(randomProduct);
 * } catch (Exception e) {
 *     throw new ProductDataAccessException("Failed to generate random product", e);
 * }
 * }</pre>
 * 
 * <h2>Impact:</h2>
 * <p>This exception typically indicates a serious system issue that may affect multiple
 * users and operations. When thrown during initialization, it can prevent the application
 * from starting properly.</p>
 * 
 * <h2>Recovery Actions:</h2>
 * <ul>
 *   <li><b>Operations Team:</b> Check application logs for detailed error information</li>
 *   <li><b>Operations Team:</b> Verify data file existence and permissions</li>
 *   <li><b>Operations Team:</b> Validate JSON file format and structure</li>
 *   <li><b>Operations Team:</b> Check available system resources (disk space, memory)</li>
 *   <li><b>Developers:</b> Review data source configuration</li>
 *   <li><b>Developers:</b> Consider implementing retry logic for transient failures</li>
 * </ul>
 * 
 * <h2>Security Considerations:</h2>
 * <p>This exception intentionally provides generic error messages to clients while
 * logging detailed information server-side. This prevents exposing sensitive system
 * details like file paths or internal structure.</p>
 * 
 * @see com.example.productcomparison.repository.ProductDataSource
 * @see com.example.productcomparison.repository.ProductRepository
 * @see DataSourceInitializationException
 * @see com.example.productcomparison.exception.GlobalExceptionHandler#handleProductDataAccessException
 * @since 1.0
 * @author Product Comparison API Team
 */
public class ProductDataAccessException extends RuntimeException {
    
    /**
     * Constructs a new ProductDataAccessException with a message and underlying cause.
     * 
     * <p>Use this constructor to wrap exceptions from data access operations while
     * providing context about what operation failed.</p>
     * 
     * @param message a descriptive message explaining the data access failure
     * @param cause the underlying exception that caused the data access failure
     */
    public ProductDataAccessException(String message, Throwable cause) {
        super(message, cause);
    }
    
    /**
     * Constructs a new ProductDataAccessException with only a message.
     * 
     * <p>Use this constructor when the data access failure doesn't have an underlying
     * exception or when the cause is not relevant.</p>
     * 
     * @param message a descriptive message explaining the data access failure
     */
    public ProductDataAccessException(String message) {
        super(message);
    }
}

