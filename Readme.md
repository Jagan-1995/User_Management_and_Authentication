# User Management and Authentication System

This project is a Spring Boot-based User Management and Authentication System that provides RESTful endpoints for user management and JWT-based authentication. It supports user creation, update, deletion, and secure login using JWT.

## Features
- User Management:
   - Create, Update, Delete, and List Users
   - Role-based Access Control (RBAC)
- Authentication:
   - Login with JWT Token Generation
   - Password Hashing using BCrypt
- Security:
   - Stateless Session Management
   - Secure JWT Handling
- Documentation:
   - API documentation with Swagger UI
- Dockerized for Easy Setup

## Requirements
- Java 17 or higher
- Maven 3.8 or higher
- MySQL 8.0 (or Docker for running the database)
- Postman (optional, for testing APIs)

## Setup Instructions
### 1. Clone the Repository
```bash
git clone <repository-url>
cd user-management
```
### 2. Configure the Application
Edit the application.properties file located in src/main/resources to configure your database and JWT secret:
```properties
# Database Configuration
spring.datasource.url=jdbc:mysql://localhost:3306/user_management
spring.datasource.username=root
spring.datasource.password=password

# Hibernate Configuration
spring.jpa.hibernate.ddl-auto=update

# JWT Configuration
jwt.secret=<your-512-bit-secret>
jwt.expiration=3600000
```
Generate a 512-bit secret key for jwt.secret using the following Java snippet:
```java
import io.jsonwebtoken.security.Keys;
import java.util.Base64;

public class GenerateKey {
    public static void main(String[] args) {
        String key = Base64.getEncoder().encodeToString(Keys.secretKeyFor(io.jsonwebtoken.SignatureAlgorithm.HS512).getEncoded());
        System.out.println("Generated Key: " + key);
    }
}
```
### 3. Build the Project
Build the application using Maven:
```bash
mvn clean install
```
### 4. Run the Application
Run the application using:
```bash
java -jar target/user-management.jar
```
The application will start on http://localhost:8080.

## Dockerized Setup
### 1. Build and Run with Docker
Make sure Docker and Docker Compose are installed.
Build the Docker image:
```bash
docker build -t user-management .
```
Run the application with Docker Compose:
```bash
docker-compose up --build
```
### 2. Verify Setup
- Access the application at http://localhost:8080.
- Verify the Swagger UI documentation at http://localhost:8080/swagger-ui.html.

## API Endpoints
### Authentication
- POST /api/auth/login: Authenticate a user and return a JWT token.
```json
{
  "email": "user@example.com",
  "password": "password123"
}
```
### User Management
- POST /api/users: Create a new user.
- PUT /api/users/{id}: Update an existing user.
- DELETE /api/users/{id}: Delete a user by ID.
- GET /api/users?page=0&size=10: Get a paginated list of users.

## Testing
### 1. Run Unit Tests
```bash
mvn test
```
### 2. Test APIs with Postman
- Import the provided Postman collection (postman_collection.json) to test the APIs.

## Additional Notes
- Swagger Documentation: Automatically available at http://localhost:8080/swagger-ui.html.
- Database Schema: The database schema is automatically created by Hibernate.
- Security: The application uses JWT for secure authentication and authorization.
- Error Handling: Custom exception handling is implemented for handling exceptions.
- Logging: Logging is implemented using SLF4J and Logback.
- Docker: The application can be easily run in a Docker container.

## License
This project is open-source and available under the [MIT License](LICENSE).



