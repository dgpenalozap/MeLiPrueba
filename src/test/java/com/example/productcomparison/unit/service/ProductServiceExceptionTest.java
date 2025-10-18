package com.example.productcomparison.unit.service;

import com.example.productcomparison.exception.repository.ProductAlreadyExistsException;
import com.example.productcomparison.exception.repository.ProductDataAccessException;
import com.example.productcomparison.exception.service.InvalidParameterException;
import com.example.productcomparison.exception.service.InvalidPriceRangeException;
import com.example.productcomparison.exception.service.InvalidRatingException;
import com.example.productcomparison.exception.service.ProductNotFoundException;
import com.example.productcomparison.model.Product;
import com.example.productcomparison.repository.IProductRepository;
import com.example.productcomparison.service.AIProductGenerator;
import com.example.productcomparison.service.ProductService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("ProductService Exception Handling Tests")
class ProductServiceExceptionTest {

    @Mock
    private IProductRepository productRepository;

    @Mock
    private AIProductGenerator aiProductGenerator;

    @InjectMocks
    private ProductService productService;

    private Product createValidProduct(String id, String name, double price, double rating) {
        return Product.builder()
                .id(id)
                .name(name)
                .price(price)
                .rating(rating)
                .build();
    }

    @Test
    @DisplayName("createProduct should throw InvalidParameterException when name is null")
    void createProduct_ThrowsInvalidParameterException_WhenNameIsNull() {
        Product product = createValidProduct("1", null, 100.0, 4.5);

        assertThrows(InvalidParameterException.class, () -> productService.createProduct(product));
        verify(productRepository, never()).save(any());
    }

    @Test
    @DisplayName("createProduct should throw InvalidParameterException when name is empty")
    void createProduct_ThrowsInvalidParameterException_WhenNameIsEmpty() {
        Product product = createValidProduct("1", "   ", 100.0, 4.5);

        assertThrows(InvalidParameterException.class, () -> productService.createProduct(product));
        verify(productRepository, never()).save(any());
    }

    @Test
    @DisplayName("createProduct should throw InvalidPriceRangeException when price is negative")
    void createProduct_ThrowsInvalidPriceRangeException_WhenPriceIsNegative() {
        Product product = createValidProduct("1", "Test Product", -10.0, 4.5);

        assertThrows(InvalidPriceRangeException.class, () -> productService.createProduct(product));
        verify(productRepository, never()).save(any());
    }

    @Test
    @DisplayName("createProduct should throw InvalidRatingException when rating is below 0")
    void createProduct_ThrowsInvalidRatingException_WhenRatingBelowZero() {
        Product product = createValidProduct("1", "Test Product", 100.0, -1.0);

        assertThrows(InvalidRatingException.class, () -> productService.createProduct(product));
        verify(productRepository, never()).save(any());
    }

    @Test
    @DisplayName("createProduct should throw InvalidRatingException when rating is above 5")
    void createProduct_ThrowsInvalidRatingException_WhenRatingAboveFive() {
        Product product = createValidProduct("1", "Test Product", 100.0, 6.0);

        assertThrows(InvalidRatingException.class, () -> productService.createProduct(product));
        verify(productRepository, never()).save(any());
    }

    @Test
    @DisplayName("createProduct should save product when valid")
    void createProduct_SavesProduct_WhenValid() {
        Product product = createValidProduct("1", "Test Product", 100.0, 4.5);
        when(productRepository.save(any())).thenReturn(product);

        Product result = productService.createProduct(product);

        assertNotNull(result);
        verify(productRepository, times(1)).save(product);
    }

    @Test
    @DisplayName("createProduct should propagate ProductAlreadyExistsException from repository")
    void createProduct_PropagatesProductAlreadyExistsException() {
        Product product = createValidProduct("1", "Test Product", 100.0, 4.5);
        when(productRepository.save(any())).thenThrow(new ProductAlreadyExistsException("1"));

        assertThrows(ProductAlreadyExistsException.class, () -> productService.createProduct(product));
    }

    @Test
    @DisplayName("updateProduct should throw InvalidParameterException when id is empty")
    void updateProduct_ThrowsInvalidParameterException_WhenIdIsEmpty() {
        Product product = createValidProduct("1", "Test Product", 100.0, 4.5);

        assertThrows(InvalidParameterException.class, () -> productService.updateProduct("   ", product));
        verify(productRepository, never()).update(anyString(), any());
    }

    @Test
    @DisplayName("updateProduct should throw InvalidParameterException when name is null")
    void updateProduct_ThrowsInvalidParameterException_WhenNameIsNull() {
        Product product = createValidProduct("1", null, 100.0, 4.5);

        assertThrows(InvalidParameterException.class, () -> productService.updateProduct("1", product));
        verify(productRepository, never()).update(anyString(), any());
    }

