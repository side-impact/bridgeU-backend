package com.example.demo.controller;

import com.example.demo.dto.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/test")
public class DatabaseTestController {

    @Autowired
    private DataSource dataSource;

    @GetMapping("/db-connection")
    public ResponseEntity<ApiResponse<Map<String, Object>>> testDatabaseConnection() {
        Map<String, Object> result = new HashMap<>();
        
        try (Connection connection = dataSource.getConnection()) {
            result.put("connected", true);
            result.put("url", connection.getMetaData().getURL());
            result.put("user", connection.getMetaData().getUserName());
            result.put("database", connection.getCatalog());
            
            // 테이블 존재 확인
            try (Statement stmt = connection.createStatement()) {
                ResultSet rs = stmt.executeQuery("SELECT COUNT(*) as table_count FROM information_schema.tables WHERE table_schema = 'public'");
                if (rs.next()) {
                    result.put("public_tables_count", rs.getInt("table_count"));
                }
            }
            
            // 태그 데이터 확인
            try (Statement stmt = connection.createStatement()) {
                ResultSet rs = stmt.executeQuery("SELECT COUNT(*) as tag_count FROM tags");
                if (rs.next()) {
                    result.put("tags_count", rs.getInt("tag_count"));
                }
            }
            
            return ResponseEntity.ok(ApiResponse.success("데이터베이스 연결 성공", result));
            
        } catch (Exception e) {
            result.put("connected", false);
            result.put("error", e.getMessage());
            return ResponseEntity.status(500).body(ApiResponse.error(500, "데이터베이스 연결 실패: " + e.getMessage()));
        }
    }
}
