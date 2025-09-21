package com.example.demo.service;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CacheConfig {

    @Value("${cache.max-entries:50000}")
    private long maxEntries;

    @Bean
    public Cache<String, CacheEntry> translateCache() {
        return Caffeine.newBuilder()
                .maximumSize(maxEntries)
                .build();
    }

    public static class CacheEntry {
        private final String translatedText;
        private final long expiresAtMs;

        public CacheEntry(String translatedText, long expiresAtMs) {
            this.translatedText = translatedText;
            this.expiresAtMs = expiresAtMs;
        }

        public String getTranslatedText() {
            return translatedText;
        }

        public long getExpiresAtMs() {
            return expiresAtMs;
        }

        public boolean isExpired() {
            return System.currentTimeMillis() > expiresAtMs;
        }
    }
}
