package com.example.productcomparison.integration;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("ProductController Integration Tests")
public class ProductControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("GET /api/products should return all products")
    void listProducts_shouldReturnAllProducts() throws Exception {
        mockMvc.perform(get("/api/products"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(38));
    }

    @Test
    @DisplayName("GET /api/products/{id} should return a product when found")
    void getProduct_shouldReturnProduct_whenFound() throws Exception {
        mockMvc.perform(get("/api/products/laptop-001"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value("laptop-001"))
                .andExpect(jsonPath("$.name").value("UltraBook Pro 15"));
    }

    @Test
    @DisplayName("GET /api/products/{id} should return 404 when not found")
    void getProduct_shouldReturnNotFound_whenNotFound() throws Exception {
        mockMvc.perform(get("/api/products/non-existent-id"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("GET /api/products/search should return matching products")
    void searchProducts_shouldReturnMatchingProducts() throws Exception {
        mockMvc.perform(get("/api/products/search").param("q", "gaming"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.[0].name").value("TechMaster Gaming X17"));
    }

    @Test
    @DisplayName("GET /api/products/search should return empty list for no matches")
    void searchProducts_shouldReturnEmptyList_forNoMatches() throws Exception {
        mockMvc.perform(get("/api/products/search").param("q", "non-existent-query"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    @DisplayName("GET /api/products/filter/price should return products in range")
    void filterByPrice_shouldReturnProductsInRange() throws Exception {
        mockMvc.perform(get("/api/products/filter/price").param("min", "1000").param("max", "1200"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    @DisplayName("GET /api/products/filter/price should return 400 for invalid range")
    void filterByPrice_shouldReturnBadRequest_forInvalidRange() throws Exception {
        mockMvc.perform(get("/api/products/filter/price").param("min", "1200").param("max", "1000"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("GET /api/products/filter/rating should return products above rating")
    void filterByRating_shouldReturnProductsAboveRating() throws Exception {
        mockMvc.perform(get("/api/products/filter/rating").param("min", "4.8"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    @DisplayName("GET /api/products/filter/rating should return 400 for invalid rating")
    void filterByRating_shouldReturnBadRequest_forInvalidRating() throws Exception {
        mockMvc.perform(get("/api/products/filter/rating").param("min", "6.0"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("GET /api/products/filter/category/{category} should return products in category")
    void filterByCategory_shouldReturnProductsInCategory() throws Exception {
        mockMvc.perform(get("/api/products/filter/category/Laptops"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    @DisplayName("GET /api/products/filter/category/{category} should return 404 for non-existent category")
    void filterByCategory_shouldReturnNotFound_forNonExistentCategory() throws Exception {
        mockMvc.perform(get("/api/products/filter/category/non-existent-category"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("GET /api/products/categories should return all categories")
    void getAllCategories_shouldReturnAllCategories() throws Exception {
        mockMvc.perform(get("/api/products/categories"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(13));
    }

    @Test
    @DisplayName("GET /api/products/compare should return products to compare")
    void compareProducts_shouldReturnProductsToCompare() throws Exception {
        mockMvc.perform(get("/api/products/compare").param("ids", "laptop-001,laptop-002"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    @DisplayName("GET /api/products/compare should return 404 if one ID is not found")
    void compareProducts_shouldReturnNotFound_ifOneIdIsNotFound() throws Exception {
        mockMvc.perform(get("/api/products/compare").param("ids", "laptop-001,non-existent-id"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("GET /api/products/sort/price should return products sorted by price")
    void sortByPrice_shouldReturnProductsSortedByPrice() throws Exception {
        mockMvc.perform(get("/api/products/sort/price").param("order", "asc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    @DisplayName("GET /api/products/sort/rating should return products sorted by rating")
    void sortByRating_shouldReturnProductsSortedByRating() throws Exception {
        mockMvc.perform(get("/api/products/sort/rating").param("order", "desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    @DisplayName("GET /api/products/top should return top N products")
    void getTopRatedProducts_shouldReturnTopNProducts() throws Exception {
        mockMvc.perform(get("/api/products/top").param("limit", "5"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(5));
    }

    @Test
    @DisplayName("GET /api/products/filter/spec should return matching products")
    void findBySpecification_shouldReturnMatchingProducts() throws Exception {
        mockMvc.perform(get("/api/products/filter/spec").param("key", "ram").param("value", "16GB"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray());
    }
}
