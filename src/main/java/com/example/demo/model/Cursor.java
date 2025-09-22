package com.example.demo.model;

import java.time.Instant;

/**
 * 커서 페이지네이션용 도메인 객체
 */
public class Cursor {
    
    private final Instant createdAt;
    private final Long id;
    
    public Cursor(Instant createdAt, Long id) {
        this.createdAt = createdAt;
        this.id = id;
    }
    
    public Instant getCreatedAt() {
        return createdAt;
    }
    
    public Long getId() {
        return id;
    }
    
    public boolean isAfter(Cursor other) {
        if (other == null) return true;
        
        int timeComparison = this.createdAt.compareTo(other.createdAt);
        if (timeComparison != 0) {
            return timeComparison > 0;
        }
        
        return this.id.compareTo(other.id) > 0;
    }
    
    public boolean isBefore(Cursor other) {
        if (other == null) return false;
        
        int timeComparison = this.createdAt.compareTo(other.createdAt);
        if (timeComparison != 0) {
            return timeComparison < 0;
        }
        
        return this.id.compareTo(other.id) < 0;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        
        Cursor cursor = (Cursor) o;
        return createdAt.equals(cursor.createdAt) && id.equals(cursor.id);
    }
    
    @Override
    public int hashCode() {
        return createdAt.hashCode() * 31 + id.hashCode();
    }
    
    @Override
    public String toString() {
        return "Cursor{" +
                "createdAt=" + createdAt +
                ", id=" + id +
                '}';
    }
}
