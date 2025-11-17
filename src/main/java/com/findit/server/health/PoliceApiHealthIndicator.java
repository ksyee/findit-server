package com.findit.server.health;

import com.findit.server.infrastructure.police.client.PoliceApiClient;
import com.findit.server.infrastructure.police.dto.PoliceApiLostItemResponse;
import jakarta.annotation.PostConstruct;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 경찰청 API 헬스 체크 인디케이터
 * 외부 경찰청 API의 상태를 검사하여 헬스 정보 제공
 *
 * <p>외부 API 호출은 주기적으로 백그라운드에서 수행하고, Health 엔드포인트는 캐시된 결과를 즉시 반환한다.</p>
 */
@Component
public class PoliceApiHealthIndicator implements HealthIndicator {

    private final PoliceApiClient apiClient;
    private final AtomicReference<Health> cachedHealth = new AtomicReference<>();
    private final Duration staleAfter;
    private volatile Instant lastCheckedAt = Instant.EPOCH;

    public PoliceApiHealthIndicator(
        PoliceApiClient apiClient,
        @Value("${police.api.health-check.stale-after-ms:900000}") long staleAfterMillis) {
        this.apiClient = apiClient;
        this.staleAfter = Duration.ofMillis(staleAfterMillis);
        this.cachedHealth.set(buildPendingHealth());
    }

    @PostConstruct
    public void initialize() {
        refreshHealth();
    }

    /**
     * 외부 API 헬스 상태는 스케줄링으로 주기적으로 갱신한다.
     */
    @Scheduled(
        fixedDelayString = "${police.api.health-check.interval-ms:300000}",
        initialDelayString = "${police.api.health-check.initial-delay-ms:0}")
    public void refreshHealth() {
        Instant checkTime = Instant.now();
        this.cachedHealth.set(checkPoliceApi(checkTime));
        this.lastCheckedAt = checkTime;
    }

    /**
     * 경찰청 API의 헬스 상태 응답
     *
     * @return 헬스 상태 정보
     */
    @Override
    public Health health() {
        Health latest = cachedHealth.get();
        if (!apiClient.isEnabled()) {
            return latest;
        }
        if (lastCheckedAt.equals(Instant.EPOCH) || latest == null) {
            return buildPendingHealth();
        }

        Duration elapsed = Duration.between(lastCheckedAt, Instant.now());
        if (elapsed.compareTo(staleAfter) > 0) {
            Map<String, Object> details = new LinkedHashMap<>(latest.getDetails());
            details.put("status", "Stale");
            details.put("message", "Police API health data is stale");
            details.put("staleForMillis", elapsed.toMillis());
            return Health.up().withDetails(details).build();
        }
        return latest;
    }

    private Health checkPoliceApi(Instant checkTime) {
        if (!apiClient.isEnabled()) {
            return buildDisabledHealth(checkTime);
        }
        try {
            LocalDate now = LocalDate.now();
            String startYmd = now.minusDays(1).format(DateTimeFormatter.BASIC_ISO_DATE);
            String endYmd = now.format(DateTimeFormatter.BASIC_ISO_DATE);

            PoliceApiLostItemResponse response = apiClient.fetchLostItems(1, 1, startYmd, endYmd);
            if (response != null && response.getHeader() != null
                && "00".equals(response.getHeader().getResultCode())) {
                return baseBuilder(checkTime)
                    .withDetail("status", "Available")
                    .withDetail("message", "Police API is accessible")
                    .build();
            }
            return baseBuilder(checkTime)
                .withDetail("status", "Unavailable")
                .withDetail("message", "Police API responded with unexpected status")
                .build();
        } catch (Exception e) {
            return baseBuilder(checkTime)
                .withDetail("status", "Unavailable")
                .withDetail("message", "Police API request failed")
                .withDetail("error", e.getMessage())
                .build();
        }
    }

    private Health buildPendingHealth() {
        return Health.up()
            .withDetail("service", "Police API")
            .withDetail("status", "Pending")
            .withDetail("message", "Police API health check has not run yet")
            .build();
    }

    private Health buildDisabledHealth(Instant checkTime) {
        return baseBuilder(checkTime)
            .withDetail("status", "Disabled")
            .withDetail("message", "Police API calls are turned off")
            .build();
    }

    private Health.Builder baseBuilder(Instant checkTime) {
        return Health.up()
            .withDetail("service", "Police API")
            .withDetail("checkedAt", checkTime.toString());
    }
}
