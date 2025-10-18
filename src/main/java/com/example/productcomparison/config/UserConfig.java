package com.example.productcomparison.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

/**
 * Configuration class for demo users loaded from application.properties
 */
@Configuration
@ConfigurationProperties(prefix = "app.security")
@Data
public class UserConfig {
    
    private List<DemoUser> users = new ArrayList<>();
    
    @Data
    public static class DemoUser {
        private String username;
        private String password;
        private String role;
    }
}
