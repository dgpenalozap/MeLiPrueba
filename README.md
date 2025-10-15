# Product Comparison API (Gradle)

This is a Spring Boot backend API that serves product details for comparison.

## ✨ Features

- ✅ **SOLID Principles** applied throughout the codebase
- ✅ **Clean Architecture** with clear separation of concerns
- ✅ **Dependency Injection & Inversion** for 100% testable business logic
- ✅ **Immutable Domain Models** with Builder pattern
- ✅ **Lombok** for clean, boilerplate-free code
- ✅ **Professional Testing** with JUnit 5 & Mockito

## 🚀 Quick Start

### Run the application
```bash
./gradlew bootRun
```

Then open:
- **API**: [http://localhost:8080/api/products](http://localhost:8080/api/products)
- **Swagger UI**: [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

### Run tests
```bash
./gradlew test
```

## 🏗️ Architecture & Design Patterns

This project is built following a **Clean Architecture** approach, emphasizing separation of concerns and SOLID principles. This makes the system testable, maintainable, and scalable.

```
+---------------------+      +-----------------------+      +--------------------------+
|   Controller Layer  |----->|    Service Layer      |----->|    Repository Layer      |
| (Spring REST)       |      | (Business Logic)      |      | (Data Access)            |
+---------------------+      +-----------------------+      +--------------------------+
        |                            |                              |
        v                            v                              v
+---------------------+      +-----------------------+      +--------------------------+
| IProductController  |      |   IProductService     |      |   IProductRepository     |
| (Interface)         |      |   (Interface)         |      |   (Interface)            |
+---------------------+      +-----------------------+      +--------------------------+
```

### Key Design Patterns Used

- **Dependency Injection (DI):** Spring's core DI mechanism is used to provide dependencies (like `ProductService` into `ProductController`). This promotes loose coupling.
- **Repository Pattern:** The `IProductRepository` interface abstracts the data source. We can easily swap the `ProductRepository` (JSON file) with a database implementation without changing the service layer.
- **Service Layer Pattern:** Business logic is encapsulated within the `ProductService`, separating it from the controller and data access layers.
- **Builder Pattern:** Used via Lombok's `@Builder` annotation on the `Product` model. This allows for the creation of complex, immutable objects in a readable way.
- **Data Transfer Object (DTO):** The `ProductDTO` is used to transfer data between the service and controller layers, preventing domain models from being directly exposed to the client.
- **Strategy Pattern (Implicit):** The use of interfaces like `IProductService` and `IProductRepository` allows for different implementations (strategies) to be injected. For example, `InMemoryProductRepository` is used for testing, while `ProductRepository` is used in production.

## 📚 API Documentation

### Interactive Swagger UI

Once the application is running, access the **interactive API documentation** via Swagger UI:

[**http://localhost:8080/swagger-ui.html**](http://localhost:8080/swagger-ui.html)

**Features:**
- 📖 Browse all 12 endpoints
- 🧪 Test endpoints directly in the browser
- 📝 See request/response examples
- 🔍 Explore data models
- 📋 Copy cURL commands

### OpenAPI Specification

- **JSON**: [http://localhost:8080/v3/api-docs](http://localhost:8080/v3/api-docs)
- **YAML**: [http://localhost:8080/v3/api-docs.yaml](http://localhost:8080/v3/api-docs.yaml)

## 📡 Endpoints

### Basic Operations
- **GET** `/api/products` - Get all products (45 products)
- **GET** `/api/products/{id}` - Get product by ID
- **GET** `/api/products/categories` - Get all categories

### Search & Filter
- **GET** `/api/products/search?q={query}` - Search by name
- **GET** `/api/products/filter/price?min={min}&max={max}` - Filter by price range
- **GET** `/api/products/filter/rating?min={rating}` - Filter by minimum rating
- **GET** `/api/products/filter/category/{category}` - Filter by category
- **GET** `/api/products/filter/spec?key={key}&value={value}` - Filter by specification

### Sorting & Top Products
- **GET** `/api/products/sort/price?order={asc|desc}` - Sort by price
- **GET** `/api/products/sort/rating?order={asc|desc}` - Sort by rating
- **GET** `/api/products/top?limit={n}` - Get top N rated products

### Comparison
- **GET** `/api/products/compare?ids={id1},{id2},{id3}` - Compare multiple products

## 🎓 Key Technologies

- **Java 17** - Language
- **Spring Boot 3.1.4** - Framework
- **Gradle** - Build Tool
- **Lombok** - Boilerplate reduction
- **Jackson** - JSON processing
- **SpringDoc OpenAPI 2.2.0** - Swagger/API documentation
- **JUnit 5** - Testing Framework
- **Mockito** - Mocking Framework

## 🛠️ Setup & Run

### 1. Build the project
```bash
./gradlew clean build
```

### 2. Run the application
```bash
./gradlew bootRun
```

### 3. Run tests
```bash
./gradlew test
```
**Expected output:** `8 tests completed, 8 passed` in < 100ms

## 💡 IDE Setup (Lombok)

### IntelliJ IDEA
1. Install Lombok plugin: `Settings` → `Plugins` → Search for "Lombok"
2. Enable annotation processing: `Settings` → `Build, Execution, Deployment` → `Compiler` → `Annotation Processors` → Check `Enable annotation processing`

### VS Code
1. Install the "Lombok Annotations Support for Java" extension.