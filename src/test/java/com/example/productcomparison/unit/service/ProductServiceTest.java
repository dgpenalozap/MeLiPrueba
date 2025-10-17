package com.example.productcomparison.unit.service;

import com.example.productcomparison.exception.CategoryNotFoundException;
import com.example.productcomparison.exception.InvalidParameterException;
import com.example.productcomparison.exception.InvalidRatingException;
import com.example.productcomparison.exception.ProductNotFoundException;
import com.example.productcomparison.model.Product;
import com.example.productcomparison.repository.IProductRepository;
import com.example.productcomparison.service.AIProductGenerator;
import com.example.productcomparison.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("ProductService Unit Tests")
class ProductServiceTest {

    @Mock
    private IProductRepository productRepository;

    @Mock
    private AIProductGenerator aiProductGenerator;

    @InjectMocks
    private ProductService productService;

    private Product product1;
    private Product product2;

    @BeforeEach
    void setUp() {
        product1 = Product.builder().id("1").name("Laptop Pro").price(1200.0).rating(4.5).specifications(Map.of("category", "Laptops")).build();
        product2 = Product.builder().id("2").name("Gaming Mouse").price(75.0).rating(4.8).specifications(Map.of("category", "Mice")).build();
    }

    @Test
    @DisplayName("getProductById should throw exception for empty ID")
    void getProductById_EmptyId_ThrowsException() {
        assertThrows(InvalidParameterException.class, () -> productService.getProductById(" "));
    }

    @Test
    @DisplayName("getProductById should throw exception when not found")
    void getProductById_NotFound_ThrowsException() {
        when(productRepository.findById("3")).thenReturn(Optional.empty());
        assertThrows(ProductNotFoundException.class, () -> productService.getProductById("3"));
    }

    @Test
    @DisplayName("createProduct should save product successfully")
    void createProduct_ValidProduct_SavesSuccessfully() {
        Product newProduct = Product.builder()
                .id("test-001")
                .name("Test Product")
                .price(99.99)
                .rating(4.0)
                .build();
        
        when(productRepository.save(any(Product.class))).thenReturn(newProduct);
        
        Product result = productService.createProduct(newProduct);
        
        assertNotNull(result);
        assertEquals("test-001", result.getId());
        verify(productRepository, times(1)).save(newProduct);
    }

    @Test
    @DisplayName("createProduct should throw exception for empty name")
    void createProduct_EmptyName_ThrowsException() {
        Product invalidProduct = Product.builder()
                .id("test-001")
                .name("")
                .price(99.99)
                .rating(4.0)
                .build();
        
        assertThrows(InvalidParameterException.class, () -> productService.createProduct(invalidProduct));
    }

    @Test
    @DisplayName("createProduct should throw exception for negative price")
    void createProduct_NegativePrice_ThrowsException() {
        Product invalidProduct = Product.builder()
                .id("test-001")
                .name("Test Product")
                .price(-10.0)
                .rating(4.0)
                .build();
        
        assertThrows(Exception.class, () -> productService.createProduct(invalidProduct));
    }

    @Test
    @DisplayName("createProduct should throw exception for invalid rating")
    void createProduct_InvalidRating_ThrowsException() {
        Product invalidProduct = Product.builder()
                .id("test-001")
                .name("Test Product")
                .price(99.99)
                .rating(6.0)
                .build();
        
        assertThrows(InvalidRatingException.class, () -> productService.createProduct(invalidProduct));
    }

    @Test
    @DisplayName("generateRandomProduct should create random product")
    void generateRandomProduct_CreatesSuccessfully() {
        Product generatedProduct = Product.builder()
                .id("gen-001")
                .name("Generated Product")
                .price(199.99)
                .rating(4.5)
                .specifications(Map.of("category", "Laptops"))
                .build();
        
        when(aiProductGenerator.generateRandomProduct()).thenReturn(generatedProduct);
        when(productRepository.save(any(Product.class))).thenReturn(generatedProduct);
        
        Product result = productService.generateRandomProduct();
        
        assertNotNull(result);
        assertEquals("gen-001", result.getId());
        verify(aiProductGenerator, times(1)).generateRandomProduct();
        verify(productRepository, times(1)).save(generatedProduct);
    }

    @Test
    @DisplayName("updateProduct should update product successfully")
    void updateProduct_ValidProduct_UpdatesSuccessfully() {
        Product updatedProduct = Product.builder()
                .name("Updated Product")
                .price(149.99)
                .rating(4.5)
                .build();
        
        Product expectedProduct = updatedProduct.toBuilder().id("1").build();
        when(productRepository.update(eq("1"), any(Product.class))).thenReturn(expectedProduct);
        
        Product result = productService.updateProduct("1", updatedProduct);
        
        assertNotNull(result);
        assertEquals("1", result.getId());
        assertEquals("Updated Product", result.getName());
        verify(productRepository, times(1)).update(eq("1"), any(Product.class));
    }

    @Test
    @DisplayName("updateProduct should throw exception for empty ID")
    void updateProduct_EmptyId_ThrowsException() {
        Product product = Product.builder()
                .name("Test")
                .price(99.99)
                .rating(4.0)
                .build();
        
        assertThrows(InvalidParameterException.class, () -> productService.updateProduct(" ", product));
    }

    @Test
    @DisplayName("deleteProduct should delete product successfully")
    void deleteProduct_ValidId_DeletesSuccessfully() {
        doNothing().when(productRepository).deleteById("1");
        
        productService.deleteProduct("1");
        
        verify(productRepository, times(1)).deleteById("1");
    }

    @Test
    @DisplayName("deleteProduct should throw exception for empty ID")
    void deleteProduct_EmptyId_ThrowsException() {
        assertThrows(InvalidParameterException.class, () -> productService.deleteProduct(" "));
    }

    @Test
    @DisplayName("searchByName should throw exception for empty query")
    void searchByName_EmptyQuery_ThrowsException() {
        assertThrows(InvalidParameterException.class, () -> productService.searchByName(" "));
    }

    @Test
    @DisplayName("filterByCategory should throw exception for non-existent category")
    void filterByCategory_NonExistentCategory_ThrowsException() {
        when(productRepository.findAll()).thenReturn(List.of(product1));
        assertThrows(CategoryNotFoundException.class, () -> productService.filterByCategory("Unknown"));
    }

    @Test
    @DisplayName("compareProducts should throw exception for empty ID list")
    void compareProducts_EmptyList_ThrowsException() {
        assertThrows(InvalidParameterException.class, () -> productService.compareProducts(Collections.emptyList()));
    }

    @Test
    @DisplayName("compareProducts should throw exception for too many IDs")
    void compareProducts_TooManyIds_ThrowsException() {
        List<String> ids = Collections.nCopies(11, "id");
        assertThrows(InvalidParameterException.class, () -> productService.compareProducts(ids));
    }

    @Test
    @DisplayName("getTopRatedProducts should throw exception for invalid limit")
    void getTopRatedProducts_InvalidLimit_ThrowsException() {
        assertThrows(InvalidParameterException.class, () -> productService.getTopRatedProducts(0));
        assertThrows(InvalidParameterException.class, () -> productService.getTopRatedProducts(101));
    }
}
