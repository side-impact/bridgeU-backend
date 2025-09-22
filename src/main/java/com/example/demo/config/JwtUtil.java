package com.example.demo.config;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;
import java.util.Date;
import java.security.Key;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class JwtUtil {
    private static final Logger logger = LoggerFactory.getLogger(JwtUtil.class);
    
    private final String SECRET_KEY = "your_very_long_secret_key_at_least_32_characters"; // 최소 32자
    private final long EXPIRATION = 1000 * 60 * 60 * 10; // 10시간

    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    }

    public String generateToken(String email) {
        logger.info("JWT 토큰 생성 시작 - email: {}", email);
        try {
            Date now = new Date();
            Date expiration = new Date(System.currentTimeMillis() + EXPIRATION);
            
            logger.info("토큰 발행 시간: {}", now);
            logger.info("토큰 만료 시간: {}", expiration);
            
            String token = Jwts.builder()
                    .setSubject(email)
                    .setIssuedAt(now)
                    .setExpiration(expiration)
                    .signWith(getSigningKey())
                    .compact();
                    
            logger.info("JWT 토큰 생성 완료 - email: {}, token length: {}", email, token.length());
            return token;
            
        } catch (Exception e) {
            logger.error("JWT 토큰 생성 실패 - email: {}, 오류: {}", email, e.getMessage());
            logger.error("오류 스택 트레이스: ", e);
            throw e;
        }
    }

    public String extractEmail(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }
}
