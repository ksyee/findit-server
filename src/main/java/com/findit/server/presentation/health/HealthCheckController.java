package com.findit.server.presentation.health;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/health")
public class HealthCheckController {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public HealthCheckController(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> healthCheck() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "UP");
        response.put("service", "FindIt Server");
        response.put("timestamp", System.currentTimeMillis());
        
        try {
            // 데이터베이스 연결 확인
            Integer result = jdbcTemplate.queryForObject("SELECT 1", Integer.class);
            response.put("database", "Connected");
            response.put("database_check", result);
        } catch (Exception e) {
            response.put("database", "Disconnected");
            response.put("error", e.getMessage());
        }
        
        return ResponseEntity.ok(response);
    }
}
