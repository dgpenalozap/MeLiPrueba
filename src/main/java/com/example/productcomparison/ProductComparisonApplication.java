package com.example.productcomparison;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;
import org.springframework.context.annotation.Bean;

/**
 * Main Spring Boot Application.
 * 
 * Dependency Injection Overview:
 * - ProductRepository implements IProductRepository (annotated with @Repository)
 * - ProductService implements IProductService (annotated with @Service)
 * - ProductController depends on IProductService (injected by constructor)
 * 
 * Spring automatically wires these dependencies based on the interfaces,
 * demonstrating the Dependency Inversion Principle in action.
 * 
 * Security:
 * - Excludes UserDetailsServiceAutoConfiguration to prevent default user generation
 * - Uses custom JWT-based authentication
 */
@SpringBootApplication(exclude = {UserDetailsServiceAutoConfiguration.class})
public class ProductComparisonApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(ProductComparisonApplication.class, args);
    }
    
}
