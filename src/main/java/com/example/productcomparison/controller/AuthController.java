package com.example.productcomparison.controller;

import com.example.productcomparison.config.JwtUtil;
import com.example.productcomparison.config.UserConfig;
import com.example.productcomparison.model.LoginRequest;
import com.example.productcomparison.model.LoginResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "A. Authentication", description = "JWT authentication endpoints")
public class AuthController {

    private final JwtUtil jwtUtil;
    private final UserConfig userConfig;

    @Operation(
        summary = "Login with credentials",
        description = """
                Authenticate and get JWT token.
                
                **Demo Users (configured in application.properties):**
                
                1. **Admin User** (Full Access - GET, POST, PUT, DELETE):
                   - Username: `admin`
                   - Password: `admin123`
                
                2. **Regular User** (Read-Only - GET only):
                   - Username: `user`
                   - Password: `user123`
                
                After successful login, use the returned token in the Authorization header:
                `Bearer <token>`
                """
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Login successful",
                    content = @Content(schema = @Schema(implementation = LoginResponse.class))),
        @ApiResponse(responseCode = "401", description = "Invalid credentials")
    })
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        String username = loginRequest.getUsername();
        String password = loginRequest.getPassword();
        
        if (username == null || password == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Username and password are required"));
        }
        
        // Find user in configuration
        UserConfig.DemoUser user = userConfig.getUsers().stream()
                .filter(u -> u.getUsername().equals(username))
                .findFirst()
                .orElse(null);
        
        if (user == null || !user.getPassword().equals(password)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Invalid username or password"));
        }
        
        String token = jwtUtil.generateToken(username, user.getRole());
        
        LoginResponse response = new LoginResponse(
                token,
                username,
                user.getRole(),
                "Login successful"
        );
        
        return ResponseEntity.ok(response);
    }

    @Operation(
        summary = "Get available demo users",
        description = "Returns information about the demo users available for testing (configured in application.properties)"
    )
    @GetMapping("/users")
    public ResponseEntity<?> getUsers() {
        List<Map<String, String>> users = userConfig.getUsers().stream()
                .map(user -> Map.of(
                        "username", user.getUsername(),
                        "password", user.getPassword(),
                        "role", user.getRole(),
                        "permissions", user.getRole().equals("ROLE_ADMIN") 
                                ? "GET, POST, PUT, DELETE" 
                                : "GET only"
                ))
                .collect(Collectors.toList());
        
        return ResponseEntity.ok(Map.of(
                "users", users,
                "note", "This is a demo project. In production, never expose passwords!",
                "source", "Users are configured in application.properties"
        ));
    }
}
