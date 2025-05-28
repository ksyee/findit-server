package com.findit.server.config;

import com.findit.server.interceptor.RequestCounterInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Spring MVC uad6cuc131 ud074ub798uc2a4
 * uc778ud130uc149ud130 ubc0f uae30ud0c0 MVC uad00ub828 uc124uc815 uad6cuc131
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    private final RequestCounterInterceptor requestCounterInterceptor;

    /**
     * uc0dduc131uc790
     * 
     * @param requestCounterInterceptor API uc694uccad uce74uc6b4ud130 uc778ud130uc149ud130
     */
    public WebMvcConfig(RequestCounterInterceptor requestCounterInterceptor) {
        this.requestCounterInterceptor = requestCounterInterceptor;
    }

    /**
     * uc778ud130uc149ud130 ub4f1ub85d
     * 
     * @param registry uc778ud130uc149ud130 ub808uc9c0uc2a4ud2b8ub9ac
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(requestCounterInterceptor)
                .addPathPatterns("/api/**"); // API uacbdub85cuc5d0ub9cc uc801uc6a9
    }
}
