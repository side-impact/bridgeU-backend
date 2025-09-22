package com.example.demo.dto.posts;

import com.example.demo.model.Post;
import com.example.demo.util.DateTimeUtil;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class PostItemDto {
    
    @JsonProperty("post_id")
    private Long postId;
    
    private List<Integer> tags;
    
    private String title;
    
    private String snippet;
    
    @JsonProperty("first_image_url")
    private String firstImageUrl;
    
    @JsonProperty("created_at")
    private String createdAt;
    
    @JsonProperty("view_count")
    private Integer viewCount;
    
    private AuthorDto author;
    
    public PostItemDto() {}
    
    public PostItemDto(Long postId, List<Integer> tags, String title, String snippet,
                       String firstImageUrl, String createdAt, Integer viewCount, AuthorDto author) {
        this.postId = postId;
        this.tags = tags;
        this.title = title;
        this.snippet = snippet;
        this.firstImageUrl = firstImageUrl;
        this.createdAt = createdAt;
        this.viewCount = viewCount;
        this.author = author;
    }
    
    /**
     * Post 도메인 객체로부터 DTO 생성
     * 
     * @param post 변환할 Post 객체
     * @return PostItemDto 객체
     */
    public static PostItemDto from(Post post) {
        if (post == null) {
            return null;
        }
        
        AuthorDto authorDto = null;
        if (post.getAuthor() != null) {
            authorDto = new AuthorDto(
                post.getAuthor().getUserId(),
                post.getAuthor().getUsername()
            );
        }
        
        return new PostItemDto(
            post.getPostId(),
            post.getTags(),
            post.getTitle(),
            post.getSnippet(),
            post.getFirstImageUrl(),
            DateTimeUtil.formatToFeed(post.getCreatedAt()),
            post.getViewCount(),
            authorDto
        );
    }
    
    public Long getPostId() {
        return postId;
    }
    
    public void setPostId(Long postId) {
        this.postId = postId;
    }
    
    public List<Integer> getTags() {
        return tags;
    }
    
    public void setTags(List<Integer> tags) {
        this.tags = tags;
    }
    
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public String getSnippet() {
        return snippet;
    }
    
    public void setSnippet(String snippet) {
        this.snippet = snippet;
    }
    
    public String getFirstImageUrl() {
        return firstImageUrl;
    }
    
    public void setFirstImageUrl(String firstImageUrl) {
        this.firstImageUrl = firstImageUrl;
    }
    
    public String getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
    
    public Integer getViewCount() {
        return viewCount;
    }
    
    public void setViewCount(Integer viewCount) {
        this.viewCount = viewCount;
    }
    
    public AuthorDto getAuthor() {
        return author;
    }
    
    public void setAuthor(AuthorDto author) {
        this.author = author;
    }
    

    public static class AuthorDto {
        
        @JsonProperty("user_id")
        private Long userId;
        
        private String username;
        
        public AuthorDto() {}
        
        public AuthorDto(Long userId, String username) {
            this.userId = userId;
            this.username = username;
        }
        
        public Long getUserId() {
            return userId;
        }
        
        public void setUserId(Long userId) {
            this.userId = userId;
        }
        
        public String getUsername() {
            return username;
        }
        
        public void setUsername(String username) {
            this.username = username;
        }
    }
}
