package com.findit.server.interceptor;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * API 요청 카운터 인터셉터
 * API 요청을 카운트하여 메트릭으로 수집
 */
@Component
public class RequestCounterInterceptor implements HandlerInterceptor {

    private final MeterRegistry meterRegistry;

    /**
     * 생성자
     * 
     * @param meterRegistry 메트릭 레지스트리
     */
    public RequestCounterInterceptor(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
    }

    /**
     * API 요청 전 처리
     * 요청 URI에 따라 카운터 증가
     * 
     * @param request 요청 객체
     * @param response 응답 객체
     * @param handler 핸들러 객체
     * @return 인터셉터 처리 결과
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String uri = request.getRequestURI();
        String method = request.getMethod();
        
        // API 요청 카운터 증가
        Counter.builder("api.requests.total")
                .tag("uri", uri)
                .tag("method", method)
                .description("Total number of API requests")
                .register(meterRegistry)
                .increment();
        
        return true;
    }
}
