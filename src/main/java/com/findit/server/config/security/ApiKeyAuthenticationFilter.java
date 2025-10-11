package com.findit.server.config.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

public class ApiKeyAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(ApiKeyAuthenticationFilter.class);

    private final SecurityProperties securityProperties;
    private final AntPathMatcher pathMatcher = new AntPathMatcher();

    public ApiKeyAuthenticationFilter(SecurityProperties securityProperties) {
        this.securityProperties = securityProperties;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return !securityProperties.isEnabled() ||
            securityProperties.getPermitAll().stream()
                .anyMatch(pattern -> pathMatcher.match(pattern, request.getServletPath()));
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
        FilterChain filterChain) throws ServletException, IOException {

        String apiKeyHeader = request.getHeader(securityProperties.getHeaderName());

        if (!StringUtils.hasText(apiKeyHeader) || !apiKeyHeader.equals(securityProperties.getKey())) {
            log.warn("Request rejected due to missing or invalid API key. path={}, remote={}",
                request.getRequestURI(), request.getRemoteAddr());
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().write("{\"error\":\"unauthorized\",\"message\":\"Invalid API key\"}");
            return;
        }

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
            "api-key",
            null,
            java.util.List.of(new SimpleGrantedAuthority("ROLE_API"))
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        try {
            filterChain.doFilter(request, response);
        } finally {
            SecurityContextHolder.clearContext();
        }
    }
}
