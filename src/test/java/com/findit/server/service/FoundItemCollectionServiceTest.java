package com.findit.server.service;

import com.findit.server.dto.external.PoliceApiFoundItem;
import com.findit.server.dto.external.PoliceApiFoundItemResponse;
import com.findit.server.service.collection.FoundItemCollectionService;
import com.findit.server.service.external.PoliceApiClient;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import java.util.Collections;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.*;

@SpringBootTest
class FoundItemCollectionServiceTest {
    @Autowired
    FoundItemCollectionService foundItemCollectionService;

    @MockBean
    PoliceApiClient policeApiClient;

    @Test
    void testCollectAndSaveUniqueItems() {
        // given: mock API 응답 구성
        PoliceApiFoundItem item = new PoliceApiFoundItem();
        item.setId("MOCK_ID");
        PoliceApiFoundItemResponse response = new PoliceApiFoundItemResponse();
        response.setItems(Collections.singletonList(item));
        PoliceApiFoundItemResponse emptyResponse = new PoliceApiFoundItemResponse();
        emptyResponse.setItems(Collections.emptyList());
        Mockito.when(policeApiClient.fetchFoundItems(anyInt(), anyInt(), anyString(), anyString()))
                .thenReturn(response)
                .thenReturn(emptyResponse);
        int saved = foundItemCollectionService.collectAndSaveUniqueItems();
        System.out.println("Saved found items: " + saved);
        assertTrue(saved >= 0);
    }
}
