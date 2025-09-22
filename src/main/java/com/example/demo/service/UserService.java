package com.example.demo.service;

import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // 회원가입
    public User register(String email, String password, String name) {
        logger.info("=== UserService.register() 시작 ===");
        logger.info("회원가입 요청 - email: {}, name: {}", email, name);
        
        try {
            // 입력값 검증 로그
            if (email == null || email.trim().isEmpty()) {
                logger.error("회원가입 실패: 이메일이 null 또는 빈 문자열입니다.");
                throw new IllegalArgumentException("이메일은 필수입니다.");
            }
            if (password == null || password.trim().isEmpty()) {
                logger.error("회원가입 실패: 비밀번호가 null 또는 빈 문자열입니다.");
                throw new IllegalArgumentException("비밀번호는 필수입니다.");
            }
            if (name == null || name.trim().isEmpty()) {
                logger.error("회원가입 실패: 이름이 null 또는 빈 문자열입니다.");
                throw new IllegalArgumentException("이름은 필수입니다.");
            }
            
            logger.info("입력값 검증 완료");
            
            // 비밀번호 암호화
            logger.info("비밀번호 암호화 시작");
            String encodedPassword = passwordEncoder.encode(password);
            logger.info("비밀번호 암호화 완료");
            
            // User 객체 생성
            logger.info("User 객체 생성 시작");
            User user = User.builder()
                    .email(email.trim())
                    .password(encodedPassword)
                    .name(name.trim())
                    .build();
            logger.info("User 객체 생성 완료 - email: {}, name: {}", user.getEmail(), user.getName());
            
            // 데이터베이스에 저장
            logger.info("데이터베이스 저장 시작");
            User savedUser = userRepository.save(user);
            logger.info("데이터베이스 저장 완료 - userId: {}, email: {}", savedUser.getId(), savedUser.getEmail());
            
            logger.info("=== UserService.register() 성공적으로 완료 ===");
            return savedUser;
            
        } catch (Exception e) {
            logger.error("=== UserService.register() 실패 ===");
            logger.error("회원가입 중 오류 발생 - email: {}, name: {}", email, name);
            logger.error("오류 메시지: {}", e.getMessage());
            logger.error("오류 스택 트레이스: ", e);
            throw e;
        }
    }

    // 이메일 중복 체크
    public boolean existsByEmail(String email) {
        logger.info("이메일 중복 체크 시작 - email: {}", email);
        try {
            boolean exists = userRepository.existsByEmail(email);
            logger.info("이메일 중복 체크 완료 - email: {}, 중복여부: {}", email, exists);
            return exists;
        } catch (Exception e) {
            logger.error("이메일 중복 체크 중 오류 발생 - email: {}, 오류: {}", email, e.getMessage());
            logger.error("오류 스택 트레이스: ", e);
            throw e;
        }
    }

    // 로그인 인증
    public User authenticate(String email, String password) {
        return userRepository.findByEmail(email)
                .filter(u -> passwordEncoder.matches(password, u.getPassword()))
                .orElse(null);
    }
    
    //이메일->유저 찾기기
    public User findUserfromDBbyEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }
}
