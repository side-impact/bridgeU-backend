package com.example.demo.controller;

import com.example.demo.config.JwtUtil;
import com.example.demo.model.User;
import com.example.demo.service.UserService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    private final UserService userService;
    private final JwtUtil jwtUtil;

    public AuthController(UserService userService, JwtUtil jwtUtil) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest request) {
        // UserService authenticate 반환값 확인
        User user = userService.authenticate(request.getEmail(), request.getPassword());
        if (user == null) {
            return ResponseEntity.status(401).body(new AuthResponse("Invalid credentials"));
        }

        String token = jwtUtil.generateToken(user.getEmail());
        return ResponseEntity.ok(new AuthResponse(token));
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest request) {
        logger.info("===============================================");
        logger.info("=== /auth/register 엔드포인트 호출됨 ===");
        logger.info("===============================================");
        
        try {
            // 요청 데이터 로그
            logger.info("회원가입 요청 데이터:");
            logger.info("  - email: {}", request.getEmail());
            logger.info("  - name: {}", request.getName());
            logger.info("  - password length: {}", request.getPassword() != null ? request.getPassword().length() : "null");
            
            // 입력값 검증
            if (request.getEmail() == null || request.getEmail().trim().isEmpty()) {
                logger.error("회원가입 실패: 이메일이 비어있음");
                return ResponseEntity.badRequest().body(new AuthResponse("이메일은 필수입니다"));
            }
            if (request.getPassword() == null || request.getPassword().trim().isEmpty()) {
                logger.error("회원가입 실패: 비밀번호가 비어있음");
                return ResponseEntity.badRequest().body(new AuthResponse("비밀번호는 필수입니다"));
            }
            if (request.getName() == null || request.getName().trim().isEmpty()) {
                logger.error("회원가입 실패: 이름이 비어있음");
                return ResponseEntity.badRequest().body(new AuthResponse("이름은 필수입니다"));
            }
            
            logger.info("입력값 검증 통과");

            // 이메일 중복 체크
            logger.info("이메일 중복 체크 시작...");
            if (userService.existsByEmail(request.getEmail())) {
                logger.warn("회원가입 실패: 이메일이 이미 존재함 - {}", request.getEmail());
                return ResponseEntity.badRequest().body(new AuthResponse("이미 사용 중인 이메일입니다"));
            }
            logger.info("이메일 중복 체크 통과");

            // 사용자 등록
            logger.info("사용자 등록 프로세스 시작...");
            User user = userService.register(request.getEmail(), request.getPassword(), request.getName());
            logger.info("사용자 등록 완료 - userId: {}, email: {}", user.getId(), user.getEmail());

            // JWT 토큰 생성
            logger.info("JWT 토큰 생성 시작...");
            String token = jwtUtil.generateToken(user.getEmail());
            logger.info("JWT 토큰 생성 완료");

            logger.info("===============================================");
            logger.info("=== 회원가입 성공 - email: {} ===", user.getEmail());
            logger.info("===============================================");
            
            return ResponseEntity.ok(new AuthResponse(token));
            
        } catch (Exception e) {
            logger.error("===============================================");
            logger.error("=== 회원가입 중 예외 발생 ===");
            logger.error("===============================================");
            logger.error("요청 이메일: {}", request.getEmail());
            logger.error("오류 메시지: {}", e.getMessage());
            logger.error("오류 클래스: {}", e.getClass().getSimpleName());
            logger.error("전체 스택 트레이스: ", e);
            
            return ResponseEntity.status(500).body(new AuthResponse("회원가입 중 오류가 발생했습니다: " + e.getMessage()));
        }
    }

    // DTO
    @Data
    @NoArgsConstructor
    static class AuthRequest {
        private String email;
        private String password;
    }

    @Data
    @NoArgsConstructor
    static class RegisterRequest {
        private String email;
        private String password;
        private String name;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    static class AuthResponse {
        private String token;
    }
}
