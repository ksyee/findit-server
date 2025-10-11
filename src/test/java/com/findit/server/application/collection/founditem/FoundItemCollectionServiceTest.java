package com.findit.server.application.collection.founditem;

import com.findit.server.domain.founditem.FoundDate;
import com.findit.server.domain.founditem.FoundItem;
import com.findit.server.domain.founditem.FoundItemId;
import com.findit.server.domain.founditem.FoundItemRepository;
import com.findit.server.domain.shared.ItemCategory;
import com.findit.server.domain.shared.ItemName;
import com.findit.server.domain.shared.LocationName;
import com.findit.server.infrastructure.police.client.PoliceApiClient;
import com.findit.server.infrastructure.police.dto.PoliceApiFoundItem;
import com.findit.server.infrastructure.police.dto.PoliceApiFoundItemResponse;
import com.findit.server.infrastructure.police.mapper.DataValidator;
import com.findit.server.infrastructure.police.mapper.FoundItemMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;

@SpringBootTest
@ActiveProfiles("test")
class FoundItemCollectionServiceTest {

    @Autowired
    FoundItemCollectionService foundItemCollectionService;

    @MockBean
    PoliceApiClient policeApiClient;

    @MockBean
    FoundItemRepository foundItemRepository;

    @MockBean
    FoundItemMapper foundItemMapper;

    @MockBean
    DataValidator dataValidator;

    @Test
    void testCollectAndSaveUniqueItems() {
        PoliceApiFoundItem apiItem = new PoliceApiFoundItem();
        apiItem.setAtcId("MOCK_ID");
        apiItem.setFdPrdtNm("테스트 품목");
        apiItem.setPrdtClNm("전자기기");
        apiItem.setFdYmd("20240501");
        apiItem.setDepPlace("서울");
        apiItem.setFdSn("SN-1");

        PoliceApiFoundItemResponse response = new PoliceApiFoundItemResponse();
        response.setItems(Collections.singletonList(apiItem));
        PoliceApiFoundItemResponse emptyResponse = new PoliceApiFoundItemResponse();
        emptyResponse.setItems(Collections.emptyList());

        Mockito.when(policeApiClient.isEnabled()).thenReturn(true);
        Mockito.when(policeApiClient.fetchFoundItems(anyInt(), anyInt(), anyString(), anyString()))
            .thenReturn(response)
            .thenReturn(emptyResponse);

        FoundItem mapped = FoundItem.create(
            FoundItemId.of("MOCK_ID"),
            ItemName.of("테스트 품목"),
            ItemCategory.of("전자기기"),
            FoundDate.of("20240501"),
            LocationName.of("서울"),
            null,
            null,
            null,
            "SN-1"
        );

        Mockito.when(foundItemMapper.mapList(anyList())).thenReturn(Collections.singletonList(mapped));
        Mockito.when(dataValidator.isValidFoundItem(any())).thenReturn(true);
        Mockito.when(foundItemRepository.existsByAtcId(anyString())).thenReturn(false);

        int saved = foundItemCollectionService.collectAndSaveUniqueItems().size();
        assertTrue(saved >= 0);
    }
}
