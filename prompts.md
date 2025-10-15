# Prompts Used with GenAI

This file documents the prompts used to generate and refactor this project.

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

## Documentation Prompts

- "Update the `README.md` to include a description of the architecture, design patterns used, and a link to the Swagger documentation."
- "Update `prompts.md` with the latest prompts used from our conversation history."
- "Create a `run.md` file with all the necessary steps to build, test, and run this project."