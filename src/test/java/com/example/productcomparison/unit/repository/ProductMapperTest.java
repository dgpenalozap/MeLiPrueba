package com.example.productcomparison.unit.repository;

import com.example.productcomparison.model.Product;
import com.example.productcomparison.model.ProductDTO;
import com.example.productcomparison.repository.ProductMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("ProductMapper Unit Tests")
class ProductMapperTest {

    private ProductMapper productMapper;

    @BeforeEach
    void setUp() {
        productMapper = Mappers.getMapper(ProductMapper.class);
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
    @DisplayName("toDomain should handle null values")
    void toDomain_HandlesNullValues() {
        ProductDTO dto = ProductDTO.builder()
                .id("1")
                .name("Test Product")
                .build();

        Product product = productMapper.toDomain(dto);

        assertNotNull(product);
        assertEquals("1", product.getId());
        assertEquals("Test Product", product.getName());
    }

    @Test
    @DisplayName("toDto should handle null values")
    void toDto_HandlesNullValues() {
        Product product = Product.builder()
                .id("1")
                .name("Test Product")
                .build();

        ProductDTO dto = productMapper.toDto(product);

        assertNotNull(dto);
        assertEquals("1", dto.getId());
        assertEquals("Test Product", dto.getName());
    }
}
