package com.example.demo.dto.posts;

import jakarta.validation.constraints.*;

/**
 * 게시물 블록 요청 DTO
 */
public class BlockRequest {
    
    @NotNull(message = "블록 인덱스는 필수입니다")
    @Min(value = 0, message = "블록 인덱스는 0 이상이어야 합니다")
    private Integer idx;
    
    @NotBlank(message = "블록 타입은 필수입니다")
    @Pattern(regexp = "^(paragraph|image)$", message = "블록 타입은 paragraph 또는 image여야 합니다")
    private String type;
    
    private String text;
    
    private String image_url;
    
    // 기본 생성자
    public BlockRequest() {}
    
    // 전체 생성자
    public BlockRequest(Integer idx, String type, String text, String image_url) {
        this.idx = idx;
        this.type = type;
        this.text = text;
        this.image_url = image_url;
    }
    
    // Getters and Setters
    public Integer getIdx() {
        return idx;
    }
    
    public void setIdx(Integer idx) {
        this.idx = idx;
    }
    
    public String getType() {
        return type;
    }
    
    public void setType(String type) {
        this.type = type;
    }
    
    public String getText() {
        return text;
    }
    
    public void setText(String text) {
        this.text = text;
    }
    
    public String getImage_url() {
        return image_url;
    }
    
    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }
}
