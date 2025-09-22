package com.example.demo.model;

public class Author {
    
    private final Long userId;
    private final String username;
    
    public Author(Long userId, String username) {
        this.userId = userId;
        this.username = username;
    }
    
    public Long getUserId() {
        return userId;
    }
    
    public String getUsername() {
        return username;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        
        Author author = (Author) o;
        return userId != null ? userId.equals(author.userId) : author.userId == null;
    }
    
    @Override
    public int hashCode() {
        return userId != null ? userId.hashCode() : 0;
    }
    
    @Override
    public String toString() {
        return "Author{" +
                "userId=" + userId +
                ", username='" + username + '\'' +
                '}';
    }
}
