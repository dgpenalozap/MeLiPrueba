package com.example.productcomparison.unit.repository;

import com.example.productcomparison.model.Product;
import com.example.productcomparison.model.ProductDTO;
import com.example.productcomparison.repository.ProductMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("ProductMapper Unit Tests")
class ProductMapperTest {

    private ProductMapper productMapper;

    @BeforeEach
    void setUp() {
        productMapper = new ProductMapper();
    }

    @Test
    @DisplayName("toDomain should convert ProductDTO to Product")
    void toDomain_ConvertsProductDTOToProduct() {
        Map<String, String> specs = new HashMap<>();
        specs.put("color", "blue");
        
        ProductDTO dto = ProductDTO.builder()
                .id("1")
                .name("Test Product")
                .imageUrl("http://example.com/image.jpg")
                .description("Test Description")
                .price(99.99)
                .rating(4.5)
                .specifications(specs)
                .build();

        Product product = productMapper.toDomain(dto);

        assertNotNull(product);
        assertEquals("1", product.getId());
        assertEquals("Test Product", product.getName());
        assertEquals("http://example.com/image.jpg", product.getImageUrl());
        assertEquals("Test Description", product.getDescription());
        assertEquals(99.99, product.getPrice());
        assertEquals(4.5, product.getRating());
        assertEquals(specs, product.getSpecifications());
    }

    @Test
    @DisplayName("toDomain should throw exception when DTO is null")
    void toDomain_ThrowsException_WhenDTOIsNull() {
        assertThrows(IllegalArgumentException.class, () -> productMapper.toDomain(null));
    }

    @Test
    @DisplayName("toDomain should throw exception when ID is null")
    void toDomain_ThrowsException_WhenIdIsNull() {
        ProductDTO dto = ProductDTO.builder()
                .name("Test Product")
                .price(99.99)
                .rating(4.5)
                .build();

        assertThrows(IllegalArgumentException.class, () -> productMapper.toDomain(dto));
    }

    @Test
    @DisplayName("toDomain should throw exception when name is null")
    void toDomain_ThrowsException_WhenNameIsNull() {
        ProductDTO dto = ProductDTO.builder()
                .id("1")
                .price(99.99)
                .rating(4.5)
                .build();

        assertThrows(IllegalArgumentException.class, () -> productMapper.toDomain(dto));
    }

    @Test
    @DisplayName("toDto should convert Product to ProductDTO")
    void toDto_ConvertsProductToProductDTO() {
        Map<String, String> specs = new HashMap<>();
        specs.put("color", "red");

        Product product = Product.builder()
                .id("2")
                .name("Another Product")
                .imageUrl("http://example.com/image2.jpg")
                .description("Another Description")
                .price(149.99)
                .rating(4.8)
                .specifications(specs)
                .build();

        ProductDTO dto = productMapper.toDto(product);

        assertNotNull(dto);
        assertEquals("2", dto.getId());
        assertEquals("Another Product", dto.getName());
        assertEquals("http://example.com/image2.jpg", dto.getImageUrl());
        assertEquals("Another Description", dto.getDescription());
        assertEquals(149.99, dto.getPrice());
        assertEquals(4.8, dto.getRating());
        assertEquals(specs, dto.getSpecifications());
    }

    @Test
    @DisplayName("validateDto should throw exception when DTO is null")
    void validateDto_ThrowsException_WhenDTOIsNull() {
        assertThrows(IllegalArgumentException.class, () -> productMapper.validateDto(null));
    }

    @Test
    @DisplayName("validateDto should throw exception when ID is empty")
    void validateDto_ThrowsException_WhenIdIsEmpty() {
        ProductDTO dto = ProductDTO.builder()
                .id("")
                .name("Test Product")
                .price(99.99)
                .rating(4.5)
                .build();

        assertThrows(IllegalArgumentException.class, () -> productMapper.validateDto(dto));
    }

    @Test
    @DisplayName("validateDto should throw exception when name is empty")
    void validateDto_ThrowsException_WhenNameIsEmpty() {
        ProductDTO dto = ProductDTO.builder()
                .id("1")
                .name("")
                .price(99.99)
                .rating(4.5)
                .build();

        assertThrows(IllegalArgumentException.class, () -> productMapper.validateDto(dto));
    }

    @Test
    @DisplayName("validateDto should pass for valid DTO")
    void validateDto_Passes_ForValidDTO() {
        ProductDTO dto = ProductDTO.builder()
                .id("1")
                .name("Test Product")
                .price(99.99)
                .rating(4.5)
                .build();

        assertDoesNotThrow(() -> productMapper.validateDto(dto));
    }

    @Test
    @DisplayName("validateProduct should return true for valid product")
    void validateProduct_ReturnsTrue_ForValidProduct() {
        Product product = Product.builder()
                .id("1")
                .name("Test Product")
                .price(99.99)
                .rating(4.5)
                .build();

        assertTrue(productMapper.validateProduct(product));
    }

    @Test
    @DisplayName("validateProduct should return false for negative price")
    void validateProduct_ReturnsFalse_ForNegativePrice() {
        Product product = Product.builder()
                .id("1")
                .name("Test Product")
                .price(-10.0)
                .rating(4.5)
                .build();

        assertFalse(productMapper.validateProduct(product));
    }

    @Test
    @DisplayName("validateProduct should return false for rating above 5")
    void validateProduct_ReturnsFalse_ForRatingAbove5() {
        Product product = Product.builder()
                .id("1")
                .name("Test Product")
                .price(99.99)
                .rating(5.5)
                .build();

        assertFalse(productMapper.validateProduct(product));
    }

    @Test
    @DisplayName("validateProduct should return false for rating below 0")
    void validateProduct_ReturnsFalse_ForRatingBelowZero() {
        Product product = Product.builder()
                .id("1")
                .name("Test Product")
                .price(99.99)
                .rating(-1.0)
                .build();

        assertFalse(productMapper.validateProduct(product));
    }

    @Test
    @DisplayName("validateProduct should return true for rating of 0")
    void validateProduct_ReturnsTrue_ForRatingZero() {
        Product product = Product.builder()
                .id("1")
                .name("Test Product")
                .price(99.99)
                .rating(0.0)
                .build();

        assertTrue(productMapper.validateProduct(product));
    }

    @Test
    @DisplayName("validateProduct should return true for rating of 5")
    void validateProduct_ReturnsTrue_ForRatingFive() {
        Product product = Product.builder()
                .id("1")
                .name("Test Product")
                .price(99.99)
                .rating(5.0)
                .build();

        assertTrue(productMapper.validateProduct(product));
    }

    @Test
    @DisplayName("validateProduct should return true for price of 0")
    void validateProduct_ReturnsTrue_ForPriceZero() {
        Product product = Product.builder()
                .id("1")
                .name("Test Product")
                .price(0.0)
                .rating(4.5)
                .build();

        assertTrue(productMapper.validateProduct(product));
    }
}
