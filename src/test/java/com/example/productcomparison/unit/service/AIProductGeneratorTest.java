package com.example.productcomparison.unit.service;

import com.example.productcomparison.model.Product;
import com.example.productcomparison.service.AIProductGenerator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("AIProductGenerator Unit Tests")
class AIProductGeneratorTest {

    @InjectMocks
    private AIProductGenerator aiProductGenerator;

    @Test
    @DisplayName("generateRandomProduct should generate product with fallback when no API key")
    void generateRandomProduct_NoApiKey_GeneratesFallbackProduct() {
        ReflectionTestUtils.setField(aiProductGenerator, "openAiApiKey", "");
        
        Product product = aiProductGenerator.generateRandomProduct();
        
        assertNotNull(product);
        assertNotNull(product.getId());
        assertNotNull(product.getName());
        assertTrue(product.getPrice() > 0);
        assertTrue(product.getRating() >= 0 && product.getRating() <= 5);
        assertNotNull(product.getSpecifications());
        assertTrue(product.getSpecifications().containsKey("category"));
    }

    @Test
    @DisplayName("generateRandomProduct should generate product with demo key")
    void generateRandomProduct_DemoKey_GeneratesFallbackProduct() {
        ReflectionTestUtils.setField(aiProductGenerator, "openAiApiKey", "demo-key");
        
        Product product = aiProductGenerator.generateRandomProduct();
        
        assertNotNull(product);
        assertNotNull(product.getId());
        assertNotNull(product.getName());
        assertTrue(product.getPrice() > 0);
        assertTrue(product.getRating() >= 0 && product.getRating() <= 5);
    }

    @Test
    @DisplayName("generateRandomProduct should create valid specifications")
    void generateRandomProduct_CreatesValidSpecifications() {
        ReflectionTestUtils.setField(aiProductGenerator, "openAiApiKey", "");
        
        Product product = aiProductGenerator.generateRandomProduct();
        
        assertNotNull(product.getSpecifications());
        assertEquals("AI Generated", product.getSpecifications().get("generated"));
        assertEquals("GenBrand", product.getSpecifications().get("brand"));
        assertNotNull(product.getSpecifications().get("category"));
    }

    @Test
    @DisplayName("generateRandomProduct should create valid price range")
    void generateRandomProduct_CreatesValidPriceRange() {
        ReflectionTestUtils.setField(aiProductGenerator, "openAiApiKey", "");
        
        for (int i = 0; i < 10; i++) {
            Product product = aiProductGenerator.generateRandomProduct();
            assertTrue(product.getPrice() >= 100 && product.getPrice() <= 2000,
                    "Price should be between 100 and 2000, but was: " + product.getPrice());
        }
    }

    @Test
    @DisplayName("generateRandomProduct should create valid rating range")
    void generateRandomProduct_CreatesValidRatingRange() {
        ReflectionTestUtils.setField(aiProductGenerator, "openAiApiKey", "");
        
        for (int i = 0; i < 10; i++) {
            Product product = aiProductGenerator.generateRandomProduct();
            assertTrue(product.getRating() >= 3.0 && product.getRating() <= 5.0,
                    "Rating should be between 3.0 and 5.0, but was: " + product.getRating());
        }
    }
}
