package com.findit.server.health;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

/**
 * ub370uc774ud130ubca0uc774uc2a4 ud5ecuc2a4 uccb4ud06c uc778ub514ucf00uc774ud130
 * ub370uc774ud130ubca0uc774uc2a4 uc5f0uacb0 ubc0f uc0c1ud0dcub97c uac80uc0acud558uc5ec ud5ecuc2a4 uc815ubcf4 uc81cuacf5
 */
@Component
public class DatabaseHealthIndicator implements HealthIndicator {

    private final JdbcTemplate jdbcTemplate;

    /**
     * uc0dduc131uc790
     * 
     * @param jdbcTemplate JDBC ud15cud50cub9bf
     */
    public DatabaseHealthIndicator(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * ub370uc774ud130ubca0uc774uc2a4 ud5ecuc2a4 uc0c1ud0dc uac80uc0ac
     * 
     * @return ud5ecuc2a4 uc0c1ud0dc uc815ubcf4
     */
    @Override
    public Health health() {
        try {
            // ub370uc774ud130ubca0uc774uc2a4 uc0c1ud0dc uac80uc0acub97c uc704ud55c uac04ub2e8ud55c ucffcub9ac uc2e4ud589
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
