package com.example.demo.config;

import com.example.demo.controller.dto.ErrorResponse;
import com.example.demo.service.TranslateService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationError(MethodArgumentNotValidException e) {
        String fieldError = e.getBindingResult().getFieldErrors().get(0).getDefaultMessage();
        
        //표준 코드명명
        String message;
        if (fieldError.contains("필수")) {
            message = "VALIDATION_EMPTY";
        } else if (fieldError.contains("500자")) {
            message = "VALIDATION_TOO_LONG";
        } else if (fieldError.contains("en, ja, zh, ko")) {
            message = "VALIDATION_UNSUPPORTED_LANG";
        } else {
            message = "VALIDATION_EMPTY";
        }
        
        return ResponseEntity.badRequest()
                .body(new ErrorResponse(400, message));
    }

    @ExceptionHandler(TranslateService.AppError.class)
    public ResponseEntity<ErrorResponse> handleAppError(TranslateService.AppError e) {
        return ResponseEntity.status(e.getCode())
                .body(new ErrorResponse(e.getCode(), e.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericError(Exception e) {
        return ResponseEntity.status(500)
                .body(new ErrorResponse(500, "INTERNAL"));
    }
}
