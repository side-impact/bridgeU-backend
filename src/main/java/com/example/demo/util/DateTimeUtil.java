package com.example.demo.util;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

/**
 * 날짜/시간 포맷팅 유틸리티 클래스
 * 여러 API에서 일관된 날짜 포맷을 사용하기 위한 공용 유틸리티
 */
public class DateTimeUtil {
    
    /**
     * 피드용 날짜 포맷터 (yyyy-MM-dd HH:mm, UTC 기준)
     */
    private static final DateTimeFormatter FEED_FORMATTER = 
        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm").withZone(ZoneId.of("UTC"));
    
    /**
     * Instant를 피드용 날짜 문자열로 포맷팅
     * 
     * @param instant 변환할 Instant 객체
     * @return yyyy-MM-dd HH:mm 형식의 UTC 문자열
     */
    public static String formatToFeed(Instant instant) {
        if (instant == null) {
            return null;
        }
        return FEED_FORMATTER.format(instant);
    }
}
