package com.findit.server.config.security;

import jakarta.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.util.StringUtils;

@ConfigurationProperties(prefix = "security.api")
public class SecurityProperties {

    private boolean enabled = true;
    private String headerName = "X-API-KEY";
    private String key;
    private final List<String> permitAll = new ArrayList<>(List.of(
        "/api/health/**",
        "/actuator/**",
        "/swagger-ui/**",
        "/v3/api-docs/**"
    ));

    @PostConstruct
    void validate() {
        if (enabled && !StringUtils.hasText(key)) {
            throw new IllegalStateException("security.api.key must be provided when API security is enabled");
        }
        if (!StringUtils.hasText(headerName)) {
            headerName = "X-API-KEY";
        }
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getHeaderName() {
        return headerName;
    }

    public void setHeaderName(String headerName) {
        this.headerName = headerName;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public List<String> getPermitAll() {
        return permitAll;
    }
}
