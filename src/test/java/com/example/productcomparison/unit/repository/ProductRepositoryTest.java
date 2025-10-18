package com.example.productcomparison.unit.repository;

import com.example.productcomparison.exception.repository.*;
import com.example.productcomparison.exception.service.ProductNotFoundException;
import com.example.productcomparison.model.Product;
import com.example.productcomparison.model.ProductDTO;
import com.example.productcomparison.repository.ProductDataSource;
import com.example.productcomparison.repository.ProductMapper;
import com.example.productcomparison.repository.ProductRepository;
import com.example.productcomparison.repository.ProductValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("ProductRepository Unit Tests")
class ProductRepositoryTest {

    @Mock
    private ProductDataSource productDataSource;

    @Spy
    private ProductMapper productMapper;

    @Spy
    private ProductValidator productValidator;

    @InjectMocks
    private ProductRepository productRepository;

    private ProductDTO productDTO1;
    private Product product1;

    @BeforeEach
    void setUp() {
        productDTO1 = ProductDTO.builder()
                .id("1")
                .name("Product 1")
                .price(100.0)
                .rating(4.5)
                .build();

        product1 = Product.builder()
                .id("1")
                .name("Product 1")
                .price(100.0)
                .rating(4.5)
                .build();
    }

    @Test
    @DisplayName("Should initialize repository successfully with valid products")
    void testInit_Success() {
        // Arrange
        ReflectionTestUtils.setField(productRepository, "jsonFilePath", "classpath:data/products.json");
        when(productDataSource.loadProductsFromJson(anyString())).thenReturn(List.of(productDTO1));
        when(productMapper.toDomain(any(ProductDTO.class))).thenReturn(product1);

        // Act
        productRepository.init();

        // Assert
        List<Product> products = productRepository.findAll();
        assertEquals(1, products.size());
        verify(productDataSource).loadProductsFromJson(anyString());
    }

    @Test
    @DisplayName("Should handle empty product list during initialization")
    void testInit_EmptyList() {
        // Arrange
        ReflectionTestUtils.setField(productRepository, "jsonFilePath", "classpath:data/products.json");
        when(productDataSource.loadProductsFromJson(anyString())).thenReturn(Collections.emptyList());

        // Act
        productRepository.init();

        // Assert
        List<Product> products = productRepository.findAll();
        assertTrue(products.isEmpty());
    }

    @Test
    @DisplayName("Should throw ProductDataAccessException when data source fails")
    void testInit_DataSourceException() {
        // Arrange
        ReflectionTestUtils.setField(productRepository, "jsonFilePath", "invalid-path.json");
        when(productDataSource.loadProductsFromJson(anyString()))
                .thenThrow(new DataSourceInitializationException("File not found"));

        // Act & Assert
        assertThrows(ProductDataAccessException.class, () -> productRepository.init());
    }

    @Test
    @DisplayName("Should skip invalid products during initialization")
    void testInit_InvalidProduct() {
        // Arrange
        ReflectionTestUtils.setField(productRepository, "jsonFilePath", "classpath:data/products.json");
        Product invalidProduct = Product.builder().id("2").name("Invalid").price(-10.0).build();
        when(productDataSource.loadProductsFromJson(anyString())).thenReturn(List.of(productDTO1));
        when(productMapper.toDomain(any(ProductDTO.class))).thenReturn(invalidProduct);

        // Act
        productRepository.init();

        // Assert
        List<Product> products = productRepository.findAll();
        assertTrue(products.isEmpty());
    }

    @Test
    @DisplayName("Should handle mapper exception during initialization")
    void testInit_MapperException() {
        // Arrange
        ReflectionTestUtils.setField(productRepository, "jsonFilePath", "classpath:data/products.json");
        when(productDataSource.loadProductsFromJson(anyString())).thenReturn(List.of(productDTO1));
        when(productMapper.toDomain(any(ProductDTO.class))).thenThrow(new IllegalArgumentException("Mapping failed"));

        // Act
        productRepository.init();

        // Assert
        List<Product> products = productRepository.findAll();
        assertTrue(products.isEmpty());
    }

