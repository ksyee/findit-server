package com.findit.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.DefaultUriBuilderFactory;

@Configuration
public class WebClientConfig {
    
    @Bean
    public WebClient webClient() {
        // URI 인코딩 비활성화 (이미 인코딩된 URI를 다시 인코딩하지 않도록)
        DefaultUriBuilderFactory factory = new DefaultUriBuilderFactory();
        factory.setEncodingMode(DefaultUriBuilderFactory.EncodingMode.NONE);
        
        // 메모리 버퍼 크기 설정 (큰 응답을 처리하기 위해)
        ExchangeStrategies strategies = ExchangeStrategies.builder()
                .codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(2 * 1024 * 1024)) // 2MB
                .build();
        
        return WebClient.builder()
                .uriBuilderFactory(factory)
                .exchangeStrategies(strategies)
                .defaultHeader("User-Agent", "Mozilla/5.0")
                .build();
    }
}