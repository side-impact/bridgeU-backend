package com.example.demo.model;

import java.time.Instant;
import java.util.List;


public class Post {
    
    private final Long postId;
    private final List<Integer> tags;
    private final String title;
    private final String contentText;
    private final String firstImageUrl;
    private final Instant createdAt;
    private final Integer viewCount;
    private final Author author;
    
    public Post(Long postId, List<Integer> tags, String title, String contentText, 
                String firstImageUrl, Instant createdAt, Integer viewCount, Author author) {
        this.postId = postId;
        this.tags = tags;
        this.title = title;
        this.contentText = contentText;
        this.firstImageUrl = firstImageUrl;
        this.createdAt = createdAt;
        this.viewCount = viewCount;
        this.author = author;
    }
    

    public String getSnippet() {
        if (contentText == null || contentText.trim().isEmpty()) {
            return "";
        }
        
        String trimmed = contentText.trim();
        if (trimmed.length() <= 50) {
            return trimmed;
        }
        
        return trimmed.substring(0, 50);
    }
    

    public Cursor toCursor() {
        return new Cursor(createdAt, postId);
    }
    

    public boolean hasFirstImage() {
        return firstImageUrl != null && !firstImageUrl.trim().isEmpty();
    }
    
    // Getters
    public Long getPostId() {
        return postId;
    }
    
    public List<Integer> getTags() {
        return tags;
    }
    
    public String getTitle() {
        return title;
    }
    
    public String getContentText() {
        return contentText;
    }
    
    public String getFirstImageUrl() {
        return firstImageUrl;
    }
    
    public Instant getCreatedAt() {
        return createdAt;
    }
    
    public Integer getViewCount() {
        return viewCount;
    }
    
    public Author getAuthor() {
        return author;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        
        Post post = (Post) o;
        return postId != null ? postId.equals(post.postId) : post.postId == null;
    }
    
    @Override
    public int hashCode() {
        return postId != null ? postId.hashCode() : 0;
    }
    
    @Override
    public String toString() {
        return "Post{" +
                "postId=" + postId +
                ", title='" + title + '\'' +
                ", author=" + author +
                ", createdAt=" + createdAt +
                '}';
    }
}
