package com.example.demo.service;
//무한 페이지네이션용 커서
import com.example.demo.model.Cursor;

import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Base64;
import java.util.Optional;


public class CursorCodec {
    
    private static final String DELIMITER = "|";
    
    public static String encode(Cursor cursor) {
        if (cursor == null) {
            return null;
        }
        
        try {
            String instantStr = cursor.getCreatedAt().toString();
            String payload = instantStr + DELIMITER + cursor.getId();
            return Base64.getEncoder().encodeToString(payload.getBytes());
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to encode cursor", e);
        }
    }
    
    public static Optional<Cursor> decode(String encodedCursor) {
        if (encodedCursor == null || encodedCursor.trim().isEmpty()) {
            return Optional.empty();
        }
        
        try {
            String payload = new String(Base64.getDecoder().decode(encodedCursor.trim()));
            
            String[] parts = payload.split("\\" + DELIMITER, 2);
            if (parts.length != 2) {
                return Optional.empty();
            }
            
            Instant createdAt = Instant.parse(parts[0]);
            Long id = Long.parseLong(parts[1]);
            
            return Optional.of(new Cursor(createdAt, id));
            
        } catch (IllegalArgumentException | DateTimeParseException e) {
            return Optional.empty();
        }
    }
    
    public static boolean isValid(String encodedCursor) {
        return decode(encodedCursor).isPresent();
    }
}
