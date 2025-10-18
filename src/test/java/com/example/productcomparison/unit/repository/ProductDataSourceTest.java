package com.example.productcomparison.unit.repository;

import com.example.productcomparison.exception.repository.DataSourceInitializationException;
import com.example.productcomparison.model.ProductDTO;
import com.example.productcomparison.repository.ProductDataSource;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("ProductDataSource Unit Tests")
class ProductDataSourceTest {

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private ResourceLoader resourceLoader;

    @Mock
    private Resource resource;

    private ProductDataSource productDataSource;

    private static final String FILE_NOT_FOUND_ERROR = "The data source file was not found at path: ";
    private static final String FILE_READ_ERROR = "Error reading data source file at path: ";
    private static final String INVALID_PATH_ERROR = "jsonFilePath cannot be null or empty";

    @BeforeEach
    void setUp() {
        productDataSource = new ProductDataSource(objectMapper, resourceLoader);
    }

    @Test
    @DisplayName("loadProducts should return a list of products")
    void loadProducts_ReturnsListOfProductsFromJsonFromJsonFromJson() throws IOException {
        // Arrange
        String jsonContent = "[{\"id\":\"1\",\"name\":\"Product 1\"}]";
        InputStream inputStream = new ByteArrayInputStream(jsonContent.getBytes());
        List<ProductDTO> expectedProducts = List.of(new ProductDTO());

        when(resourceLoader.getResource(anyString())).thenReturn(resource);
        when(resource.exists()).thenReturn(true);
        when(resource.getInputStream()).thenReturn(inputStream);
        when(objectMapper.readValue(any(InputStream.class), ArgumentMatchers.<TypeReference<List<ProductDTO>>>any())).thenReturn(expectedProducts);

        // Act
        List<ProductDTO> actualProducts = productDataSource.loadProductsFromJson("classpath:products.json");

        // Assert
        assertNotNull(actualProducts);
        assertEquals(expectedProducts, actualProducts);
    }

    @Test
    @DisplayName("loadProductsFromJson should throw DataSourceInitializationException when getting input stream fails")
    void loadProductsFromJson_whenInputStreamThrowsIOException_throwsDataSourceInitializationException() throws IOException {
        // Arrange
        String filePath = "classpath:products.json";
        when(resourceLoader.getResource(anyString())).thenReturn(resource);
        when(resource.exists()).thenReturn(true);
        when(resource.getInputStream()).thenThrow(new IOException("File not found"));

        // Act
        DataSourceInitializationException exception = assertThrows(
                DataSourceInitializationException.class,
                () -> productDataSource.loadProductsFromJson(filePath)
        );

        // Assert
        assertTrue(exception.getMessage().contains(FILE_READ_ERROR));
    }

    @Test
    @DisplayName("loadProductsFromJson should throw DataSourceInitializationException on JSON parsing error")
    void loadProductsFromJson_whenParsingThrowsIOException_throwsDataSourceInitializationException() throws IOException {
        // Arrange
        String filePath = "classpath:products.json";
        String invalidJsonContent = "invalid-json";
        InputStream inputStream = new ByteArrayInputStream(invalidJsonContent.getBytes());

        when(resourceLoader.getResource(anyString())).thenReturn(resource);
        when(resource.exists()).thenReturn(true);
        when(resource.getInputStream()).thenReturn(inputStream);
        when(objectMapper.readValue(any(InputStream.class), ArgumentMatchers.<TypeReference<List<ProductDTO>>>any()))
                .thenThrow(new IOException("JSON parsing error"));

        // Act
        DataSourceInitializationException exception = assertThrows(
                DataSourceInitializationException.class,
                () -> productDataSource.loadProductsFromJson(filePath)
        );

        // Assert
        assertTrue(exception.getMessage().contains(FILE_READ_ERROR));
    }

    @Test
    @DisplayName("loadProductsFromJson should throw DataSourceInitializationException when file does not exist")
    void loadProductsFromJson_whenFileDoesNotExist_throwsDataSourceInitializationException() {
        // Arrange
        String filePath = "classpath:nonexistent.json";
        when(resourceLoader.getResource(anyString())).thenReturn(resource);
        when(resource.exists()).thenReturn(false);

        // Act
        DataSourceInitializationException exception = assertThrows(
                DataSourceInitializationException.class,
                () -> productDataSource.loadProductsFromJson(filePath)
        );

        // Assert
        assertTrue(exception.getMessage().contains(FILE_NOT_FOUND_ERROR));
    }

    @Test
    @DisplayName("loadProductsFromJson should throw IllegalArgumentException when jsonFilePath is null")
    void loadProductsFromJson_whenJsonFilePathIsNull_throwsIllegalArgumentException() {
        // Arrange
        // No specific arrangement needed for this test case

        // Act
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> productDataSource.loadProductsFromJson(null)
        );

        // Assert
        assertEquals(INVALID_PATH_ERROR, exception.getMessage());
    }

}