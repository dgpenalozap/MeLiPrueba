package com.example.productcomparison.repository;

import com.example.productcomparison.exception.DataSourceInitializationException;
import com.example.productcomparison.model.ProductDTO;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

/**
 * Capa de acceso a datos para productos, separada del repositorio.
 */
@Component
@RequiredArgsConstructor
public class ProductDataSource {
    private final ObjectMapper objectMapper;
    private final ResourceLoader resourceLoader;

    public List<ProductDTO> loadProductsFromJson(String jsonFilePath) {
        Resource resource = resourceLoader.getResource(jsonFilePath);

        if (!resource.exists()) {
            throw new DataSourceInitializationException("The data source file was not found at path: " + jsonFilePath);
        }

        try {
            return objectMapper.readValue(
                    resource.getInputStream(),
                    new TypeReference<>() {
                    }
            );
        } catch (IOException e) {
            throw new DataSourceInitializationException("Error reading data source file at path: " + jsonFilePath, e);
        }
    }
}