    @Test
    @DisplayName("Should handle unexpected exception during initialization")
    void testInit_UnexpectedException() {
        // Arrange
        ReflectionTestUtils.setField(productRepository, "jsonFilePath", "classpath:data/products.json");
        when(productDataSource.loadProductsFromJson(anyString())).thenThrow(new RuntimeException("Unexpected error"));

        // Act & Assert
        assertThrows(ProductDataAccessException.class, () -> productRepository.init());
    }

    @Test
    @DisplayName("Should return all products")
    void testFindAll_Success() {
        // Arrange
        ReflectionTestUtils.setField(productRepository, "jsonFilePath", "classpath:data/products.json");
        when(productDataSource.loadProductsFromJson(anyString())).thenReturn(List.of(productDTO1));
        when(productMapper.toDomain(any(ProductDTO.class))).thenReturn(product1);
        productRepository.init();

        // Act
        List<Product> result = productRepository.findAll();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("1", result.get(0).getId());
    }

    @Test
    @DisplayName("Should find product by valid ID")
    void testFindById_Success() {
        // Arrange
        ReflectionTestUtils.setField(productRepository, "jsonFilePath", "classpath:data/products.json");
        when(productDataSource.loadProductsFromJson(anyString())).thenReturn(List.of(productDTO1));
        when(productMapper.toDomain(any(ProductDTO.class))).thenReturn(product1);
        productRepository.init();

        // Act
        Optional<Product> result = productRepository.findById("1");

        // Assert
        assertTrue(result.isPresent());
        assertEquals("1", result.get().getId());
    }

