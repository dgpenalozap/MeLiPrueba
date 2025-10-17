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

## Documentation Prompts

- "Update the `README.md` to include a description of the architecture, design patterns used, and a link to the Swagger documentation."
- "Update `prompts.md` with the latest prompts used from our conversation history and mention the AI tools used."
- "Create a `run.md` file with all the necessary steps to build, test, and run this project."
