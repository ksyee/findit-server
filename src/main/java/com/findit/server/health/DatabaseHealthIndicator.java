package com.findit.server.health;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

/**
 * 데이터베이스 헬스 체크 인디케이터
 * 데이터베이스 연결 및 상태를 검사하여 헬스 정보 제공
 */
@Component
public class DatabaseHealthIndicator implements HealthIndicator {

    private final JdbcTemplate jdbcTemplate;

    /**
     * 생성자
     * 
     * @param jdbcTemplate JDBC 템플릿
     */
    public DatabaseHealthIndicator(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * 데이터베이스 헬스 상태 검사
     * 
     * @return 헬스 상태 정보
     */
    @Override
    public Health health() {
        try {
            // 데이터베이스 상태 검사를 위한 간단한 쿼리 실행
            int result = jdbcTemplate.queryForObject("SELECT 1", Integer.class);
            
            if (result == 1) {
                return Health.up()
                        .withDetail("service", "Database")
                        .withDetail("status", "Available")
                        .build();
            } else {
                return Health.down()
                        .withDetail("service", "Database")
                        .withDetail("status", "Unavailable")
                        .withDetail("error", "Database query returned unexpected result")
                        .build();
            }
        } catch (Exception e) {
            return Health.down()
                    .withDetail("service", "Database")
                    .withDetail("status", "Unavailable")
                    .withDetail("error", e.getMessage())
                    .build();
        }
    }
}
