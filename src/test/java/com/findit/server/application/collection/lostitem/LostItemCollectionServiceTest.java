package com.findit.server.application.collection.lostitem;

import com.findit.server.domain.lostitem.LostDate;
import com.findit.server.domain.lostitem.LostItem;
import com.findit.server.domain.lostitem.LostItemId;
import com.findit.server.domain.lostitem.LostItemRepository;
import com.findit.server.domain.shared.ItemCategory;
import com.findit.server.domain.shared.LocationName;
import com.findit.server.infrastructure.police.client.PoliceApiClient;
import com.findit.server.infrastructure.police.dto.PoliceApiLostItem;
import com.findit.server.infrastructure.police.dto.PoliceApiLostItemResponse;
import com.findit.server.infrastructure.police.mapper.DataValidator;
import com.findit.server.infrastructure.police.mapper.LostItemMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;

class LostItemCollectionServiceTest {

  @Mock
  private PoliceApiClient policeApiClient;
  @Mock
  private LostItemRepository repository;
  @Mock
  private LostItemMapper mapper;
  @Mock
  private DataValidator validator;
  @Mock
  private MeterRegistry registry;
  @Mock
  private Counter fetchedCounter;
  @Mock
  private Counter savedCounter;
  @Mock
  private Timer fetchTimer;

  private LostItemCollectionService lostItemCollectionService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    Mockito.when(registry.counter("api.lost_items.fetched")).thenReturn(fetchedCounter);
    Mockito.when(registry.counter("api.lost_items.saved")).thenReturn(savedCounter);
    Mockito.when(registry.timer("api.lost_items.fetch_duration")).thenReturn(fetchTimer);

    lostItemCollectionService = new LostItemCollectionService(
      policeApiClient,
      repository,
      mapper,
      validator,
      registry,
      new ObjectMapper()
    );
  }

  @Test
  void testCollectAndSaveUniqueItems() {
    PoliceApiLostItem item = new PoliceApiLostItem();
    item.setLostItemId("MOCK_ID");
    item.setLostItemCategory("전자기기");
    item.setLostPlace("서울");
    item.setLostDate("20240501");

    PoliceApiLostItemResponse response = new PoliceApiLostItemResponse();
    response.setItems(Collections.singletonList(item));
    PoliceApiLostItemResponse emptyResponse = new PoliceApiLostItemResponse();
    emptyResponse.setItems(Collections.emptyList());

    Mockito.when(policeApiClient.isEnabled()).thenReturn(true);
    Mockito.when(policeApiClient.fetchLostItems(anyInt(), anyInt(), anyString(), anyString()))
      .thenReturn(response)
      .thenReturn(emptyResponse);

    LostItem mappedItem = LostItem.create(
      LostItemId.of("MOCK_ID"),
      ItemCategory.of("전자기기"),
      LocationName.of("서울"),
      LostDate.of("20240501"),
      null,
      null,
      null
    );

    Mockito.when(mapper.mapList(anyList())).thenReturn(Collections.singletonList(mappedItem));
    Mockito.when(validator.isValidLostItem(any())).thenReturn(true);
    Mockito.when(repository.existsByAtcId(anyString())).thenReturn(false);

    int saved = lostItemCollectionService.collectAndSaveUniqueItems().size();
    assertTrue(saved > 0);
  }
}
