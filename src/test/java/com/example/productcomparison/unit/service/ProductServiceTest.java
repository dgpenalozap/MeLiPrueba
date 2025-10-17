package com.example.productcomparison.unit.service;

import com.example.productcomparison.exception.CategoryNotFoundException;
import com.example.productcomparison.exception.InvalidParameterException;
import com.example.productcomparison.exception.ProductNotFoundException;
import com.example.productcomparison.model.Product;
import com.example.productcomparison.repository.IProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("ProductService Unit Tests")
class ProductServiceTest {

    @Mock
    private IProductRepository productRepository;

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