    @Test
    @DisplayName("updateProduct should throw InvalidPriceRangeException when price is negative")
    void updateProduct_ThrowsInvalidPriceRangeException_WhenPriceIsNegative() {
        Product product = createValidProduct("1", "Test Product", -10.0, 4.5);

        assertThrows(InvalidPriceRangeException.class, () -> productService.updateProduct("1", product));
        verify(productRepository, never()).update(anyString(), any());
    }

    @Test
    @DisplayName("updateProduct should throw InvalidRatingException when rating is invalid")
    void updateProduct_ThrowsInvalidRatingException_WhenRatingIsInvalid() {
        Product product = createValidProduct("1", "Test Product", 100.0, 6.0);

        assertThrows(InvalidRatingException.class, () -> productService.updateProduct("1", product));
        verify(productRepository, never()).update(anyString(), any());
    }

    @Test
    @DisplayName("updateProduct should update product when valid")
    void updateProduct_UpdatesProduct_WhenValid() {
        Product product = createValidProduct("1", "Test Product", 100.0, 4.5);
        when(productRepository.update(anyString(), any())).thenReturn(product);

        Product result = productService.updateProduct("1", product);

        assertNotNull(result);
        verify(productRepository, times(1)).update("1", product);
    }

    @Test
    @DisplayName("updateProduct should propagate ProductNotFoundException from repository")
    void updateProduct_PropagatesProductNotFoundException() {
        Product product = createValidProduct("1", "Test Product", 100.0, 4.5);
        when(productRepository.update(anyString(), any())).thenThrow(new ProductNotFoundException("1"));

        assertThrows(ProductNotFoundException.class, () -> productService.updateProduct("1", product));
    }

    @Test
    @DisplayName("deleteProduct should throw InvalidParameterException when id is empty")
    void deleteProduct_ThrowsInvalidParameterException_WhenIdIsEmpty() {
        assertThrows(InvalidParameterException.class, () -> productService.deleteProduct("   "));
        verify(productRepository, never()).deleteById(anyString());
    }

    @Test
    @DisplayName("deleteProduct should delete product when id is valid")
    void deleteProduct_DeletesProduct_WhenIdIsValid() {
        doNothing().when(productRepository).deleteById("1");

        assertDoesNotThrow(() -> productService.deleteProduct("1"));
        verify(productRepository, times(1)).deleteById("1");
    }

    @Test
    @DisplayName("deleteProduct should propagate ProductNotFoundException from repository")
    void deleteProduct_PropagatesProductNotFoundException() {
        doThrow(new ProductNotFoundException("1")).when(productRepository).deleteById("1");

        assertThrows(ProductNotFoundException.class, () -> productService.deleteProduct("1"));
    }

    @Test
    @DisplayName("generateRandomProduct should throw ProductDataAccessException when save fails")
    void generateRandomProduct_ThrowsProductDataAccessException_WhenSaveFails() {
        Product randomProduct = createValidProduct("gen-1", "Random Product", 100.0, 4.5);
        when(aiProductGenerator.generateRandomProduct()).thenReturn(randomProduct);
        when(productRepository.save(any())).thenThrow(new RuntimeException("Save failed"));

        assertThrows(ProductDataAccessException.class, () -> productService.generateRandomProduct());
    }

    @Test
    @DisplayName("generateRandomProduct should save generated product successfully")
    void generateRandomProduct_SavesGeneratedProduct_Successfully() {
        Product randomProduct = createValidProduct("gen-1", "Random Product", 100.0, 4.5);
        when(aiProductGenerator.generateRandomProduct()).thenReturn(randomProduct);
        when(productRepository.save(any())).thenReturn(randomProduct);

        Product result = productService.generateRandomProduct();

        assertNotNull(result);
        assertEquals("gen-1", result.getId());
        verify(productRepository, times(1)).save(randomProduct);
    }

    @Test
    @DisplayName("getProductById should throw InvalidParameterException when id is empty")
    void getProductById_ThrowsInvalidParameterException_WhenIdIsEmpty() {
        assertThrows(InvalidParameterException.class, () -> productService.getProductById("   "));
        verify(productRepository, never()).findById(anyString());
    }

    @Test
    @DisplayName("getProductById should throw ProductNotFoundException when product not found")
    void getProductById_ThrowsProductNotFoundException_WhenNotFound() {
        when(productRepository.findById("999")).thenReturn(Optional.empty());

        assertThrows(ProductNotFoundException.class, () -> productService.getProductById("999"));
    }

