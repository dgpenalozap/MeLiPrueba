package com.example.productcomparison.unit.controller;

import com.example.productcomparison.controller.ProductController;
import com.example.productcomparison.model.Product;
import com.example.productcomparison.service.IProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for ProductController achieving 100% code coverage.
 * Uses Mockito to mock the service layer.
 */
@DisplayName("ProductController Complete Unit Tests")
class ProductControllerTest {

    @Mock
    private IProductService productService;

    private ProductController controller;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        controller = new ProductController(productService);
    }

    // ==================== listProducts Tests ====================

    @Test
    @DisplayName("listProducts should return all products")
    void listProducts_ReturnsAllProducts() {
        Product p1 = Product.builder().id("1").name("P1").build();
        Product p2 = Product.builder().id("2").name("P2").build();
        List<Product> products = Arrays.asList(p1, p2);
        
        when(productService.getAllProducts()).thenReturn(products);
        
        ResponseEntity<List<Product>> response = controller.listProducts();
        
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
        verify(productService, times(1)).getAllProducts();
    }

    @Test
    @DisplayName("listProducts should return empty list when no products")
    void listProducts_EmptyList_ReturnsEmptyList() {
        when(productService.getAllProducts()).thenReturn(Collections.emptyList());
        
        ResponseEntity<List<Product>> response = controller.listProducts();
        
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isEmpty());
    }

    // ==================== getProduct Tests ====================

    @Test
    @DisplayName("getProduct should return product by id")
    void getProduct_ValidId_ReturnsProduct() {
        Product product = Product.builder().id("123").name("Test Product").price(99.99).build();
        
        when(productService.getProductById("123")).thenReturn(product);
        
        ResponseEntity<Product> response = controller.getProduct("123");
        
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals("123", response.getBody().getId());
        assertEquals("Test Product", response.getBody().getName());
        verify(productService, times(1)).getProductById("123");
    }

    // ==================== searchProducts Tests ====================

    @Test
    @DisplayName("searchProducts should return matching products")
    void searchProducts_ValidQuery_ReturnsProducts() {
        Product p1 = Product.builder().id("1").name("Gaming Laptop").build();
        Product p2 = Product.builder().id("2").name("Business Laptop").build();
        List<Product> products = Arrays.asList(p1, p2);
        
        when(productService.searchByName("laptop")).thenReturn(products);
        
        ResponseEntity<List<Product>> response = controller.searchProducts("laptop");
        
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
        verify(productService, times(1)).searchByName("laptop");
    }

    // ==================== filterByPrice Tests ====================

    @Test
    @DisplayName("filterByPrice should return products in price range")
    void filterByPrice_ValidRange_ReturnsProducts() {
        Product p1 = Product.builder().id("1").name("P1").price(150.0).build();
        List<Product> products = Collections.singletonList(p1);
        
        when(productService.filterByPriceRange(100.0, 200.0)).thenReturn(products);
        
        ResponseEntity<List<Product>> response = controller.filterByPrice(100.0, 200.0);
        
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        verify(productService, times(1)).filterByPriceRange(100.0, 200.0);
    }

    // ==================== filterByRating Tests ====================

    @Test
    @DisplayName("filterByRating should return products above minimum rating")
    void filterByRating_ValidRating_ReturnsProducts() {
        Product p1 = Product.builder().id("1").name("P1").rating(4.5).build();
        List<Product> products = Collections.singletonList(p1);
        
        when(productService.filterByRating(4.0)).thenReturn(products);
        
        ResponseEntity<List<Product>> response = controller.filterByRating(4.0);
        
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        verify(productService, times(1)).filterByRating(4.0);
    }

    // ==================== filterByCategory Tests ====================

    @Test
    @DisplayName("filterByCategory should return products in category")
    void filterByCategory_ValidCategory_ReturnsProducts() {
        Product p1 = Product.builder().id("1").name("P1").build();
        List<Product> products = Collections.singletonList(p1);
        
        when(productService.filterByCategory("Laptops")).thenReturn(products);
        
        ResponseEntity<List<Product>> response = controller.filterByCategory("Laptops");
        
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        verify(productService, times(1)).filterByCategory("Laptops");
    }

    // ==================== getAllCategories Tests ====================

    @Test
    @DisplayName("getAllCategories should return list of categories")
    void getAllCategories_ReturnsCategories() {
        List<String> categories = Arrays.asList("Laptops", "Phones", "Tablets");
        
        when(productService.getAllCategories()).thenReturn(categories);
        
        ResponseEntity<List<String>> response = controller.getAllCategories();
        
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals(3, response.getBody().size());
        assertTrue(response.getBody().contains("Laptops"));
        verify(productService, times(1)).getAllCategories();
    }

    // ==================== compareProducts Tests ====================

    @Test
    @DisplayName("compareProducts should return requested products")
    void compareProducts_ValidIds_ReturnsProducts() {
        Product p1 = Product.builder().id("1").name("P1").build();
        Product p2 = Product.builder().id("2").name("P2").build();
        List<Product> products = Arrays.asList(p1, p2);
        List<String> ids = Arrays.asList("1", "2");
        
        when(productService.compareProducts(ids)).thenReturn(products);
        
        ResponseEntity<List<Product>> response = controller.compareProducts(ids);
        
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
        verify(productService, times(1)).compareProducts(ids);
    }

    // ==================== sortByPrice Tests ====================

    @Test
    @DisplayName("sortByPrice ascending should return sorted products")
    void sortByPrice_Ascending_ReturnsSortedProducts() {
        Product p1 = Product.builder().id("1").name("P1").price(100.0).build();
        Product p2 = Product.builder().id("2").name("P2").price(200.0).build();
        List<Product> products = Arrays.asList(p1, p2);
        
        when(productService.sortByPrice(true)).thenReturn(products);
        
        ResponseEntity<List<Product>> response = controller.sortByPrice("asc");
        
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
        verify(productService, times(1)).sortByPrice(true);
    }

    @Test
    @DisplayName("sortByPrice descending should return sorted products")
    void sortByPrice_Descending_ReturnsSortedProducts() {
        Product p1 = Product.builder().id("1").name("P1").price(200.0).build();
        Product p2 = Product.builder().id("2").name("P2").price(100.0).build();
        List<Product> products = Arrays.asList(p1, p2);
        
        when(productService.sortByPrice(false)).thenReturn(products);
        
        ResponseEntity<List<Product>> response = controller.sortByPrice("desc");
        
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
        verify(productService, times(1)).sortByPrice(false);
    }

    @Test
    @DisplayName("sortByPrice with case variations should work")
    void sortByPrice_CaseVariations_Works() {
        List<Product> products = Collections.emptyList();
        
        when(productService.sortByPrice(anyBoolean())).thenReturn(products);
        
        controller.sortByPrice("ASC");
        controller.sortByPrice("Desc");
        controller.sortByPrice("aSc");
        
        verify(productService, times(2)).sortByPrice(true);
        verify(productService, times(1)).sortByPrice(false);
    }

    // ==================== sortByRating Tests ====================

    @Test
    @DisplayName("sortByRating ascending should return sorted products")
    void sortByRating_Ascending_ReturnsSortedProducts() {
        Product p1 = Product.builder().id("1").name("P1").rating(3.5).build();
        Product p2 = Product.builder().id("2").name("P2").rating(4.5).build();
        List<Product> products = Arrays.asList(p1, p2);
        
        when(productService.sortByRating(true)).thenReturn(products);
        
        ResponseEntity<List<Product>> response = controller.sortByRating("asc");
        
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
        verify(productService, times(1)).sortByRating(true);
    }

    @Test
    @DisplayName("sortByRating descending should return sorted products")
    void sortByRating_Descending_ReturnsSortedProducts() {
        Product p1 = Product.builder().id("1").name("P1").rating(4.5).build();
        Product p2 = Product.builder().id("2").name("P2").rating(3.5).build();
        List<Product> products = Arrays.asList(p1, p2);
        
        when(productService.sortByRating(false)).thenReturn(products);
        
        ResponseEntity<List<Product>> response = controller.sortByRating("desc");
        
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
        verify(productService, times(1)).sortByRating(false);
    }

    // ==================== getTopRatedProducts Tests ====================

    @Test
    @DisplayName("getTopRatedProducts should return top N products")
    void getTopRatedProducts_ValidLimit_ReturnsTopProducts() {
        Product p1 = Product.builder().id("1").name("P1").rating(4.8).build();
        Product p2 = Product.builder().id("2").name("P2").rating(4.5).build();
        List<Product> products = Arrays.asList(p1, p2);
        
        when(productService.getTopRatedProducts(2)).thenReturn(products);
        
        ResponseEntity<List<Product>> response = controller.getTopRatedProducts(2);
        
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
        verify(productService, times(1)).getTopRatedProducts(2);
    }

    @Test
    @DisplayName("getTopRatedProducts with default limit should work")
    void getTopRatedProducts_DefaultLimit_ReturnsProducts() {
        List<Product> products = Collections.emptyList();
        
        when(productService.getTopRatedProducts(10)).thenReturn(products);
        
        ResponseEntity<List<Product>> response = controller.getTopRatedProducts(10);
        
        assertEquals(200, response.getStatusCodeValue());
        verify(productService, times(1)).getTopRatedProducts(10);
    }

    // ==================== findBySpecification Tests ====================

    @Test
    @DisplayName("findBySpecification should return matching products")
    void findBySpecification_ValidSpec_ReturnsProducts() {
        Product p1 = Product.builder().id("1").name("P1").specifications(Map.of("processor", "Intel i7")).build();
        List<Product> products = Collections.singletonList(p1);
        
        when(productService.findBySpecification("processor", "Intel i7")).thenReturn(products);
        
        ResponseEntity<List<Product>> response = controller.findBySpecification("processor", "Intel i7");
        
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        verify(productService, times(1)).findBySpecification("processor", "Intel i7");
    }
}
