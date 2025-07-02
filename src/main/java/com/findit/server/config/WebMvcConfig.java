package com.findit.server.config;

import com.findit.server.interceptor.RequestCounterInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Spring MVC 구성 클래스
 * 인터셉터 및 기타 MVC 관련 설정 구성
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    private final RequestCounterInterceptor requestCounterInterceptor;

    /**
     * 생성자
     * 
     * @param requestCounterInterceptor API 요청 카운터 인터셉터
     */
    public WebMvcConfig(RequestCounterInterceptor requestCounterInterceptor) {
        this.requestCounterInterceptor = requestCounterInterceptor;
    }

    /**
     * 인터셉터 등록
     * 
     * @param registry 인터셉터 레지스트리
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(requestCounterInterceptor)
                .addPathPatterns("/api/**"); // API 경로에만 적용
    }
}
