package com.example.productcomparison.acceptance;

import com.example.productcomparison.model.CreateProductRequest;
import com.example.productcomparison.model.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DisplayName("ProductController Acceptance Tests")
public class ProductControllerAcceptanceTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    @DisplayName("GET /api/products should return all products")
    void listProducts_shouldReturnAllProducts() {
        ResponseEntity<Product[]> response = restTemplate.getForEntity("/api/products", Product[].class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().length).isEqualTo(38);
    }

    @Test
    @DisplayName("GET /api/products/{id} should return a product when found")
    void getProduct_shouldReturnProduct_whenFound() {
        ResponseEntity<Product> response = restTemplate.getForEntity("/api/products/laptop-001", Product.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getId()).isEqualTo("laptop-001");
    }

    @Test
    @DisplayName("GET /api/products/{id} should return 404 when not found")
    void getProduct_shouldReturnNotFound_whenNotFound() {
        ResponseEntity<Object> response = restTemplate.getForEntity("/api/products/non-existent-id", Object.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    @DisplayName("POST /api/products should create a new product")
    void createProduct_shouldCreateNewProduct() {
        Map<String, String> specs = new HashMap<>();
        specs.put("category", "Laptops");
        specs.put("brand", "TestBrand");

        CreateProductRequest request = CreateProductRequest.builder()
                .id("acceptance-test-001")
                .name("Acceptance Test Product")
                .imageUrl("https://example.com/test.jpg")
                .description("Test description")
                .price(299.99)
                .rating(4.5)
                .specifications(specs)
                .build();

        ResponseEntity<Product> response = restTemplate.postForEntity("/api/products", request, Product.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getId()).isEqualTo("acceptance-test-001");
        assertThat(response.getBody().getName()).isEqualTo("Acceptance Test Product");
    }

    @Test
    @DisplayName("POST /api/products/generate should generate a random product")
    void generateRandomProduct_shouldGenerateProduct() {
        ResponseEntity<Product> response = restTemplate.postForEntity("/api/products/generate", null, Product.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getId()).isNotNull();
        assertThat(response.getBody().getName()).isNotNull();
        assertThat(response.getBody().getPrice()).isGreaterThan(0);
    }

    @Test
    @DisplayName("PUT /api/products/{id} should update existing product")
    void updateProduct_shouldUpdateExistingProduct() {
        // First, create a product
        Map<String, String> specs = new HashMap<>();
        specs.put("category", "Laptops");
        
        CreateProductRequest createRequest = CreateProductRequest.builder()
                .id("update-test-001")
                .name("Original Name")
                .price(199.99)
                .rating(4.0)
                .specifications(specs)
                .build();
        
        restTemplate.postForEntity("/api/products", createRequest, Product.class);

        // Now update it
        CreateProductRequest updateRequest = CreateProductRequest.builder()
                .name("Updated Name")
                .price(249.99)
                .rating(4.5)
                .specifications(specs)
                .build();

        HttpEntity<CreateProductRequest> entity = new HttpEntity<>(updateRequest);
        ResponseEntity<Product> response = restTemplate.exchange(
                "/api/products/update-test-001",
                HttpMethod.PUT,
                entity,
                Product.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getName()).isEqualTo("Updated Name");
        assertThat(response.getBody().getPrice()).isEqualTo(249.99);
    }

    @Test
    @DisplayName("DELETE /api/products/{id} should delete existing product")
    void deleteProduct_shouldDeleteExistingProduct() {
        // First, create a product to delete
        Map<String, String> specs = new HashMap<>();
        specs.put("category", "Laptops");
        
        CreateProductRequest createRequest = CreateProductRequest.builder()
                .id("delete-test-002")
                .name("To Delete")
                .price(99.99)
                .rating(4.0)
                .specifications(specs)
                .build();
        
        restTemplate.postForEntity("/api/products", createRequest, Product.class);

        // Now delete it
        ResponseEntity<Void> response = restTemplate.exchange(
                "/api/products/delete-test-002",
                HttpMethod.DELETE,
                null,
                Void.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    @DisplayName("GET /api/products/search should return matching products")
    void searchProducts_shouldReturnMatchingProducts() {
        ResponseEntity<Product[]> response = restTemplate.getForEntity("/api/products/search?q=gaming", Product[].class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().length).isGreaterThan(0);
    }

    @Test
    @DisplayName("GET /api/products/filter/price should return products in range")
    void filterByPrice_shouldReturnProductsInRange() {
        ResponseEntity<Product[]> response = restTemplate.getForEntity("/api/products/filter/price?min=1000&max=1200", Product[].class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
    }

    @Test
    @DisplayName("GET /api/products/filter/rating should return products above rating")
    void filterByRating_shouldReturnProductsAboveRating() {
        ResponseEntity<Product[]> response = restTemplate.getForEntity("/api/products/filter/rating?min=4.8", Product[].class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
    }

    @Test
    @DisplayName("GET /api/products/filter/category/{category} should return products in category")
    void filterByCategory_shouldReturnProductsInCategory() {
        ResponseEntity<Product[]> response = restTemplate.getForEntity("/api/products/filter/category/Laptops", Product[].class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
    }

    @Test
    @DisplayName("GET /api/products/categories should return all categories")
    void getAllCategories_shouldReturnAllCategories() {
        ResponseEntity<String[]> response = restTemplate.getForEntity("/api/products/categories", String[].class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().length).isEqualTo(13);
    }

    @Test
    @DisplayName("GET /api/products/compare should return products to compare")
    void compareProducts_shouldReturnProductsToCompare() {
        ResponseEntity<Product[]> response = restTemplate.getForEntity("/api/products/compare?ids=laptop-001,laptop-002", Product[].class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().length).isEqualTo(2);
    }

    @Test
    @DisplayName("GET /api/products/sort/price should return products sorted by price")
    void sortByPrice_shouldReturnProductsSortedByPrice() {
        ResponseEntity<Product[]> response = restTemplate.getForEntity("/api/products/sort/price?order=asc", Product[].class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
    }

    @Test
    @DisplayName("GET /api/products/sort/rating should return products sorted by rating")
    void sortByRating_shouldReturnProductsSortedByRating() {
        ResponseEntity<Product[]> response = restTemplate.getForEntity("/api/products/sort/rating?order=desc", Product[].class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
    }

    @Test
    @DisplayName("GET /api/products/top should return top N products")
    void getTopRatedProducts_shouldReturnTopNProducts() {
        ResponseEntity<Product[]> response = restTemplate.getForEntity("/api/products/top?limit=5", Product[].class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().length).isEqualTo(5);
    }

    @Test
    @DisplayName("GET /api/products/filter/spec should return matching products")
    void findBySpecification_shouldReturnMatchingProducts() {
        ResponseEntity<Product[]> response = restTemplate.getForEntity("/api/products/filter/spec?key=ram&value=16GB", Product[].class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
    }
}
