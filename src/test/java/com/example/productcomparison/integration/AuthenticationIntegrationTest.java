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
        String requestBody = """
            {
                "username": "admin",
                "password": "admin123"
            }
            """;

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
        String requestBody = """
            {
                "username": "user",
                "password": "user123"
            }
            """;

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
        String requestBody = """
            {
                "username": "invalid-user",
                "password": "password"
            }
            """;

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.error").value("Invalid username or password"));
    }

    @Test
    @DisplayName("POST /auth/login should return 401 for invalid password")
    void login_shouldReturn401_forInvalidPassword() throws Exception {
        String requestBody = """
            {
                "username": "admin",
                "password": "wrong-password"
            }
            """;

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.error").value("Invalid username or password"));
    }

    @Test
    @DisplayName("POST /auth/login should return 401 for null username")
    void login_shouldReturn401_forNullUsername() throws Exception {
        String requestBody = """
            {
                "username": null,
                "password": "password"
            }
            """;

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.error").value("Username and password are required"));
    }

    @Test
    @DisplayName("POST /auth/login should return 401 for null password")
    void login_shouldReturn401_forNullPassword() throws Exception {
        String requestBody = """
            {
                "username": "admin",
                "password": null
            }
            """;

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.error").value("Username and password are required"));
    }

    @Test
    @DisplayName("GET /auth/users should return list of available demo users")
    void getUsers_shouldReturnUsersList() throws Exception {
        mockMvc.perform(get("/auth/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.users").exists())
                .andExpect(jsonPath("$.users").isArray())
                .andExpect(jsonPath("$.users.length()").value(2))
                .andExpect(jsonPath("$.note").exists());
    }

    @Test
    @DisplayName("Authenticated request should work with valid token")
    void authenticatedRequest_shouldWork_withValidToken() throws Exception {
        // First, login to get a token
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

        // Extract token (simple extraction for test)
        String token = response.split("\"token\":\"")[1].split("\"")[0];

        // Use the token to access protected endpoint
        mockMvc.perform(get("/api/products")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    @DisplayName("Request with invalid token should return 401")
    void request_shouldReturn401_withInvalidToken() throws Exception {
        mockMvc.perform(get("/api/products")
                        .header("Authorization", "Bearer invalid-token-xyz"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("Request with malformed authorization header should return 401")
    void request_shouldReturn401_withMalformedHeader() throws Exception {
        mockMvc.perform(get("/api/products")
                        .header("Authorization", "InvalidFormat token"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("Request without authorization header should return 401")
    void request_shouldReturn401_withoutAuthHeader() throws Exception {
        mockMvc.perform(get("/api/products"))
                .andExpect(status().isUnauthorized());
    }
}
