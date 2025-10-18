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
        LoginRequest request = new LoginRequest("admin", "admin123");
        
        ResponseEntity<LoginResponse> response = restTemplate.postForEntity(
                "/auth/login",
                request,
                LoginResponse.class
        );

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
        LoginRequest request = new LoginRequest("user", "user123");
        
        ResponseEntity<LoginResponse> response = restTemplate.postForEntity(
                "/auth/login",
                request,
                LoginResponse.class
        );

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
        LoginRequest request = new LoginRequest("invalid-user", "password");
        
        ResponseEntity<Map> response = restTemplate.postForEntity(
                "/auth/login",
                request,
                Map.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().get("error")).isEqualTo("Invalid username or password");
    }

    @Test
    @DisplayName("POST /auth/login should return 401 for invalid password")
    void login_shouldReturn401_forInvalidPassword() {
        LoginRequest request = new LoginRequest("admin", "wrong-password");
        
        ResponseEntity<Map> response = restTemplate.postForEntity(
                "/auth/login",
                request,
                Map.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().get("error")).isEqualTo("Invalid username or password");
    }

    @Test
    @DisplayName("POST /auth/login should return 401 for null username")
    void login_shouldReturn401_forNullUsername() {
        LoginRequest request = new LoginRequest(null, "password");
        
        ResponseEntity<Map> response = restTemplate.postForEntity(
                "/auth/login",
                request,
                Map.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().get("error")).isEqualTo("Username and password are required");
    }

    @Test
    @DisplayName("POST /auth/login should return 401 for null password")
    void login_shouldReturn401_forNullPassword() {
        LoginRequest request = new LoginRequest("admin", null);
        
        ResponseEntity<Map> response = restTemplate.postForEntity(
                "/auth/login",
                request,
                Map.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().get("error")).isEqualTo("Username and password are required");
    }

    @Test
    @DisplayName("GET /auth/users should return list of available demo users")
    void getUsers_shouldReturnUsersList() {
        ResponseEntity<Map> response = restTemplate.getForEntity("/auth/users", Map.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().get("users")).isNotNull();
        assertThat(response.getBody().get("note")).isNotNull();
    }
}