    @Test
    @DisplayName("getProductById should return product when found")
    void getProductById_ReturnsProduct_WhenFound() {
        Product product = createValidProduct("1", "Test Product", 100.0, 4.5);
        when(productRepository.findById("1")).thenReturn(Optional.of(product));

        Product result = productService.getProductById("1");

        assertNotNull(result);
        assertEquals("1", result.getId());
    }

    @Test
    @DisplayName("searchByName should throw InvalidParameterException when query is empty")
    void searchByName_ThrowsInvalidParameterException_WhenQueryIsEmpty() {
        assertThrows(InvalidParameterException.class, () -> productService.searchByName("   "));
    }

    @Test
    @DisplayName("filterByPriceRange should throw InvalidPriceRangeException when min is negative")
    void filterByPriceRange_ThrowsInvalidPriceRangeException_WhenMinIsNegative() {
        assertThrows(InvalidPriceRangeException.class, () -> productService.filterByPriceRange(-10.0, 100.0));
    }

    @Test
    @DisplayName("filterByPriceRange should throw InvalidPriceRangeException when max is negative")
    void filterByPriceRange_ThrowsInvalidPriceRangeException_WhenMaxIsNegative() {
        assertThrows(InvalidPriceRangeException.class, () -> productService.filterByPriceRange(10.0, -100.0));
    }

    @Test
    @DisplayName("filterByPriceRange should throw InvalidPriceRangeException when min > max")
    void filterByPriceRange_ThrowsInvalidPriceRangeException_WhenMinGreaterThanMax() {
        assertThrows(InvalidPriceRangeException.class, () -> productService.filterByPriceRange(200.0, 100.0));
    }

    @Test
    @DisplayName("filterByRating should throw InvalidRatingException when rating is below 0")
    void filterByRating_ThrowsInvalidRatingException_WhenRatingBelowZero() {
        assertThrows(InvalidRatingException.class, () -> productService.filterByRating(-1.0));
    }

    @Test
    @DisplayName("filterByRating should throw InvalidRatingException when rating is above 5")
    void filterByRating_ThrowsInvalidRatingException_WhenRatingAboveFive() {
        assertThrows(InvalidRatingException.class, () -> productService.filterByRating(6.0));
    }

    @Test
    @DisplayName("filterByCategory should throw InvalidParameterException when category is empty")
    void filterByCategory_ThrowsInvalidParameterException_WhenCategoryIsEmpty() {
        assertThrows(InvalidParameterException.class, () -> productService.filterByCategory("   "));
    }

    @Test
    @DisplayName("compareProducts should throw InvalidParameterException when list is empty")
    void compareProducts_ThrowsInvalidParameterException_WhenListIsEmpty() {
        assertThrows(InvalidParameterException.class, () -> productService.compareProducts(List.of()));
    }

    @Test
    @DisplayName("compareProducts should throw InvalidParameterException when more than 10 products")
    void compareProducts_ThrowsInvalidParameterException_WhenMoreThanTenProducts() {
        List<String> ids = List.of("1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11");
        assertThrows(InvalidParameterException.class, () -> productService.compareProducts(ids));
    }

    @Test
    @DisplayName("getTopRatedProducts should throw InvalidParameterException when limit is 0")
    void getTopRatedProducts_ThrowsInvalidParameterException_WhenLimitIsZero() {
        assertThrows(InvalidParameterException.class, () -> productService.getTopRatedProducts(0));
    }

    @Test
    @DisplayName("getTopRatedProducts should throw InvalidParameterException when limit is negative")
    void getTopRatedProducts_ThrowsInvalidParameterException_WhenLimitIsNegative() {
        assertThrows(InvalidParameterException.class, () -> productService.getTopRatedProducts(-1));
    }

    @Test
    @DisplayName("getTopRatedProducts should throw InvalidParameterException when limit exceeds 100")
    void getTopRatedProducts_ThrowsInvalidParameterException_WhenLimitExceeds100() {
        assertThrows(InvalidParameterException.class, () -> productService.getTopRatedProducts(101));
    }

    @Test
    @DisplayName("findBySpecification should throw InvalidParameterException when key is empty")
    void findBySpecification_ThrowsInvalidParameterException_WhenKeyIsEmpty() {
        assertThrows(InvalidParameterException.class, () -> productService.findBySpecification("   ", "value"));
    }

    @Test
    @DisplayName("findBySpecification should throw InvalidParameterException when value is empty")
    void findBySpecification_ThrowsInvalidParameterException_WhenValueIsEmpty() {
        assertThrows(InvalidParameterException.class, () -> productService.findBySpecification("key", "   "));
    }
}
