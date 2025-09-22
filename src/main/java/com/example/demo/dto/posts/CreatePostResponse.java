package com.example.demo.dto.posts;

/**
 * 게시물 생성 응답 DTO
 */
public class CreatePostResponse {
    
    private Long post_id;
    
    // 기본 생성자
    public CreatePostResponse() {}
    
    // 생성자
    public CreatePostResponse(Long post_id) {
        this.post_id = post_id;
    }
    
    // Getter and Setter
    public Long getPost_id() {
        return post_id;
    }
    
    public void setPost_id(Long post_id) {
        this.post_id = post_id;
    }
}
