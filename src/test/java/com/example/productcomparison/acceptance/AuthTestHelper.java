package com.example.productcomparison.acceptance;

import com.example.productcomparison.config.JwtUtil;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

/**
 * Helper class for JWT authentication in acceptance tests
 */
@Component
public class AuthTestHelper {

    private final JwtUtil jwtUtil;

    public AuthTestHelper(JwtUtil jwtUtil) {
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
     * Create HttpHeaders with admin authorization
     */
    public HttpHeaders getAdminHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + generateAdminToken());
        return headers;
    }

    /**
     * Create HttpHeaders with user authorization
     */
    public HttpHeaders getUserHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + generateUserToken());
        return headers;
    }

    /**
     * Create HttpHeaders with custom token
     */
    public HttpHeaders getHeadersWithToken(String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        return headers;
    }
}
