# How to Run This Project

Follow these steps to build, test, and run the application.

## Requirements

- **Java 17** or higher.
- **Gradle** (Optional, as you can use the provided Gradle Wrapper).
- **Docker** (Optional, for running with Docker).

## Option 1: Run Locally with Gradle

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

## Option 2: Run with Docker

### 1. Build the Docker Image

From the project's root directory, build the Docker image:

```bash
docker build -t product-comparison-app .
```

### 2. Run the Docker Container

Run the application in a Docker container:

```bash
docker run -p 8080:8080 product-comparison-app
```

**With environment variables (optional):**
```bash
docker run -p 8080:8080 \
  -e SPRING_PROFILES_ACTIVE=prod \
  -e OPENAI_API_KEY=your_api_key_here \
  product-comparison-app
```

**Run in detached mode:**
```bash
docker run -d -p 8080:8080 --name product-comparison product-comparison-app
```

### 3. Docker Commands

**View logs:**
```bash
docker logs product-comparison
```

**Follow logs:**
```bash
docker logs -f product-comparison
```

**Stop the container:**
```bash
docker stop product-comparison
```

**Remove the container:**
```bash
docker rm product-comparison
```

**Remove the image:**
```bash
docker rmi product-comparison-app
```

## Access the API

Once the application is running (locally or in Docker), you can access the following URLs:

- **Main API Endpoint:**
  [http://localhost:8080/api/products](http://localhost:8080/api/products)

- **Swagger UI for Interactive Documentation:**
  [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

- **Health Check (if actuator is enabled):**
  [http://localhost:8080/actuator/health](http://localhost:8080/actuator/health)