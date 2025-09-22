package com.example.demo.controller;

import com.example.demo.config.JwtUtil;
import com.example.demo.model.User;
import com.example.demo.service.UserService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    private final UserService userService;
    private final JwtUtil jwtUtil;

    public AuthController(UserService userService, JwtUtil jwtUtil) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest request) {
        // UserService authenticate 반환값 확인
        User user = userService.authenticate(request.getEmail(), request.getPassword());
        if (user == null) {
            return ResponseEntity.status(401).body(new AuthResponse("Invalid credentials"));
        }

        String token = jwtUtil.generateToken(user.getEmail());
        return ResponseEntity.ok(new AuthResponse(token));
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest request) {
        logger.info("Register endpoint called");
        logger.info("Received register request - email: {}, name: {}", request.getEmail(), request.getName());

        if (userService.existsByEmail(request.getEmail())) {
            logger.warn("Email already exists: {}", request.getEmail());
            return ResponseEntity.badRequest().body(new AuthResponse("Email already exists"));
        }

        User user = userService.register(request.getEmail(), request.getPassword(), request.getName());
        String token = jwtUtil.generateToken(user.getEmail());
        logger.info("User registered successfully: {}", user.getEmail());

        return ResponseEntity.ok(new AuthResponse(token));
    }

    // DTO
    @Data
    @NoArgsConstructor
    static class AuthRequest {
        private String email;
        private String password;
    }

    @Data
    @NoArgsConstructor
    static class RegisterRequest {
        private String email;
        private String password;
        private String name;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    static class AuthResponse {
        private String token;
    }
}
