package com.example.demo.entity;

import jakarta.persistence.*;
import java.time.Instant;


@Entity
@Table(name = "posts")
public class Post {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "title")
    private String title;
    
    @Column(name = "created_at")
    private Instant createdAt;
    
    // 기본 생성자 (JPA 필수)
    public Post() {}
    
    // Getters
    public Long getId() {
        return id;
    }
    
    public String getTitle() {
        return title;
    }
    
    public Instant getCreatedAt() {
        return createdAt;
    }
    
    // Setters (필요시)
    public void setId(Long id) {
        this.id = id;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }
}
