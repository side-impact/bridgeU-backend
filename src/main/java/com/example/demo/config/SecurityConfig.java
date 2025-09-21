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
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/auth/**", "/", "/error", "/webjars/**", "/api/**", "/debug/**").permitAll()
                .anyRequest().authenticated()
            );
            // .oauth2Login(oauth -> oauth
            //     .userInfoEndpoint(userInfo -> userInfo.userService(customOAuth2UserService))
            //     .defaultSuccessUrl("/welcome", true)
            // );
        return http.build();
    }
}
