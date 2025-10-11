package com.findit.server.infrastructure.police.client;

import com.findit.server.infrastructure.police.dto.PoliceApiFoundItemResponse;
import com.findit.server.infrastructure.police.dto.PoliceApiLostItemResponse;
import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;

class PoliceApiClientTest {

    private final RestTemplate restTemplate = new RestTemplate();

    @Test
    void disabledClientSkipsRemoteCalls() {
        PoliceApiClient client = new PoliceApiClient(
                restTemplate,
                "http://localhost",
                "",
                "/lost",
                "/found",
                false
        );

        assertFalse(client.isEnabled());

        PoliceApiLostItemResponse lostResponse = client.fetchLostItems(1, 1, null, null);
        assertNotNull(lostResponse);
        assertEquals("API_DISABLED", lostResponse.getHeader().getResultCode());
        assertTrue(lostResponse.getItems().isEmpty());

        PoliceApiFoundItemResponse foundResponse = client.fetchFoundItems(1, 1, null, null);
        assertNotNull(foundResponse);
        assertEquals("API_DISABLED", foundResponse.getHeader().getResultCode());
        assertTrue(foundResponse.getItems().isEmpty());
    }
}
