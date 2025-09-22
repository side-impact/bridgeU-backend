package com.example.demo.controller;

import com.example.demo.dto.ApiResponse;
import com.example.demo.dto.TranslateRequest;
import com.example.demo.dto.TranslateResponse;
import com.example.demo.service.TranslateService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class TranslateController {

    private final TranslateService translateService;

    public TranslateController(TranslateService translateService) {
        this.translateService = translateService;
    }

    /**
     * 텍스트 번역 API
     * 
     * @param request 번역 요청 (sourceText, sourceLang, targetLang)
     * @return 번역 결과 (translatedText)
     */
    @GetMapping("/test")
    public ResponseEntity<ApiResponse<String>> test() {
        return ResponseEntity.ok(ApiResponse.success("Hello World!"));
    }
    
    @PostMapping("/translate")
    public ResponseEntity<ApiResponse<TranslateResponse>> translate(@Valid @RequestBody TranslateRequest request) {
        
        String translatedText = translateService.translate(
            request.getSourceText(),
            request.getSourceLang(), 
            request.getTargetLang()
        );
        
        TranslateResponse response = new TranslateResponse(translatedText);
        return ResponseEntity.ok(ApiResponse.success("번역이 완료되었습니다.", response));
    }
}
