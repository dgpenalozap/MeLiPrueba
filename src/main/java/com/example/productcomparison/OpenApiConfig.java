package com.example.productcomparison;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * OpenAPI/Swagger configuration for API documentation.
 * 
 * Access documentation at:
 * - Swagger UI: http://localhost:8080/swagger-ui.html
 * - OpenAPI JSON: http://localhost:8080/v3/api-docs
 */
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI productComparisonOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Product Comparison API")
                        .description("""
                                RESTful API for product comparison with advanced filtering, search, and comparison capabilities.
                                
                                ## Features
                                - 🔍 Search products by name
                                - 💰 Filter by price range
                                - ⭐ Filter by rating
                                - 🏷️ Filter by category
                                - 🔧 Filter by specifications
                                - 📊 Sort by price or rating
                                - 🏆 Get top-rated products
                                - 🔄 Compare multiple products
                                
                                ## Database
                                Contains **45 products** across **13 categories** including:
                                Laptops, Smartphones, Tablets, Monitors, Keyboards, Mice, Headphones, 
                                Cameras, Smartwatches, Speakers, Networking, Storage, and Printers.
                                
                                ## Architecture
                                Built following **SOLID principles** with:
                                - Dependency Inversion for testability
                                - Clean architecture with clear separation of concerns
                                - Lombok for reduced boilerplate
                                """)
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Product Comparison Team")
                                .email("dgpenalozap@gmail.com")
                                .url("https://contact@example.comgithub.com/example/product-comparison"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")))
                .servers(List.of(
                        new Server()
                                .url("http://localhost:8080")
                                .description("Local development server")
                ));
    }
}
