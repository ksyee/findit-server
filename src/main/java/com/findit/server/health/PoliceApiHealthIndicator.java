package com.findit.server.health;

import com.findit.server.infrastructure.police.client.PoliceApiClient;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;
import com.findit.server.infrastructure.police.dto.PoliceApiLostItemResponse;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

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
            // 경찰청 API는 날짜 파라미터가 없으면 502(backend forwarding error)를 반환하므로
            // 가장 최근 1일 범위로 최소 파라미터를 채워서 상태를 확인한다.
            LocalDate now = LocalDate.now();
            String startYmd = now.minusDays(1).format(DateTimeFormatter.BASIC_ISO_DATE);
            String endYmd = now.format(DateTimeFormatter.BASIC_ISO_DATE);

            PoliceApiLostItemResponse response = apiClient.fetchLostItems(1, 1, startYmd, endYmd);
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
