# How to Run This Project

Follow these steps to build, test, and run the application.

## Requirements

- **Java 17** or higher.
- **Gradle** (Optional, as you can use the provided Gradle Wrapper).

## Steps

### 1. Clone or Download the Project

Unzip the project folder if you have downloaded it as a ZIP file.

### 2. Build the Application

Open a terminal or command prompt in the project's root directory and run the following command to compile the code and build the project. This will also download all necessary dependencies.

**On macOS/Linux:**
```bash
./gradlew clean build
```

**On Windows:**
```bash
gradlew.bat clean build
```

### 3. Run the Tests

To ensure everything is working correctly, run the unit tests:

**On macOS/Linux:**
```bash
./gradlew test
```

**On Windows:**
```bash
gradlew.bat test
```

You should see a `BUILD SUCCESSFUL` message, indicating that all 8 tests have passed.

### 4. Run the Application

Once the project is built and tested, you can start the application with the `bootRun` command.

**On macOS/Linux:**
```bash
./gradlew bootRun
```

**On Windows:**
```bash
gradlew.bat bootRun
```

The application will start on the embedded Tomcat server, typically on port 8080.

## 5. Access the API

Once the application is running, you can access the following URLs:

- **Main API Endpoint:**
  [http://localhost:8080/api/products](http://localhost:8080/api/products)

- **Swagger UI for Interactive Documentation:**
  [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)