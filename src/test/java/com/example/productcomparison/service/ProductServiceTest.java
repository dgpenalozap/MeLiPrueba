package com.example.productcomparison.service;

import com.example.productcomparison.exception.*;
import com.example.productcomparison.model.Product;
import com.example.productcomparison.repository.IProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive unit tests for ProductService achieving 100% code coverage.
 * Tests use InMemoryProductRepository instead of real JSON files,
 * making them fast, isolated, and deterministic.
 */
@DisplayName("ProductService Complete Unit Tests")
class ProductServiceTest {

    private IProductRepository repository;
    private IProductService service;

    @BeforeEach
    void setUp() {
        repository = new InMemoryProductRepository();
        service = new ProductService(repository);
    }

    // ==================== getAllProducts Tests ====================
    
    @Test
    @DisplayName("getAllProducts should return all products from repository")
    void getAllProducts_ReturnsAllProducts() {
        Product product1 = Product.builder()
            .id("1")
            .name("Laptop")
            .price(999.99)
            .rating(4.5)
            .specifications(Map.of("RAM", "16GB"))
            .build();
        
        Product product2 = Product.builder()
            .id("2")
            .name("Mouse")
            .price(29.99)
            .rating(4.2)
            .specifications(Map.of("DPI", "3200"))
            .build();
        
        repository = new InMemoryProductRepository(Arrays.asList(product1, product2));
        service = new ProductService(repository);
        
        List<Product> result = service.getAllProducts();
        
        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.contains(product1));
        assertTrue(result.contains(product2));
    }

    @Test
    @DisplayName("getAllProducts should return empty list when no products exist")
    void getAllProducts_WhenEmpty_ReturnsEmptyList() {
        List<Product> result = service.getAllProducts();
        
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    // ==================== getProductById Tests ====================

    @Test
    @DisplayName("getProductById should return product when it exists")
    void getProductById_ExistingProduct_ReturnsProduct() {
        Product expectedProduct = Product.builder()
            .id("123")
            .name("Test Product")
            .description("Test Description")
            .price(49.99)
            .rating(4.0)
            .build();
        
        repository = new InMemoryProductRepository(Arrays.asList(expectedProduct));
        service = new ProductService(repository);
        
        Product result = service.getProductById("123");
        
        assertNotNull(result);
        assertEquals("123", result.getId());
        assertEquals("Test Product", result.getName());
        assertEquals(49.99, result.getPrice());
    }

    @Test
    @DisplayName("getProductById should throw ProductNotFoundException when product does not exist")
    void getProductById_NonExistingProduct_ThrowsException() {
        ProductNotFoundException exception = assertThrows(
            ProductNotFoundException.class,
            () -> service.getProductById("non-existing-id")
        );
        
        assertTrue(exception.getMessage().contains("Product not found"));
        assertTrue(exception.getMessage().contains("non-existing-id"));
        assertEquals("non-existing-id", exception.getProductId());
    }

    @Test
    @DisplayName("getProductById should throw exception when id is empty")
    void getProductById_EmptyId_ThrowsException() {
        InvalidParameterException exception = assertThrows(
            InvalidParameterException.class,
            () -> service.getProductById("")
        );
        
        assertTrue(exception.getMessage().contains("cannot be empty"));
    }

    // ==================== searchByName Tests ====================

    @Test
    @DisplayName("searchByName should return matching products")
    void searchByName_WithMatches_ReturnsProducts() {
        Product laptop1 = Product.builder().id("1").name("Gaming Laptop").price(1000.0).build();
        Product laptop2 = Product.builder().id("2").name("Business Laptop").price(800.0).build();
        Product mouse = Product.builder().id("3").name("Gaming Mouse").price(50.0).build();
        
        repository = new InMemoryProductRepository(Arrays.asList(laptop1, laptop2, mouse));
        service = new ProductService(repository);
        
        List<Product> result = service.searchByName("laptop");
        
        assertEquals(2, result.size());
        assertTrue(result.contains(laptop1));
        assertTrue(result.contains(laptop2));
    }

    @Test
    @DisplayName("searchByName should be case insensitive")
    void searchByName_CaseInsensitive_ReturnsProducts() {
        Product product = Product.builder().id("1").name("LAPTOP PRO").price(1000.0).build();
        
        repository = new InMemoryProductRepository(Arrays.asList(product));
        service = new ProductService(repository);
        
        List<Product> result1 = service.searchByName("laptop");
        List<Product> result2 = service.searchByName("LAPTOP");
        List<Product> result3 = service.searchByName("LaPtOp");
        
        assertEquals(1, result1.size());
        assertEquals(1, result2.size());
        assertEquals(1, result3.size());
    }

    @Test
    @DisplayName("searchByName should return empty list when no matches")
    void searchByName_NoMatches_ReturnsEmptyList() {
        Product product = Product.builder().id("1").name("Laptop").price(1000.0).build();
        
        repository = new InMemoryProductRepository(Arrays.asList(product));
        service = new ProductService(repository);
        
        List<Product> result = service.searchByName("phone");
        
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("searchByName should throw exception when query is empty")
    void searchByName_EmptyQuery_ThrowsException() {
        InvalidParameterException exception = assertThrows(
            InvalidParameterException.class,
            () -> service.searchByName("")
        );
        
        assertTrue(exception.getMessage().contains("cannot be empty"));
    }

    // ==================== filterByPriceRange Tests ====================

    @Test
    @DisplayName("filterByPriceRange should return products within range")
    void filterByPriceRange_ValidRange_ReturnsProducts() {
        Product p1 = Product.builder().id("1").name("P1").price(100.0).build();
        Product p2 = Product.builder().id("2").name("P2").price(200.0).build();
        Product p3 = Product.builder().id("3").name("P3").price(300.0).build();
        
        repository = new InMemoryProductRepository(Arrays.asList(p1, p2, p3));
        service = new ProductService(repository);
        
        List<Product> result = service.filterByPriceRange(150.0, 250.0);
        
        assertEquals(1, result.size());
        assertEquals("P2", result.get(0).getName());
    }

    @Test
    @DisplayName("filterByPriceRange should throw exception when minPrice is negative")
    void filterByPriceRange_NegativeMin_ThrowsException() {
        InvalidPriceRangeException exception = assertThrows(
            InvalidPriceRangeException.class,
            () -> service.filterByPriceRange(-10.0, 100.0)
        );
        
        assertTrue(exception.getMessage().contains("cannot be negative"));
        assertEquals(-10.0, exception.getMinPrice());
    }

    @Test
    @DisplayName("filterByPriceRange should throw exception when maxPrice is negative")
    void filterByPriceRange_NegativeMax_ThrowsException() {
        InvalidPriceRangeException exception = assertThrows(
            InvalidPriceRangeException.class,
            () -> service.filterByPriceRange(100.0, -10.0)
        );
        
        assertTrue(exception.getMessage().contains("cannot be negative"));
    }

    @Test
    @DisplayName("filterByPriceRange should throw exception when min > max")
    void filterByPriceRange_MinGreaterThanMax_ThrowsException() {
        InvalidPriceRangeException exception = assertThrows(
            InvalidPriceRangeException.class,
            () -> service.filterByPriceRange(500.0, 100.0)
        );
        
        assertTrue(exception.getMessage().contains("greater than maximum"));
        assertEquals(500.0, exception.getMinPrice());
        assertEquals(100.0, exception.getMaxPrice());
    }

    @Test
    @DisplayName("filterByPriceRange should include boundary values")
    void filterByPriceRange_IncludesBoundaries_ReturnsProducts() {
        Product p1 = Product.builder().id("1").name("P1").price(100.0).build();
        Product p2 = Product.builder().id("2").name("P2").price(200.0).build();
        
        repository = new InMemoryProductRepository(Arrays.asList(p1, p2));
        service = new ProductService(repository);
        
        List<Product> result = service.filterByPriceRange(100.0, 200.0);
        
        assertEquals(2, result.size());
    }

    // ==================== filterByRating Tests ====================

    @Test
    @DisplayName("filterByRating should return products above minimum rating")
    void filterByRating_ValidRating_ReturnsProducts() {
        Product p1 = Product.builder().id("1").name("P1").rating(4.5).build();
        Product p2 = Product.builder().id("2").name("P2").rating(3.5).build();
        Product p3 = Product.builder().id("3").name("P3").rating(4.8).build();
        
        repository = new InMemoryProductRepository(Arrays.asList(p1, p2, p3));
        service = new ProductService(repository);
        
        List<Product> result = service.filterByRating(4.0);
        
        assertEquals(2, result.size());
    }

    @Test
    @DisplayName("filterByRating should throw exception when rating is negative")
    void filterByRating_NegativeRating_ThrowsException() {
        InvalidRatingException exception = assertThrows(
            InvalidRatingException.class,
            () -> service.filterByRating(-1.0)
        );
        
        assertEquals(-1.0, exception.getRating());
    }

    @Test
    @DisplayName("filterByRating should throw exception when rating is above 5")
    void filterByRating_RatingAbove5_ThrowsException() {
        InvalidRatingException exception = assertThrows(
            InvalidRatingException.class,
            () -> service.filterByRating(6.0)
        );
        
        assertEquals(6.0, exception.getRating());
    }

    // ==================== filterByCategory Tests ====================

    @Test
    @DisplayName("filterByCategory should return products in category")
    void filterByCategory_ValidCategory_ReturnsProducts() {
        Product p1 = Product.builder().id("1").name("P1").specifications(Map.of("category", "Laptops")).build();
        Product p2 = Product.builder().id("2").name("P2").specifications(Map.of("category", "Phones")).build();
        Product p3 = Product.builder().id("3").name("P3").specifications(Map.of("category", "Laptops")).build();
        
        repository = new InMemoryProductRepository(Arrays.asList(p1, p2, p3));
        service = new ProductService(repository);
        
        List<Product> result = service.filterByCategory("Laptops");
        
        assertEquals(2, result.size());
    }

    @Test
    @DisplayName("filterByCategory should be case insensitive")
    void filterByCategory_CaseInsensitive_ReturnsProducts() {
        Product p1 = Product.builder().id("1").name("P1").specifications(Map.of("category", "Laptops")).build();
        
        repository = new InMemoryProductRepository(Arrays.asList(p1));
        service = new ProductService(repository);
        
        List<Product> result = service.filterByCategory("laptops");
        
        assertEquals(1, result.size());
    }

    @Test
    @DisplayName("filterByCategory should throw exception when category is empty")
    void filterByCategory_EmptyCategory_ThrowsException() {
        InvalidParameterException exception = assertThrows(
            InvalidParameterException.class,
            () -> service.filterByCategory("")
        );
        
        assertTrue(exception.getMessage().contains("cannot be empty"));
    }

    @Test
    @DisplayName("filterByCategory should throw exception when category doesn't exist")
    void filterByCategory_NonExistentCategory_ThrowsException() {
        Product p1 = Product.builder().id("1").name("P1").specifications(Map.of("category", "Laptops")).build();
        
        repository = new InMemoryProductRepository(Arrays.asList(p1));
        service = new ProductService(repository);
        
        CategoryNotFoundException exception = assertThrows(
            CategoryNotFoundException.class,
            () -> service.filterByCategory("InvalidCategory")
        );
        
        assertEquals("InvalidCategory", exception.getCategory());
    }

    // ==================== getAllCategories Tests ====================

    @Test
    @DisplayName("getAllCategories should return unique categories")
    void getAllCategories_ReturnsUniqueCategories() {
        Product p1 = Product.builder().id("1").name("P1").specifications(Map.of("category", "Laptops")).build();
        Product p2 = Product.builder().id("2").name("P2").specifications(Map.of("category", "Phones")).build();
        Product p3 = Product.builder().id("3").name("P3").specifications(Map.of("category", "Laptops")).build();
        
        repository = new InMemoryProductRepository(Arrays.asList(p1, p2, p3));
        service = new ProductService(repository);
        
        List<String> result = service.getAllCategories();
        
        assertEquals(2, result.size());
        assertTrue(result.contains("Laptops"));
        assertTrue(result.contains("Phones"));
    }

    @Test
    @DisplayName("getAllCategories should return sorted categories")
    void getAllCategories_ReturnsSorted() {
        Product p1 = Product.builder().id("1").name("P1").specifications(Map.of("category", "Zebra")).build();
        Product p2 = Product.builder().id("2").name("P2").specifications(Map.of("category", "Apple")).build();
        Product p3 = Product.builder().id("3").name("P3").specifications(Map.of("category", "Mango")).build();
        
        repository = new InMemoryProductRepository(Arrays.asList(p1, p2, p3));
        service = new ProductService(repository);
        
        List<String> result = service.getAllCategories();
        
        assertEquals("Apple", result.get(0));
        assertEquals("Mango", result.get(1));
        assertEquals("Zebra", result.get(2));
    }

    // ==================== compareProducts Tests ====================

    @Test
    @DisplayName("compareProducts should return requested products")
    void compareProducts_ValidIds_ReturnsProducts() {
        Product p1 = Product.builder().id("1").name("P1").build();
        Product p2 = Product.builder().id("2").name("P2").build();
        Product p3 = Product.builder().id("3").name("P3").build();
        
        repository = new InMemoryProductRepository(Arrays.asList(p1, p2, p3));
        service = new ProductService(repository);
        
        List<Product> result = service.compareProducts(Arrays.asList("1", "2"));
        
        assertEquals(2, result.size());
    }

    @Test
    @DisplayName("compareProducts should throw exception when list is empty")
    void compareProducts_EmptyList_ThrowsException() {
        InvalidParameterException exception = assertThrows(
            InvalidParameterException.class,
            () -> service.compareProducts(Collections.emptyList())
        );
        
        assertTrue(exception.getMessage().contains("cannot be empty"));
    }

    @Test
    @DisplayName("compareProducts should throw exception when more than 10 products")
    void compareProducts_TooManyProducts_ThrowsException() {
        List<String> ids = Arrays.asList("1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11");
        
        InvalidParameterException exception = assertThrows(
            InvalidParameterException.class,
            () -> service.compareProducts(ids)
        );
        
        assertTrue(exception.getMessage().contains("Cannot compare more than 10"));
    }

    @Test
    @DisplayName("compareProducts should throw exception when no products found")
    void compareProducts_NoProductsFound_ThrowsException() {
        repository = new InMemoryProductRepository(Collections.emptyList());
        service = new ProductService(repository);
        
        ProductNotFoundException exception = assertThrows(
            ProductNotFoundException.class,
            () -> service.compareProducts(Arrays.asList("1", "2"))
        );
        
        assertTrue(exception.getMessage().contains("None of the requested products"));
    }

    // ==================== sortByPrice Tests ====================

    @Test
    @DisplayName("sortByPrice ascending should sort correctly")
    void sortByPrice_Ascending_SortsCorrectly() {
        Product p1 = Product.builder().id("1").name("P1").price(300.0).build();
        Product p2 = Product.builder().id("2").name("P2").price(100.0).build();
        Product p3 = Product.builder().id("3").name("P3").price(200.0).build();
        
        repository = new InMemoryProductRepository(Arrays.asList(p1, p2, p3));
        service = new ProductService(repository);
        
        List<Product> result = service.sortByPrice(true);
        
        assertEquals(100.0, result.get(0).getPrice());
        assertEquals(200.0, result.get(1).getPrice());
        assertEquals(300.0, result.get(2).getPrice());
    }

    @Test
    @DisplayName("sortByPrice descending should sort correctly")
    void sortByPrice_Descending_SortsCorrectly() {
        Product p1 = Product.builder().id("1").name("P1").price(100.0).build();
        Product p2 = Product.builder().id("2").name("P2").price(300.0).build();
        Product p3 = Product.builder().id("3").name("P3").price(200.0).build();
        
        repository = new InMemoryProductRepository(Arrays.asList(p1, p2, p3));
        service = new ProductService(repository);
        
        List<Product> result = service.sortByPrice(false);
        
        assertEquals(300.0, result.get(0).getPrice());
        assertEquals(200.0, result.get(1).getPrice());
        assertEquals(100.0, result.get(2).getPrice());
    }

    // ==================== sortByRating Tests ====================

    @Test
    @DisplayName("sortByRating ascending should sort correctly")
    void sortByRating_Ascending_SortsCorrectly() {
        Product p1 = Product.builder().id("1").name("P1").rating(4.5).build();
        Product p2 = Product.builder().id("2").name("P2").rating(3.5).build();
        Product p3 = Product.builder().id("3").name("P3").rating(4.0).build();
        
        repository = new InMemoryProductRepository(Arrays.asList(p1, p2, p3));
        service = new ProductService(repository);
        
        List<Product> result = service.sortByRating(true);
        
        assertEquals(3.5, result.get(0).getRating());
        assertEquals(4.0, result.get(1).getRating());
        assertEquals(4.5, result.get(2).getRating());
    }

    @Test
    @DisplayName("sortByRating descending should sort correctly")
    void sortByRating_Descending_SortsCorrectly() {
        Product p1 = Product.builder().id("1").name("P1").rating(3.5).build();
        Product p2 = Product.builder().id("2").name("P2").rating(4.5).build();
        Product p3 = Product.builder().id("3").name("P3").rating(4.0).build();
        
        repository = new InMemoryProductRepository(Arrays.asList(p1, p2, p3));
        service = new ProductService(repository);
        
        List<Product> result = service.sortByRating(false);
        
        assertEquals(4.5, result.get(0).getRating());
        assertEquals(4.0, result.get(1).getRating());
        assertEquals(3.5, result.get(2).getRating());
    }

    // ==================== getTopRatedProducts Tests ====================

    @Test
    @DisplayName("getTopRatedProducts should return top N products")
    void getTopRatedProducts_ValidLimit_ReturnsTopProducts() {
        Product p1 = Product.builder().id("1").name("P1").rating(3.0).build();
        Product p2 = Product.builder().id("2").name("P2").rating(5.0).build();
        Product p3 = Product.builder().id("3").name("P3").rating(4.0).build();
        
        repository = new InMemoryProductRepository(Arrays.asList(p1, p2, p3));
        service = new ProductService(repository);
        
        List<Product> result = service.getTopRatedProducts(2);
        
        assertEquals(2, result.size());
        assertEquals(5.0, result.get(0).getRating());
        assertEquals(4.0, result.get(1).getRating());
    }

    @Test
    @DisplayName("getTopRatedProducts should throw exception when limit is zero")
    void getTopRatedProducts_ZeroLimit_ThrowsException() {
        InvalidParameterException exception = assertThrows(
            InvalidParameterException.class,
            () -> service.getTopRatedProducts(0)
        );
        
        assertTrue(exception.getMessage().contains("must be a positive number"));
    }

    @Test
    @DisplayName("getTopRatedProducts should throw exception when limit is negative")
    void getTopRatedProducts_NegativeLimit_ThrowsException() {
        InvalidParameterException exception = assertThrows(
            InvalidParameterException.class,
            () -> service.getTopRatedProducts(-5)
        );
        
        assertTrue(exception.getMessage().contains("must be a positive number"));
    }

    @Test
    @DisplayName("getTopRatedProducts should throw exception when limit exceeds 100")
    void getTopRatedProducts_LimitExceeds100_ThrowsException() {
        InvalidParameterException exception = assertThrows(
            InvalidParameterException.class,
            () -> service.getTopRatedProducts(101)
        );
        
        assertTrue(exception.getMessage().contains("cannot exceed 100"));
    }

    // ==================== findBySpecification Tests ====================

    @Test
    @DisplayName("findBySpecification should return matching products")
    void findBySpecification_ValidSpec_ReturnsProducts() {
        Product p1 = Product.builder().id("1").name("P1").specifications(Map.of("processor", "Intel i7")).build();
        Product p2 = Product.builder().id("2").name("P2").specifications(Map.of("processor", "AMD Ryzen")).build();
        Product p3 = Product.builder().id("3").name("P3").specifications(Map.of("processor", "Intel i7")).build();
        
        repository = new InMemoryProductRepository(Arrays.asList(p1, p2, p3));
        service = new ProductService(repository);
        
        List<Product> result = service.findBySpecification("processor", "Intel i7");
        
        assertEquals(2, result.size());
    }

    @Test
    @DisplayName("findBySpecification should be case insensitive for values")
    void findBySpecification_CaseInsensitive_ReturnsProducts() {
        Product p1 = Product.builder().id("1").name("P1").specifications(Map.of("ram", "16GB")).build();
        
        repository = new InMemoryProductRepository(Arrays.asList(p1));
        service = new ProductService(repository);
        
        List<Product> result = service.findBySpecification("ram", "16gb");
        
        assertEquals(1, result.size());
    }

    @Test
    @DisplayName("findBySpecification should throw exception when key is empty")
    void findBySpecification_EmptyKey_ThrowsException() {
        InvalidParameterException exception = assertThrows(
            InvalidParameterException.class,
            () -> service.findBySpecification("", "value")
        );
        
        assertTrue(exception.getMessage().contains("key cannot be empty"));
    }

    @Test
    @DisplayName("findBySpecification should throw exception when value is empty")
    void findBySpecification_EmptyValue_ThrowsException() {
        InvalidParameterException exception = assertThrows(
            InvalidParameterException.class,
            () -> service.findBySpecification("key", "")
        );
        
        assertTrue(exception.getMessage().contains("value cannot be empty"));
    }

    // ==================== Dependency Inversion Tests ====================

    @Test
    @DisplayName("Repository should be easily swappable (Dependency Inversion)")
    void repository_IsSwappable() {
        Product product = Product.builder().id("1").name("Test").price(50.0).build();
        
        IProductRepository memoryRepo = new InMemoryProductRepository(Arrays.asList(product));
        IProductService memoryService = new ProductService(memoryRepo);
        
        List<Product> result = memoryService.getAllProducts();
        
        assertEquals(1, result.size());
        assertEquals("Test", result.get(0).getName());
    }
}
