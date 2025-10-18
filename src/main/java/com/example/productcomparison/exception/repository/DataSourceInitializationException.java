package com.example.productcomparison.exception.repository;

/**
 * Exception thrown when the data source fails to initialize or load product data.
 * 
 * <p>This exception represents low-level failures in reading, parsing, or loading product
 * data from the underlying data source (JSON file). It is typically caught and wrapped
 * by higher-level exceptions like {@link ProductDataAccessException} before being exposed
 * to upper layers.</p>
 * 
 * <h2>Usage Layer:</h2>
 * <ul>
 *   <li><b>Data Source Layer:</b> Thrown by {@code ProductDataSource.loadProductsFromJson()} when file operations fail</li>
 *   <li><b>Repository Layer:</b> Caught and wrapped in {@code ProductDataAccessException}</li>
 * </ul>
 * 
 * <h2>Common Causes:</h2>
 * <ul>
 *   <li>JSON file not found at specified path</li>
 *   <li>Insufficient file system permissions to read the file</li>
 *   <li>Malformed JSON structure or syntax errors</li>
 *   <li>Invalid JSON encoding or character set issues</li>
 *   <li>File locked by another process</li>
 *   <li>Disk I/O errors or hardware failures</li>
 *   <li>Path configuration errors (wrong file path)</li>
 * </ul>
 * 
 * <h2>Example Usage:</h2>
 * <pre>{@code
 * public List<ProductDTO> loadProductsFromJson(String jsonFilePath) {
 *     try {
 *         Path path = Paths.get(jsonFilePath);
 *         if (!Files.exists(path)) {
 *             throw new DataSourceInitializationException(
 *                 "JSON file not found: " + jsonFilePath);
 *         }
 *         
 *         String content = Files.readString(path);
 *         return objectMapper.readValue(content, 
 *             new TypeReference<List<ProductDTO>>() {});
 *             
 *     } catch (IOException e) {
 *         throw new DataSourceInitializationException(
 *             "Failed to load products from: " + jsonFilePath, e);
 *     } catch (JsonProcessingException e) {
 *         throw new DataSourceInitializationException(
 *             "Invalid JSON format in: " + jsonFilePath, e);
 *     }
 * }
 * }</pre>
 * 
 * <h2>Impact:</h2>
 * <p>This exception during application startup will prevent the repository from
 * initializing properly, which may cause the application to fail to start. When thrown
 * during runtime reload operations, it may prevent data refresh but shouldn't crash
 * the application.</p>
 * 
 * <h2>Configuration Requirements:</h2>
 * <ul>
 *   <li>Property: {@code product.data.json-file} must be defined in application.properties</li>
 *   <li>File must exist at the configured path</li>
 *   <li>File must contain valid JSON array of products</li>
 *   <li>Application must have read permissions for the file</li>
 * </ul>
 * 
 * <h2>Recovery Actions:</h2>
 * <ul>
 *   <li><b>Verify File Path:</b> Check that {@code product.data.json-file} property is correctly configured</li>
 *   <li><b>Check File Existence:</b> Ensure the JSON file exists at the specified location</li>
 *   <li><b>Validate Permissions:</b> Verify the application has read access to the file</li>
 *   <li><b>Validate JSON:</b> Use a JSON validator to check file format</li>
 *   <li><b>Check Logs:</b> Review stack trace for specific I/O or parsing errors</li>
 *   <li><b>Restore Backup:</b> If file is corrupted, restore from backup</li>
 * </ul>
 * 
 * <h2>Prevention:</h2>
 * <ul>
 *   <li>Use absolute paths or classpath resources for data files</li>
 *   <li>Validate JSON structure in CI/CD pipeline</li>
 *   <li>Implement health checks for data source availability</li>
 *   <li>Consider using externalized configuration for file paths</li>
 * </ul>
 * 
 * @see com.example.productcomparison.repository.ProductDataSource
 * @see ProductDataAccessException
 * @since 1.0
 * @author Product Comparison API Team
 */
public class DataSourceInitializationException extends RuntimeException {
    
    /**
     * Constructs a new DataSourceInitializationException with a descriptive message.
     * 
     * <p>Use this constructor when the failure doesn't have an underlying exception
     * or when the cause is a logical condition (e.g., file not found check).</p>
     * 
     * @param message a descriptive message explaining the initialization failure
     */
    public DataSourceInitializationException(String message) {
        super(message);
    }

    /**
     * Constructs a new DataSourceInitializationException with a message and cause.
     * 
     * <p>Use this constructor to wrap I/O exceptions or JSON parsing exceptions
     * while providing additional context.</p>
     * 
     * @param message a descriptive message explaining the initialization failure
     * @param cause the underlying exception that caused the initialization failure
     */
    public DataSourceInitializationException(String message, Throwable cause) {
        super(message, cause);
    }
}
