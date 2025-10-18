package com.example.productcomparison.exception.service;

import lombok.Getter;

/**
 * Exception thrown when a query or search operation returns no results unexpectedly.
 * 
 * <p>This exception is used when an operation is expected to return at least one result
 * but returns an empty collection instead. It differs from {@link ProductNotFoundException}
 * which is for single-item lookups, as this is for queries, searches, and filter operations
 * that conceptually should return results.</p>
 * 
 * <h2>Usage Layer:</h2>
 * <ul>
 *   <li><b>Service Layer:</b> Can be thrown by {@code ProductService} when search/filter operations return empty results</li>
 *   <li><b>Controller Layer:</b> Handled by {@code GlobalExceptionHandler} and returned as HTTP 404 Not Found</li>
 * </ul>
 * 
 * <h2>HTTP Response:</h2>
 * <ul>
 *   <li><b>Status Code:</b> 404 Not Found</li>
 *   <li><b>Error Code:</b> EMPTY_RESULT</li>
 *   <li><b>Response Body:</b> ErrorResponse with query details and helpful message</li>
 * </ul>
 * 
 * <h2>Typical Use Cases:</h2>
 * <ul>
 *   <li>Search queries that return no matching products</li>
 *   <li>Filter operations with no results (when expecting at least one)</li>
 *   <li>Specification lookups that don't match any products</li>
 *   <li>Aggregate queries that return empty collections</li>
 * </ul>
 * 
 * <h2>Design Decision:</h2>
 * <p><strong>Note:</strong> In the current implementation, most search and filter operations
 * return empty lists rather than throwing this exception. This follows REST best practices
 * where empty results are valid responses. This exception is available for future use cases
 * where empty results should be treated as errors (e.g., required aggregations).</p>
 * 
 * <h2>Empty Result vs Not Found:</h2>
 * <ul>
 *   <li><b>ProductNotFoundException:</b> Single item lookup by ID fails</li>
 *   <li><b>CategoryNotFoundException:</b> Category doesn't exist in the system</li>
 *   <li><b>EmptyResultException:</b> Query executes successfully but returns no items</li>
 * </ul>
 * 
 * <h2>Example Usage:</h2>
 * <pre>{@code
 * // Current approach (returns empty list)
 * public List<Product> searchByName(String query) {
 *     return productRepository.findAll().stream()
 *         .filter(p -> p.getName().toLowerCase().contains(query.toLowerCase()))
 *         .toList(); // Returns empty list if no matches
 * }
 * 
 * // Alternative approach with exception (if empty results should be errors)
 * public List<Product> searchByNameStrict(String query) {
 *     List<Product> results = productRepository.findAll().stream()
 *         .filter(p -> p.getName().toLowerCase().contains(query.toLowerCase()))
 *         .toList();
 *     
 *     if (results.isEmpty()) {
 *         throw new EmptyResultException(
 *             "No products found matching search query", query);
 *     }
 *     return results;
 * }
 * }</pre>
 * 
 * <h2>When to Use:</h2>
 * <ul>
 *   <li>When empty results indicate a problem rather than a valid state</li>
 *   <li>When the business logic requires at least one result</li>
 *   <li>When you want to differentiate "no matches" from "successful query with no results"</li>
 *   <li>When client needs explicit notification that query had no results</li>
 * </ul>
 * 
 * <h2>When NOT to Use:</h2>
 * <ul>
 *   <li>Regular search/filter operations (return empty list instead)</li>
 *   <li>Optional results that may legitimately be empty</li>
 *   <li>Pagination where last page might be empty</li>
 *   <li>Count operations that can validly return zero</li>
 * </ul>
 * 
 * <h2>Client Actions:</h2>
 * <ul>
 *   <li>Broaden search criteria</li>
 *   <li>Check for typos in search terms</li>
 *   <li>Try different filter combinations</li>
 *   <li>Verify the query parameters are correct</li>
 * </ul>
 * 
 * @see ProductNotFoundException
 * @see CategoryNotFoundException
 * @see com.example.productcomparison.exception.GlobalExceptionHandler#handleEmptyResultException
 * @since 1.0
 * @author Product Comparison API Team
 */
@Getter
public class EmptyResultException extends RuntimeException {
    
    private final String query;
    
    /**
     * Constructs a new EmptyResultException with a general message.
     * 
     * <p>Use this constructor when the query details are included in the message
     * or when query information is not available.</p>
     * 
     * @param message a descriptive message explaining why empty results are unexpected
     */
    public EmptyResultException(String message) {
        super(message);
        this.query = null;
    }
    
    /**
     * Constructs a new EmptyResultException with a message and the query that produced no results.
     * 
     * <p>Use this constructor to capture both the error context and the specific query
     * that produced empty results, which helps with debugging and user feedback.</p>
     * 
     * @param message a descriptive message explaining why empty results are unexpected
     * @param query the search query or filter criteria that produced no results
     */
    public EmptyResultException(String message, String query) {
        super(message);
        this.query = query;
    }

}
