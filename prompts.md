# Prompts & Tools Used with GenAI

This file documents the prompts and AI tools used to generate and refactor this project.

## AI Tools Used

- **GitHub Copilot CLI**: For general command-line operations and file manipulation.
- **Gemini CLI**: For code generation, refactoring, and documentation updates.
- **GitHub Copilot (IntelliJ IDEA Plugin)**: For real-time code completion and suggestions within the IDE.

## Initial Project Generation

- "Generate a Spring Boot project with Gradle for a product comparison API using a JSON file instead of a database."
- "Include REST endpoints for `/api/products` and `/api/products/{id}`."
- "Add basic error handling and inline comments explaining the logic."
- "Create a JSON file with realistic test data for at least 10 products."

## Refactoring & Feature Prompts

- "Refactor the project to use a hexagonal architecture. Create interfaces for services and repositories."
- "Apply SOLID principles throughout the code, especially Dependency Inversion."
- "Use dependency injection for all components."
- "Add more services for filtering products by price, rating, and category."
- "Implement a global exception handler to provide consistent JSON error responses for all controllers."
- "Add Swagger (SpringDoc) for API documentation."
- "Create all the tests for the controller, model, repository, and service folders."

## Authentication and Security

- "Implement JWT for authentication in the products API."
- "Configure Spring Security to protect endpoints with token-based authentication."
- "Create endpoints for user login and registration that return JWT tokens."
- "Add roles and permissions for different user types (admin, user)."

## Testing

### Unit Tests

- "Create unit tests for each service using JUnit 5 and Mockito."
- "Implement tests for ProductRepository methods using mocks."
- "Write tests for validation classes and custom exceptions."

### Integration Tests

- "Develop integration tests for REST endpoints using MockMvc."
- "Implement tests to verify repository behavior with real data."
- "Create tests that verify integration between services and controllers."

### Acceptance Tests

- "Implement acceptance tests with Cucumber to validate complete API flows."
- "Create Gherkin scenarios for the main functionalities of the product comparison tool."
- "Add end-to-end tests that simulate real user behavior."

## Data Mapping and Transformation

- "Create mappers using MapStruct to convert between entities, DTOs, and response models."
- "Implement custom validations in DTOs using Bean Validation."
- "Add support for pagination and sorting in endpoints that return product lists."

## Additional CRUD Operations

- "Implement PUT endpoints to update product information."
- "Create PATCH operations for partial resource updates."
- "Add support for logical deletion (soft delete) of products."

## Data and Content Generation

- "Use 'dev.langchain4j:langchain4j:0.33.0' library to generate realistic random product data."
- "Create a script that generates a JSON file with 100 products with varied categories, prices, and descriptions."
- "Implement a detailed description generator for products using LLMs."

## Additional Documentation

- "Create technical documentation explaining JWT implementation and security."
- "Generate architecture diagrams using PlantUML to visualize the hexagonal structure."
- "Document all available endpoints with request/response examples in Markdown format."
- "Add a step-by-step tutorial on how to consume the API from different clients (curl, Postman, JavaScript)."

## Documentation Prompts

- "Update the `README.md` to include a description of the architecture, design patterns used, and a link to the Swagger
  documentation."
- "Update `prompts.md` with the latest prompts used from our conversation history and mention the AI tools used."
- "Create a `run.md` file with all the necessary steps to build, test, and run this project."
