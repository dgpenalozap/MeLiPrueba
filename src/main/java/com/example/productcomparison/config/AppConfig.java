package com.example.productcomparison.config;

import com.example.productcomparison.repository.IProductRepository;
import com.example.productcomparison.repository.ProductDataSource;
import com.example.productcomparison.repository.ProductRepository;
import com.example.productcomparison.service.IProductService;
import com.example.productcomparison.service.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ResourceLoader;

@Configuration
public class AppConfig {

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    @Bean
    public ProductDataSource productDataSource(ObjectMapper objectMapper, ResourceLoader resourceLoader) {
        return new ProductDataSource(objectMapper, resourceLoader);
    }

    @Bean
    public IProductRepository productRepository(ProductDataSource productDataSource) {
        return new ProductRepository(productDataSource);
    }

    @Bean
    public IProductService productService(IProductRepository productRepository) {
        return new ProductService(productRepository);
    }

}
