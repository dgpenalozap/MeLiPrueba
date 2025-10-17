package com.example.productcomparison.repository;

import com.example.productcomparison.model.ProductDTO;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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

    @BeforeEach
    void setUp() {
        productDataSource = new ProductDataSource(objectMapper, resourceLoader);
    }

    @Test
    @DisplayName("loadProducts should return a list of products")
    void loadProducts_ReturnsListOfProductsFromJsonFromJsonFromJson() throws IOException {
        String jsonContent = "[{\"id\":\"1\",\"name\":\"Product 1\"}]";
        InputStream inputStream = new ByteArrayInputStream(jsonContent.getBytes());
        List<ProductDTO> expectedProducts = List.of(new ProductDTO());

        when(resourceLoader.getResource(anyString())).thenReturn(resource);
        when(resource.exists()).thenReturn(true);
        when(resource.getInputStream()).thenReturn(inputStream);
        when(objectMapper.readValue(any(InputStream.class), any(TypeReference.class))).thenReturn(expectedProducts);

        List<ProductDTO> actualProducts = productDataSource.loadProductsFromJson("classpath:products.json");

        assertNotNull(actualProducts);
        assertEquals(expectedProducts, actualProducts);
    }

    @Test
    @DisplayName("loadProductsFromJson should throw DataSourceInitializationException when getting input stream fails")
    void loadProductsFromJson_whenInputStreamThrowsIOException_throwsDataSourceInitializationException() throws IOException {
        when(resourceLoader.getResource(anyString())).thenReturn(resource);
        when(resource.exists()).thenReturn(true);
        when(resource.getInputStream()).thenThrow(new IOException("File not found"));

        assertThrows(com.example.productcomparison.exception.DataSourceInitializationException.class, () -> productDataSource.loadProductsFromJson("classpath:products.json"));
    }

    @Test
    @DisplayName("loadProductsFromJson should throw DataSourceInitializationException on JSON parsing error")
    void loadProductsFromJson_whenParsingThrowsIOException_throwsDataSourceInitializationException() throws IOException {
        String invalidJsonContent = "invalid-json";
        InputStream inputStream = new ByteArrayInputStream(invalidJsonContent.getBytes());

        when(resourceLoader.getResource(anyString())).thenReturn(resource);
        when(resource.exists()).thenReturn(true);
        when(resource.getInputStream()).thenReturn(inputStream);
        when(objectMapper.readValue(any(InputStream.class), any(TypeReference.class))).thenThrow(new IOException("JSON parsing error"));

        assertThrows(com.example.productcomparison.exception.DataSourceInitializationException.class, () -> productDataSource.loadProductsFromJson("classpath:products.json"));
    }

    @Test
    @DisplayName("loadProductsFromJson should throw DataSourceInitializationException when file does not exist")
    void loadProductsFromJson_whenFileDoesNotExist_throwsDataSourceInitializationException() {
        when(resourceLoader.getResource(anyString())).thenReturn(resource);
        when(resource.exists()).thenReturn(false);

        assertThrows(com.example.productcomparison.exception.DataSourceInitializationException.class, () -> productDataSource.loadProductsFromJson("classpath:nonexistent.json"));
    }
}
