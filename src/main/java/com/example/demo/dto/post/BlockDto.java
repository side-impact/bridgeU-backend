package com.example.demo.dto.post;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 게시물 블록 DTO
 * 게시물의 개별 블록 정보를 담는 DTO
 */
public class BlockDto {
    
    private Integer idx;
    
    private String type;
    
    private String text;
    
    @JsonProperty("image_url")
    private String imageUrl;
    
    // 기본 생성자
    public BlockDto() {}
    
    // 전체 생성자
    public BlockDto(Integer idx, String type, String text, String imageUrl) {
        this.idx = idx;
        this.type = type;
        this.text = text;
        this.imageUrl = imageUrl;
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
    
    public String getImageUrl() {
        return imageUrl;
    }
    
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
