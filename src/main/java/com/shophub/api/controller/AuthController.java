package com.shophub.api.controller;

import com.shophub.api.model.User;
import com.shophub.api.model.request.LoginRequest;
import com.shophub.api.model.request.RegisterRequest;
import com.shophub.api.security.JwtUtil;
import com.shophub.api.security.ShopHubUserDetails;
import com.shophub.api.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    private final JwtUtil jwtUtil;

    public AuthController(AuthService authService, JwtUtil jwtUtil) {
        this.authService = authService;
        this.jwtUtil = jwtUtil;
    }

    /**
     * Register a new user. Request body: { "name", "email", "password" }
     */
    @PostMapping("/register")
    public ResponseEntity<User> register(@Valid @RequestBody RegisterRequest request) {
        User user = authService.register(request.name(), request.email(), request.password());
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }

    /**
     * Login and get JWT token. Request body: { "email", "password" }
     * Returns: { "token": "jwt-token", "user": {...} }
     */
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        User user = authService.login(request.email(), request.password());
        String token = jwtUtil.generateToken(user.getUserId(), user.getEmail());
        return ResponseEntity.ok(new LoginResponse(token, user));
    }

    /**
     * Get current logged-in user from SecurityContext.
     */
    @GetMapping("/me")
    public ResponseEntity<User> me() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        if (authentication == null || !authentication.isAuthenticated() 
                || !(authentication.getPrincipal() instanceof ShopHubUserDetails)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        
        ShopHubUserDetails userDetails = (ShopHubUserDetails) authentication.getPrincipal();
        User user = userDetails.getUser();
        return ResponseEntity.ok(user);
    }

    /**
     * Login response containing JWT token and user details.
     */
    public record LoginResponse(String token, User user) {}
}
