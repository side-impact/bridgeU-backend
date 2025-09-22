package com.example.demo.config;

import com.example.demo.dto.ApiResponse;
import com.example.demo.service.TranslateService;
import com.example.demo.service.PostService;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.MissingRequestHeaderException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Object>> handleValidationError(MethodArgumentNotValidException e) {
        String fieldError = e.getBindingResult().getFieldErrors().get(0).getDefaultMessage();
        
        //표준 코드명
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
                .body(ApiResponse.badRequest(message));
    }

    @ExceptionHandler(TranslateService.AppError.class)
    public ResponseEntity<ApiResponse<Object>> handleAppError(TranslateService.AppError e) {
        return ResponseEntity.status(e.getCode())
                .body(ApiResponse.error(e.getCode(), e.getMessage()));
    }

    /**
     * 필수 헤더 누락 처리 (X-Temp-User-Id)
     */
    @ExceptionHandler(MissingRequestHeaderException.class)
    public ResponseEntity<ApiResponse<Object>> handleMissingHeader(MissingRequestHeaderException e) {
        if ("X-Temp-User-Id".equals(e.getHeaderName())) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.badRequest("X-Temp-User-Id header is required"));
        }
        return ResponseEntity.badRequest()
                .body(ApiResponse.badRequest("Required header missing: " + e.getHeaderName()));
    }

    /**
     * 유효하지 않은 사용자 처리
     */
    @ExceptionHandler(PostService.InvalidUserException.class)
    public ResponseEntity<ApiResponse<Object>> handleInvalidUser(PostService.InvalidUserException e) {
        return ResponseEntity.badRequest()
                .body(ApiResponse.badRequest("Invalid user"));
    }

    /**
     * 데이터베이스 제약 조건 위반 처리 (409 Conflict)
     */
    @ExceptionHandler(PostService.DatabaseConstraintException.class)
    public ResponseEntity<ApiResponse<Object>> handleDatabaseConstraint(PostService.DatabaseConstraintException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(ApiResponse.error(409, e.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Object>> handleGenericError(Exception e) {
        return ResponseEntity.status(500)
                .body(ApiResponse.internalError("INTERNAL_SERVER_ERROR"));
    }
}
