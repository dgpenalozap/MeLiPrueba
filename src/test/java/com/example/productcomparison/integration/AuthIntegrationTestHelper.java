package com.example.productcomparison.integration;

import com.example.productcomparison.config.JwtUtil;
import org.springframework.stereotype.Component;

/**
 * Helper class for JWT authentication in integration tests
 */
@Component
public class AuthIntegrationTestHelper {

    private final JwtUtil jwtUtil;

    public AuthIntegrationTestHelper(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

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
