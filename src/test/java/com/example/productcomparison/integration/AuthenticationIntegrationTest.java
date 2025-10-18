package com.example.productcomparison.integration;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("Authentication Integration Tests")
public class AuthenticationIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("POST /auth/login should return token for valid admin credentials")
    void login_shouldReturnToken_forValidAdminCredentials() throws Exception {
        // Arrange
        String requestBody = """
            {
                "username": "admin",
                "password": "admin123"
            }
            """;

        // Act & Assert
        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists())
                .andExpect(jsonPath("$.username").value("admin"))
                .andExpect(jsonPath("$.role").value("ROLE_ADMIN"))
                .andExpect(jsonPath("$.message").value("Login successful"));
    }

    @Test
    @DisplayName("POST /auth/login should return token for valid user credentials")
    void login_shouldReturnToken_forValidUserCredentials() throws Exception {
        // Arrange
        String requestBody = """
            {
                "username": "user",
                "password": "user123"
            }
            """;

        // Act & Assert
        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists())
                .andExpect(jsonPath("$.username").value("user"))
                .andExpect(jsonPath("$.role").value("ROLE_USER"))
                .andExpect(jsonPath("$.message").value("Login successful"));
    }

    @Test
    @DisplayName("POST /auth/login should return 401 for invalid username")
    void login_shouldReturn401_forInvalidUsername() throws Exception {
        // Arrange
        String requestBody = """
            {
                "username": "invalid-user",
                "password": "password"
            }
            """;

        // Act & Assert
        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("POST /auth/login should return 401 for invalid password")
    void login_shouldReturn401_forInvalidPassword() throws Exception {
        // Arrange
        String requestBody = """
            {
                "username": "admin",
                "password": "wrong-password"
            }
            """;

        // Act & Assert
        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("POST /auth/login should return 401 for null username")
    void login_shouldReturn401_forNullUsername() throws Exception {
        // Arrange
        String requestBody = """
            {
                "username": null,
                "password": "password"
            }
            """;

        // Act & Assert
        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("POST /auth/login should return 401 for null password")
    void login_shouldReturn401_forNullPassword() throws Exception {
        // Arrange
        String requestBody = """
            {
                "username": "admin",
                "password": null
            }
            """;

        // Act & Assert
        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("GET /auth/users should return list of available demo users")
    void getUsers_shouldReturnUsersList() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/auth/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.users").exists())
                .andExpect(jsonPath("$.users").isArray())
                .andExpect(jsonPath("$.users.length()").value(2))
                .andExpect(jsonPath("$.note").exists())
                .andExpect(jsonPath("$.source").value("Users are configured in application.properties"));
    }

    @Test
    @DisplayName("Authenticated request should work with valid token")
    void authenticatedRequest_shouldWork_withValidToken() throws Exception {
        // Arrange
        String loginBody = """
            {
                "username": "admin",
                "password": "admin123"
            }
            """;

        String response = mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginBody))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        // Extract token
        String token = response.split("\"token\":\"")[1].split("\"")[0];

        // Act & Assert
        mockMvc.perform(get("/api/products")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    @DisplayName("Request with invalid token should return 401")
    void request_shouldReturn401_withInvalidToken() throws Exception {
        // Arrange
        String invalidToken = "invalid-token-xyz";

        // Act & Assert - Verify we get 401 Unauthorized with JSON response
        mockMvc.perform(get("/api/products")
                        .header("Authorization", "Bearer " + invalidToken))
                .andExpect(status().isUnauthorized())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").exists())
                .andExpect(jsonPath("$.error").exists())
                .andExpect(jsonPath("$.errorCode").exists());
    }

    @Test
    @DisplayName("Request with malformed authorization header should return 401")
    void request_shouldReturn401_withMalformedHeader() throws Exception {
        // Arrange
        String malformedHeader = "InvalidFormat token";

        // Act & Assert - Verify we get 401 Unauthorized with JSON response
        mockMvc.perform(get("/api/products")
                        .header("Authorization", malformedHeader))
                .andExpect(status().isUnauthorized())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").exists())
                .andExpect(jsonPath("$.error").exists())
                .andExpect(jsonPath("$.errorCode").exists());
    }

    @Test
    @DisplayName("Request without authorization header should return 401")
    void request_shouldReturn401_withoutAuthHeader() throws Exception {
        // Act & Assert - Verify we get 401 Unauthorized with JSON response
        mockMvc.perform(get("/api/products"))
                .andExpect(status().isUnauthorized())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").exists())
                .andExpect(jsonPath("$.error").exists())
                .andExpect(jsonPath("$.errorCode").exists());
    }
}
