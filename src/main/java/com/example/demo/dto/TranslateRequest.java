package com.example.demo.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class TranslateRequest {
    
    @NotNull(message = "sourceText는 필수입니다")
    @Size(min = 1, max = 500, message = "sourceText는 1자 이상 500자 이하여야 합니다")
    private String sourceText;
    
    @NotNull(message = "sourceLang은 필수입니다")
    @Pattern(regexp = "auto|en|ja|zh\\-CN|ko", message = "sourceLang은 지원되는 언어 코드여야 합니다 (auto, en, ja, zh-CN, ko)")
    private String sourceLang;
    
    @NotNull(message = "targetLang은 필수입니다")
    @Pattern(regexp = "en|ja|zh\\-CN|ko", message = "targetLang은 지원되는 언어 코드여야 합니다 (en, ja, zh-CN, ko)")
    private String targetLang;
    
    public TranslateRequest() {}
    
    public TranslateRequest(String sourceText, String sourceLang, String targetLang) {
        this.sourceText = sourceText;
        this.sourceLang = sourceLang;
        this.targetLang = targetLang;
    }
    
    // Getter/Setter
    public String getSourceText() {
        return sourceText;
    }
    
    public void setSourceText(String sourceText) {
        this.sourceText = sourceText;
    }
    
    public String getSourceLang() {
        return sourceLang;
    }
    
    public void setSourceLang(String sourceLang) {
        this.sourceLang = sourceLang;
    }
    
    public String getTargetLang() {
        return targetLang;
    }
    
    public void setTargetLang(String targetLang) {
        this.targetLang = targetLang;
    }
}
