package com.example.productcomparison.service;

import com.example.productcomparison.model.Product;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

/**
 * Service for generating random products using AI.
 * Uses LangChain4j for AI-powered product generation.
 */
@Service
@Slf4j
public class AIProductGenerator {

    @Value("${openai.api.key:}")
    private String openAiApiKey;

    private final Random random = new Random();
    private final String[] categories = {
        "Laptops", "Smartphones", "Tablets", "Monitors", "Keyboards",
        "Mice", "Headphones", "Cameras", "Smartwatches", "Speakers",
        "Networking", "Storage", "Printers"
    };

    /**
     * Generates a random product using AI if API key is available,
     * otherwise generates a random product with predefined data.
     *
     * @return randomly generated product
     */
    public Product generateRandomProduct() {
        if (openAiApiKey != null && !openAiApiKey.isBlank() && !openAiApiKey.equals("demo-key")) {
            return generateAIProduct();
        } else {
            log.info("OpenAI API key not configured, using fallback random generation");
            return generateFallbackProduct();
        }
    }

    private Product generateAIProduct() {
        try {
            ChatLanguageModel model = OpenAiChatModel.builder()
                    .apiKey(openAiApiKey)
                    .modelName("gpt-3.5-turbo")
                    .build();

            String category = categories[random.nextInt(categories.length)];
            String prompt = String.format(
                "Generate a realistic product for category: %s. " +
                "Respond with ONLY the product name (10-30 characters), nothing else.",
                category
            );

            String productName = model.generate(prompt).trim();
            
            return buildProduct(productName, category);
        } catch (Exception e) {
            log.error("Failed to generate AI product, using fallback", e);
            return generateFallbackProduct();
        }
    }

    private Product generateFallbackProduct() {
        String category = categories[random.nextInt(categories.length)];
        String[] prefixes = {"Pro", "Ultra", "Premium", "Elite", "Advanced", "Smart"};
        String[] suffixes = {"X", "Plus", "Max", "Air", "Lite", "Neo"};
        
        String prefix = prefixes[random.nextInt(prefixes.length)];
        String suffix = suffixes[random.nextInt(suffixes.length)];
        String productName = String.format("%s %s %s", prefix, category.substring(0, category.length() - 1), suffix);
        
        return buildProduct(productName, category);
    }

    private Product buildProduct(String name, String category) {
        String id = "gen-" + UUID.randomUUID().toString().substring(0, 8);
        double price = Math.round((100 + random.nextDouble() * 1900) * 100.0) / 100.0;
        double rating = Math.round((3.0 + random.nextDouble() * 2.0) * 10.0) / 10.0;
        
        Map<String, String> specs = new HashMap<>();
        specs.put("category", category);
        specs.put("generated", "AI Generated");
        specs.put("brand", "GenBrand");
        
        return Product.builder()
                .id(id)
                .name(name)
                .imageUrl("https://via.placeholder.com/400x300?text=" + name.replace(" ", "+"))
                .description("AI-generated product: " + name)
                .price(price)
                .rating(rating)
                .specifications(specs)
                .build();
    }
}
