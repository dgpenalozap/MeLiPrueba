package com.example.productcomparison.integration;

import com.example.productcomparison.config.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Helper class for JWT authentication in integration tests.
 * Generates valid JWT tokens for test users.
 */
@Component
public class AuthIntegrationTestHelper {

    @Autowired
    private JwtUtil jwtUtil;

    /**
     * Generate admin token for tests
     */
    public String generateAdminToken() {
        return jwtUtil.generateToken("admin", "ROLE_ADMIN");
    }

    /**
     * Generate user token for tests
     */
    public String generateUserToken() {
        return jwtUtil.generateToken("user", "ROLE_USER");
    }

    /**
     * Get Bearer token header value for admin
     */
    public String getAdminBearerToken() {
        return "Bearer " + generateAdminToken();
    }

    /**
     * Get Bearer token header value for user
     */
    public String getUserBearerToken() {
        return "Bearer " + generateUserToken();
    }
}
