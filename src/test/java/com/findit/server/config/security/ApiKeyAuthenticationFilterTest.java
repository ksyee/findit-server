package com.findit.server.config.security;

import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import static org.assertj.core.api.Assertions.assertThat;

class ApiKeyAuthenticationFilterTest {

    @Test
    void permitAllPathSkipsFilter() {
        SecurityProperties props = new SecurityProperties();
        props.setEnabled(true);
        props.setKey("test-key");
        ApiKeyAuthenticationFilter filter = new ApiKeyAuthenticationFilter(props);

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setContextPath("");
        request.setRequestURI("/api/health");

        assertThat(filter.shouldNotFilter(request)).isTrue();
    }

    @Test
    void protectedPathRequiresApiKey() {
        SecurityProperties props = new SecurityProperties();
        props.setEnabled(true);
        props.setKey("test-key");
        ApiKeyAuthenticationFilter filter = new ApiKeyAuthenticationFilter(props);

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setContextPath("");
        request.setRequestURI("/api/private");

        assertThat(filter.shouldNotFilter(request)).isFalse();
    }
}
