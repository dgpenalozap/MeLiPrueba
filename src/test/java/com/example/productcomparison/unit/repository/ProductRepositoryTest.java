package com.example.productcomparison.unit.repository;

import com.example.productcomparison.exception.repository.*;
import com.example.productcomparison.exception.service.ProductNotFoundException;
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
import java.util.concurrent.ConcurrentHashMap;

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

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(productRepository, "jsonFilePath", "test.json");
    }

    private ProductDTO createProductDTO(String id, String name, double price, double rating) {
        return ProductDTO.builder()
                .id(id)
                .name(name)
                .price(price)
                .rating(rating)
                .build();
    }

    private ProductDTO createValidProductDTO(String id, String name) {
        return ProductDTO.builder()
                .id(id)
                .name(name)
                .price(100.0)
                .rating(4.5)
                .build();
    }

    private Product createValidProduct(String id, String name) {
        return Product.builder()
                .id(id)
                .name(name)
                .price(100.0)
                .rating(4.5)
                .build();
    }


    @Test
    @DisplayName("findAll should return a list of products")
    void findAll_ReturnsListOfProducts() {
        ProductDTO dto = createProductDTO("1", "Product 1", 100.0, 4.5);
        when(productDataSource.loadProductsFromJson("test.json")).thenReturn(List.of(dto));
        productRepository.init();

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
        productRepository.init();

        Optional<Product> product = productRepository.findById("1");

        assertTrue(product.isPresent());
        assertEquals("1", product.get().getId());
    }

    @Test
    @DisplayName("findById should return empty when not found")
    void findById_ReturnsEmpty_WhenNotFound() {
        when(productDataSource.loadProductsFromJson("test.json")).thenReturn(List.of());
        productRepository.init();

        Optional<Product> product = productRepository.findById("1");

        assertFalse(product.isPresent());
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"  ", "\t", "\n"})
    @DisplayName("findById should return empty for null or blank IDs")
    void findById_whenIdIsNullOrBlank_returnsEmpty(String invalidId) {
        when(productDataSource.loadProductsFromJson("test.json")).thenReturn(List.of());
        productRepository.init();

        Optional<Product> product = productRepository.findById(invalidId);

        assertFalse(product.isPresent());
    }

    @Test
    @DisplayName("init should throw ProductDataAccessException when data source fails")
    void init_whenDataSourceThrowsException_throwsProductDataAccessException() {
        when(productDataSource.loadProductsFromJson("test.json"))
                .thenThrow(new DataSourceInitializationException("File not found"));

        assertThrows(ProductDataAccessException.class, () -> productRepository.init());
    }

    @Test
    @DisplayName("save should throw ProductAlreadyExistsException when product already exists")
    void save_whenProductAlreadyExists_throwsProductAlreadyExistsException() {
        ProductDTO existingDto = createProductDTO("1", "Existing Product", 100.0, 4.5);
        when(productDataSource.loadProductsFromJson("test.json")).thenReturn(List.of(existingDto));
        productRepository.init();

        Product newProduct = Product.builder()
                .id("1")
                .name("New Product")
                .price(150.0)
                .rating(5.0)
                .build();

        assertThrows(ProductAlreadyExistsException.class, () -> productRepository.save(newProduct));
    }

    @Test
    @DisplayName("save should add a new product")
    void save_shouldAddNewProduct() {
        when(productDataSource.loadProductsFromJson("test.json")).thenReturn(List.of());
        productRepository.init();

        Product newProduct = Product.builder()
                .id("2")
                .name("New Product")
                .price(200.0)
                .rating(4.0)
                .build();

        Product savedProduct = productRepository.save(newProduct);

        assertNotNull(savedProduct);
        assertEquals("2", savedProduct.getId());
        Optional<Product> foundProduct = productRepository.findById("2");
        assertTrue(foundProduct.isPresent());
        assertEquals("New Product", foundProduct.get().getName());
    }

    @Test
    @DisplayName("update should modify an existing product")
    void update_shouldModifyExistingProduct() {
        ProductDTO existingDto = createProductDTO("1", "Original Product", 100.0, 4.5);
        when(productDataSource.loadProductsFromJson("test.json")).thenReturn(List.of(existingDto));
        productRepository.init();

        Product productToUpdate = Product.builder()
                .name("Updated Product")
                .price(120.0)
                .rating(4.7)
                .build();

        Product updatedProduct = productRepository.update("1", productToUpdate);

        assertNotNull(updatedProduct);
        assertEquals("1", updatedProduct.getId());
        assertEquals("Updated Product", updatedProduct.getName());
        Optional<Product> foundProduct = productRepository.findById("1");
        assertTrue(foundProduct.isPresent());
        assertEquals("Updated Product", foundProduct.get().getName());
    }

    @Test
    @DisplayName("update should throw ProductNotFoundException when product not found")
    void update_whenProductNotFound_throwsProductNotFoundException() {
        when(productDataSource.loadProductsFromJson("test.json")).thenReturn(List.of());
        productRepository.init();

        Product productToUpdate = Product.builder()
                .name("Non-existent Product")
                .price(100.0)
                .rating(4.5)
                .build();
        String nonExistentId = "999";

        assertThrows(ProductNotFoundException.class,
                () -> productRepository.update(nonExistentId, productToUpdate));
    }

    @Test
    @DisplayName("deleteById should remove an existing product")
    void deleteById_shouldRemoveExistingProduct() {
        ProductDTO existingDto = createProductDTO("1", "Product to delete", 100.0, 4.5);
        when(productDataSource.loadProductsFromJson("test.json")).thenReturn(List.of(existingDto));
        productRepository.init();
        assertTrue(productRepository.findById("1").isPresent());

        productRepository.deleteById("1");

        assertFalse(productRepository.findById("1").isPresent());
    }

    @Test
    @DisplayName("deleteById should throw ProductNotFoundException when product not found")
    void deleteById_whenProductNotFound_throwsProductNotFoundException() {
        when(productDataSource.loadProductsFromJson("test.json")).thenReturn(List.of());
        productRepository.init();
        String nonExistentId = "999";

        assertThrows(ProductNotFoundException.class,
                () -> productRepository.deleteById(nonExistentId));
    }

    @Test
    @DisplayName("save should throw ProductAlreadyExistsException when product exists")
    void save_ThrowsProductAlreadyExistsException_WhenProductExists() {
        ProductDTO dto = createValidProductDTO("1", "Product 1");
        when(productDataSource.loadProductsFromJson(anyString())).thenReturn(List.of(dto));
        productRepository.init();

        Product newProduct = createValidProduct("1", "New Product");

        assertThrows(ProductAlreadyExistsException.class, () -> productRepository.save(newProduct));
    }

    @Test
    @DisplayName("save should throw ProductValidationException when validation fails")
    void save_ThrowsProductValidationException_WhenValidationFails() {
        when(productDataSource.loadProductsFromJson(anyString())).thenReturn(List.of());
        productRepository.init();

        Product invalidProduct = Product.builder()
                .id("")
                .name("Test")
                .price(100.0)
                .rating(4.5)
                .build();

        assertThrows(ProductValidationException.class, () -> productRepository.save(invalidProduct));
    }

    @Test
    @DisplayName("save should save product successfully when valid")
    void save_SavesProduct_WhenValid() {
        when(productDataSource.loadProductsFromJson(anyString())).thenReturn(List.of());
        productRepository.init();

        Product product = createValidProduct("1", "Product 1");
        Product saved = productRepository.save(product);

        assertNotNull(saved);
        assertEquals("1", saved.getId());
        assertEquals("Product 1", saved.getName());
    }

    @Test
    @DisplayName("update should throw ProductNotFoundException when product not found")
    void update_ThrowsProductNotFoundException_WhenNotFound() {
        when(productDataSource.loadProductsFromJson(anyString())).thenReturn(List.of());
        productRepository.init();

        Product product = createValidProduct("999", "Updated Product");

        assertThrows(ProductNotFoundException.class, () -> productRepository.update("999", product));
    }

    @Test
    @DisplayName("update should throw ProductValidationException when validation fails")
    void update_ThrowsProductValidationException_WhenValidationFails() {
        ProductDTO dto = createValidProductDTO("1", "Product 1");
        when(productDataSource.loadProductsFromJson(anyString())).thenReturn(List.of(dto));
        productRepository.init();

        Product invalidProduct = Product.builder()
                .id("1")
                .name("")
                .price(100.0)
                .rating(4.5)
                .build();

        assertThrows(ProductValidationException.class, () -> productRepository.update("1", invalidProduct));
    }

    @Test
    @DisplayName("update should update product successfully when valid")
    void update_UpdatesProduct_WhenValid() {
        ProductDTO dto = createValidProductDTO("1", "Product 1");
        when(productDataSource.loadProductsFromJson(anyString())).thenReturn(List.of(dto));
        productRepository.init();

        Product updatedProduct = createValidProduct("1", "Updated Product");
        Product result = productRepository.update("1", updatedProduct);

        assertNotNull(result);
        assertEquals("1", result.getId());
        assertEquals("Updated Product", result.getName());
    }

    @Test
    @DisplayName("deleteById should throw ProductNotFoundException when product not found")
    void deleteById_ThrowsProductNotFoundException_WhenNotFound() {
        when(productDataSource.loadProductsFromJson(anyString())).thenReturn(List.of());
        productRepository.init();

        assertThrows(ProductNotFoundException.class, () -> productRepository.deleteById("999"));
    }

    @Test
    @DisplayName("deleteById should delete product successfully when exists")
    void deleteById_DeletesProduct_WhenExists() {
        ProductDTO dto = createValidProductDTO("1", "Product 1");
        when(productDataSource.loadProductsFromJson(anyString())).thenReturn(List.of(dto));
        productRepository.init();

        assertDoesNotThrow(() -> productRepository.deleteById("1"));

        Optional<Product> deleted = productRepository.findById("1");
        assertFalse(deleted.isPresent());
    }

    @Test
    @DisplayName("init should throw ProductDataAccessException when data source fails")
    void init_ThrowsProductDataAccessException_WhenDataSourceFails() {
        when(productDataSource.loadProductsFromJson(anyString()))
                .thenThrow(new DataSourceInitializationException("File not found"));

        assertThrows(ProductDataAccessException.class, () -> productRepository.init());
    }

    @Test
    @DisplayName("init should handle invalid products gracefully")
    void init_HandlesInvalidProductsGracefully() {
        ProductDTO validDto = createValidProductDTO("1", "Valid Product");
        ProductDTO invalidDto = ProductDTO.builder()
                .id("2")
                .name("Invalid Product")
                .price(-100.0)
                .rating(4.5)
                .build();

        when(productDataSource.loadProductsFromJson(anyString()))
                .thenReturn(List.of(validDto, invalidDto));

        assertDoesNotThrow(() -> productRepository.init());

        List<Product> products = productRepository.findAll();
        assertEquals(1, products.size());
        assertEquals("1", products.get(0).getId());
    }

    @Test
    @DisplayName("findById should return empty for null or blank id")
    void findById_ReturnsEmpty_ForNullOrBlankId() {
        when(productDataSource.loadProductsFromJson(anyString())).thenReturn(List.of());
        productRepository.init();

        Optional<Product> nullResult = productRepository.findById(null);
        assertFalse(nullResult.isPresent());

        Optional<Product> blankResult = productRepository.findById("");
        assertFalse(blankResult.isPresent());

        Optional<Product> whitespaceResult = productRepository.findById("   ");
        assertFalse(whitespaceResult.isPresent());
    }

    @Test
    @DisplayName("findAll should return empty list when no products loaded")
    void findAll_ReturnsEmptyList_WhenNoProducts() {
        when(productDataSource.loadProductsFromJson(anyString())).thenReturn(List.of());
        productRepository.init();

        List<Product> products = productRepository.findAll();
        assertNotNull(products);
        assertTrue(products.isEmpty());
    }


    @Test
    @DisplayName("save lanza ProductSaveException en error inesperado")
    void save_ThrowsProductSaveException_OnUnexpectedError() {
        // Arrange
        when(productDataSource.loadProductsFromJson(anyString())).thenReturn(List.of());
        productRepository.init();
        Product product = createValidProduct("1", "Producto válido");
        doThrow(new RuntimeException("Error inesperado")).when(productMapper).validateDto(any());

        // Act & Assert
        ProductSaveException ex = assertThrows(ProductSaveException.class, () -> productRepository.save(product));
        assertEquals("1", ex.getProductId());
        assertTrue(ex.getMessage().contains("Unexpected error during product save"));
    }

    @Test
    @DisplayName("init maneja excepción general durante la carga inicial")
    void init_HandlesGeneralException_DuringInitialDataLoad() {
        // Arrange
        ProductDTO validDto = createValidProductDTO("1", "Producto válido");
        List<ProductDTO> dtos = List.of(validDto);
        when(productDataSource.loadProductsFromJson(anyString())).thenReturn(dtos);

        // Simular error genérico durante la carga de datos (después de obtener los DTOs)
        doThrow(new RuntimeException("Error inesperado")).when(productMapper).toDomain(any(ProductDTO.class));

        // Act & Assert
        ProductDataAccessException ex = assertThrows(ProductDataAccessException.class,
                () -> productRepository.init());

        assertTrue(ex.getMessage().contains("Unexpected error during initial data load"));
        verify(productDataSource).loadProductsFromJson(anyString());
    }

    @Test
    @DisplayName("init omite productos inválidos durante la carga")
    void init_SkipsInvalidProducts_DuringMapping() {
        // Arrange
        ProductDTO validDto = createValidProductDTO("1", "Producto válido");
        ProductDTO invalidDto = createValidProductDTO("2", "Producto inválido");
        List<ProductDTO> dtos = List.of(validDto, invalidDto);

        when(productDataSource.loadProductsFromJson(anyString())).thenReturn(dtos);

        // Usamos doAnswer para manejar dinámicamente las llamadas según el ID
        doAnswer(invocation -> {
            ProductDTO dto = invocation.getArgument(0);
            if (dto.getId().equals("1")) {
                return createValidProduct("1", "Producto válido");
            } else {
                throw new IllegalArgumentException("Formato inválido");
            }
        }).when(productMapper).toDomain(any(ProductDTO.class));

        // Act
        productRepository.init();

        // Assert
        List<Product> products = productRepository.findAll();
        assertEquals(1, products.size());
        assertEquals("1", products.get(0).getId());

        // Verificar que se llamó dos veces a toDomain
        verify(productMapper, times(2)).toDomain(any(ProductDTO.class));
    }

    @Test
    @DisplayName("update lanza ProductUpdateException en error inesperado")
    void update_ThrowsProductUpdateException_OnUnexpectedError() {
        // Arrange
        ProductDTO dto = createValidProductDTO("1", "Producto 1");
        when(productDataSource.loadProductsFromJson(anyString())).thenReturn(List.of(dto));
        productRepository.init();
        Product product = createValidProduct("1", "Producto actualizado");
        doThrow(new RuntimeException("Error inesperado")).when(productMapper).validateDto(any());

        // Act & Assert
        ProductUpdateException ex = assertThrows(ProductUpdateException.class, () -> productRepository.update("1", product));
        assertEquals("1", ex.getProductId());
        assertTrue(ex.getMessage().contains("Unexpected error during product update"));
    }

    @Test
    @DisplayName("deleteById lanza ProductDeleteException en error inesperado")
    void deleteById_ThrowsProductDeleteException_OnUnexpectedError() {
        // Arrange
        ProductDTO dto = createValidProductDTO("1", "Producto 1");
        when(productDataSource.loadProductsFromJson(anyString())).thenReturn(List.of(dto));
        productRepository.init();

        // Obtener y modificar el mapa interno con un spy
        ConcurrentHashMap<String, Product> inMemoryMap =
                (ConcurrentHashMap<String, Product>) ReflectionTestUtils.getField(productRepository, "inMemoryProducts");
        ConcurrentHashMap<String, Product> spyMap = spy(inMemoryMap);

        // Configurar el spy para lanzar excepción cuando se llame al método remove
        doThrow(new RuntimeException("Error inesperado")).when(spyMap).remove("1");
        ReflectionTestUtils.setField(productRepository, "inMemoryProducts", spyMap);

        // Act & Assert
        ProductDeleteException ex = assertThrows(ProductDeleteException.class, () -> productRepository.deleteById("1"));
        assertEquals("1", ex.getProductId());
        assertTrue(ex.getMessage().contains("Unexpected error during product deletion"));
    }
}
