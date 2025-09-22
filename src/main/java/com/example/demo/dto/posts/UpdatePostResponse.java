package com.example.demo.dto.posts;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 게시물 수정 응답 DTO
 */
public class UpdatePostResponse {
    
    @JsonProperty("post_id")
    private Long postId;
    
    private String message;
    
    public UpdatePostResponse() {}
    
    public UpdatePostResponse(Long postId, String message) {
        this.postId = postId;
        this.message = message;
    }
    
    public UpdatePostResponse(Long postId) {
        this.postId = postId;
        this.message = "Post updated successfully";
    }
    
    // Getters and Setters
    public Long getPostId() {
        return postId;
    }
    
    public void setPostId(Long postId) {
        this.postId = postId;
    }
    
    public String getMessage() {
        return message;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
}
