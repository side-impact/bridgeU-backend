package com.example.demo.repository;

import java.time.Instant;


public interface PostProjection {
    
    Long getPostId();
    
    Integer[] getTags();
    
    String getTitle();
    
    String getContentText();
    
    Instant getCreatedAt();
    
    Integer getViewCount();
    
    Long getUserId();
    
    String getUsername();
    
    String getFirstImageUrl();
}
