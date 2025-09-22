package com.example.demo.dto.hot;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * 핫 게시물 응답 DTO
 * GET /api/posts/hot API의 개별 게시물 데이터 구조
 */
public class HotPostDto {
    
    @JsonProperty("post_id")
    private Long postId;
    
    private List<Integer> tags;
    
    private String title;
    
    @JsonProperty("view_count")
    private Long viewCount;
    
    // 기본 생성자
    public HotPostDto() {}
    
    // 전체 생성자
    public HotPostDto(Long postId, List<Integer> tags, String title, Long viewCount) {
        this.postId = postId;
        this.tags = tags;
        this.title = title;
        this.viewCount = viewCount;
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
    
    public Long getViewCount() {
        return viewCount;
    }
    
    public void setViewCount(Long viewCount) {
        this.viewCount = viewCount;
    }
}
