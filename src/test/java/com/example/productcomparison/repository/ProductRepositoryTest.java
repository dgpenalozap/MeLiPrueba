package com.example.productcomparison.repository;

import com.example.productcomparison.exception.ProductDataAccessException;
import com.example.productcomparison.model.Product;
import com.example.productcomparison.model.ProductDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("ProductRepository Unit Tests")
class ProductRepositoryTest {

    @Mock
    private ProductDataSource productDataSource;

    @InjectMocks
    private ProductRepository productRepository;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(productRepository, "jsonFilePath", "test.json");
    }

    private ProductDTO createProductDTO(String id, String name, double price, double rating) {
        ProductDTO dto = new ProductDTO();
        dto.setId(id);
        dto.setName(name);
        dto.setPrice(price);
        dto.setRating(rating);
        return dto;
    }

    @Test
    @DisplayName("findAll should return a list of products")
    void findAll_ReturnsListOfProducts() {
        ProductDTO dto = createProductDTO("1", "Product 1", 100.0, 4.5);
        when(productDataSource.loadProductsFromJson("test.json")).thenReturn(List.of(dto));

        List<Product> products = productRepository.findAll();

        assertNotNull(products);
        assertEquals(1, products.size());
        assertEquals("1", products.get(0).getId());
    }

    @Test
    @DisplayName("findById should return a product when found")
    void findById_ReturnsProduct_WhenFound() {
        ProductDTO dto = createProductDTO("1", "Product 1", 100.0, 4.5);
        when(productDataSource.loadProductsFromJson("test.json")).thenReturn(List.of(dto));

        Optional<Product> product = productRepository.findById("1");

        assertTrue(product.isPresent());
        assertEquals("1", product.get().getId());
    }

    @Test
    @DisplayName("findById should return empty when not found")
    void findById_ReturnsEmpty_WhenNotFound() {
        when(productDataSource.loadProductsFromJson("test.json")).thenReturn(List.of());

        Optional<Product> product = productRepository.findById("1");

        assertFalse(product.isPresent());
    }

    @Test
    @DisplayName("findAll should throw ProductDataAccessException when data source fails")
    void findAll_whenDataSourceThrowsException_throwsProductDataAccessException() {
        when(productDataSource.loadProductsFromJson("test.json")).thenThrow(new com.example.productcomparison.exception.DataSourceInitializationException("File not found"));
        assertThrows(ProductDataAccessException.class, () -> productRepository.findAll());
    }

    @Test
    @DisplayName("init should throw ProductDataAccessException on validation failure")
    void init_whenDataSourceThrowsException_throwsProductDataAccessException() {
        when(productDataSource.loadProductsFromJson("test.json")).thenThrow(new com.example.productcomparison.exception.DataSourceInitializationException("File not found"));
        assertThrows(ProductDataAccessException.class, () -> productRepository.init());
    }
}
