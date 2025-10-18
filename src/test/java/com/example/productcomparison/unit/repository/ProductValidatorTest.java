package com.example.productcomparison.unit.repository;

import com.example.productcomparison.model.Product;
import com.example.productcomparison.model.ProductDTO;
import com.example.productcomparison.repository.ProductValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("ProductValidator Unit Tests")
class ProductValidatorTest {

    private ProductValidator productValidator;

    @BeforeEach
    void setUp() {
        productValidator = new ProductValidator();
    }

    @Test
    @DisplayName("validateDto should throw exception when DTO is null")
    void validateDto_ThrowsException_WhenDTOIsNull() {
        assertThrows(IllegalArgumentException.class, () -> productValidator.validateDto(null));
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

        assertThrows(IllegalArgumentException.class, () -> productValidator.validateDto(dto));
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

        assertThrows(IllegalArgumentException.class, () -> productValidator.validateDto(dto));
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

        assertDoesNotThrow(() -> productValidator.validateDto(dto));
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

        assertTrue(productValidator.validateProduct(product));
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

        assertFalse(productValidator.validateProduct(product));
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

        assertFalse(productValidator.validateProduct(product));
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

        assertFalse(productValidator.validateProduct(product));
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

        assertTrue(productValidator.validateProduct(product));
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

        assertTrue(productValidator.validateProduct(product));
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

        assertTrue(productValidator.validateProduct(product));
    }

    @Test
    @DisplayName("isValidPrice should return true for positive price")
    void isValidPrice_ReturnsTrue_ForPositivePrice() {
        assertTrue(productValidator.isValidPrice(100.0));
    }

    @Test
    @DisplayName("isValidPrice should return true for zero price")
    void isValidPrice_ReturnsTrue_ForZeroPrice() {
        assertTrue(productValidator.isValidPrice(0.0));
    }

    @Test
    @DisplayName("isValidPrice should return false for negative price")
    void isValidPrice_ReturnsFalse_ForNegativePrice() {
        assertFalse(productValidator.isValidPrice(-10.0));
    }

    @Test
    @DisplayName("isValidPrice should return false for null price")
    void isValidPrice_ReturnsFalse_ForNullPrice() {
        assertFalse(productValidator.isValidPrice(null));
    }

    @Test
    @DisplayName("isValidRating should return true for valid rating")
    void isValidRating_ReturnsTrue_ForValidRating() {
        assertTrue(productValidator.isValidRating(4.5));
    }

    @Test
    @DisplayName("isValidRating should return true for null rating")
    void isValidRating_ReturnsTrue_ForNullRating() {
        assertTrue(productValidator.isValidRating(null));
    }

    @Test
    @DisplayName("isValidRating should return true for rating of 0")
    void isValidRating_ReturnsTrue_ForRatingZero() {
        assertTrue(productValidator.isValidRating(0.0));
    }

    @Test
    @DisplayName("isValidRating should return true for rating of 5")
    void isValidRating_ReturnsTrue_ForRatingFive() {
        assertTrue(productValidator.isValidRating(5.0));
    }

    @Test
    @DisplayName("isValidRating should return false for rating above 5")
    void isValidRating_ReturnsFalse_ForRatingAbove5() {
        assertFalse(productValidator.isValidRating(5.5));
    }

    @Test
    @DisplayName("isValidRating should return false for rating below 0")
    void isValidRating_ReturnsFalse_ForRatingBelowZero() {
        assertFalse(productValidator.isValidRating(-0.5));
    }
}
