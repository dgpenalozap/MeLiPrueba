package com.example.productcomparison.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class AppConfig {

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    @Bean
    public OpenAPI productComparisonOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Product Comparison API")
                        .description("""
                                RESTful API for product comparison with JWT authentication.
                                
                                ## 🔐 Authentication
                                This API uses JWT Bearer token authentication.
                                
                                **Demo Users:**
                                1. **Admin** (Full Access):
                                   - Username: `admin`
                                   - Password: `admin123`
                                   - Permissions: GET, POST, PUT, DELETE
                                
                                2. **User** (Read-Only):
                                   - Username: `user`
                                   - Password: `user123`
                                   - Permissions: GET only
                                
                                **How to authenticate:**
                                1. Call `/auth/login` with username and password
                                2. Copy the token from the response
                                3. Click "Authorize" button above
                                4. Enter: `Bearer <your-token>`
                                5. Click "Authorize" and "Close"
                                
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
                ))
                .components(new Components()
                        .addSecuritySchemes("Bearer Authentication",
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                                        .description("Enter JWT token obtained from /auth/login endpoint")))
                .addSecurityItem(new SecurityRequirement().addList("Bearer Authentication"));
    }
}