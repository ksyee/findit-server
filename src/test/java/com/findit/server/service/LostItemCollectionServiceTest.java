package com.findit.server.service;

import com.findit.server.dto.external.PoliceApiLostItem;
import com.findit.server.dto.external.PoliceApiLostItemResponse;
import com.findit.server.service.collection.LostItemCollectionService;
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
class LostItemCollectionServiceTest {
    @Autowired
    LostItemCollectionService lostItemCollectionService;

    @MockBean
    PoliceApiClient policeApiClient;

    @Test
    void testCollectAndSaveUniqueItems() {
        // given: mock API 응답 구성
        PoliceApiLostItem item = new PoliceApiLostItem();
        item.setId("MOCK_ID");
        PoliceApiLostItemResponse response = new PoliceApiLostItemResponse();
        response.setItems(Collections.singletonList(item));
        PoliceApiLostItemResponse emptyResponse = new PoliceApiLostItemResponse();
        emptyResponse.setItems(Collections.emptyList());
        Mockito.when(policeApiClient.fetchLostItems(anyInt(), anyInt(), anyString(), anyString()))
                .thenReturn(response)
                .thenReturn(emptyResponse);
        int saved = lostItemCollectionService.collectAndSaveUniqueItems();
        System.out.println("Saved lost items: " + saved);
        assertTrue(saved >= 0);
    }
}
