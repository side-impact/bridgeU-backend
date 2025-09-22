package com.example.demo.service;

import com.example.demo.config.JwtUtil;
import com.example.demo.model.User;
import org.springframework.stereotype.Service;

@Service
public class JwtAuthService {
    
    private final JwtUtil jwtUtil;
    private final UserService userService;
    
    public JwtAuthService(JwtUtil jwtUtil, UserService userService) {
        this.jwtUtil = jwtUtil;
        this.userService = userService;
    }
    
    //토큰에서 사용자 추출
    public User getUserFromToken(String token) {
        try {
            String email = jwtUtil.extractEmail(token);
            return userService.findUserfromDBbyEmail(email);
        } catch (Exception e) {
            return null;
        }
    }
    
    //헤더에서 toekn 추출
    public String extractTokenFromHeader(String authHeader) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }
        return null;
    }
    
    public boolean isValidToken(String token) {
        return getUserFromToken(token) != null;
    }
}
