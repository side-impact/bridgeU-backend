package com.example.demo.dto.posts;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import java.util.List;


public class UpdatePostRequest {
    
    @NotBlank(message = "언어 코드는 필수입니다")
    @Pattern(regexp = "^(en|ja|zh-CN|ko)$", message = "언어 코드는 en, ja, zh-CN, ko 중 하나여야 합니다")
    private String lang;
    
    @NotBlank(message = "제목은 필수입니다")
    @Size(min = 1, max = 60, message = "제목은 1자 이상 60자 이하여야 합니다")
    private String title;
    
    @NotNull(message = "태그는 필수입니다")
    private List<Integer> tags;
    
    @NotNull(message = "블록은 필수입니다")
    @Valid
    private List<BlockRequest> blocks;
    
    public UpdatePostRequest() {}
    
    public UpdatePostRequest(String lang, String title, List<Integer> tags, List<BlockRequest> blocks) {
        this.lang = lang;
        this.title = title;
        this.tags = tags;
        this.blocks = blocks;
    }
    
    // Getters and Setters
    public String getLang() {
        return lang;
    }
    
    public void setLang(String lang) {
        this.lang = lang;
    }
    
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public List<Integer> getTags() {
        return tags;
    }
    
    public void setTags(List<Integer> tags) {
        this.tags = tags;
    }
    
    public List<BlockRequest> getBlocks() {
        return blocks;
    }
    
    public void setBlocks(List<BlockRequest> blocks) {
        this.blocks = blocks;
    }
}
