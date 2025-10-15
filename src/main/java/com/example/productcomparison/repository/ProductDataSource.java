package com.example.productcomparison.repository;

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
@RequiredArgsConstructor
public class ProductDataSource {
    private final ObjectMapper objectMapper;
    private final ResourceLoader resourceLoader;

    public List<ProductDTO> loadProducts(String jsonFilePath) throws IOException {
        Resource resource = resourceLoader.getResource(jsonFilePath);
        return objectMapper.readValue(
                resource.getInputStream(),
                new TypeReference<>() {}
        );
    }
}

