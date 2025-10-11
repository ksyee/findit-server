package com.findit.server.config.security;

import static org.junit.jupiter.api.Assertions.assertEquals;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;

class ApiKeyAuthenticationFilterTest {

    private SecurityProperties properties;
    private ApiKeyAuthenticationFilter filter;

    @BeforeEach
    void setUp() {
        properties = new SecurityProperties();
        properties.setEnabled(true);
        properties.setKey("test-key");
        properties.setHeaderName("X-API-KEY");
        properties.getPermitAll().clear();
        filter = new ApiKeyAuthenticationFilter(properties);
    }

    @Test
    void validKeyAllowsRequest() throws ServletException, IOException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("X-API-KEY", "test-key");
        MockHttpServletResponse response = new MockHttpServletResponse();
        filter.doFilter(request, response, new MockFilterChain());

        assertEquals(HttpServletResponse.SC_OK, response.getStatus());
        SecurityContextHolder.clearContext();
    }

    @Test
    void invalidKeyReturnsUnauthorized() throws ServletException, IOException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("X-API-KEY", "wrong");
        MockHttpServletResponse response = new MockHttpServletResponse();

        filter.doFilter(request, response, new MockFilterChain());

        assertEquals(HttpServletResponse.SC_UNAUTHORIZED, response.getStatus());
        SecurityContextHolder.clearContext();
    }

    @Test
    void disabledSecuritySkipsFilter() throws ServletException, IOException {
        properties.setEnabled(false);
        filter = new ApiKeyAuthenticationFilter(properties);

        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();

        filter.doFilter(request, response, new MockFilterChain());

        assertEquals(HttpServletResponse.SC_OK, response.getStatus());
    }
}
