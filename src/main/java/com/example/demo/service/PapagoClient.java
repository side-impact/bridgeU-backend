package com.example.demo.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;

import java.time.Duration;
import java.util.Map;

@Component
public class PapagoClient {

    private final RestClient restClient;
    private final String nmtPath;
    private final int timeoutMs;

    public PapagoClient(
            RestClient.Builder restClientBuilder,
            @Value("${papago.base-url}") String baseUrl,
            @Value("${papago.nmt-path}") String nmtPath,
            @Value("${papago.timeout-ms:3000}") int timeoutMs,
            @Value("${papago.client-id}") String clientId,
            @Value("${papago.client-secret}") String clientSecret
    ) {
        this.nmtPath = nmtPath;
        this.timeoutMs = timeoutMs;
        
        this.restClient = restClientBuilder
                .baseUrl(baseUrl)
                .defaultHeader("X-NCP-APIGW-API-KEY-ID", clientId)
                .defaultHeader("X-NCP-APIGW-API-KEY", clientSecret)
                .build();
    }

    /**
     * Papago 번역 API 호출
     * 
     * @param source
     * @param target 
     * @param text 번역할 텍스트
     * @return 번역된 텍스트
     * @throws RuntimeException 번역 실패
     */
    public String translate(String source, String target, String text) {
        System.out.println("DEBUG: translate() called with source=" + source + ", target=" + target + ", text=" + text);
        
        try {
            System.out.println("DEBUG: Calling Papago API with source=" + source + ", target=" + target + ", text=" + text);
            
            MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
            formData.add("source", source);
            formData.add("target", target);
            formData.add("text", text);

            Map<String, Object> response = restClient.post()
                    .uri(nmtPath)
                    .header("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8")
                    .body(formData)
                    .retrieve()
                    .body(Map.class);

            System.out.println("DEBUG: Papago API response: " + response);

            if (response != null && response.containsKey("message")) {
                Map<String, Object> message = (Map<String, Object>) response.get("message");
                if (message != null && message.containsKey("result")) {
                    Map<String, Object> result = (Map<String, Object>) message.get("result");
                    if (result != null && result.containsKey("translatedText")) {
                        String translatedText = (String) result.get("translatedText");
                        System.out.println("DEBUG: Translation successful: " + translatedText);
                        return translatedText;
                    }
                }
            }

            throw new RuntimeException("Invalid response format from Papago API");

        } catch (Exception e) {
            System.out.println("DEBUG: Papago API call failed: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Papago translation failed", e);
        }
    }
}
