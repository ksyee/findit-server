package com.findit.server.config;

import com.findit.server.infrastructure.police.client.PoliceApiClient;
import io.micrometer.core.instrument.MeterRegistry;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@ActiveProfiles("test")
public class DatabaseConfigTest {

    private static final Logger logger = LoggerFactory.getLogger(DatabaseConfigTest.class);

    @TestConfiguration
    static class TestMockConfig {
        @Bean
        public PoliceApiClient policeApiClient() {
            return Mockito.mock(PoliceApiClient.class);
        }

    }

    @Autowired
    private DataSource dataSource;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private PoliceApiClient policeApiClient;

    @Autowired
    private MeterRegistry meterRegistry;

    @Test
    public void testDatabaseConnection() {
        logger.info("Starting testDatabaseConnection...");
        assertNotNull(dataSource, "DataSource should not be null");
        try (Connection connection = dataSource.getConnection()) {
            String dbUrl = connection.getMetaData().getURL();
            logger.info("DataSource URL: {}", dbUrl);
            assertEquals("jdbc:h2:mem:testdb", dbUrl, "DataSource URL should be for H2 in-memory DB.");
        } catch (SQLException e) {
            logger.error("SQLException while getting DataSource metadata", e);
            throw new RuntimeException(e);
        }
        logger.info("DataSource is not null: {}", dataSource.getClass().getName());
        assertNotNull(jdbcTemplate, "JdbcTemplate should not be null");
        logger.info("JdbcTemplate is not null: {}", jdbcTemplate.getClass().getName());
        
        assertNotNull(meterRegistry, "MeterRegistry should not be null in test context");
        logger.info("MeterRegistry is not null: {}", meterRegistry.getClass().getName());

        Integer result = null;
        String testQuery = "SELECT 1";
        Integer expectedResult = 1;
        try {
            logger.info("Executing '{}' query...", testQuery);
            result = jdbcTemplate.queryForObject(testQuery, Integer.class);
            logger.info("'{}' query returned: {} (Type: {})", testQuery, result, (result != null ? result.getClass().getName() : "null"));
        } catch (DataAccessException e) {
            logger.error("DataAccessException during '{}' query", testQuery, e);
            throw e; 
        }
        
        logger.info("Asserting: expected <{}>, actual <{}>", expectedResult, result);
        assertEquals(expectedResult, result, "Database connection test failed. Expected " + expectedResult + ", got " + result);
        logger.info("testDatabaseConnection completed successfully.");
    }
}
