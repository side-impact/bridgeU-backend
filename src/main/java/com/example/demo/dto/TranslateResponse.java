package com.example.demo.dto;

public class TranslateResponse {
    
    private String translatedText;
    
    public TranslateResponse() {}
    
    public TranslateResponse(String translatedText) {
        this.translatedText = translatedText;
    }
    
    // Getter/Setter
    public String getTranslatedText() {
        return translatedText;
    }
    
    public void setTranslatedText(String translatedText) {
        this.translatedText = translatedText;
    }
}
