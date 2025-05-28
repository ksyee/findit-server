package com.findit.server.config;

import io.micrometer.core.aop.TimedAspect;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.boot.actuate.autoconfigure.metrics.MeterRegistryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 메트릭 설정 클래스
 * 애플리케이션 메트릭 수집 및 모니터링을 위한 설정
 */
@Configuration
public class MetricsConfig {

    /**
     * 메트릭 레지스트리 커스터마이저 빈
     * 모든 메트릭에 공통 태그를 추가
     * 
     * @return MeterRegistryCustomizer 인스턴스
     */
    @Bean
    public MeterRegistryCustomizer<MeterRegistry> metricsCommonTags() {
        return registry -> registry.config().commonTags("application", "lost-found-api");
    }

    /**
     * @Timed 어노테이션을 사용하기 위한 TimedAspect 빈
     * 
     * @param registry 메트릭 레지스트리
     * @return TimedAspect 인스턴스
     */
    @Bean
    public TimedAspect timedAspect(MeterRegistry registry) {
        return new TimedAspect(registry);
    }
}
