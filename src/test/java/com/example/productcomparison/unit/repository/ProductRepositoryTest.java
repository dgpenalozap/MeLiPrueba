package com.example.productcomparison.unit.repository;

import com.example.productcomparison.exception.DataSourceInitializationException;
import com.example.productcomparison.exception.ProductDataAccessException;
import com.example.productcomparison.model.Product;
import com.example.productcomparison.model.ProductDTO;
import com.example.productcomparison.repository.ProductDataSource;
import com.example.productcomparison.repository.ProductMapper;
import com.example.productcomparison.repository.ProductRepository;
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

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("ProductRepository Unit Tests")
class ProductRepositoryTest {

    @Mock
    private ProductDataSource productDataSource;

    @Spy
    private ProductMapper productMapper;

    @InjectMocks
    private ProductRepository productRepository;

    private static final String ERROR_PRODUCT_EXISTS = "Product with ID 1 already exists";
    private static final String ERROR_PRODUCT_NOT_FOUND = "Product with ID %s not found";


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
        // Arrange
        ProductDTO dto = createProductDTO("1", "Product 1", 100.0, 4.5);
        when(productDataSource.loadProductsFromJson("test.json")).thenReturn(List.of(dto));
        productRepository.init();

        // Act
        List<Product> products = productRepository.findAll();

        // Assert
        assertNotNull(products);
        assertEquals(1, products.size());
        assertEquals("1", products.get(0).getId());
    }

    @Test
    @DisplayName("findById should return a product when found")
    void findById_ReturnsProduct_WhenFound() {
        // Arrange
        ProductDTO dto = createProductDTO("1", "Product 1", 100.0, 4.5);
        when(productDataSource.loadProductsFromJson("test.json")).thenReturn(List.of(dto));
        productRepository.init();

        // Act
        Optional<Product> product = productRepository.findById("1");

        // Assert
        assertTrue(product.isPresent());
        assertEquals("1", product.get().getId());
    }

    @Test
    @DisplayName("findById should return empty when not found")
    void findById_ReturnsEmpty_WhenNotFound() {
        // Arrange
        when(productDataSource.loadProductsFromJson("test.json")).thenReturn(List.of());
        productRepository.init();

        // Act
        Optional<Product> product = productRepository.findById("1");

        // Assert
        assertFalse(product.isPresent());
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"  ", "\t", "\n"})
    @DisplayName("findById should return empty for null or blank IDs")
    void findById_whenIdIsNullOrBlank_returnsEmpty(String invalidId) {
        // Arrange
        productRepository.init();

        // Act
        Optional<Product> product = productRepository.findById(invalidId);

        // Assert
        assertFalse(product.isPresent());
    }

    @Test
    @DisplayName("init should throw ProductDataAccessException when data source fails")
    void init_whenDataSourceThrowsException_throwsProductDataAccessException() {
        // Arrange
        when(productDataSource.loadProductsFromJson("test.json")).thenThrow(new DataSourceInitializationException("File not found"));

        // Act & Assert
        assertThrows(ProductDataAccessException.class, () -> productRepository.init());
    }

    @Test
    @DisplayName("save should throw IllegalArgumentException when product already exists")
    void save_whenProductAlreadyExists_throwsIllegalArgumentException() {
        // Arrange
        ProductDTO existingDto = createProductDTO("1", "Existing Product", 100.0, 4.5);
        when(productDataSource.loadProductsFromJson("test.json")).thenReturn(List.of(existingDto));
        productRepository.init();
        Product newProduct = Product.builder().id("1").name("New Product").price(150.0).rating(5.0).build();

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> productRepository.save(newProduct));
        assertEquals(ERROR_PRODUCT_EXISTS, exception.getMessage());
    }

    @Test
    @DisplayName("save should add a new product")
    void save_shouldAddNewProduct() {
        // Arrange
        productRepository.init(); // Iniciar con mapa vacío
        Product newProduct = Product.builder().id("2").name("New Product").price(200.0).rating(4.0).build();

        // Act
        Product savedProduct = productRepository.save(newProduct);

        // Assert
        assertNotNull(savedProduct);
        assertEquals("2", savedProduct.getId());
        Optional<Product> foundProduct = productRepository.findById("2");
        assertTrue(foundProduct.isPresent());
        assertEquals("New Product", foundProduct.get().getName());
    }

    @Test
    @DisplayName("update should modify an existing product")
    void update_shouldModifyExistingProduct() {
        // Arrange
        ProductDTO existingDto = createProductDTO("1", "Original Product", 100.0, 4.5);
        when(productDataSource.loadProductsFromJson("test.json")).thenReturn(List.of(existingDto));
        productRepository.init();
        Product productToUpdate = Product.builder().name("Updated Product").price(120.0).rating(4.7).build();

        // Act
        Product updatedProduct = productRepository.update("1", productToUpdate);

        // Assert
        assertNotNull(updatedProduct);
        assertEquals("1", updatedProduct.getId());
        assertEquals("Updated Product", updatedProduct.getName());
        Optional<Product> foundProduct = productRepository.findById("1");
        assertTrue(foundProduct.isPresent());
        assertEquals("Updated Product", foundProduct.get().getName());
    }

    @Test
    @DisplayName("update should throw IllegalArgumentException when product not found")
    void update_whenProductNotFound_throwsIllegalArgumentException() {
        // Arrange
        productRepository.init();
        Product productToUpdate = Product.builder().name("Non-existent Product").build();
        String nonExistentId = "999";

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> productRepository.update(nonExistentId, productToUpdate));
        assertEquals(String.format(ERROR_PRODUCT_NOT_FOUND, nonExistentId), exception.getMessage());
    }

    @Test
    @DisplayName("deleteById should remove an existing product")
    void deleteById_shouldRemoveExistingProduct() {
        // Arrange
        ProductDTO existingDto = createProductDTO("1", "Product to delete", 100.0, 4.5);
        when(productDataSource.loadProductsFromJson("test.json")).thenReturn(List.of(existingDto));
        productRepository.init();
        assertTrue(productRepository.findById("1").isPresent());

        // Act
        productRepository.deleteById("1");

        // Assert
        assertFalse(productRepository.findById("1").isPresent());
    }

    @Test
    @DisplayName("deleteById should throw IllegalArgumentException when product not found")
    void deleteById_whenProductNotFound_throwsIllegalArgumentException() {
        // Arrange
        productRepository.init();
        String nonExistentId = "999";

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> productRepository.deleteById(nonExistentId));
        assertEquals(String.format(ERROR_PRODUCT_NOT_FOUND, nonExistentId), exception.getMessage());
    }
}