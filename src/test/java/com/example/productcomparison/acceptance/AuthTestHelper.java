package com.example.productcomparison.acceptance;

import com.example.productcomparison.config.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

/**
 * Helper class for JWT authentication in acceptance tests.
 * Generates valid JWT tokens for test users based on application.properties configuration.
 * 
 * Test users:
 * - admin/admin123 with ROLE_ADMIN
 * - user/user123 with ROLE_USER
 */
@Component
public class AuthTestHelper {

    @Autowired
    private JwtUtil jwtUtil;

    /**
     * Generate admin token for tests
     * User: admin with ROLE_ADMIN
     */
    public String generateAdminToken() {
        return jwtUtil.generateToken("admin", "ROLE_ADMIN");
    }

    /**
     * Generate user token for tests
     * User: user with ROLE_USER
     */
    public String generateUserToken() {
        return jwtUtil.generateToken("user", "ROLE_USER");
    }

    /**
     * Generate token for a custom user
     */
    public String generateToken(String username, String role) {
        return jwtUtil.generateToken(username, role);
    }

    /**
     * Create HttpHeaders with admin authorization and JSON content type
     */
    public HttpHeaders getAdminHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + generateAdminToken());
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }

    /**
     * Create HttpHeaders with user authorization and JSON content type
     */
    public HttpHeaders getUserHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + generateUserToken());
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }

    /**
     * Create HttpHeaders with custom token and JSON content type
     */
    public HttpHeaders getHeadersWithToken(String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }

    /**
     * Create HttpHeaders without authorization (for testing unauthorized access)
     */
    public HttpHeaders getUnauthenticatedHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }
}
