package com.example.productcomparison.controller;

import com.example.productcomparison.config.JwtUtil;
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

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "JWT authentication endpoints")
public class AuthController {

    private final JwtUtil jwtUtil;
    
    // In-memory users (for demo purposes only)
    // Username: admin, Password: admin123, Role: ROLE_ADMIN (full access)
    // Username: user, Password: user123, Role: ROLE_USER (read-only)
    private static final Map<String, String[]> USERS = new HashMap<>() {{
        put("admin", new String[]{"admin123", "ROLE_ADMIN"});
        put("user", new String[]{"user123", "ROLE_USER"});
    }};

    @Operation(
        summary = "Login with credentials",
        description = """
                Authenticate and get JWT token.
                
                **Demo Users:**
                
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
        
        String[] userData = USERS.get(username);
        
        if (userData == null || !userData[0].equals(password)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Invalid username or password"));
        }
        
        String role = userData[1];
        String token = jwtUtil.generateToken(username, role);
        
        LoginResponse response = new LoginResponse(
                token,
                username,
                role,
                "Login successful"
        );
        
        return ResponseEntity.ok(response);
    }

    @Operation(
        summary = "Get available demo users",
        description = "Returns information about the demo users available for testing"
    )
    @GetMapping("/users")
    public ResponseEntity<?> getUsers() {
        return ResponseEntity.ok(Map.of(
                "users", new Object[]{
                        Map.of(
                                "username", "admin",
                                "password", "admin123",
                                "role", "ROLE_ADMIN",
                                "permissions", "GET, POST, PUT, DELETE"
                        ),
                        Map.of(
                                "username", "user",
                                "password", "user123",
                                "role", "ROLE_USER",
                                "permissions", "GET only"
                        )
                },
                "note", "This is a demo project. In production, never expose passwords!"
        ));
    }
}
