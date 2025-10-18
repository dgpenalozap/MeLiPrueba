package com.example.productcomparison.acceptance;

import com.example.productcomparison.model.LoginRequest;
import com.example.productcomparison.model.LoginResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DisplayName("Authentication Acceptance Tests")
public class AuthenticationAcceptanceTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    @DisplayName("POST /auth/login should return token for valid admin credentials")
    void login_shouldReturnToken_forValidAdminCredentials() {
        // Arrange
        LoginRequest request = new LoginRequest("admin", "admin123");
        
        // Act
        ResponseEntity<LoginResponse> response = restTemplate.postForEntity(
                "/auth/login",
                request,
                LoginResponse.class
        );

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getToken()).isNotNull();
        assertThat(response.getBody().getUsername()).isEqualTo("admin");
        assertThat(response.getBody().getRole()).isEqualTo("ROLE_ADMIN");
        assertThat(response.getBody().getMessage()).isEqualTo("Login successful");
    }

    @Test
    @DisplayName("POST /auth/login should return token for valid user credentials")
    void login_shouldReturnToken_forValidUserCredentials() {
        // Arrange
        LoginRequest request = new LoginRequest("user", "user123");
        
        // Act
        ResponseEntity<LoginResponse> response = restTemplate.postForEntity(
                "/auth/login",
                request,
                LoginResponse.class
        );

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getToken()).isNotNull();
        assertThat(response.getBody().getUsername()).isEqualTo("user");
        assertThat(response.getBody().getRole()).isEqualTo("ROLE_USER");
        assertThat(response.getBody().getMessage()).isEqualTo("Login successful");
    }

    @Test
    @DisplayName("POST /auth/login should return 401 for invalid username")
    void login_shouldReturn401_forInvalidUsername() {
        // Arrange
        LoginRequest request = new LoginRequest("invalid-user", "password");
        
        // Act
        ResponseEntity<Map> response = restTemplate.postForEntity(
                "/auth/login",
                request,
                Map.class
        );

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().get("error")).isEqualTo("Invalid username or password");
    }

    @Test
    @DisplayName("POST /auth/login should return 401 for invalid password")
    void login_shouldReturn401_forInvalidPassword() {
        // Arrange
        LoginRequest request = new LoginRequest("admin", "wrong-password");
        
        // Act
        ResponseEntity<Map> response = restTemplate.postForEntity(
                "/auth/login",
                request,
                Map.class
        );

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().get("error")).isEqualTo("Invalid username or password");
    }

    @Test
    @DisplayName("POST /auth/login should return 401 for null username")
    void login_shouldReturn401_forNullUsername() {
        // Arrange
        LoginRequest request = new LoginRequest(null, "password");
        
        // Act
        ResponseEntity<Map> response = restTemplate.postForEntity(
                "/auth/login",
                request,
                Map.class
        );

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().get("error")).isEqualTo("Username and password are required");
    }

    @Test
    @DisplayName("POST /auth/login should return 401 for null password")
    void login_shouldReturn401_forNullPassword() {
        // Arrange
        LoginRequest request = new LoginRequest("admin", null);
        
        // Act
        ResponseEntity<Map> response = restTemplate.postForEntity(
                "/auth/login",
                request,
                Map.class
        );

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().get("error")).isEqualTo("Username and password are required");
    }

    @Test
    @DisplayName("GET /auth/users should return list of available demo users")
    void getUsers_shouldReturnUsersList() {
        // Act
        ResponseEntity<Map> response = restTemplate.getForEntity("/auth/users", Map.class);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().get("users")).isNotNull();
        assertThat(response.getBody().get("note")).isNotNull();
        assertThat(response.getBody().get("source")).isEqualTo("Users are configured in application.properties");
    }
}
