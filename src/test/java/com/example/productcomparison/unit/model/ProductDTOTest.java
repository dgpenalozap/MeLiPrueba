package com.example.productcomparison.unit.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("ProductDTO Model Unit Tests")
class ProductDTOTest {

    @Test
    @DisplayName("Getters and Setters should work correctly")
    void gettersAndSetters_WorkCorrectly() {
        ProductDTO dto = new ProductDTO();
        dto.setId("dto-001");
        dto.setName("DTO Product");
        dto.setImageUrl("http://example.com/image.jpg");
        dto.setDescription("DTO Description");
        dto.setPrice(199.99);
        dto.setRating(4.7);
        Map<String, String> specs = new HashMap<>();
        specs.put("color", "blue");
        dto.setSpecifications(specs);

        assertEquals("dto-001", dto.getId());
        assertEquals("DTO Product", dto.getName());
        assertEquals("http://example.com/image.jpg", dto.getImageUrl());
        assertEquals("DTO Description", dto.getDescription());
        assertEquals(199.99, dto.getPrice());
        assertEquals(4.7, dto.getRating());
        assertEquals(specs, dto.getSpecifications());
    }

    @Test
    @DisplayName("Equals and HashCode should be consistent")
    void equalsAndHashCode_AreConsistent() {
        ProductDTO dto1 = new ProductDTO();
        dto1.setId("dto-001");
        dto1.setName("DTO Product");

        ProductDTO dto2 = new ProductDTO();
        dto2.setId("dto-001");
        dto2.setName("DTO Product");

        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    @DisplayName("ToString should contain field values")
    void toString_ContainsFieldValues() {
        ProductDTO dto = new ProductDTO();
        dto.setId("dto-001");
        dto.setName("DTO Product");

        String toString = dto.toString();

        assertTrue(toString.contains("dto-001"));
        assertTrue(toString.contains("DTO Product"));
    }

    @Test
    @DisplayName("No-args constructor should create an empty object")
    void noArgsConstructor_CreatesEmptyObject() {
        ProductDTO dto = new ProductDTO();
        assertNull(dto.getId());
        assertNull(dto.getName());
        assertEquals(0.0, dto.getPrice());
    }
}
