package com.example.productcomparison.acceptance;

import com.example.productcomparison.model.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

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
