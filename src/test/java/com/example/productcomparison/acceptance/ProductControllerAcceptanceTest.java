package com.example.productcomparison.acceptance;

import com.example.productcomparison.model.CreateProductRequest;
import com.example.productcomparison.model.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DisplayName("ProductController Acceptance Tests with JWT Security")
public class ProductControllerAcceptanceTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private AuthTestHelper authHelper;

    @Test
    @DisplayName("GET /api/products should return 401 without authentication")
    void listProducts_shouldReturn401_withoutAuth() {
        ResponseEntity<Object> response = restTemplate.getForEntity("/api/products", Object.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    @DisplayName("GET /api/products should return all products with admin token")
    void listProducts_shouldReturnAllProducts_withAdminAuth() {
        HttpEntity<Void> entity = new HttpEntity<>(authHelper.getAdminHeaders());
        ResponseEntity<Product[]> response = restTemplate.exchange(
                "/api/products",
                HttpMethod.GET,
                entity,
                Product[].class
        );
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().length).isEqualTo(38);
    }

    @Test
    @DisplayName("GET /api/products should return all products with user token")
    void listProducts_shouldReturnAllProducts_withUserAuth() {
        HttpEntity<Void> entity = new HttpEntity<>(authHelper.getUserHeaders());
        ResponseEntity<Product[]> response = restTemplate.exchange(
                "/api/products",
                HttpMethod.GET,
                entity,
                Product[].class
        );
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().length).isEqualTo(38);
    }

    @Test
    @DisplayName("GET /api/products/{id} should return a product when found with authentication")
    void getProduct_shouldReturnProduct_whenFound() {
        HttpEntity<Void> entity = new HttpEntity<>(authHelper.getAdminHeaders());
        ResponseEntity<Product> response = restTemplate.exchange(
                "/api/products/laptop-001",
                HttpMethod.GET,
                entity,
                Product.class
        );
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getId()).isEqualTo("laptop-001");
    }

    @Test
    @DisplayName("GET /api/products/{id} should return 404 when not found")
    void getProduct_shouldReturnNotFound_whenNotFound() {
        HttpEntity<Void> entity = new HttpEntity<>(authHelper.getAdminHeaders());
        ResponseEntity<Object> response = restTemplate.exchange(
                "/api/products/non-existent-id",
                HttpMethod.GET,
                entity,
                Object.class
        );
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    @DisplayName("POST /api/products should return 401 without authentication")
    void createProduct_shouldReturn401_withoutAuth() {
        Map<String, String> specs = new HashMap<>();
        specs.put("category", "Laptops");

        CreateProductRequest request = CreateProductRequest.builder()
                .id("test-no-auth")
                .name("Test Product")
                .price(299.99)
                .rating(4.5)
                .specifications(specs)
                .build();

        ResponseEntity<Object> response = restTemplate.postForEntity("/api/products", request, Object.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    @DisplayName("POST /api/products should return 403 with user token (insufficient permissions)")
    void createProduct_shouldReturn403_withUserAuth() {
        Map<String, String> specs = new HashMap<>();
        specs.put("category", "Laptops");

        CreateProductRequest request = CreateProductRequest.builder()
                .id("test-user-forbidden")
                .name("Test Product")
                .price(299.99)
                .rating(4.5)
                .specifications(specs)
                .build();

        HttpEntity<CreateProductRequest> entity = new HttpEntity<>(request, authHelper.getUserHeaders());
        ResponseEntity<Object> response = restTemplate.exchange(
                "/api/products",
                HttpMethod.POST,
                entity,
                Object.class
        );
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }

    @Test
    @DisplayName("POST /api/products should create a new product with admin token")
    void createProduct_shouldCreateNewProduct_withAdminAuth() {
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

        HttpEntity<CreateProductRequest> entity = new HttpEntity<>(request, authHelper.getAdminHeaders());
        ResponseEntity<Product> response = restTemplate.exchange(
                "/api/products",
                HttpMethod.POST,
                entity,
                Product.class
        );
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getId()).isEqualTo("acceptance-test-001");
        assertThat(response.getBody().getName()).isEqualTo("Acceptance Test Product");
    }

    @Test
    @DisplayName("POST /api/products/generate should generate a random product with admin token")
    void generateRandomProduct_shouldGenerateProduct() {
        HttpEntity<Void> entity = new HttpEntity<>(authHelper.getAdminHeaders());
        ResponseEntity<Product> response = restTemplate.exchange(
                "/api/products/generate",
                HttpMethod.POST,
                entity,
                Product.class
        );
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getId()).isNotNull();
        assertThat(response.getBody().getName()).isNotNull();
        assertThat(response.getBody().getPrice()).isGreaterThan(0);
    }

    @Test
    @DisplayName("POST /api/products/generate should return 403 with user token")
    void generateRandomProduct_shouldReturn403_withUserAuth() {
        HttpEntity<Void> entity = new HttpEntity<>(authHelper.getUserHeaders());
        ResponseEntity<Object> response = restTemplate.exchange(
                "/api/products/generate",
                HttpMethod.POST,
                entity,
                Object.class
        );
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }

    @Test
    @DisplayName("PUT /api/products/{id} should update existing product with admin token")
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
        
        HttpEntity<CreateProductRequest> createEntity = new HttpEntity<>(createRequest, authHelper.getAdminHeaders());
        restTemplate.exchange("/api/products", HttpMethod.POST, createEntity, Product.class);

        // Now update it
        CreateProductRequest updateRequest = CreateProductRequest.builder()
                .name("Updated Name")
                .price(249.99)
                .rating(4.5)
                .specifications(specs)
                .build();

        HttpEntity<CreateProductRequest> updateEntity = new HttpEntity<>(updateRequest, authHelper.getAdminHeaders());
        ResponseEntity<Product> response = restTemplate.exchange(
                "/api/products/update-test-001",
                HttpMethod.PUT,
                updateEntity,
                Product.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getName()).isEqualTo("Updated Name");
        assertThat(response.getBody().getPrice()).isEqualTo(249.99);
    }

    @Test
    @DisplayName("PUT /api/products/{id} should return 403 with user token")
    void updateProduct_shouldReturn403_withUserAuth() {
        Map<String, String> specs = new HashMap<>();
        specs.put("category", "Laptops");
        
        CreateProductRequest updateRequest = CreateProductRequest.builder()
                .name("Updated Name")
                .price(249.99)
                .rating(4.5)
                .specifications(specs)
                .build();

        HttpEntity<CreateProductRequest> entity = new HttpEntity<>(updateRequest, authHelper.getUserHeaders());
        ResponseEntity<Object> response = restTemplate.exchange(
                "/api/products/laptop-001",
                HttpMethod.PUT,
                entity,
                Object.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }

    @Test
    @DisplayName("DELETE /api/products/{id} should delete existing product with admin token")
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
        
        HttpEntity<CreateProductRequest> createEntity = new HttpEntity<>(createRequest, authHelper.getAdminHeaders());
        restTemplate.exchange("/api/products", HttpMethod.POST, createEntity, Product.class);

        // Now delete it
        HttpEntity<Void> deleteEntity = new HttpEntity<>(authHelper.getAdminHeaders());
        ResponseEntity<Void> response = restTemplate.exchange(
                "/api/products/delete-test-002",
                HttpMethod.DELETE,
                deleteEntity,
                Void.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    @DisplayName("DELETE /api/products/{id} should return 403 with user token")
    void deleteProduct_shouldReturn403_withUserAuth() {
        HttpEntity<Void> entity = new HttpEntity<>(authHelper.getUserHeaders());
        ResponseEntity<Object> response = restTemplate.exchange(
                "/api/products/laptop-001",
                HttpMethod.DELETE,
                entity,
                Object.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }

    @Test
    @DisplayName("GET /api/products/search should return matching products with authentication")
    void searchProducts_shouldReturnMatchingProducts() {
        HttpEntity<Void> entity = new HttpEntity<>(authHelper.getUserHeaders());
        ResponseEntity<Product[]> response = restTemplate.exchange(
                "/api/products/search?q=gaming",
                HttpMethod.GET,
                entity,
                Product[].class
        );
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().length).isGreaterThan(0);
    }

    @Test
    @DisplayName("GET /api/products/filter/price should return products in range with authentication")
    void filterByPrice_shouldReturnProductsInRange() {
        HttpEntity<Void> entity = new HttpEntity<>(authHelper.getUserHeaders());
        ResponseEntity<Product[]> response = restTemplate.exchange(
                "/api/products/filter/price?min=1000&max=1200",
                HttpMethod.GET,
                entity,
                Product[].class
        );
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
    }

    @Test
    @DisplayName("GET /api/products/filter/rating should return products above rating with authentication")
    void filterByRating_shouldReturnProductsAboveRating() {
        HttpEntity<Void> entity = new HttpEntity<>(authHelper.getUserHeaders());
        ResponseEntity<Product[]> response = restTemplate.exchange(
                "/api/products/filter/rating?min=4.8",
                HttpMethod.GET,
                entity,
                Product[].class
        );
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
    }

    @Test
    @DisplayName("GET /api/products/filter/category/{category} should return products in category with authentication")
    void filterByCategory_shouldReturnProductsInCategory() {
        HttpEntity<Void> entity = new HttpEntity<>(authHelper.getUserHeaders());
        ResponseEntity<Product[]> response = restTemplate.exchange(
                "/api/products/filter/category/Laptops",
                HttpMethod.GET,
                entity,
                Product[].class
        );
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
    }

    @Test
    @DisplayName("GET /api/products/categories should return all categories with authentication")
    void getAllCategories_shouldReturnAllCategories() {
        HttpEntity<Void> entity = new HttpEntity<>(authHelper.getUserHeaders());
        ResponseEntity<String[]> response = restTemplate.exchange(
                "/api/products/categories",
                HttpMethod.GET,
                entity,
                String[].class
        );
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().length).isEqualTo(13);
    }

    @Test
    @DisplayName("GET /api/products/compare should return products to compare with authentication")
    void compareProducts_shouldReturnProductsToCompare() {
        HttpEntity<Void> entity = new HttpEntity<>(authHelper.getUserHeaders());
        ResponseEntity<Product[]> response = restTemplate.exchange(
                "/api/products/compare?ids=laptop-001,laptop-002",
                HttpMethod.GET,
                entity,
                Product[].class
        );
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().length).isEqualTo(2);
    }

    @Test
    @DisplayName("GET /api/products/sort/price should return products sorted by price with authentication")
    void sortByPrice_shouldReturnProductsSortedByPrice() {
        HttpEntity<Void> entity = new HttpEntity<>(authHelper.getUserHeaders());
        ResponseEntity<Product[]> response = restTemplate.exchange(
                "/api/products/sort/price?order=asc",
                HttpMethod.GET,
                entity,
                Product[].class
        );
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
    }

    @Test
    @DisplayName("GET /api/products/sort/rating should return products sorted by rating with authentication")
    void sortByRating_shouldReturnProductsSortedByRating() {
        HttpEntity<Void> entity = new HttpEntity<>(authHelper.getUserHeaders());
        ResponseEntity<Product[]> response = restTemplate.exchange(
                "/api/products/sort/rating?order=desc",
                HttpMethod.GET,
                entity,
                Product[].class
        );
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
    }

    @Test
    @DisplayName("GET /api/products/top should return top N products with authentication")
    void getTopRatedProducts_shouldReturnTopNProducts() {
        HttpEntity<Void> entity = new HttpEntity<>(authHelper.getUserHeaders());
        ResponseEntity<Product[]> response = restTemplate.exchange(
                "/api/products/top?limit=5",
                HttpMethod.GET,
                entity,
                Product[].class
        );
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().length).isEqualTo(5);
    }

    @Test
    @DisplayName("GET /api/products/filter/spec should return matching products with authentication")
    void findBySpecification_shouldReturnMatchingProducts() {
        HttpEntity<Void> entity = new HttpEntity<>(authHelper.getUserHeaders());
        ResponseEntity<Product[]> response = restTemplate.exchange(
                "/api/products/filter/spec?key=ram&value=16GB",
                HttpMethod.GET,
                entity,
                Product[].class
        );
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
    }
}
