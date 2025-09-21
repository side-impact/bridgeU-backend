//package com.example.bridgeu.config;
package com.example.demo.config;

//import com.example.bridgeu.service.CustomOAuth2UserService;
//import com.example.demo.service.CustomOAuth2UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    //private final CustomOAuth2UserService customOAuth2UserService;

    // public SecurityConfig(CustomOAuth2UserService customOAuth2UserService) {
    //     this.customOAuth2UserService = customOAuth2UserService;
    // }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable()) // CSRF 비활성화
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/", "/error", "/webjars/**", "/api/**", "/debug/**").permitAll()
                .anyRequest().authenticated()
            );
            // .oauth2Login(oauth -> oauth
            //     .userInfoEndpoint(userInfo -> userInfo.userService(customOAuth2UserService))
            //     .defaultSuccessUrl("/welcome", true)
            // );
        return http.build();
    }
}
