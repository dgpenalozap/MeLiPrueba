package com.example.productcomparison.controller;

import com.example.productcomparison.exception.ErrorResponse;
import com.example.productcomparison.model.CreateProductRequest;
import com.example.productcomparison.model.Product;
import com.example.productcomparison.service.IProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for product endpoints.
 * Following Single Responsibility Principle (SRP) - only handles HTTP concerns.
 * Depends on IProductService interface (DIP).
 *
 * @RequiredArgsConstructor generates constructor with final fields (Dependency Injection)
 */
@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
@Tag(name = "Products", description = "Product management and comparison API")
public class ProductController {

    private final IProductService productService;

    @Operation(
            summary = "Get all products",
            description = "Retrieves a list of all available products (45 products across 13 categories)"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved list of products",
                    content = @Content(schema = @Schema(implementation = Product.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping
    public ResponseEntity<List<Product>> listProducts() {
        List<Product> products = productService.getAllProducts();
        return ResponseEntity.ok(products);
    }

    @Operation(
            summary = "Create a new product",
            description = "Creates a new product with the provided data"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Product created successfully",
                    content = @Content(schema = @Schema(implementation = Product.class))),
            @ApiResponse(responseCode = "400", description = "Invalid product data",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping
    public ResponseEntity<Product> createProduct(@Valid @RequestBody CreateProductRequest request) {
        Product product = Product.builder()
                .id(request.getId())
                .name(request.getName())
                .imageUrl(request.getImageUrl())
                .description(request.getDescription())
                .price(request.getPrice())
                .rating(request.getRating())
                .specifications(request.getSpecifications())
                .build();
        Product created = productService.createProduct(product);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @Operation(
            summary = "Generate a random product using AI",
            description = "Generates and saves a random product using AI (LangChain4j). Falls back to random generation if OpenAI API key is not configured."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Random product generated and created successfully",
                    content = @Content(schema = @Schema(implementation = Product.class))),
            @ApiResponse(responseCode = "500", description = "Failed to generate product",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("/generate")
    public ResponseEntity<Product> generateRandomProduct() {
        Product generated = productService.generateRandomProduct();
        return ResponseEntity.status(HttpStatus.CREATED).body(generated);
    }

    @Operation(
            summary = "Update an existing product",
            description = "Updates an existing product with the provided data"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product updated successfully",
                    content = @Content(schema = @Schema(implementation = Product.class))),
            @ApiResponse(responseCode = "400", description = "Invalid product data",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Product not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(
            @Parameter(description = "Product ID to update", required = true, example = "laptop-001")
            @PathVariable String id,
            @Valid @RequestBody CreateProductRequest request) {
        Product product = Product.builder()
                .name(request.getName())
                .imageUrl(request.getImageUrl())
                .description(request.getDescription())
                .price(request.getPrice())
                .rating(request.getRating())
                .specifications(request.getSpecifications())
                .build();
        Product updated = productService.updateProduct(id, product);
        return ResponseEntity.ok(updated);
    }

    @Operation(
            summary = "Delete a product",
            description = "Deletes a product by its ID"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Product deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Product not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(
            @Parameter(description = "Product ID to delete", required = true, example = "laptop-001")
            @PathVariable String id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Get product by ID",
            description = "Retrieves detailed information about a specific product"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product found",
                    content = @Content(schema = @Schema(implementation = Product.class))),
            @ApiResponse(responseCode = "400", description = "Invalid product ID",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Product not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<Product> getProduct(
            @Parameter(description = "Product ID (e.g., laptop-001, phone-001)", required = true, example = "laptop-001")
            @PathVariable String id) {
        Product product = productService.getProductById(id);
        return ResponseEntity.ok(product);
    }

    @Operation(
            summary = "Search products by name",
            description = "Performs a case-insensitive partial match search on product names"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Search completed successfully",
                    content = @Content(schema = @Schema(implementation = Product.class))),
            @ApiResponse(responseCode = "400", description = "Invalid or empty search query",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/search")
    public ResponseEntity<List<Product>> searchProducts(
            @Parameter(description = "Search query (e.g., 'laptop', 'gaming', 'pro')", required = true, example = "laptop")
            @RequestParam("q") String query) {
        List<Product> products = productService.searchByName(query);
        return ResponseEntity.ok(products);
    }

    @Operation(
            summary = "Filter products by price range",
            description = "Returns products within the specified price range (inclusive)"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Filter applied successfully",
                    content = @Content(schema = @Schema(implementation = Product.class))),
            @ApiResponse(responseCode = "400", description = "Invalid price range (negative values, min > max, etc.)",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/filter/price")
    public ResponseEntity<List<Product>> filterByPrice(
            @Parameter(description = "Minimum price in USD (must be >= 0)", required = true, example = "100")
            @RequestParam("min") double minPrice,
            @Parameter(description = "Maximum price in USD (must be >= min)", required = true, example = "500")
            @RequestParam("max") double maxPrice) {
        List<Product> products = productService.filterByPriceRange(minPrice, maxPrice);
        return ResponseEntity.ok(products);
    }

    @Operation(
            summary = "Filter products by minimum rating",
            description = "Returns products with rating equal to or greater than the specified value"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Filter applied successfully",
                    content = @Content(schema = @Schema(implementation = Product.class))),
            @ApiResponse(responseCode = "400", description = "Invalid rating value (must be between 0.0 and 5.0)",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/filter/rating")
    public ResponseEntity<List<Product>> filterByRating(
            @Parameter(description = "Minimum rating (0.0 - 5.0)", required = true, example = "4.5")
            @RequestParam("min") double minRating) {
        List<Product> products = productService.filterByRating(minRating);
        return ResponseEntity.ok(products);
    }

    @Operation(
            summary = "Filter products by category",
            description = "Returns all products in the specified category. Available categories: Laptops, Smartphones, Tablets, Monitors, Keyboards, Mice, Headphones, Cameras, Smartwatches, Speakers, Networking, Storage, Printers"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Filter applied successfully",
                    content = @Content(schema = @Schema(implementation = Product.class))),
            @ApiResponse(responseCode = "400", description = "Invalid or empty category name",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Category not found - use /categories to see available categories",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/filter/category/{category}")
    public ResponseEntity<List<Product>> filterByCategory(
            @Parameter(description = "Category name (case-insensitive)", required = true, example = "Laptops")
            @PathVariable String category) {
        List<Product> products = productService.filterByCategory(category);
        return ResponseEntity.ok(products);
    }

    @Operation(
            summary = "Get all product categories",
            description = "Returns a list of all unique product categories available in the database"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Categories retrieved successfully")
    })
    @GetMapping("/categories")
    public ResponseEntity<List<String>> getAllCategories() {
        List<String> categories = productService.getAllCategories();
        return ResponseEntity.ok(categories);
    }

    @Operation(
            summary = "Compare multiple products",
            description = "Retrieves detailed information for multiple products to enable side-by-side comparison. Maximum 10 products at once."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Products retrieved for comparison",
                    content = @Content(schema = @Schema(implementation = Product.class))),
            @ApiResponse(responseCode = "400", description = "Invalid product IDs list (empty, too many, etc.)",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "One or more products not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/compare")
    public ResponseEntity<List<Product>> compareProducts(
            @Parameter(description = "Comma-separated list of product IDs (max 10)", required = true,
                    example = "laptop-001,laptop-002,laptop-003")
            @RequestParam("ids") List<String> productIds) {
        List<Product> products = productService.compareProducts(productIds);
        return ResponseEntity.ok(products);
    }

    @Operation(
            summary = "Sort products by price",
            description = "Returns all products sorted by price in ascending or descending order"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Products sorted successfully",
                    content = @Content(schema = @Schema(implementation = Product.class)))
    })
    @GetMapping("/sort/price")
    public ResponseEntity<List<Product>> sortByPrice(
            @Parameter(description = "Sort order: 'asc' for ascending, 'desc' for descending",
                    example = "asc")
            @RequestParam(value = "order", defaultValue = "asc") String order) {
        boolean ascending = "asc".equalsIgnoreCase(order);
        List<Product> products = productService.sortByPrice(ascending);
        return ResponseEntity.ok(products);
    }

    @Operation(
            summary = "Sort products by rating",
            description = "Returns all products sorted by rating in ascending or descending order"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Products sorted successfully",
                    content = @Content(schema = @Schema(implementation = Product.class)))
    })
    @GetMapping("/sort/rating")
    public ResponseEntity<List<Product>> sortByRating(
            @Parameter(description = "Sort order: 'asc' for ascending, 'desc' for descending",
                    example = "desc")
            @RequestParam(value = "order", defaultValue = "desc") String order) {
        boolean ascending = "asc".equalsIgnoreCase(order);
        List<Product> products = productService.sortByRating(ascending);
        return ResponseEntity.ok(products);
    }

    @Operation(
            summary = "Get top-rated products",
            description = "Returns the top N products sorted by rating in descending order. Maximum limit is 100."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Top products retrieved successfully",
                    content = @Content(schema = @Schema(implementation = Product.class))),
            @ApiResponse(responseCode = "400", description = "Invalid limit value (must be positive, max 100)",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/top")
    public ResponseEntity<List<Product>> getTopRatedProducts(
            @Parameter(description = "Number of products to return (1-100)", example = "10")
            @RequestParam(value = "limit", defaultValue = "10") int limit) {
        List<Product> products = productService.getTopRatedProducts(limit);
        return ResponseEntity.ok(products);
    }

    @Operation(
            summary = "Filter products by specification",
            description = "Returns products that match a specific specification key-value pair. Common keys: processor, ram, storage, screen, battery, category, etc."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Filter applied successfully",
                    content = @Content(schema = @Schema(implementation = Product.class))),
            @ApiResponse(responseCode = "400", description = "Invalid or empty specification key/value",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/filter/spec")
    public ResponseEntity<List<Product>> findBySpecification(
            @Parameter(description = "Specification key (e.g., 'processor', 'ram')", required = true, example = "processor")
            @RequestParam("key") String specKey,
            @Parameter(description = "Specification value to match (case-insensitive)", required = true,
                    example = "Intel Core i7-12700H")
            @RequestParam("value") String specValue) {
        List<Product> products = productService.findBySpecification(specKey, specValue);
        return ResponseEntity.ok(products);
    }
}

