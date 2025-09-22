package com.example.demo.dto.post;

import com.example.demo.dto.posts.PostItemDto.AuthorDto;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * 게시물 상세 정보 응답 DTO
 * GET /api/posts/{post_id} API의 응답 데이터 구조
 */
public class PostDetailDto {
    
    @JsonProperty("post_id")
    private Long postId;
    
    private List<Integer> tags;
    
    private String title;
    
    private AuthorDto author;
    
    @JsonProperty("created_at")
    private String createdAt;
    
    @JsonProperty("updated_at")
    private String updatedAt;
    
    private String lang;
    
    @JsonProperty("view_count")
    private Long viewCount;
    
    private List<BlockDto> blocks;
    
    @JsonProperty("content_text")
    private String contentText;
    
    @JsonProperty("is_owner")
    private Boolean isOwner;
    
    // 기본 생성자
    public PostDetailDto() {}
    
    // 전체 생성자
    public PostDetailDto(Long postId, List<Integer> tags, String title, AuthorDto author,
                         String createdAt, String updatedAt, String lang, Long viewCount,
                         List<BlockDto> blocks, String contentText, Boolean isOwner) {
        this.postId = postId;
        this.tags = tags;
        this.title = title;
        this.author = author;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.lang = lang;
        this.viewCount = viewCount;
        this.blocks = blocks;
        this.contentText = contentText;
        this.isOwner = isOwner;
    }
    
    // Getters and Setters
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
    
    public AuthorDto getAuthor() {
        return author;
    }
    
    public void setAuthor(AuthorDto author) {
        this.author = author;
    }
    
    public String getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
    
    public String getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }
    
    public String getLang() {
        return lang;
    }
    
    public void setLang(String lang) {
        this.lang = lang;
    }
    
    public Long getViewCount() {
        return viewCount;
    }
    
    public void setViewCount(Long viewCount) {
        this.viewCount = viewCount;
    }
    
    public List<BlockDto> getBlocks() {
        return blocks;
    }
    
    public void setBlocks(List<BlockDto> blocks) {
        this.blocks = blocks;
    }
    
    public String getContentText() {
        return contentText;
    }
    
    public void setContentText(String contentText) {
        this.contentText = contentText;
    }
    
    public Boolean getIsOwner() {
        return isOwner;
    }
    
    public void setIsOwner(Boolean isOwner) {
        this.isOwner = isOwner;
    }
}
