package com.example.productcomparison.acceptance;

import com.example.productcomparison.exception.ErrorResponse;
import com.example.productcomparison.model.CreateProductRequest;
import com.example.productcomparison.model.Product;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DisplayName("ProductController Acceptance Tests with JWT Security")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ProductControllerAcceptanceTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private AuthTestHelper authHelper;

    private static final String BASE_URL = "/api/products";
    private static String testProductId;

    // ===================== AUTHENTICATION TESTS =====================

    @Test
    @Order(1)
    @DisplayName("Should return 401 when accessing without JWT token")
    void testAccessWithoutToken() {
        ResponseEntity<String> response = restTemplate.getForEntity(BASE_URL, String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    @Order(2)
    @DisplayName("Should return 401 when using invalid JWT token")
    void testAccessWithInvalidToken() {
        HttpHeaders headers = authHelper.getHeadersWithToken("invalid-token-12345");
        HttpEntity<Void> entity = new HttpEntity<>(headers);
        
        ResponseEntity<String> response = restTemplate.exchange(
            BASE_URL, HttpMethod.GET, entity, String.class
        );
        
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    @Order(3)
    @DisplayName("Should return 401 when using malformed JWT token")
    void testAccessWithMalformedToken() {
        // Use a malformed JWT token (not properly formatted)
        String malformedToken = "eyJhbGciOiJIUzI1NiJ9.invalidpayload.invalidsignature";
        HttpHeaders headers = authHelper.getHeadersWithToken(malformedToken);
        HttpEntity<Void> entity = new HttpEntity<>(headers);
        
        ResponseEntity<String> response = restTemplate.exchange(
            BASE_URL, HttpMethod.GET, entity, String.class
        );
        
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    // ===================== GET ALL PRODUCTS TESTS =====================

    @Test
    @Order(10)
    @DisplayName("Should get all products with valid user token - 200 OK")
    void testGetAllProductsWithUserToken() {
        HttpEntity<Void> entity = new HttpEntity<>(authHelper.getUserHeaders());
        
        ResponseEntity<List> response = restTemplate.exchange(
            BASE_URL, HttpMethod.GET, entity, List.class
        );
        
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody()).isNotEmpty();
    }

    @Test
    @Order(11)
    @DisplayName("Should get all products with valid admin token - 200 OK")
    void testGetAllProductsWithAdminToken() {
        HttpEntity<Void> entity = new HttpEntity<>(authHelper.getAdminHeaders());
        
        ResponseEntity<Product[]> response = restTemplate.exchange(
            BASE_URL, HttpMethod.GET, entity, Product[].class
        );
        
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().length).isGreaterThan(0);
    }

    // ===================== CREATE PRODUCT TESTS =====================

    @Test
    @Order(20)
    @DisplayName("Should create product with valid data - 201 CREATED")
    void testCreateProductSuccess() {
        Map<String, String> specs = new HashMap<>();
        specs.put("category", "Laptops");
        specs.put("processor", "Test Processor");
        
        CreateProductRequest request = CreateProductRequest.builder()
            .id("test-product-" + UUID.randomUUID().toString().substring(0, 8))
            .name("Test Product")
            .imageUrl("https://example.com/test.jpg")
            .description("Test Description")
            .price(999.99)
            .rating(4.5)
            .specifications(specs)
            .build();
        
        HttpEntity<CreateProductRequest> entity = new HttpEntity<>(request, authHelper.getAdminHeaders());
        
        ResponseEntity<Product> response = restTemplate.exchange(
            BASE_URL, HttpMethod.POST, entity, Product.class
        );
        
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getName()).isEqualTo("Test Product");
        testProductId = response.getBody().getId();
    }

    @Test
    @Order(21)
    @DisplayName("Should return 400 when creating product with blank ID - VALIDATION_ERROR")
    void testCreateProductBlankId() {
        CreateProductRequest request = CreateProductRequest.builder()
            .id("")
            .name("Test Product")
            .price(999.99)
            .rating(4.5)
            .build();
        
        HttpEntity<CreateProductRequest> entity = new HttpEntity<>(request, authHelper.getAdminHeaders());
        
        ResponseEntity<ErrorResponse> response = restTemplate.exchange(
            BASE_URL, HttpMethod.POST, entity, ErrorResponse.class
        );
        
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
    }

    @Test
    @Order(22)
    @DisplayName("Should return 400 when creating product with blank name - VALIDATION_ERROR")
    void testCreateProductBlankName() {
        CreateProductRequest request = CreateProductRequest.builder()
            .id("test-123")
            .name("")
            .price(999.99)
            .rating(4.5)
            .build();
        
        HttpEntity<CreateProductRequest> entity = new HttpEntity<>(request, authHelper.getAdminHeaders());
        
        ResponseEntity<ErrorResponse> response = restTemplate.exchange(
            BASE_URL, HttpMethod.POST, entity, ErrorResponse.class
        );
        
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    @Order(23)
    @DisplayName("Should return 400 when creating product with negative price - VALIDATION_ERROR")
    void testCreateProductNegativePrice() {
        CreateProductRequest request = CreateProductRequest.builder()
            .id("test-123")
            .name("Test Product")
            .price(-100.00)
            .rating(4.5)
            .build();
        
        HttpEntity<CreateProductRequest> entity = new HttpEntity<>(request, authHelper.getAdminHeaders());
        
        ResponseEntity<ErrorResponse> response = restTemplate.exchange(
            BASE_URL, HttpMethod.POST, entity, ErrorResponse.class
        );
        
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    @Order(24)
    @DisplayName("Should return 400 when creating product with rating > 5.0 - VALIDATION_ERROR")
    void testCreateProductRatingTooHigh() {
        CreateProductRequest request = CreateProductRequest.builder()
            .id("test-123")
            .name("Test Product")
            .price(999.99)
            .rating(6.0)
            .build();
        
        HttpEntity<CreateProductRequest> entity = new HttpEntity<>(request, authHelper.getAdminHeaders());
        
        ResponseEntity<ErrorResponse> response = restTemplate.exchange(
            BASE_URL, HttpMethod.POST, entity, ErrorResponse.class
        );
        
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    @Order(25)
    @DisplayName("Should return 400 when creating product with rating < 0.0 - VALIDATION_ERROR")
    void testCreateProductRatingTooLow() {
        CreateProductRequest request = CreateProductRequest.builder()
            .id("test-123")
            .name("Test Product")
            .price(999.99)
            .rating(-1.0)
            .build();
        
        HttpEntity<CreateProductRequest> entity = new HttpEntity<>(request, authHelper.getAdminHeaders());
        
        ResponseEntity<ErrorResponse> response = restTemplate.exchange(
            BASE_URL, HttpMethod.POST, entity, ErrorResponse.class
        );
        
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    @Order(26)
    @DisplayName("Should return 409 when creating product with duplicate ID - PRODUCT_ALREADY_EXISTS")
    void testCreateProductDuplicateId() {
        CreateProductRequest request = CreateProductRequest.builder()
            .id("laptop-001")
            .name("Duplicate Product")
            .price(999.99)
            .rating(4.5)
            .build();
        
        HttpEntity<CreateProductRequest> entity = new HttpEntity<>(request, authHelper.getAdminHeaders());
        
        ResponseEntity<ErrorResponse> response = restTemplate.exchange(
            BASE_URL, HttpMethod.POST, entity, ErrorResponse.class
        );
        
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
        assertThat(response.getBody().getErrorCode()).isEqualTo("PRODUCT_ALREADY_EXISTS");
    }

    // ===================== GENERATE RANDOM PRODUCT TESTS =====================

    @Test
    @Order(30)
    @DisplayName("Should generate random product - 201 CREATED")
    void testGenerateRandomProduct() {
        HttpEntity<Void> entity = new HttpEntity<>(authHelper.getAdminHeaders());
        
        ResponseEntity<Product> response = restTemplate.exchange(
            BASE_URL + "/generate", HttpMethod.POST, entity, Product.class
        );
        
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getId()).isNotNull();
    }

    // ===================== GET PRODUCT BY ID TESTS =====================

    @Test
    @Order(40)
    @DisplayName("Should get product by valid ID - 200 OK")
    void testGetProductByIdSuccess() {
        HttpEntity<Void> entity = new HttpEntity<>(authHelper.getUserHeaders());
        
        ResponseEntity<Product> response = restTemplate.exchange(
            BASE_URL + "/laptop-001", HttpMethod.GET, entity, Product.class
        );
        
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getId()).isEqualTo("laptop-001");
    }

    @Test
    @Order(41)
    @DisplayName("Should return 404 when product not found - PRODUCT_NOT_FOUND")
    void testGetProductByIdNotFound() {
        HttpEntity<Void> entity = new HttpEntity<>(authHelper.getUserHeaders());
        
        ResponseEntity<ErrorResponse> response = restTemplate.exchange(
            BASE_URL + "/non-existent-id", HttpMethod.GET, entity, ErrorResponse.class
        );
        
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody().getErrorCode()).isEqualTo("PRODUCT_NOT_FOUND");
    }

    @Test
    @Order(42)
    @DisplayName("Should return 400 when product ID is blank - INVALID_PARAMETER")
    void testGetProductByIdBlank() {
        HttpEntity<Void> entity = new HttpEntity<>(authHelper.getUserHeaders());
        
        ResponseEntity<ErrorResponse> response = restTemplate.exchange(
            BASE_URL + "/   ", HttpMethod.GET, entity, ErrorResponse.class
        );
        
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    // ===================== UPDATE PRODUCT TESTS =====================

    @Test
    @Order(50)
    @DisplayName("Should update product successfully - 200 OK")
    void testUpdateProductSuccess() {
        CreateProductRequest request = CreateProductRequest.builder()
            .id("laptop-001")
            .name("Updated Laptop Name")
            .price(1499.99)
            .rating(4.8)
            .build();
        
        HttpEntity<CreateProductRequest> entity = new HttpEntity<>(request, authHelper.getAdminHeaders());
        
        ResponseEntity<Product> response = restTemplate.exchange(
            BASE_URL + "/laptop-001", HttpMethod.PUT, entity, Product.class
        );
        
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getName()).isEqualTo("Updated Laptop Name");
    }

    @Test
    @Order(51)
    @DisplayName("Should return 404 when updating non-existent product - PRODUCT_NOT_FOUND")
    void testUpdateProductNotFound() {
        CreateProductRequest request = CreateProductRequest.builder()
            .id("non-existent")
            .name("Updated Name")
            .price(999.99)
            .rating(4.5)
            .build();
        
        HttpEntity<CreateProductRequest> entity = new HttpEntity<>(request, authHelper.getAdminHeaders());
        
        ResponseEntity<ErrorResponse> response = restTemplate.exchange(
            BASE_URL + "/non-existent-id", HttpMethod.PUT, entity, ErrorResponse.class
        );
        
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody().getErrorCode()).isEqualTo("PRODUCT_NOT_FOUND");
    }

    @Test
    @Order(52)
    @DisplayName("Should return 400 when updating with invalid data - VALIDATION_ERROR")
    void testUpdateProductInvalidData() {
        CreateProductRequest request = CreateProductRequest.builder()
            .id("laptop-001")
            .name("")
            .price(999.99)
            .rating(4.5)
            .build();
        
        HttpEntity<CreateProductRequest> entity = new HttpEntity<>(request, authHelper.getAdminHeaders());
        
        ResponseEntity<ErrorResponse> response = restTemplate.exchange(
            BASE_URL + "/laptop-001", HttpMethod.PUT, entity, ErrorResponse.class
        );
        
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    // ===================== SEARCH PRODUCTS TESTS =====================

    @Test
    @Order(60)
    @DisplayName("Should search products by name successfully - 200 OK")
    void testSearchProductsSuccess() {
        HttpEntity<Void> entity = new HttpEntity<>(authHelper.getUserHeaders());
        
        ResponseEntity<Product[]> response = restTemplate.exchange(
            BASE_URL + "/search?q=laptop", HttpMethod.GET, entity, Product[].class
        );
        
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
    }

    @Test
    @Order(61)
    @DisplayName("Should return 400 when search query is empty - INVALID_PARAMETER")
    void testSearchProductsEmptyQuery() {
        HttpEntity<Void> entity = new HttpEntity<>(authHelper.getUserHeaders());
        
        ResponseEntity<ErrorResponse> response = restTemplate.exchange(
            BASE_URL + "/search?q=", HttpMethod.GET, entity, ErrorResponse.class
        );
        
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    @Order(62)
    @DisplayName("Should return 400 when search query parameter is missing - MISSING_PARAMETER")
    void testSearchProductsMissingQuery() {
        HttpEntity<Void> entity = new HttpEntity<>(authHelper.getUserHeaders());
        
        ResponseEntity<ErrorResponse> response = restTemplate.exchange(
            BASE_URL + "/search", HttpMethod.GET, entity, ErrorResponse.class
        );
        
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody().getErrorCode()).isEqualTo("MISSING_PARAMETER");
    }

    // ===================== FILTER BY PRICE TESTS =====================

    @Test
    @Order(70)
    @DisplayName("Should filter products by price range successfully - 200 OK")
    void testFilterByPriceSuccess() {
        HttpEntity<Void> entity = new HttpEntity<>(authHelper.getUserHeaders());
        
        ResponseEntity<Product[]> response = restTemplate.exchange(
            BASE_URL + "/filter/price?min=100&max=500", HttpMethod.GET, entity, Product[].class
        );
        
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
    }

    @Test
    @Order(71)
    @DisplayName("Should return 400 when min price is negative - INVALID_PRICE_RANGE")
    void testFilterByPriceNegativeMin() {
        HttpEntity<Void> entity = new HttpEntity<>(authHelper.getUserHeaders());
        
        ResponseEntity<ErrorResponse> response = restTemplate.exchange(
            BASE_URL + "/filter/price?min=-100&max=500", HttpMethod.GET, entity, ErrorResponse.class
        );
        
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody().getErrorCode()).isEqualTo("INVALID_PRICE_RANGE");
    }

    @Test
    @Order(72)
    @DisplayName("Should return 400 when max price is negative - INVALID_PRICE_RANGE")
    void testFilterByPriceNegativeMax() {
        HttpEntity<Void> entity = new HttpEntity<>(authHelper.getUserHeaders());
        
        ResponseEntity<ErrorResponse> response = restTemplate.exchange(
            BASE_URL + "/filter/price?min=100&max=-500", HttpMethod.GET, entity, ErrorResponse.class
        );
        
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody().getErrorCode()).isEqualTo("INVALID_PRICE_RANGE");
    }

    @Test
    @Order(73)
    @DisplayName("Should return 400 when min > max - INVALID_PRICE_RANGE")
    void testFilterByPriceMinGreaterThanMax() {
        HttpEntity<Void> entity = new HttpEntity<>(authHelper.getUserHeaders());
        
        ResponseEntity<ErrorResponse> response = restTemplate.exchange(
            BASE_URL + "/filter/price?min=500&max=100", HttpMethod.GET, entity, ErrorResponse.class
        );
        
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody().getErrorCode()).isEqualTo("INVALID_PRICE_RANGE");
    }

    @Test
    @Order(74)
    @DisplayName("Should return 400 when price parameters are missing - MISSING_PARAMETER")
    void testFilterByPriceMissingParameters() {
        HttpEntity<Void> entity = new HttpEntity<>(authHelper.getUserHeaders());
        
        ResponseEntity<ErrorResponse> response = restTemplate.exchange(
            BASE_URL + "/filter/price?min=100", HttpMethod.GET, entity, ErrorResponse.class
        );
        
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody().getErrorCode()).isEqualTo("MISSING_PARAMETER");
    }

    @Test
    @Order(75)
    @DisplayName("Should return 400 when price is not a number - TYPE_MISMATCH")
    void testFilterByPriceInvalidType() {
        HttpEntity<Void> entity = new HttpEntity<>(authHelper.getUserHeaders());
        
        ResponseEntity<ErrorResponse> response = restTemplate.exchange(
            BASE_URL + "/filter/price?min=abc&max=500", HttpMethod.GET, entity, ErrorResponse.class
        );
        
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody().getErrorCode()).isEqualTo("TYPE_MISMATCH");
    }

    // ===================== FILTER BY RATING TESTS =====================

    @Test
    @Order(80)
    @DisplayName("Should filter products by rating successfully - 200 OK")
    void testFilterByRatingSuccess() {
        HttpEntity<Void> entity = new HttpEntity<>(authHelper.getUserHeaders());
        
        ResponseEntity<Product[]> response = restTemplate.exchange(
            BASE_URL + "/filter/rating?min=4.0", HttpMethod.GET, entity, Product[].class
        );
        
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
    }

    @Test
    @Order(81)
    @DisplayName("Should return 400 when rating is negative - INVALID_RATING")
    void testFilterByRatingNegative() {
        HttpEntity<Void> entity = new HttpEntity<>(authHelper.getUserHeaders());
        
        ResponseEntity<ErrorResponse> response = restTemplate.exchange(
            BASE_URL + "/filter/rating?min=-1.0", HttpMethod.GET, entity, ErrorResponse.class
        );
        
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody().getErrorCode()).isEqualTo("INVALID_RATING");
    }

    @Test
    @Order(82)
    @DisplayName("Should return 400 when rating > 5.0 - INVALID_RATING")
    void testFilterByRatingTooHigh() {
        HttpEntity<Void> entity = new HttpEntity<>(authHelper.getUserHeaders());
        
        ResponseEntity<ErrorResponse> response = restTemplate.exchange(
            BASE_URL + "/filter/rating?min=6.0", HttpMethod.GET, entity, ErrorResponse.class
        );
        
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody().getErrorCode()).isEqualTo("INVALID_RATING");
    }

    @Test
    @Order(83)
    @DisplayName("Should return 400 when rating parameter is missing - MISSING_PARAMETER")
    void testFilterByRatingMissingParameter() {
        HttpEntity<Void> entity = new HttpEntity<>(authHelper.getUserHeaders());
        
        ResponseEntity<ErrorResponse> response = restTemplate.exchange(
            BASE_URL + "/filter/rating", HttpMethod.GET, entity, ErrorResponse.class
        );
        
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody().getErrorCode()).isEqualTo("MISSING_PARAMETER");
    }

    // ===================== FILTER BY CATEGORY TESTS =====================

    @Test
    @Order(90)
    @DisplayName("Should filter products by category successfully - 200 OK")
    void testFilterByCategorySuccess() {
        HttpEntity<Void> entity = new HttpEntity<>(authHelper.getUserHeaders());
        
        ResponseEntity<Product[]> response = restTemplate.exchange(
            BASE_URL + "/filter/category/Laptops", HttpMethod.GET, entity, Product[].class
        );
        
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
    }

    @Test
    @Order(91)
    @DisplayName("Should return 404 when category not found - CATEGORY_NOT_FOUND")
    void testFilterByCategoryNotFound() {
        HttpEntity<Void> entity = new HttpEntity<>(authHelper.getUserHeaders());
        
        ResponseEntity<ErrorResponse> response = restTemplate.exchange(
            BASE_URL + "/filter/category/NonExistentCategory", HttpMethod.GET, entity, ErrorResponse.class
        );
        
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody().getErrorCode()).isEqualTo("CATEGORY_NOT_FOUND");
    }

    @Test
    @Order(92)
    @DisplayName("Should return 400 when category is empty - INVALID_PARAMETER")
    void testFilterByCategoryEmpty() {
        HttpEntity<Void> entity = new HttpEntity<>(authHelper.getUserHeaders());
        
        ResponseEntity<ErrorResponse> response = restTemplate.exchange(
            BASE_URL + "/filter/category/   ", HttpMethod.GET, entity, ErrorResponse.class
        );
        
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    // ===================== GET CATEGORIES TESTS =====================

    @Test
    @Order(100)
    @DisplayName("Should get all categories successfully - 200 OK")
    void testGetAllCategories() {
        HttpEntity<Void> entity = new HttpEntity<>(authHelper.getUserHeaders());
        
        ResponseEntity<List> response = restTemplate.exchange(
            BASE_URL + "/categories", HttpMethod.GET, entity, List.class
        );
        
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody()).isNotEmpty();
    }

    // ===================== COMPARE PRODUCTS TESTS =====================

    @Test
    @Order(110)
    @DisplayName("Should compare products successfully - 200 OK")
    void testCompareProductsSuccess() {
        HttpEntity<Void> entity = new HttpEntity<>(authHelper.getUserHeaders());
        
        ResponseEntity<Product[]> response = restTemplate.exchange(
            BASE_URL + "/compare?ids=laptop-001,laptop-002,laptop-003", 
            HttpMethod.GET, entity, Product[].class
        );
        
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().length).isGreaterThan(0);
    }

    @Test
    @Order(111)
    @DisplayName("Should return 400 when comparing with empty list - INVALID_PARAMETER")
    void testCompareProductsEmptyList() {
        HttpEntity<Void> entity = new HttpEntity<>(authHelper.getUserHeaders());
        
        ResponseEntity<ErrorResponse> response = restTemplate.exchange(
            BASE_URL + "/compare?ids=", HttpMethod.GET, entity, ErrorResponse.class
        );
        
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    @Order(112)
    @DisplayName("Should return 400 when comparing more than 10 products - INVALID_PARAMETER")
    void testCompareProductsTooMany() {
        String ids = "laptop-001,laptop-002,laptop-003,phone-001,phone-002,phone-003,tablet-001,tablet-002,tablet-003,monitor-001,monitor-002";
        HttpEntity<Void> entity = new HttpEntity<>(authHelper.getUserHeaders());
        
        ResponseEntity<ErrorResponse> response = restTemplate.exchange(
            BASE_URL + "/compare?ids=" + ids, HttpMethod.GET, entity, ErrorResponse.class
        );
        
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody().getErrorCode()).isEqualTo("INVALID_PARAMETER");
    }

    @Test
    @Order(113)
    @DisplayName("Should return 404 when one or more products not found - PRODUCT_NOT_FOUND")
    void testCompareProductsNotFound() {
        HttpEntity<Void> entity = new HttpEntity<>(authHelper.getUserHeaders());
        
        ResponseEntity<ErrorResponse> response = restTemplate.exchange(
            BASE_URL + "/compare?ids=laptop-001,non-existent-id", 
            HttpMethod.GET, entity, ErrorResponse.class
        );
        
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody().getErrorCode()).isEqualTo("PRODUCT_NOT_FOUND");
    }

    @Test
    @Order(114)
    @DisplayName("Should return 400 when ids parameter is missing - MISSING_PARAMETER")
    void testCompareProductsMissingParameter() {
        HttpEntity<Void> entity = new HttpEntity<>(authHelper.getUserHeaders());
        
        ResponseEntity<ErrorResponse> response = restTemplate.exchange(
            BASE_URL + "/compare", HttpMethod.GET, entity, ErrorResponse.class
        );
        
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody().getErrorCode()).isEqualTo("MISSING_PARAMETER");
    }

    // ===================== SORT BY PRICE TESTS =====================

    @Test
    @Order(120)
    @DisplayName("Should sort products by price ascending - 200 OK")
    void testSortByPriceAscending() {
        HttpEntity<Void> entity = new HttpEntity<>(authHelper.getUserHeaders());
        
        ResponseEntity<Product[]> response = restTemplate.exchange(
            BASE_URL + "/sort/price?order=asc", HttpMethod.GET, entity, Product[].class
        );
        
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        
        Product[] products = response.getBody();
        for (int i = 0; i < products.length - 1; i++) {
            assertThat(products[i].getPrice()).isLessThanOrEqualTo(products[i + 1].getPrice());
        }
    }

    @Test
    @Order(121)
    @DisplayName("Should sort products by price descending - 200 OK")
    void testSortByPriceDescending() {
        HttpEntity<Void> entity = new HttpEntity<>(authHelper.getUserHeaders());
        
        ResponseEntity<Product[]> response = restTemplate.exchange(
            BASE_URL + "/sort/price?order=desc", HttpMethod.GET, entity, Product[].class
        );
        
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        
        Product[] products = response.getBody();
        for (int i = 0; i < products.length - 1; i++) {
            assertThat(products[i].getPrice()).isGreaterThanOrEqualTo(products[i + 1].getPrice());
        }
    }

    @Test
    @Order(122)
    @DisplayName("Should sort by price with default order (asc) - 200 OK")
    void testSortByPriceDefaultOrder() {
        HttpEntity<Void> entity = new HttpEntity<>(authHelper.getUserHeaders());
        
        ResponseEntity<Product[]> response = restTemplate.exchange(
            BASE_URL + "/sort/price", HttpMethod.GET, entity, Product[].class
        );
        
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
    }

    // ===================== SORT BY RATING TESTS =====================

    @Test
    @Order(130)
    @DisplayName("Should sort products by rating ascending - 200 OK")
    void testSortByRatingAscending() {
        HttpEntity<Void> entity = new HttpEntity<>(authHelper.getUserHeaders());
        
        ResponseEntity<Product[]> response = restTemplate.exchange(
            BASE_URL + "/sort/rating?order=asc", HttpMethod.GET, entity, Product[].class
        );
        
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        
        Product[] products = response.getBody();
        for (int i = 0; i < products.length - 1; i++) {
            assertThat(products[i].getRating()).isLessThanOrEqualTo(products[i + 1].getRating());
        }
    }

    @Test
    @Order(131)
    @DisplayName("Should sort products by rating descending - 200 OK")
    void testSortByRatingDescending() {
        HttpEntity<Void> entity = new HttpEntity<>(authHelper.getUserHeaders());
        
        ResponseEntity<Product[]> response = restTemplate.exchange(
            BASE_URL + "/sort/rating?order=desc", HttpMethod.GET, entity, Product[].class
        );
        
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        
        Product[] products = response.getBody();
        for (int i = 0; i < products.length - 1; i++) {
            assertThat(products[i].getRating()).isGreaterThanOrEqualTo(products[i + 1].getRating());
        }
    }

    // ===================== GET TOP PRODUCTS TESTS =====================

    @Test
    @Order(140)
    @DisplayName("Should get top rated products with default limit - 200 OK")
    void testGetTopProductsDefault() {
        HttpEntity<Void> entity = new HttpEntity<>(authHelper.getUserHeaders());
        
        ResponseEntity<Product[]> response = restTemplate.exchange(
            BASE_URL + "/top", HttpMethod.GET, entity, Product[].class
        );
        
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().length).isLessThanOrEqualTo(10);
    }

    @Test
    @Order(141)
    @DisplayName("Should get top 5 rated products - 200 OK")
    void testGetTopProductsWithLimit() {
        HttpEntity<Void> entity = new HttpEntity<>(authHelper.getUserHeaders());
        
        ResponseEntity<Product[]> response = restTemplate.exchange(
            BASE_URL + "/top?limit=5", HttpMethod.GET, entity, Product[].class
        );
        
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().length).isLessThanOrEqualTo(5);
    }

    @Test
    @Order(142)
    @DisplayName("Should return 400 when limit is negative - INVALID_PARAMETER")
    void testGetTopProductsNegativeLimit() {
        HttpEntity<Void> entity = new HttpEntity<>(authHelper.getUserHeaders());
        
        ResponseEntity<ErrorResponse> response = restTemplate.exchange(
            BASE_URL + "/top?limit=-5", HttpMethod.GET, entity, ErrorResponse.class
        );
        
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody().getErrorCode()).isEqualTo("INVALID_PARAMETER");
    }

    @Test
    @Order(143)
    @DisplayName("Should return 400 when limit exceeds maximum (100) - INVALID_PARAMETER")
    void testGetTopProductsLimitTooHigh() {
        HttpEntity<Void> entity = new HttpEntity<>(authHelper.getUserHeaders());
        
        ResponseEntity<ErrorResponse> response = restTemplate.exchange(
            BASE_URL + "/top?limit=101", HttpMethod.GET, entity, ErrorResponse.class
        );
        
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody().getErrorCode()).isEqualTo("INVALID_PARAMETER");
    }

    // ===================== FILTER BY SPECIFICATION TESTS =====================

    @Test
    @Order(150)
    @DisplayName("Should filter products by specification successfully - 200 OK")
    void testFilterBySpecificationSuccess() {
        HttpEntity<Void> entity = new HttpEntity<>(authHelper.getUserHeaders());
        
        ResponseEntity<Product[]> response = restTemplate.exchange(
            BASE_URL + "/filter/spec?key=category&value=Laptops", 
            HttpMethod.GET, entity, Product[].class
        );
        
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
    }

    @Test
    @Order(151)
    @DisplayName("Should return 400 when specification key is empty - INVALID_PARAMETER")
    void testFilterBySpecificationEmptyKey() {
        HttpEntity<Void> entity = new HttpEntity<>(authHelper.getUserHeaders());
        
        ResponseEntity<ErrorResponse> response = restTemplate.exchange(
            BASE_URL + "/filter/spec?key=&value=test", 
            HttpMethod.GET, entity, ErrorResponse.class
        );
        
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    @Order(152)
    @DisplayName("Should return 400 when specification value is empty - INVALID_PARAMETER")
    void testFilterBySpecificationEmptyValue() {
        HttpEntity<Void> entity = new HttpEntity<>(authHelper.getUserHeaders());
        
        ResponseEntity<ErrorResponse> response = restTemplate.exchange(
            BASE_URL + "/filter/spec?key=processor&value=", 
            HttpMethod.GET, entity, ErrorResponse.class
        );
        
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    @Order(153)
    @DisplayName("Should return 400 when specification parameters are missing - MISSING_PARAMETER")
    void testFilterBySpecificationMissingParameters() {
        HttpEntity<Void> entity = new HttpEntity<>(authHelper.getUserHeaders());
        
        ResponseEntity<ErrorResponse> response = restTemplate.exchange(
            BASE_URL + "/filter/spec?key=processor", 
            HttpMethod.GET, entity, ErrorResponse.class
        );
        
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody().getErrorCode()).isEqualTo("MISSING_PARAMETER");
    }

    // ===================== DELETE PRODUCT TESTS =====================

    @Test
    @Order(200)
    @DisplayName("Should delete product successfully - 204 NO CONTENT")
    void testDeleteProductSuccess() {
        // First create a product to delete
        Map<String, String> specs = new HashMap<>();
        specs.put("category", "Test");
        
        CreateProductRequest request = CreateProductRequest.builder()
            .id("test-delete-" + UUID.randomUUID().toString().substring(0, 8))
            .name("Product to Delete")
            .price(99.99)
            .rating(4.0)
            .specifications(specs)
            .build();
        
        HttpEntity<CreateProductRequest> createEntity = new HttpEntity<>(request, authHelper.getAdminHeaders());
        ResponseEntity<Product> createResponse = restTemplate.exchange(
            BASE_URL, HttpMethod.POST, createEntity, Product.class
        );
        
        String productId = createResponse.getBody().getId();
        
        // Now delete the product
        HttpEntity<Void> deleteEntity = new HttpEntity<>(authHelper.getAdminHeaders());
        ResponseEntity<Void> deleteResponse = restTemplate.exchange(
            BASE_URL + "/" + productId, HttpMethod.DELETE, deleteEntity, Void.class
        );
        
        assertThat(deleteResponse.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    @Order(201)
    @DisplayName("Should return 404 when deleting non-existent product - PRODUCT_NOT_FOUND")
    void testDeleteProductNotFound() {
        HttpEntity<Void> entity = new HttpEntity<>(authHelper.getAdminHeaders());
        
        ResponseEntity<ErrorResponse> response = restTemplate.exchange(
            BASE_URL + "/non-existent-product-id", 
            HttpMethod.DELETE, entity, ErrorResponse.class
        );
        
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody().getErrorCode()).isEqualTo("PRODUCT_NOT_FOUND");
    }

    // ===================== ENDPOINT NOT FOUND TEST =====================

    @Test
    @Order(210)
    @DisplayName("Should return 404 for non-existent endpoint - ENDPOINT_NOT_FOUND")
    void testNonExistentEndpoint() {
        HttpEntity<Void> entity = new HttpEntity<>(authHelper.getUserHeaders());
        
        ResponseEntity<ErrorResponse> response = restTemplate.exchange(
            BASE_URL + "/invalid-endpoint", 
            HttpMethod.GET, entity, ErrorResponse.class
        );
        
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
}
