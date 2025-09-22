package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf().disable() // Flutter, Postman 테스트용
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/auth/**").permitAll() // /auth/**는 인증 없이 접근 가능
                .anyRequest().authenticated()
            );
        return http.build();
    }
}
