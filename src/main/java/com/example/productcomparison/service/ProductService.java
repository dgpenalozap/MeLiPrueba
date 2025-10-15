package com.example.productcomparison.service;

import com.example.productcomparison.model.Product;
import com.example.productcomparison.repository.IProductRepository;
import com.example.productcomparison.exception.*;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation of IProductService containing business logic.
 * Following Single Responsibility Principle (SRP) - only handles business logic.
 * Depends on abstractions (IProductRepository interface) not concretions (DIP).
 * 
 * @RequiredArgsConstructor generates constructor with final fields (Dependency Injection)
 */
@Service
@RequiredArgsConstructor
public class ProductService implements IProductService {
    
    @NonNull
    private final IProductRepository productRepository;

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public Product getProductById(@NonNull String id) {
        if (id.trim().isEmpty()) {
            throw new InvalidParameterException("id", id, "Product ID cannot be empty");
        }
        return productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));
    }

    @Override
    public List<Product> searchByName(@NonNull String query) {
        if (query.trim().isEmpty()) {
            throw new InvalidParameterException("query", query, "Search query cannot be empty");
        }
        
        String lowerQuery = query.toLowerCase();
        return productRepository.findAll().stream()
                .filter(p -> p.getName().toLowerCase().contains(lowerQuery))
                .collect(Collectors.toList());
    }

    @Override
    public List<Product> filterByPriceRange(double minPrice, double maxPrice) {
        // Validate price range
        if (minPrice < 0) {
            throw new InvalidPriceRangeException(minPrice, maxPrice, "Minimum price cannot be negative");
        }
        if (maxPrice < 0) {
            throw new InvalidPriceRangeException(minPrice, maxPrice, "Maximum price cannot be negative");
        }
        if (minPrice > maxPrice) {
            throw new InvalidPriceRangeException(minPrice, maxPrice,
                    "Minimum price cannot be greater than maximum price");
        }

        return productRepository.findAll().stream()
                .filter(p -> p.getPrice() >= minPrice && p.getPrice() <= maxPrice)
                .collect(Collectors.toList());
    }

    @Override
    public List<Product> filterByRating(double minRating) {
        if (minRating < 0 || minRating > 5) {
            throw new InvalidRatingException(minRating);
        }
        
        return productRepository.findAll().stream()
                .filter(p -> p.getRating() >= minRating)
                .collect(Collectors.toList());
    }

    @Override
    public List<Product> filterByCategory(@NonNull String category) {
        if (category.trim().isEmpty()) {
            throw new InvalidParameterException("category", category, "Category cannot be empty");
        }
        
        List<Product> results = productRepository.findAll().stream()
                .filter(p -> p.getSpecifications() != null)
                .filter(p -> category.equalsIgnoreCase(p.getSpecifications().get("category")))
                .collect(Collectors.toList());
        
        // If no results and category doesn't exist, throw specific exception
        if (results.isEmpty()) {
            List<String> existingCategories = getAllCategories();
            boolean categoryExists = existingCategories.stream()
                    .anyMatch(cat -> cat.equalsIgnoreCase(category));
            
            if (!categoryExists) {
                throw new CategoryNotFoundException(category);
            }
        }
        
        return results;
    }

    @Override
    public List<String> getAllCategories() {
        return productRepository.findAll().stream()
                .filter(p -> p.getSpecifications() != null)
                .map(p -> p.getSpecifications().get("category"))
                .filter(category -> category != null && !category.isEmpty())
                .distinct()
                .sorted()
                .collect(Collectors.toList());
    }

    @Override
    public List<Product> compareProducts(@NonNull List<String> productIds) {
        if (productIds.isEmpty()) {
            throw new InvalidParameterException("ids", productIds, "Product IDs list cannot be empty");
        }
        
        if (productIds.size() > 10) {
            throw new InvalidParameterException("ids", productIds, 
                    "Cannot compare more than 10 products at once");
        }
        
        List<Product> products = productIds.stream()
                .map(id -> productRepository.findById(id).orElse(null))
                .filter(product -> product != null)
                .collect(Collectors.toList());
        
        // Report which products were not found
        if (products.size() < productIds.size()) {
            List<String> foundIds = products.stream()
                    .map(Product::getId)
                    .collect(Collectors.toList());
            List<String> notFoundIds = productIds.stream()
                    .filter(id -> !foundIds.contains(id))
                    .collect(Collectors.toList());
            
            if (products.isEmpty()) {
                throw new ProductNotFoundException(String.join(", ", notFoundIds), 
                        "None of the requested products were found");
            }
        }
        
        return products;
    }

    @Override
    public List<Product> sortByPrice(boolean ascending) {
        Comparator<Product> comparator = Comparator.comparingDouble(Product::getPrice);
        if (!ascending) {
            comparator = comparator.reversed();
        }
        return productRepository.findAll().stream()
                .sorted(comparator)
                .collect(Collectors.toList());
    }

    @Override
    public List<Product> sortByRating(boolean ascending) {
        Comparator<Product> comparator = Comparator.comparingDouble(Product::getRating);
        if (!ascending) {
            comparator = comparator.reversed();
        }
        return productRepository.findAll().stream()
                .sorted(comparator)
                .collect(Collectors.toList());
    }

    @Override
    public List<Product> getTopRatedProducts(int limit) {
        if (limit <= 0) {
            throw new InvalidParameterException("limit", limit, "Limit must be a positive number");
        }
        if (limit > 100) {
            throw new InvalidParameterException("limit", limit, 
                    "Limit cannot exceed 100. Please use a smaller value");
        }
        
        return productRepository.findAll().stream()
                .sorted(Comparator.comparingDouble(Product::getRating).reversed())
                .limit(limit)
                .collect(Collectors.toList());
    }

    @Override
    public List<Product> findBySpecification(@NonNull String specKey, @NonNull String specValue) {
        if (specKey.trim().isEmpty()) {
            throw new InvalidParameterException("key", specKey, "Specification key cannot be empty");
        }
        if (specValue.trim().isEmpty()) {
            throw new InvalidParameterException("value", specValue, "Specification value cannot be empty");
        }
        
        return productRepository.findAll().stream()
                .filter(p -> p.getSpecifications() != null)
                .filter(p -> specValue.equalsIgnoreCase(p.getSpecifications().get(specKey)))
                .collect(Collectors.toList());
    }
}
