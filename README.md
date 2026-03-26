# 🎨 StyleMatch API

A RESTful web service built with **Spring Boot 3** for managing customer color profiles.

## 🛠️ Tech Stack & Features
- **Java 17+ / Spring Boot 3**
- **Spring Web** for REST Endpoints
- **Spring Data JPA & PostgreSQL** for persistence
- **Validation** via Jakarta Annotations
- **Lombok** for reducing boilerplate code
- **OpenAPI / Swagger UI** (springdoc) integration
- **Clean Architecture** layout
- **Global Exception Handling** using `@ControllerAdvice`
- **SLF4J / Logback** standard logging

## 📁 Project Structure

```
com.stylematch
  ├── config         # Configuration classes (e.g. DataInitializer)
  ├── controller     # REST API Entry points (CustomerController, HealthController)
  ├── domain         # JPA Entities and Enums (Customer, ColorType, etc.)
  ├── dto            # Request/Response objects
  ├── exception      # Global Exception Handler and custom exceptions
  ├── mapper         # Mapping logic (Entity ↔ DTO)
  ├── repository     # Spring Data JPA repositories
  └── service
      └── impl       # Service business logic implementations
```

## 🚀 Getting Started

### 1. Database Configuration (Docker & Liquibase)
The application expects a PostgreSQL instance running on `localhost:5432`. We provide a `docker-compose.yml` to spin this up quickly:

```bash
# Start the PostgreSQL database in detached mode
docker-compose up -d
```
You can verify the container is running securely inside Docker Desktop. 
Liquibase is configured to automatically run schema migrations and create the `customers` table when you start the Spring Boot application.

### 2. Run the Application
You can use Maven Wrapper to build and run the application:

```bash
# Build the application
./mvnw clean package

# Run the application
./mvnw spring-boot:run
```

The application will start on port `8080`.

## 📚 API Documentation (OpenAPI)

We have integrated **Swagger UI**. To access the UI:
- **Swagger UI**: [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)
- **OpenAPI JSON Docs**: [http://localhost:8080/api-docs](http://localhost:8080/api-docs)

An exported `openapi.yaml` is also available in the root directory.

## 🧪 Testing the API

### 1. Health Check
```bash
curl http://localhost:8080/ping
# Returns: pong
```

### 2. Fetch all customers
```bash
curl http://localhost:8080/customers
```

*(By default, 2 sample customers are seeded using `DataInitializer`)*
