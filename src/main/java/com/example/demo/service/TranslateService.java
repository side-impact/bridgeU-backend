package com.example.demo.service;

import com.github.benmanes.caffeine.cache.Cache;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.HttpServerErrorException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.text.Normalizer;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.Set;
import java.util.concurrent.TimeoutException;

@Service
public class TranslateService {

    private final PapagoClient papagoClient;
    private final Cache<String, CacheConfig.CacheEntry> translateCache;
    private final String hmacSecret;
    private final long defaultTtlHours;
    private final long shortTextTtlDays;
    private final int shortTextMaxLen;

    private static final Set<String> SUPPORTED_SOURCE_LANGS = Set.of("auto", "en", "ja", "zh-CN", "ko");
    private static final Set<String> SUPPORTED_TARGET_LANGS = Set.of("en", "ja", "zh-CN", "ko");

    public TranslateService(
            PapagoClient papagoClient,
            Cache<String, CacheConfig.CacheEntry> translateCache,
            @Value("${translator.hmac-secret}") String hmacSecret,
            @Value("${cache.ttl.hours.default:24}") long defaultTtlHours,
            @Value("${cache.ttl.days.shortText:7}") long shortTextTtlDays,
            @Value("${cache.shortText.maxLen:64}") int shortTextMaxLen
    ) {
        this.papagoClient = papagoClient;
        this.translateCache = translateCache;
        this.hmacSecret = hmacSecret;
        this.defaultTtlHours = defaultTtlHours;
        this.shortTextTtlDays = shortTextTtlDays;
        this.shortTextMaxLen = shortTextMaxLen;
    }

    /**
     * 텍스트 번역
     * 
     * @param sourceText
     * @param sourceLang
     * @param targetLang
     * @return 번역된 텍스트
     * @throws AppError 오류류
     */
    public String translate(String sourceText, String sourceLang, String targetLang) {
        String normalizedText = normalizeText(sourceText);
        validateInput(normalizedText, sourceLang, targetLang);

        //캐싱 : 키 생성 -> 조회
        String cacheKey = generateCacheKey(normalizedText, sourceLang, targetLang);
        CacheConfig.CacheEntry cachedEntry = translateCache.getIfPresent(cacheKey);
        if (cachedEntry != null && !cachedEntry.isExpired()) {
            return cachedEntry.getTranslatedText();
        }

        synchronized (cacheKey.intern()) {
            cachedEntry = translateCache.getIfPresent(cacheKey);
            if (cachedEntry != null && !cachedEntry.isExpired()) {
                return cachedEntry.getTranslatedText();
            }

            try {
                //Papago API 호출
                String translatedText = papagoClient.translate(sourceLang, targetLang, normalizedText);

                //ttl계산, 캐시 저장
                long ttlMs = calculateTtl(normalizedText);
                long expiresAtMs = System.currentTimeMillis() + ttlMs;
                CacheConfig.CacheEntry newEntry = new CacheConfig.CacheEntry(translatedText, expiresAtMs);
                translateCache.put(cacheKey, newEntry);

                return translatedText;

            } catch (Exception e) {
                throw mapException(e);
            }
        }
    }

    private String normalizeText(String text) {
        if (text == null) {
            return "";
        }
        
        String normalized = Normalizer.normalize(text, Normalizer.Form.NFKC);
        normalized = normalized.trim();
        normalized = normalized.replaceAll("\\s+", " ");
        
        return normalized;
    }

    private void validateInput(String normalizedText, String sourceLang, String targetLang) {
        if (normalizedText.isEmpty()) {
            throw new AppError(400, "VALIDATION_EMPTY");
        }

        if (normalizedText.codePointCount(0, normalizedText.length()) > 500) {
            throw new AppError(400, "VALIDATION_TOO_LONG");
        }

        if (!SUPPORTED_SOURCE_LANGS.contains(sourceLang)) {
            throw new AppError(400, "VALIDATION_UNSUPPORTED_LANG");
        }
        if (!SUPPORTED_TARGET_LANGS.contains(targetLang)) {
            throw new AppError(400, "VALIDATION_UNSUPPORTED_LANG");
        }
    }

    // SHA256 캐시 키 생성
    private String generateCacheKey(String normalizedText, String sourceLang, String targetLang) {
        try {
            String input = normalizedText + "|" + sourceLang + "|" + targetLang;
            
            Mac mac = Mac.getInstance("HmacSHA256");
            SecretKeySpec secretKeySpec = new SecretKeySpec(
                hmacSecret.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
            mac.init(secretKeySpec);
            
            byte[] hash = mac.doFinal(input.getBytes(StandardCharsets.UTF_8));
            
            return Base64.getUrlEncoder().withoutPadding().encodeToString(hash);
            
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate cache key", e);
        }
    }

    private long calculateTtl(String normalizedText) {
        String textWithoutSpaces = normalizedText.replaceAll("\\s", "");
        int lengthWithoutSpaces = textWithoutSpaces.codePointCount(0, textWithoutSpaces.length());
        
        if (lengthWithoutSpaces <= shortTextMaxLen) {
            return shortTextTtlDays * 24 * 60 * 60 * 1000L; // days2ms
        } else {
            return defaultTtlHours * 60 * 60 * 1000L; // hours2ms
        }
    }

    private AppError mapException(Exception e) {
        if (e instanceof AppError) {
            return (AppError) e;
        }
        
        if (e instanceof ResourceAccessException || 
            e instanceof TimeoutException ||
            e.getCause() instanceof java.net.SocketTimeoutException ||
            e.getCause() instanceof java.net.ConnectException) {
            return new AppError(503, "UPSTREAM_TIMEOUT");
        }
        
        if (e instanceof HttpServerErrorException) {
            return new AppError(502, "UPSTREAM_ERROR");
        }
        
        return new AppError(500, "INTERNAL");
    }

    public static class AppError extends RuntimeException {
        private final int code;
        private final String message;

        public AppError(int code, String message) {
            super(message);
            this.code = code;
            this.message = message;
        }

        public int getCode() {
            return code;
        }

        @Override
        public String getMessage() {
            return message;
        }
    }
}
