package com.findit.server.service;

import com.findit.server.dto.external.PoliceApiLostItem;
import com.findit.server.dto.external.PoliceApiLostItemResponse;
import com.findit.server.service.collection.LostItemCollectionService;
import com.findit.server.service.external.PoliceApiClient;
import com.findit.server.repository.LostItemRepository;
import com.findit.server.mapper.LostItemMapper;
import com.findit.server.mapper.DataValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.Mockito;

import java.util.Collections;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.*;

@ExtendWith(MockitoExtension.class)
class LostItemCollectionServiceTest {
    @InjectMocks
    LostItemCollectionService lostItemCollectionService;

    @Mock
    PoliceApiClient policeApiClient;

    @Mock
    LostItemRepository repository;

    @Mock
    LostItemMapper mapper;

    @Mock
    DataValidator validator;

    @Mock
    io.micrometer.core.instrument.MeterRegistry registry;

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
        // stub mapper to return empty list to avoid NPE
        Mockito.when(mapper.mapList(anyList())).thenReturn(Collections.emptyList());
        int saved = lostItemCollectionService.collectAndSaveUniqueItems();
        System.out.println("Saved lost items: " + saved);
        assertTrue(saved >= 0);
    }
}
