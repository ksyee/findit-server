package com.findit.server.config.security;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableConfigurationProperties(SecurityProperties.class)
public class SecurityConfig {

    @Bean
    @ConditionalOnProperty(name = "security.api.enabled", havingValue = "true")
    public ApiKeyAuthenticationFilter apiKeyAuthenticationFilter(SecurityProperties securityProperties) {
        return new ApiKeyAuthenticationFilter(securityProperties);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http,
        SecurityProperties securityProperties,
        ObjectProvider<ApiKeyAuthenticationFilter> apiKeyAuthenticationFilterProvider) throws Exception {

        http
            .csrf(csrf -> csrf.disable())
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .httpBasic(Customizer.withDefaults());

        if (securityProperties.isEnabled()) {
            http.authorizeHttpRequests(auth -> auth
                    .requestMatchers(securityProperties.getPermitAll().toArray(String[]::new)).permitAll()
                    .anyRequest().authenticated());
            apiKeyAuthenticationFilterProvider.ifAvailable(filter ->
                http.addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class));
        } else {
            http.authorizeHttpRequests(auth -> auth.anyRequest().permitAll());
        }

        return http.build();
    }
}
