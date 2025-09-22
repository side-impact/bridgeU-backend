package com.example.demo.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * 표준 API 응답 형식
 * 
 * @param <T> 응답 데이터 타입
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {
    
    private int code;
    private String message;
    private T data;
    
    // 기본 생성자
    public ApiResponse() {}
    
    // 성공 응답용 생성자
    public ApiResponse(T data) {
        this.code = 200;
        this.message = "SUCCESS";
        this.data = data;
    }
    
    // 모든 필드 생성자
    public ApiResponse(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }
    
    // 에러 응답용 생성자 (data 없이)
    public ApiResponse(int code, String message) {
        this.code = code;
        this.message = message;
        this.data = null;
    }
    
    // 정적 팩토리 메서드들
    
    /**
     * 성공 응답 생성
     */
    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(data);
    }
    
    /**
     * 성공 응답 생성 (데이터 없이)
     */
    public static <T> ApiResponse<T> success() {
        return new ApiResponse<>(200, "SUCCESS", null);
    }
    
    /**
     * 성공 응답 생성 (커스텀 메시지)
     */
    public static <T> ApiResponse<T> success(String message, T data) {
        return new ApiResponse<>(200, message, data);
    }
    
    /**
     * 에러 응답 생성
     */
    public static <T> ApiResponse<T> error(int code, String message) {
        return new ApiResponse<>(code, message, null);
    }
    
    /**
     * 400 Bad Request 응답
     */
    public static <T> ApiResponse<T> badRequest(String message) {
        return new ApiResponse<>(400, message, null);
    }
    
    /**
     * 404 Not Found 응답
     */
    public static <T> ApiResponse<T> notFound(String message) {
        return new ApiResponse<>(404, message, null);
    }
    
    /**
     * 500 Internal Server Error 응답
     */
    public static <T> ApiResponse<T> internalError(String message) {
        return new ApiResponse<>(500, message, null);
    }
    
    // Getters and Setters
    public int getCode() {
        return code;
    }
    
    public void setCode(int code) {
        this.code = code;
    }
    
    public String getMessage() {
        return message;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
    
    public T getData() {
        return data;
    }
    
    public void setData(T data) {
        this.data = data;
    }
}
