package com.findit.server.config;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.xml.Jaxb2RootElementHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

/**
 * API 클라이언트 설정 클래스
 * 외부 API 통신을 위한 RestTemplate 설정
 */
@Configuration
public class ApiClientConfig {
    
    /**
     * RestTemplate 빈 생성
     * 연결 타임아웃, 읽기 타임아웃 설정 및 JAXB2 메시지 컨버터 추가
     * 
     * @return 설정된 RestTemplate 인스턴스
     */
    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) { 
        RestTemplate restTemplate = builder
                .setConnectTimeout(Duration.ofSeconds(90)) // 5초 -> 90초
                .setReadTimeout(Duration.ofSeconds(90))   // 30초 -> 90초
                .build();

        // JAXB2 컨버터 추가
        List<HttpMessageConverter<?>> messageConverters = new ArrayList<>(restTemplate.getMessageConverters());
        messageConverters.add(new Jaxb2RootElementHttpMessageConverter());
        restTemplate.setMessageConverters(messageConverters);

        return restTemplate;
    }
}
