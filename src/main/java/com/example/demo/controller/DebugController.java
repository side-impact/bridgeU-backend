package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/debug")
public class DebugController {

    @Value("${papago.client-id:NOT_SET}")
    private String clientId;
    
    @Value("${papago.client-secret:NOT_SET}")
    private String clientSecret;
    
    @Value("${papago.base-url:NOT_SET}")
    private String baseUrl;
    
    @Value("${translator.hmac-secret:NOT_SET}")
    private String hmacSecret;

    @GetMapping("/env")
    public Map<String, String> checkEnvironment() {
        Map<String, String> env = new HashMap<>();
        env.put("papago.client-id", clientId.length() > 10 ? clientId.substring(0, 10) + "..." : clientId);
        env.put("papago.client-secret", clientSecret.length() > 10 ? clientSecret.substring(0, 10) + "..." : clientSecret);
        env.put("papago.base-url", baseUrl);
        env.put("translator.hmac-secret", hmacSecret.length() > 10 ? hmacSecret.substring(0, 10) + "..." : hmacSecret);
        return env;
    }
}
