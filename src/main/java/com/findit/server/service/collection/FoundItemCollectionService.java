package com.findit.server.service.collection;

import com.findit.server.dto.external.PoliceApiFoundItem;
import com.findit.server.dto.external.PoliceApiFoundItemResponse;
import com.findit.server.entity.FoundItem;
import com.findit.server.mapper.DataValidator;
import com.findit.server.mapper.FoundItemMapper;
import com.findit.server.repository.FoundItemRepository;
import com.findit.server.service.external.PoliceApiClient;
import io.micrometer.core.annotation.Timed;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ArrayList;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonProcessingException;

/**
 * 습득물 데이터 수집 서비스 경찰청 API에서 습득물 데이터를 수집하여 데이터베이스에 저장
 */
@Service
public class FoundItemCollectionService {
  
  private static final Logger logger = LoggerFactory.getLogger(FoundItemCollectionService.class);
  
  // 한 번 실행 시 최대 10페이지(페이지당 100건)만 저장
  private static final int MAX_PAGES = 10;
  
  private final PoliceApiClient apiClient;
  private final FoundItemRepository repository;
  private final FoundItemMapper mapper;
  private final DataValidator validator;
  private final Counter foundItemsFetchedCounter;
  private final Counter foundItemsSavedCounter;
  private final Timer fetchTimer;
  private final ObjectMapper objectMapper;
  
  /**
   * 생성자
   *
   * @param apiClient    경찰청 API 클라이언트
   * @param repository   습득물 저장소
   * @param mapper       습득물 매퍼
   * @param validator    데이터 유효성 검증기
   * @param registry     메트릭 레지스트리
   * @param objectMapper JSON 객체 매퍼
   */
  public FoundItemCollectionService(PoliceApiClient apiClient,
    FoundItemRepository repository,
    FoundItemMapper mapper,
    DataValidator validator,
    MeterRegistry registry,
    ObjectMapper objectMapper) {
    this.apiClient = apiClient;
    this.repository = repository;
    this.mapper = mapper;
    this.validator = validator;
    this.objectMapper = objectMapper;
    
    // 메트릭 등록
    this.foundItemsFetchedCounter = registry.counter("api.found_items.fetched");
    this.foundItemsSavedCounter = registry.counter("api.found_items.saved");
    this.fetchTimer = registry.timer("api.found_items.fetch_duration");
  }
  
  /**
   * 경찰청 API에서 새로운 습득물 데이터를 가져와 저장
   *
   * @return 저장된 습득물 목록
   */
  @Transactional
  @Timed(value = "found_items.collection", description = "Time taken to collect found items")
  public List<FoundItem> fetchAndSaveNewItems() {
    if (!apiClient.isEnabled()) {
      logger.info("Police API 비활성화 상태로 습득물 신규 수집을 건너뜁니다.");
      return new ArrayList<>();
    }
    logger.info("Fetching new found items from Police API");
    
    return fetchTimer.record(() -> {
      int totalSaved = 0;
      List<FoundItem> savedItems = new ArrayList<>();
      
      try {
        // 날짜 범위 설정
        String endYmdStr = LocalDate.now().minusDays(1).format(DateTimeFormatter.BASIC_ISO_DATE);
        String startYmdStr = LocalDate.now().minusDays(7).format(DateTimeFormatter.BASIC_ISO_DATE);
        
        // API 호출 및 응답 처리
        PoliceApiFoundItemResponse response = apiClient.fetchFoundItems(1, 100, startYmdStr,
          endYmdStr);
        try {
          String json = objectMapper.writeValueAsString(response);
          logger.info("[습득물 DTO JSON]: {}", json);
        } catch (JsonProcessingException e) {
          logger.error("습득물 DTO JSON 변환 오류: {}", e.getMessage(), e);
        }
        
        List<PoliceApiFoundItem> foundItems = response.getItems();
        logger.info("[습득물] API에서 받은 총 데이터 수: {}건", foundItems.size());
        
        if (!foundItems.isEmpty()) {
          foundItemsFetchedCounter.increment(foundItems.size());
          List<FoundItem> mappedItems = mapper.mapList(foundItems);
          savedItems = saveItems(mappedItems);
          totalSaved = savedItems.size();
          foundItemsSavedCounter.increment(totalSaved);
          
          logger.info("[습득물] 신규 수집 저장 완료: {}건", totalSaved);
          return savedItems;
        } else {
          logger.warn("[습득물] API에서 빈 목록을 반환했습니다.");
        }
      } catch (org.springframework.web.client.HttpClientErrorException.TooManyRequests e) {
        logger.error("Too many requests error fetching found items: {}", e.getMessage(), e);
      } catch (Exception e) {
        logger.error("Error fetching or saving found items: {}", e.getMessage(), e);
      }
      return savedItems;
    });
  }
  
  /**
   * 습득물 데이터를 처리하고 저장
   *
   * @param items 처리할 습득물 목록
   * @return 저장된 습득물 목록
   */
  private List<FoundItem> saveItems(List<FoundItem> items) {
    List<FoundItem> savedItems = new ArrayList<>();
    
    for (FoundItem item : items) {
      if (!validator.isValidFoundItem(item)) {
        logger.warn("Skipping invalid item: {}", item);
        continue;
      }
      
      // 중복 검사
      if (!repository.existsByAtcId(item.getAtcId())) {
        FoundItem savedItem = repository.save(item);
        savedItems.add(savedItem);
      }
    }
    
    return savedItems;
  }
  
  /**
   * (스케줄러용) 중복 발견 전까지 습득물 데이터를 저장
   *
   * @return 저장된 습득물 목록
   */
  public List<FoundItem> collectAndSaveUniqueItems() {
    if (!apiClient.isEnabled()) {
      logger.info("Police API 비활성화 상태로 습득물 배치 수집을 건너뜁니다.");
      return new ArrayList<>();
    }
    List<FoundItem> savedItems = new ArrayList<>();
    LocalDate now = LocalDate.now();
    String endYmd = now.format(DateTimeFormatter.BASIC_ISO_DATE);
    String startYmd = now.minusDays(7).format(DateTimeFormatter.BASIC_ISO_DATE);
    int page = 1;
    while (page <= MAX_PAGES) {
      PoliceApiFoundItemResponse response = apiClient.fetchFoundItems(page, 100, startYmd, endYmd);
      try {
        String json = objectMapper.writeValueAsString(response);
        logger.info("[습득물 DTO JSON, page {}]: {}", page, json);
      } catch (JsonProcessingException e) {
        logger.error("습득물 DTO JSON 변환 오류: {}", e.getMessage(), e);
      }
      List<PoliceApiFoundItem> foundItems = response.getItems();
      logger.info("[습득물] 페이지{} API 반환 데이터 수: {}건", page, foundItems.size());
      if (foundItems.isEmpty()) {
        break;
      }
      
      List<FoundItem> mappedItems = mapper.mapList(foundItems);
      List<FoundItem> validItems = mappedItems.stream()
        .filter(validator::isValidFoundItem)
        .toList();
      repository.upsertBatch(validItems);
      savedItems.addAll(validItems);
      // 중복 등으로 저장 건수가 0이거나 최대 페이지를 모두 조회했으면 종료
      if (validItems.isEmpty()) {
        break;
      }
      page++;
    }
    
    logger.info("[습득물] 전체 배치 저장 완료: {}건", savedItems.size());
    
    return savedItems;
  }

  public boolean isCollectionEnabled() {
    return apiClient.isEnabled();
  }
}