    @Test
    @DisplayName("Should return empty when product not found by ID")
    void testFindById_NotFound() {
        // Arrange
        ReflectionTestUtils.setField(productRepository, "jsonFilePath", "classpath:data/products.json");
        when(productDataSource.loadProductsFromJson(anyString())).thenReturn(Collections.emptyList());
        productRepository.init();

        // Act
        Optional<Product> result = productRepository.findById("999");

        // Assert
        assertFalse(result.isPresent());
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" ", "  "})
    @DisplayName("Should return empty when ID is null, empty or blank")
    void testFindById_InvalidId(String id) {
        // Arrange & Act
        Optional<Product> result = productRepository.findById(id);

        // Assert
        assertFalse(result.isPresent());
    }

    @Test
    @DisplayName("Should save product successfully")
    void testSave_Success() {
        // Arrange
        when(productMapper.toDto(any(Product.class))).thenReturn(productDTO1);
        doNothing().when(productValidator).validateDto(any(ProductDTO.class));

        // Act
        Product result = productRepository.save(product1);

        // Assert
        assertNotNull(result);
        assertEquals("1", result.getId());
        verify(productValidator).validateDto(any(ProductDTO.class));
    }

    @Test
    @DisplayName("Should throw ProductAlreadyExistsException when saving duplicate")
    void testSave_ProductAlreadyExists() {
        // Arrange
        when(productMapper.toDto(any(Product.class))).thenReturn(productDTO1);
        doNothing().when(productValidator).validateDto(any(ProductDTO.class));
        productRepository.save(product1);

        // Act & Assert
        assertThrows(ProductAlreadyExistsException.class, () -> productRepository.save(product1));
    }

    @Test
    @DisplayName("Should throw ProductValidationException when validation fails")
    void testSave_ValidationFails() {
        // Arrange
        when(productMapper.toDto(any(Product.class))).thenReturn(productDTO1);
        doThrow(new IllegalArgumentException("Invalid product"))
                .when(productValidator).validateDto(any(ProductDTO.class));

        // Act & Assert
        assertThrows(ProductValidationException.class, () -> productRepository.save(product1));
    }

    @Test
    @DisplayName("Should throw ProductSaveException on unexpected error")
    void testSave_UnexpectedException() {
        // Arrange
        when(productMapper.toDto(any(Product.class))).thenThrow(new RuntimeException("Unexpected error"));

        // Act & Assert
        assertThrows(ProductSaveException.class, () -> productRepository.save(product1));
    }

    @Test
    @DisplayName("Should update product successfully")
    void testUpdate_Success() {
        // Arrange
        when(productMapper.toDto(any(Product.class))).thenReturn(productDTO1);
        doNothing().when(productValidator).validateDto(any(ProductDTO.class));
        productRepository.save(product1);

        Product updatedProduct = product1.toBuilder().name("Updated Product").build();
        ProductDTO updatedDTO = productDTO1.toBuilder().name("Updated Product").build();
        when(productMapper.toDto(any(Product.class))).thenReturn(updatedDTO);

        // Act
        Product result = productRepository.update("1", updatedProduct);

        // Assert
        assertNotNull(result);
        assertEquals("1", result.getId());
        verify(productValidator, atLeastOnce()).validateDto(any(ProductDTO.class));
    }

    @Test
    @DisplayName("Should throw ProductNotFoundException when updating non-existent product")
    void testUpdate_ProductNotFound() {
        // Arrange
        Product updatedProduct = product1.toBuilder().name("Updated").build();

        // Act & Assert
        assertThrows(ProductNotFoundException.class, () -> productRepository.update("999", updatedProduct));
    }

    @Test
    @DisplayName("Should throw ProductValidationException when update validation fails")
    void testUpdate_ValidationFails() {
        // Arrange
        when(productMapper.toDto(any(Product.class))).thenReturn(productDTO1);
        doNothing().when(productValidator).validateDto(any(ProductDTO.class));
        productRepository.save(product1);

        doThrow(new IllegalArgumentException("Invalid product"))
                .when(productValidator).validateDto(any(ProductDTO.class));

        // Act & Assert
        assertThrows(ProductValidationException.class, () -> productRepository.update("1", product1));
    }

    @Test
    @DisplayName("Should throw ProductUpdateException on unexpected error during update")
    void testUpdate_UnexpectedException() {
        // Arrange
        when(productMapper.toDto(any(Product.class))).thenReturn(productDTO1);
        doNothing().when(productValidator).validateDto(any(ProductDTO.class));
        productRepository.save(product1);

        when(productMapper.toDto(any(Product.class))).thenThrow(new RuntimeException("Unexpected error"));

        // Act & Assert
        assertThrows(ProductUpdateException.class, () -> productRepository.update("1", product1));
    }

    @Test
    @DisplayName("Should delete product successfully")
    void testDeleteById_Success() {
        // Arrange
        when(productMapper.toDto(any(Product.class))).thenReturn(productDTO1);
        doNothing().when(productValidator).validateDto(any(ProductDTO.class));
        productRepository.save(product1);

        // Act
        productRepository.deleteById("1");

        // Assert
        Optional<Product> result = productRepository.findById("1");
        assertFalse(result.isPresent());
    }

    @Test
    @DisplayName("Should throw ProductNotFoundException when deleting non-existent product")
    void testDeleteById_ProductNotFound() {
        // Arrange & Act & Assert
        assertThrows(ProductNotFoundException.class, () -> productRepository.deleteById("999"));
    }

    @Test
    @DisplayName("Should handle null product list during initialization")
    void testInit_NullProductList() {
        // Arrange
        ReflectionTestUtils.setField(productRepository, "jsonFilePath", "classpath:data/products.json");
        when(productDataSource.loadProductsFromJson(anyString())).thenReturn(null);

        // Act
        productRepository.init();

        // Assert
        List<Product> products = productRepository.findAll();
        assertTrue(products.isEmpty());
    }

    @Test
    @DisplayName("Should throw ProductDeleteException on unexpected error during deletion")
    void testDeleteById_UnexpectedException() {
        // Arrange
        when(productMapper.toDto(any(Product.class))).thenReturn(productDTO1);
        doNothing().when(productValidator).validateDto(any(ProductDTO.class));
        productRepository.save(product1);

        // Simular una excepción inesperada al intentar eliminar
        // Esto se puede lograr modificando el estado interno del repositorio
        ReflectionTestUtils.setField(productRepository, "inMemoryProducts", null);

        // Act & Assert
        ProductDeleteException exception = assertThrows(ProductDeleteException.class,
                () -> productRepository.deleteById("1"));

        assertNotNull(exception);
        assertTrue(exception.getMessage().contains("Unexpected error during product deletion"));
    }
}