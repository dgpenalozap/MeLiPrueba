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
 * Data source for products that handles loading from JSON files.
 * <p>
 * This class is responsible for reading and deserializing product data
 * from JSON files using Spring's resource system.
 * It is separated from the repository to maintain a clear separation of responsibilities.
 * </p>
 */
@Component
@RequiredArgsConstructor
public class ProductDataSource {

    private final ObjectMapper objectMapper;
    private final ResourceLoader resourceLoader;

    private static final String FILE_NOT_FOUND_ERROR = "The data source file was not found at path: ";
    private static final String FILE_READ_ERROR = "Error reading data source file at path: ";
    private static final String INVALID_PATH_ERROR = "jsonFilePath cannot be null or empty";

    /**
     * Loads product data from a JSON file located at the specified path.
     *
     * @param jsonFilePath path to the JSON file (e.g., "classpath:data/products.json")
     * @return a list of deserialized ProductDTO objects
     * @throws IllegalArgumentException if the path is null or empty
     * @throws DataSourceInitializationException if the file is missing or unreadable
     */
    public List<ProductDTO> loadProductsFromJson(String jsonFilePath) {

        if (jsonFilePath == null || jsonFilePath.trim().isEmpty()) {
            throw new IllegalArgumentException(INVALID_PATH_ERROR);
        }

        Resource resource = resourceLoader.getResource(jsonFilePath);

        if (!resource.exists()) {
            throw new DataSourceInitializationException(FILE_NOT_FOUND_ERROR + jsonFilePath);
        }

        try {
            return objectMapper.readValue(
                    resource.getInputStream(),
                    new TypeReference<>() {
                    }
            );
        }
        catch (IOException e) {
            throw new DataSourceInitializationException(FILE_READ_ERROR + jsonFilePath, e);
        }
    }
}

