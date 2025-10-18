# 🛍️ Product Comparison API

> **RESTful API for product comparison with JWT Authentication, AI-powered data generation, and comprehensive CRUD operations**

[![Java](https://img.shields.io/badge/Java-17-orange.svg)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.1.4-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Gradle](https://img.shields.io/badge/Gradle-8.x-blue.svg)](https://gradle.org/)
[![License](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)

---

## 📋 Tabla de Contenidos

- [Descripción del Proyecto](#-descripción-del-proyecto)
- [Características Principales](#-características-principales)
- [Tecnologías Utilizadas](#-tecnologías-utilizadas)
- [Arquitectura](#️-arquitectura)
- [Inicio Rápido](#-inicio-rápido)
- [Configuración](#️-configuración)
- [Autenticación y Seguridad](#-autenticación-y-seguridad)
- [API Endpoints](#-api-endpoints)
- [Validación](#-validación)
- [Manejo de Excepciones](#-manejo-de-excepciones)
- [Generación de Datos con IA](#-generación-de-datos-con-ia)
- [Testing](#-testing)
- [Documentación de la API](#-documentación-de-la-api)

---

## 📖 Descripción del Proyecto

**Product Comparison API** es una aplicación backend desarrollada con **Spring Boot 3** que proporciona un sistema completo de gestión y comparación de productos. La API incluye autenticación JWT, validación robusta, manejo centralizado de excepciones y la capacidad de generar productos aleatorios usando inteligencia artificial.

### ¿Para qué sirve?

- **Gestión de Productos**: CRUD completo (Crear, Leer, Actualizar, Eliminar)
- **Búsqueda Avanzada**: Filtrado por precio, rating, categoría y especificaciones
- **Comparación**: Comparar múltiples productos lado a lado
- **Autenticación Segura**: Sistema JWT con roles de usuario
- **Generación IA**: Crear productos aleatorios realistas usando LangChain4j

### Base de Datos

Incluye **45 productos** pre-cargados distribuidos en **13 categorías**:
- Laptops, Smartphones, Tablets, Monitors, Keyboards, Mice, Headphones, Cameras, Smartwatches, Speakers, Networking, Storage, Printers

---

## ✨ Características Principales

### 🏗️ Arquitectura y Diseño
- ✅ **Principios SOLID** aplicados en toda la aplicación
- ✅ **Clean Architecture** con separación clara de responsabilidades
- ✅ **Inyección de Dependencias** para código 100% testeable
- ✅ **Repository Pattern** para abstracción de datos
- ✅ **Service Layer Pattern** para lógica de negocio
- ✅ **DTO Pattern** para transferencia de datos

### 🔐 Seguridad
- ✅ **Autenticación JWT** (JSON Web Tokens)
- ✅ **Control de Acceso Basado en Roles** (RBAC)
- ✅ **HTTP Basic Auth deshabilitado**
- ✅ **Sesiones stateless** (sin JSESSIONID)
- ✅ **Configuración externalizada** de credenciales

### 🎯 Funcionalidades
- ✅ **CRUD Completo** de productos
- ✅ **Búsqueda y Filtrado** avanzado
- ✅ **Ordenamiento** por precio y rating
- ✅ **Comparación** de múltiples productos
- ✅ **Validación** automática con Bean Validation
- ✅ **Generación IA** de productos aleatorios
- ✅ **Manejo Centralizado** de excepciones

### 🛠️ Calidad de Código
- ✅ **Lombok** para reducir boilerplate
- ✅ **MapStruct** para mapeo de objetos
- ✅ **Bean Validation** para validación declarativa
- ✅ **Tests Unitarios** con JUnit 5 y Mockito
- ✅ **Documentación Swagger/OpenAPI** interactiva

---

## 🚀 Tecnologías Utilizadas

### Core Framework
| Tecnología | Versión | Descripción |
|-----------|---------|-------------|
| **Java** | 17 | Lenguaje de programación |
| **Spring Boot** | 3.1.4 | Framework principal |
| **Gradle** | 8.x | Herramienta de construcción |

### Spring Ecosystem
| Dependencia | Propósito |
|------------|-----------|
| `spring-boot-starter-web` | API REST |
| `spring-boot-starter-security` | Seguridad y autenticación |
| `spring-boot-starter-validation` | Validación de datos |

### Seguridad
| Dependencia | Propósito |
|------------|-----------|
| `jjwt-api:0.11.5` | Generación y validación de JWT |
| `jjwt-impl:0.11.5` | Implementación de JWT |
| `jjwt-jackson:0.11.5` | Serialización JSON de JWT |

### Documentación
| Dependencia | Propósito |
|------------|-----------|
| `springdoc-openapi-starter-webmvc-ui:2.2.0` | Swagger UI y OpenAPI 3 |

### IA y Datos
| Dependencia | Propósito |
|------------|-----------|
| `langchain4j:0.33.0` | Framework de IA |
| `langchain4j-open-ai:0.33.0` | Integración con OpenAI para generación de productos |

### Utilidades
| Dependencia | Propósito |
|------------|-----------|
| `lombok` | Reducción de boilerplate code |
| `mapstruct:1.5.5.Final` | Mapeo automático de objetos (DTO ↔ Entity) |
| `jackson-databind` | Serialización/deserialización JSON |

### Testing
| Dependencia | Propósito |
|------------|-----------|
| `spring-boot-starter-test` | Testing framework |
| `junit-jupiter` | Tests unitarios |
| `mockito` | Mocking de dependencias |

---

## 🏛️ Arquitectura

### Diagrama de Capas

```
┌─────────────────────────────────────────────────────────────┐
│                    CONTROLLER LAYER                          │
│  - ProductController (REST endpoints)                        │
│  - AuthController (JWT authentication)                       │
│  - Maneja HTTP requests/responses                            │
│  - Validación con @Valid (Bean Validation)                   │
└───────────────────────┬─────────────────────────────────────┘
                        │
                        ▼
┌─────────────────────────────────────────────────────────────┐
│                     SERVICE LAYER                            │
│  - ProductService (lógica de negocio)                        │
│  - AIProductGenerator (generación con IA)                    │
│  - Validación de reglas de negocio                           │
│  - Orquestación de operaciones                               │
└───────────────────────┬─────────────────────────────────────┘
                        │
                        ▼
┌─────────────────────────────────────────────────────────────┐
│                   REPOSITORY LAYER                           │
│  - ProductRepository (acceso a datos)                        │
│  - ProductDataSource (carga desde JSON)                      │
│  - ProductMapper (DTO ↔ Entity mapping)                      │
│  - ProductValidator (validación de datos)                    │
└─────────────────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────────────────┐
│                   CROSS-CUTTING CONCERNS                     │
│  - GlobalExceptionHandler (manejo centralizado de errores)   │
│  - SecurityConfig (configuración de seguridad)               │
│  - JwtAuthenticationFilter (filtro de autenticación)         │
│  - JwtUtil (generación y validación de tokens)               │
└─────────────────────────────────────────────────────────────┘
```

### Responsabilidades por Capa

#### 🎮 Controller Layer
**Responsabilidad**: Manejo de HTTP
- Recibe requests HTTP
- Valida datos de entrada con `@Valid`
- Invoca servicios de negocio
- Retorna responses HTTP
- **NO contiene lógica de negocio**

**Clases principales**:
- `ProductController`: CRUD y operaciones de productos
- `AuthController`: Login y gestión de usuarios

#### 💼 Service Layer
**Responsabilidad**: Lógica de Negocio
- Implementa reglas de negocio
- Coordina operaciones entre repositorios
- Valida reglas específicas del dominio
- **NO conoce detalles HTTP**

**Clases principales**:
- `ProductService`: Operaciones CRUD, búsqueda, filtrado
- `AIProductGenerator`: Generación de productos con IA

#### 💾 Repository Layer
**Responsabilidad**: Acceso a Datos
- Abstrae el origen de datos
- Operaciones CRUD sobre datos
- Validación de integridad de datos
- **NO contiene lógica de negocio**

**Clases principales**:
- `ProductRepository`: Implementación con ConcurrentHashMap
- `ProductDataSource`: Carga de datos desde JSON
- `ProductMapper`: Conversión DTO ↔ Entity (generado por MapStruct)
- `ProductValidator`: Validación de datos

#### 🔧 Cross-Cutting Concerns
**Responsabilidad**: Funcionalidades Transversales

**GlobalExceptionHandler**:
- Manejo centralizado de todas las excepciones
- Retorna responses consistentes con `ErrorResponse`
- Logging de errores

**Security**:
- `SecurityConfig`: Configuración de seguridad Spring
- `JwtAuthenticationFilter`: Intercepta requests y valida JWT
- `JwtUtil`: Genera y valida tokens JWT
- `UserConfig`: Carga usuarios desde properties

---

## 🚀 Inicio Rápido

### Prerrequisitos

- **Java 17** o superior
- **Gradle 8.x** (incluido en el proyecto)
- **IDE** (IntelliJ IDEA, Eclipse, VS Code)

### 1. Clonar el Repositorio

```bash
git clone <repository-url>
cd product-comparison-java-gradle
```

### 2. Compilar el Proyecto

```bash
# Windows
.\gradlew.bat clean build

# Linux/Mac
./gradlew clean build
```

### 3. Ejecutar la Aplicación

```bash
# Windows
.\gradlew.bat bootRun

# Linux/Mac
./gradlew bootRun
```

### 4. Verificar que Funciona

Abre tu navegador en:
- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **API Products**: http://localhost:8080/api/products (requiere autenticación)

---

## ⚙️ Configuración

### application.properties

El archivo `src/main/resources/application.properties` contiene toda la configuración del proyecto:

```properties
# ==========================================
# SERVER CONFIGURATION
# ==========================================
server.port=8080

# ==========================================
# PRODUCT DATA CONFIGURATION
# ==========================================
product.data.json-file=classpath:productos.json

# ==========================================
# OPENAI CONFIGURATION (OPCIONAL)
# ==========================================
# Para generación de productos con IA
# Configurar OPENAI_API_KEY como variable de entorno
openai.api.key=${OPENAI_API_KEY:demo-key}

# ==========================================
# JACKSON CONFIGURATION
# ==========================================
spring.jackson.serialization.write-dates-as-timestamps=false
spring.jackson.date-format=yyyy-MM-dd'T'HH:mm:ss

# ==========================================
# SPRING SECURITY CONFIGURATION
# ==========================================
# Deshabilita la generación automática de contraseña
spring.security.user.name=disabled
spring.security.user.password=disabled

# ==========================================
# JWT CONFIGURATION
# ==========================================
# Clave secreta para firmar tokens (cambiar en producción)
jwt.secret-key=ProductComparisonSecretKeyForJWTTokenGeneration2024
# Tiempo de expiración: 36000000 ms = 10 horas
jwt.expiration-time=36000000

# ==========================================
# DEMO USERS CONFIGURATION
# ==========================================
# Usuario Administrador (acceso completo)
app.security.users[0].username=admin
app.security.users[0].password=admin123
app.security.users[0].role=ROLE_ADMIN

# Usuario Regular (solo lectura)
app.security.users[1].username=user
app.security.users[1].password=user123
app.security.users[1].role=ROLE_USER
```

### Configuraciones Importantes

#### 🔐 JWT Secret Key
```properties
jwt.secret-key=TuClaveSecretaMuyLarga
```
**⚠️ IMPORTANTE**: En producción, usa una clave segura de al menos 256 bits y guárdala en variables de entorno.

#### ⏰ Tiempo de Expiración de Tokens
```properties
# 1 hora = 3600000 ms
jwt.expiration-time=3600000

# 10 horas (actual) = 36000000 ms
jwt.expiration-time=36000000

# 24 horas = 86400000 ms
jwt.expiration-time=86400000
```

#### 👥 Agregar Nuevos Usuarios

Para agregar más usuarios de autenticación, simplemente agrega más entradas en `application.properties`:

```properties
# Usuario 3 - Manager
app.security.users[2].username=manager
app.security.users[2].password=manager123
app.security.users[2].role=ROLE_ADMIN

# Usuario 4 - Viewer
app.security.users[3].username=viewer
app.security.users[3].password=viewer123
app.security.users[3].role=ROLE_USER
```

**Reinicia la aplicación** para que los cambios surtan efecto.

#### 🤖 Configuración de OpenAI (Opcional)

Para habilitar la generación de productos con IA real:

1. **Obtén una API Key** de [OpenAI](https://platform.openai.com/)

2. **Configura la variable de entorno**:
   ```bash
   # Windows
   set OPENAI_API_KEY=tu-api-key-aqui
   
   # Linux/Mac
   export OPENAI_API_KEY=tu-api-key-aqui
   ```

3. **O edita application.properties**:
   ```properties
   openai.api.key=sk-tu-api-key-real
   ```

Si no configuras OpenAI, el sistema usa un generador aleatorio fallback (no requiere API key).

---

## 🔐 Autenticación y Seguridad

### Sistema de Autenticación

La API usa **JWT (JSON Web Tokens)** para autenticación stateless.

#### Flujo de Autenticación

```
┌─────────┐         ┌──────────────┐         ┌─────────────┐
│ Cliente │         │    /auth     │         │     API     │
└────┬────┘         │   /login     │         │  Endpoints  │
     │              └──────┬───────┘         └──────┬──────┘
     │                     │                        │
     │  1. POST /auth/login                        │
     │  {username, password}                       │
     ├────────────────────>│                        │
     │                     │                        │
     │  2. Validar usuario │                        │
     │     en UserConfig   │                        │
     │                     │                        │
     │  3. Generar JWT     │                        │
     │                     │                        │
     │  4. Return token    │                        │
     │<────────────────────┤                        │
     │                     │                        │
     │  5. GET /api/products                        │
     │     Header: Bearer <token>                   │
     ├──────────────────────────────────────────────>│
     │                                               │
     │                      6. JwtFilter valida token│
     │                                               │
     │  7. Return products                           │
     │<──────────────────────────────────────────────┤
     │                                               │
```

### Usuarios Demo

La aplicación incluye 2 usuarios de demostración:

| Usuario | Contraseña | Rol | Permisos |
|---------|-----------|-----|----------|
| `admin` | `admin123` | `ROLE_ADMIN` | GET, POST, PUT, DELETE |
| `user` | `user123` | `ROLE_USER` | GET solamente |

### Cómo Autenticarse

#### 1. Login para Obtener Token

```bash
curl -X POST http://localhost:8080/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "admin",
    "password": "admin123"
  }'
```

**Respuesta**:
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJhZG1pbiIsInJvbGUiOiJST0xFX0FETUlOIiwiaWF0IjoxNzA4MzYyMDAwLCJleHAiOjE3MDgzOTgwMDB9.signature",
  "username": "admin",
  "role": "ROLE_ADMIN",
  "message": "Login successful"
}
```

#### 2. Usar el Token en Requests

```bash
curl http://localhost:8080/api/products \
  -H "Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
```

### Configuración de Seguridad

#### SecurityConfig.java

```java
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) {
        http
            .csrf(disable)                    // ❌ CSRF deshabilitado (API stateless)
            .httpBasic(disable)               // ❌ Basic Auth deshabilitado
            .formLogin(disable)               // ❌ Form Login deshabilitado
            .sessionManagement(STATELESS)     // ✅ Sin sesiones (JWT only)
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/auth/**").permitAll()        // Público
                .requestMatchers("/swagger-ui/**").permitAll()  // Público
                .requestMatchers(GET, "/api/products/**")       // ADMIN + USER
                    .hasAnyAuthority("ROLE_ADMIN", "ROLE_USER")
                .requestMatchers(POST, "/api/products/**")      // Solo ADMIN
                    .hasAuthority("ROLE_ADMIN")
                .requestMatchers(PUT, "/api/products/**")       // Solo ADMIN
                    .hasAuthority("ROLE_ADMIN")
                .requestMatchers(DELETE, "/api/products/**")    // Solo ADMIN
                    .hasAuthority("ROLE_ADMIN")
            )
            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
    }
}
```

### Endpoints Públicos vs Protegidos

#### 🌐 Endpoints Públicos (sin autenticación)
- `POST /auth/login` - Iniciar sesión
- `GET /auth/users` - Ver usuarios disponibles (solo para demo)
- `GET /swagger-ui/**` - Documentación Swagger
- `GET /v3/api-docs/**` - Especificación OpenAPI

#### 🔒 Endpoints Protegidos

**Accesibles por ROLE_ADMIN y ROLE_USER**:
- `GET /api/products` - Listar todos los productos
- `GET /api/products/{id}` - Ver un producto
- `GET /api/products/search` - Buscar productos
- `GET /api/products/filter/**` - Filtrar productos
- `GET /api/products/sort/**` - Ordenar productos
- `GET /api/products/top` - Top productos
- `GET /api/products/compare` - Comparar productos
- `GET /api/products/categories` - Listar categorías

**Solo accesibles por ROLE_ADMIN**:
- `POST /api/products` - Crear producto
- `POST /api/products/generate` - Generar producto con IA
- `PUT /api/products/{id}` - Actualizar producto
- `DELETE /api/products/{id}` - Eliminar producto

---

## 📡 API Endpoints

### Autenticación

| Método | Endpoint | Descripción | Auth |
|--------|----------|-------------|------|
| POST | `/auth/login` | Iniciar sesión y obtener JWT | Público |
| GET | `/auth/users` | Ver usuarios disponibles (demo) | Público |

### Gestión de Productos (CRUD)

| Método | Endpoint | Descripción | Auth |
|--------|----------|-------------|------|
| GET | `/api/products` | Listar todos los productos | 🔒 ADMIN/USER |
| GET | `/api/products/{id}` | Obtener producto por ID | 🔒 ADMIN/USER |
| POST | `/api/products` | Crear nuevo producto | 🔒 ADMIN |
| PUT | `/api/products/{id}` | Actualizar producto | 🔒 ADMIN |
| DELETE | `/api/products/{id}` | Eliminar producto | 🔒 ADMIN |
| POST | `/api/products/generate` | Generar producto con IA | 🔒 ADMIN |

### Búsqueda y Filtrado

| Método | Endpoint | Parámetros | Descripción | Auth |
|--------|----------|------------|-------------|------|
| GET | `/api/products/search` | `q` | Buscar por nombre | 🔒 ADMIN/USER |
| GET | `/api/products/filter/price` | `min`, `max` | Filtrar por rango de precio | 🔒 ADMIN/USER |
| GET | `/api/products/filter/rating` | `min` | Filtrar por rating mínimo | 🔒 ADMIN/USER |
| GET | `/api/products/filter/category/{category}` | - | Filtrar por categoría | 🔒 ADMIN/USER |
| GET | `/api/products/filter/spec` | `key`, `value` | Filtrar por especificación | 🔒 ADMIN/USER |

### Ordenamiento

| Método | Endpoint | Parámetros | Descripción | Auth |
|--------|----------|------------|-------------|------|
| GET | `/api/products/sort/price` | `order=asc\|desc` | Ordenar por precio | 🔒 ADMIN/USER |
| GET | `/api/products/sort/rating` | `order=asc\|desc` | Ordenar por rating | 🔒 ADMIN/USER |
| GET | `/api/products/top` | `limit` | Top N productos por rating | 🔒 ADMIN/USER |

### Otros

| Método | Endpoint | Descripción | Auth |
|--------|----------|-------------|------|
| GET | `/api/products/categories` | Listar todas las categorías | 🔒 ADMIN/USER |
| GET | `/api/products/compare` | Comparar múltiples productos | 🔒 ADMIN/USER |

### Ejemplos de Uso

#### Crear Producto
```bash
curl -X POST http://localhost:8080/api/products \
  -H "Authorization: Bearer <token>" \
  -H "Content-Type: application/json" \
  -d '{
    "id": "laptop-999",
    "name": "UltraBook Pro 15",
    "imageUrl": "https://example.com/images/ultrabook.jpg",
    "description": "Premium laptop para profesionales",
    "price": 1299.99,
    "rating": 4.7,
    "specifications": {
      "processor": "Intel Core i7",
      "ram": "16GB DDR5",
      "storage": "512GB SSD",
      "category": "Laptops"
    }
  }'
```

#### Buscar Productos
```bash
curl "http://localhost:8080/api/products/search?q=laptop" \
  -H "Authorization: Bearer <token>"
```

#### Filtrar por Precio
```bash
curl "http://localhost:8080/api/products/filter/price?min=500&max=1500" \
  -H "Authorization: Bearer <token>"
```

#### Comparar Productos
```bash
curl "http://localhost:8080/api/products/compare?ids=laptop-001,laptop-002,laptop-003" \
  -H "Authorization: Bearer <token>"
```

---

## ✅ Validación

La aplicación usa **Bean Validation** (Jakarta Validation) para validar datos de entrada automáticamente.

### Anotaciones Utilizadas

#### CreateProductRequest

```java
public class CreateProductRequest {
    
    @NotBlank(message = "Product ID cannot be blank")
    private String id;
    
    @NotBlank(message = "Product name cannot be blank")
    private String name;
    
    @NotNull(message = "Product price cannot be null")
    @Positive(message = "Product price must be positive")
    private double price;
    
    @NotNull(message = "Product rating cannot be null")
    @Min(value = 0, message = "Product rating must be at least 0.0")
    @Max(value = 5, message = "Product rating must be at most 5.0")
    private double rating;
    
    // imageUrl, description, specifications son opcionales
}
```

### Validación Automática

```java
@PostMapping
public ResponseEntity<Product> createProduct(
    @Valid @RequestBody CreateProductRequest request) {  // ← @Valid activa la validación
    // ...
}
```

### Respuestas de Error de Validación

Si los datos no son válidos, la API retorna **400 Bad Request** con detalles:

```json
{
  "timestamp": "2024-10-18T22:00:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Validation failed",
  "path": "/api/products",
  "errorCode": "VALIDATION_ERROR",
  "validationErrors": {
    "name": "Product name cannot be blank",
    "price": "Product price must be positive",
    "rating": "Product rating must be at most 5.0"
  }
}
```

---

## 🚨 Manejo de Excepciones

Todas las excepciones son manejadas centralizadamente por `GlobalExceptionHandler`.

### Jerarquía de Excepciones

```
Exception
│
├── Service Layer Exceptions (Business Logic)
│   ├── ProductNotFoundException          (404)
│   ├── CategoryNotFoundException         (404)
│   ├── InvalidParameterException         (400)
│   ├── InvalidPriceRangeException        (400)
│   ├── InvalidRatingException            (400)
│   └── EmptyResultException              (404)
│
└── Repository Layer Exceptions (Data Access)
    ├── ProductAlreadyExistsException     (409)
    ├── ProductDataAccessException        (500)
    ├── ProductSaveException              (500)
    ├── ProductUpdateException            (500)
    ├── ProductDeleteException            (500)
    ├── ProductValidationException        (400)
    └── DataSourceInitializationException (500)
```

### Tabla de Excepciones

#### Service Layer (Lógica de Negocio)

| Excepción | HTTP Status | Cuándo se lanza |
|-----------|-------------|-----------------|
| `ProductNotFoundException` | 404 Not Found | Producto no encontrado por ID |
| `CategoryNotFoundException` | 404 Not Found | Categoría no existe |
| `InvalidParameterException` | 400 Bad Request | Parámetro inválido (vacío, nulo, fuera de rango) |
| `InvalidPriceRangeException` | 400 Bad Request | Precio negativo o min > max |
| `InvalidRatingException` | 400 Bad Request | Rating fuera del rango 0-5 |
| `EmptyResultException` | 404 Not Found | Búsqueda sin resultados |

#### Repository Layer (Acceso a Datos)

| Excepción | HTTP Status | Cuándo se lanza |
|-----------|-------------|-----------------|
| `ProductAlreadyExistsException` | 409 Conflict | Intentar crear producto con ID existente |
| `ProductDataAccessException` | 500 Internal Server Error | Error al acceder a datos |
| `ProductSaveException` | 500 Internal Server Error | Error al guardar producto |
| `ProductUpdateException` | 500 Internal Server Error | Error al actualizar producto |
| `ProductDeleteException` | 500 Internal Server Error | Error al eliminar producto |
| `ProductValidationException` | 400 Bad Request | Validación de datos falla |
| `DataSourceInitializationException` | 500 Internal Server Error | Error al cargar JSON inicial |

### Formato de Respuesta de Error

Todas las excepciones retornan un `ErrorResponse` consistente:

```json
{
  "timestamp": "2024-10-18T22:00:00",
  "status": 404,
  "error": "Not Found",
  "message": "Product with ID 'laptop-999' not found",
  "path": "/api/products/laptop-999",
  "errorCode": "PRODUCT_NOT_FOUND",
  "validationErrors": null
}
```

### GlobalExceptionHandler

El `GlobalExceptionHandler` intercepta todas las excepciones y las convierte en responses HTTP apropiadas:

```java
@ControllerAdvice
public class GlobalExceptionHandler {
    
    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleProductNotFound(
            ProductNotFoundException ex, HttpServletRequest request) {
        // Retorna 404 con ErrorResponse
    }
    
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationErrors(
            MethodArgumentNotValidException ex, HttpServletRequest request) {
        // Retorna 400 con detalles de validación por campo
    }
    
    // ... más handlers para cada tipo de excepción
}
```

---

## 🤖 Generación de Datos con IA

La aplicación incluye capacidad de generar productos aleatorios usando **LangChain4j** y **OpenAI**.

### Dependencias

```gradle
implementation 'dev.langchain4j:langchain4j:0.33.0'
implementation 'dev.langchain4j:langchain4j-open-ai:0.33.0'
```

### Cómo Funciona

```
┌─────────────────┐
│ POST /generate  │
└────────┬────────┘
         │
         ▼
┌──────────────────────┐
│ AIProductGenerator   │
└─────────┬────────────┘
          │
          ├─── ¿Tiene OpenAI API Key?
          │
          ├─── SÍ ──────> Genera con OpenAI ────┐
          │                                      │
          └─── NO ──────> Genera con Random ────┤
                                                 │
                                                 ▼
                                    ┌─────────────────────┐
                                    │ Producto Generado   │
                                    └──────────┬──────────┘
                                               │
                                               ▼
                                    ┌─────────────────────┐
                                    │ Guardar en Repo     │
                                    └─────────────────────┘
```

### Configuración

#### Opción 1: Con OpenAI (Generación real con IA)

```bash
# Configurar variable de entorno
export OPENAI_API_KEY=sk-tu-api-key-aqui

# Ejecutar aplicación
./gradlew bootRun
```

#### Opción 2: Sin OpenAI (Fallback automático)

Si no configuras la API key, el sistema usa un generador aleatorio que crea productos realistas sin necesidad de API externa.

### Usar el Endpoint

```bash
curl -X POST http://localhost:8080/api/products/generate \
  -H "Authorization: Bearer <token>"
```

**Respuesta**:
```json
{
  "id": "gen-a1b2c3d4",
  "name": "Premium Wireless Headphones",
  "imageUrl": "https://via.placeholder.com/400x300",
  "description": "AI-generated product: Premium Wireless Headphones",
  "price": 249.99,
  "rating": 4.5,
  "specifications": {
    "category": "Headphones",
    "generated": "AI Generated",
    "brand": "GenBrand"
  }
}
```

### AIProductGenerator

```java
@Service
public class AIProductGenerator {
    
    @Value("${openai.api.key:}")
    private String openAiApiKey;
    
    public Product generateRandomProduct() {
        if (hasValidApiKey()) {
            return generateWithOpenAI();  // Usa IA real
        } else {
            return generateWithRandom();  // Usa generador fallback
        }
    }
}
```

---

## 🧪 Testing

### Estructura de Tests

```
src/test/java/
└── com.example.productcomparison/
    ├── unit/
    │   ├── controller/     # Tests de controllers
    │   ├── service/        # Tests de services
    │   └── repository/     # Tests de repositories
    ├── integration/        # Tests de integración
    └── acceptance/         # Tests de aceptación
```

### Ejecutar Tests

```bash
# Todos los tests
./gradlew test

# Tests específicos
./gradlew test --tests "ProductServiceTest"

# Con reporte HTML
./gradlew test
# Ver: build/reports/tests/test/index.html
```

### Ejemplo de Test Unitario

```java
@ExtendWith(MockitoExtension.class)
class ProductServiceTest {
    
    @Mock
    private IProductRepository productRepository;
    
    @Mock
    private AIProductGenerator aiProductGenerator;
    
    @InjectMocks
    private ProductService productService;
    
    @Test
    @DisplayName("createProduct should save product successfully")
    void createProduct_ValidProduct_SavesSuccessfully() {
        // Arrange
        Product product = Product.builder()
            .id("test-001")
            .name("Test Product")
            .price(99.99)
            .rating(4.0)
            .build();
        
        when(productRepository.save(any())).thenReturn(product);
        
        // Act
        Product result = productService.createProduct(product);
        
        // Assert
        assertNotNull(result);
        assertEquals("test-001", result.getId());
        verify(productRepository, times(1)).save(product);
    }
}
```

---

## 📚 Documentación de la API

### Swagger UI

La aplicación incluye **Swagger UI** interactivo para explorar y probar la API.

**URL**: http://localhost:8080/swagger-ui.html

**Características**:
- 📖 Explorar todos los endpoints
- 🧪 Probar endpoints directamente desde el navegador
- 🔐 Autenticarse con JWT
- 📝 Ver modelos de datos
- 📋 Copiar ejemplos de código (cURL, JavaScript, etc.)

### Cómo Autenticarse en Swagger

1. **Login** para obtener token:
   - Expandir `Authentication` → `/auth/login`
   - Click en "Try it out"
   - Ingresar credenciales (`admin` / `admin123`)
   - Click en "Execute"
   - Copiar el `token` de la respuesta

2. **Autorizar** en Swagger:
   - Click en el botón "Authorize" (🔓) arriba a la derecha
   - Ingresar: `Bearer <tu-token-aqui>`
   - Click en "Authorize" y luego "Close"

3. **Usar** endpoints protegidos:
   - Ahora todos los endpoints mostrarán un candado cerrado (🔒)
   - Puedes probar cualquier endpoint

### OpenAPI Specification

- **JSON**: http://localhost:8080/v3/api-docs
- **YAML**: http://localhost:8080/v3/api-docs.yaml

---

## 🛠️ Configuración de IDE

### IntelliJ IDEA

1. **Lombok Plugin**:
   - `File` → `Settings` → `Plugins`
   - Buscar "Lombok" e instalar

2. **Annotation Processing**:
   - `File` → `Settings` → `Build, Execution, Deployment` → `Compiler` → `Annotation Processors`
   - ✅ Check "Enable annotation processing"

3. **MapStruct**:
   - El proyecto ya está configurado
   - Después de compilar, la implementación estará en `build/generated/sources/`

### VS Code

1. **Extensiones requeridas**:
   - Extension Pack for Java
   - Spring Boot Extension Pack
   - Lombok Annotations Support

2. **Configuración**:
   - Abrir el proyecto
   - Esperar que Java Language Server descargue dependencias
   - ✅ Listo

### Eclipse

1. **Lombok**:
   - Descargar `lombok.jar` de https://projectlombok.org/download
   - Ejecutar: `java -jar lombok.jar`
   - Seleccionar Eclipse installation
   - Install/Update

2. **Gradle**:
   - `File` → `Import` → `Gradle` → `Existing Gradle Project`

---

## 📦 Estructura del Proyecto

```
product-comparison-java-gradle/
├── src/
│   ├── main/
│   │   ├── java/com/example/productcomparison/
│   │   │   ├── config/              # Configuraciones
│   │   │   │   ├── AppConfig.java
│   │   │   │   ├── SecurityConfig.java
│   │   │   │   ├── JwtUtil.java
│   │   │   │   ├── JwtAuthenticationFilter.java
│   │   │   │   └── UserConfig.java
│   │   │   ├── controller/          # Controllers REST
│   │   │   │   ├── ProductController.java
│   │   │   │   └── AuthController.java
│   │   │   ├── service/             # Lógica de negocio
│   │   │   │   ├── IProductService.java
│   │   │   │   ├── ProductService.java
│   │   │   │   └── AIProductGenerator.java
│   │   │   ├── repository/          # Acceso a datos
│   │   │   │   ├── IProductRepository.java
│   │   │   │   ├── ProductRepository.java
│   │   │   │   ├── ProductDataSource.java
│   │   │   │   ├── ProductMapper.java
│   │   │   │   └── ProductValidator.java
│   │   │   ├── model/               # Modelos y DTOs
│   │   │   │   ├── Product.java
│   │   │   │   ├── ProductDTO.java
│   │   │   │   ├── CreateProductRequest.java
│   │   │   │   ├── LoginRequest.java
│   │   │   │   └── LoginResponse.java
│   │   │   ├── exception/           # Excepciones
│   │   │   │   ├── GlobalExceptionHandler.java
│   │   │   │   ├── ErrorResponse.java
│   │   │   │   ├── service/
│   │   │   │   │   ├── ProductNotFoundException.java
│   │   │   │   │   ├── InvalidParameterException.java
│   │   │   │   │   └── ...
│   │   │   │   └── repository/
│   │   │   │       ├── ProductDataAccessException.java
│   │   │   │       └── ...
│   │   │   └── ProductComparisonApplication.java
│   │   └── resources/
│   │       ├── application.properties
│   │       └── productos.json
│   └── test/                        # Tests
│       └── java/com/example/productcomparison/
│           ├── unit/
│           ├── integration/
│           └── acceptance/
├── build.gradle                     # Configuración Gradle
├── settings.gradle
├── gradlew                          # Gradle Wrapper (Linux/Mac)
├── gradlew.bat                      # Gradle Wrapper (Windows)
├── rebuild.bat                      # Script de recompilación
├── README.md                        # Este archivo
└── [Documentación adicional]
    ├── JWT_CONFIGURATION.md
    ├── SECURITY_CONFIGURATION.md
    ├── FIX_DEPENDENCY_INJECTION.md
    └── FINAL_SUMMARY.md
```

---

## 🚀 Despliegue

### Variables de Entorno para Producción

```bash
# JWT Configuration
export JWT_SECRET_KEY="clave-super-secreta-para-produccion-minimo-256-bits"
export JWT_EXPIRATION_TIME=3600000  # 1 hora

# Database (cuando migres de JSON a DB)
export DB_URL=jdbc:postgresql://localhost:5432/products
export DB_USERNAME=admin
export DB_PASSWORD=secure_password

# OpenAI (opcional)
export OPENAI_API_KEY=sk-real-api-key
```

### Build para Producción

```bash
# Compilar JAR
./gradlew clean build

# El JAR estará en:
# build/libs/product-comparison-0.0.1-SNAPSHOT.jar

# Ejecutar
java -jar build/libs/product-comparison-0.0.1-SNAPSHOT.jar
```

### Docker (Ejemplo)

```dockerfile
FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app
COPY build/libs/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","app.jar"]
```

---

## 🔧 Solución de Problemas

### Error: Using generated security password

**Causa**: Spring Security genera usuario por defecto.

**Solución**: Ya está configurado en `ProductComparisonApplication.java`:
```java
@SpringBootApplication(exclude = {UserDetailsServiceAutoConfiguration.class})
```

### Error: UnsatisfiedDependencyException (ProductMapper)

**Causa**: MapStruct no generó la implementación.

**Solución**:
```bash
.\rebuild.bat
# O manualmente:
.\gradlew clean compileJava build
```

Ver `FIX_DEPENDENCY_INJECTION.md` para más detalles.

### Error: 401 Unauthorized

**Causa**: Token JWT inválido o expirado.

**Solución**:
1. Hacer login nuevamente
2. Usar el nuevo token
3. Verificar que incluyes `Bearer` en el header

---

## 📄 Licencia

Este proyecto está licenciado bajo la Licencia MIT.

---

## 👥 Contribuir

Las contribuciones son bienvenidas. Por favor:

1. Fork el proyecto
2. Crea una rama para tu feature (`git checkout -b feature/AmazingFeature`)
3. Commit tus cambios (`git commit -m 'Add some AmazingFeature'`)
4. Push a la rama (`git push origin feature/AmazingFeature`)
5. Abre un Pull Request

---

## 📞 Contacto

- **Email**: dgpenalozap@gmail.com
- **GitHub**: [Tu GitHub](https://github.com/tu-usuario)

---

## 🙏 Agradecimientos

- Spring Boot Team por el excelente framework
- LangChain4j por la integración con IA
- MapStruct por el mapeo automático
- Lombok por reducir el boilerplate
- La comunidad de Spring y Java

---

<div align="center">
  <p>Hecho con ❤️ usando Spring Boot 3</p>
  <p>⭐ Si te gusta este proyecto, por favor dale una estrella ⭐</p>
</div>
