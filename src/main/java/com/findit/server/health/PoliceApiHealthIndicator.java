package com.findit.server.health;

import com.findit.server.service.external.PoliceApiClient;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;
import com.findit.server.dto.external.PoliceApiLostItemResponse;

/**
 * 경찰청 API 헬스 체크 인디케이터
 * 외부 경찰청 API의 상태를 검사하여 헬스 정보 제공
 */
@Component
public class PoliceApiHealthIndicator implements HealthIndicator {
    private final PoliceApiClient apiClient;

    /**
     * 생성자
     * 
     * @param apiClient 경찰청 API 클라이언트
     */
    public PoliceApiHealthIndicator(PoliceApiClient apiClient) {
        this.apiClient = apiClient;
    }

    /**
     * 경찰청 API의 헬스 상태 검사
     * 
     * @return 헬스 상태 정보
     */
    @Override
    public Health health() {
        if (!apiClient.isEnabled()) {
            return Health.unknown()
                    .withDetail("service", "Police API")
                    .withDetail("status", "Disabled")
                    .withDetail("message", "Police API calls are turned off")
                    .build();
        }
        try {
            // 간단한 API 호출로 서비스 상태 확인 (첫 페이지, 1개 항목, 날짜/카테고리 필터 없음)
            PoliceApiLostItemResponse response = apiClient.fetchLostItems(1, 1, null, null);
            if (response != null && response.getHeader() != null && "00".equals(response.getHeader().getResultCode())) {
                return Health.up()
                        .withDetail("service", "Police API")
                        .withDetail("status", "Available")
                        .withDetail("message", "Police API is accessible")
                        .build();
            } else {
                return Health.down()
                        .withDetail("service", "Police API")
                        .withDetail("status", "Unavailable")
                        .build();
            }
        } catch (Exception e) {
            return Health.down()
                    .withDetail("service", "Police API")
                    .withDetail("status", "Unavailable")
                    .withDetail("error", e.getMessage())
                    .build();
        }
    }
}
