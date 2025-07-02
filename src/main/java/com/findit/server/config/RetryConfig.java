package com.findit.server.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.retry.annotation.EnableRetry;

/**
 * Spring Retry 기능을 활성화하는 설정 클래스
 */
@Configuration
@EnableRetry
public class RetryConfig {
    // 추가 설정이 필요한 경우 여기에 추가
}
