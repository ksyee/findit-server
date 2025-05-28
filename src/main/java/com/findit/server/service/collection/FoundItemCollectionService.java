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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * 습득물 데이터 수집 서비스 경찰청 API에서 습득물 데이터를 수집하여 데이터베이스에 저장
 */
@Service
public class FoundItemCollectionService {
  
  private static final Logger logger = LoggerFactory.getLogger(FoundItemCollectionService.class);
  
  private final PoliceApiClient apiClient;
  private final FoundItemRepository repository;
  private final FoundItemMapper mapper;
  private final DataValidator validator;
  private final Counter foundItemsFetchedCounter;
  private final Counter foundItemsSavedCounter;
  private final Timer fetchTimer;
  
  /**
   * 생성자
   *
   * @param apiClient  경찰청 API 클라이언트
   * @param repository 습득물 저장소
   * @param mapper     습득물 매퍼
   * @param validator  데이터 유효성 검증기
   * @param registry   메트릭 레지스트리
   */
  public FoundItemCollectionService(PoliceApiClient apiClient,
    FoundItemRepository repository,
    FoundItemMapper mapper,
    DataValidator validator,
    MeterRegistry registry) {
    this.apiClient = apiClient;
    this.repository = repository;
    this.mapper = mapper;
    this.validator = validator;
    
    // 메트릭 등록
    this.foundItemsFetchedCounter = registry.counter("api.found_items.fetched");
    this.foundItemsSavedCounter = registry.counter("api.found_items.saved");
    this.fetchTimer = registry.timer("api.found_items.fetch_duration");
  }
  
  /**
   * 경찰청 API에서 새로운 습득물 데이터를 가져와 저장
   *
   * @return 저장된 습득물 수
   */
  @Transactional
  @Timed(value = "found_items.collection", description = "Time taken to collect found items")
  public int fetchAndSaveNewItems() {
    logger.info("Fetching new found items from Police API");
    
    return fetchTimer.record(() -> {
      int totalSaved = 0;
      
      try {
        String endYmdStr = LocalDate.now().minusDays(1).format(DateTimeFormatter.BASIC_ISO_DATE);
        String startYmdStr = LocalDate.now().minusDays(7).format(DateTimeFormatter.BASIC_ISO_DATE);
        // 사용자 요청: 페이지번호, 행수, 시작/종료일자 외 파라미터 사용 안함
        PoliceApiFoundItemResponse response = apiClient.fetchFoundItems(1, 100, startYmdStr,
          endYmdStr);
        if (response != null && response.getBody() != null
          && response.getBody().getItems() != null) {
          List<PoliceApiFoundItem> foundItems = response.getBody().getItems();
          foundItemsFetchedCounter.increment(foundItems.size());
          
          List<FoundItem> mappedItems = mapper.mapList(foundItems);
          
          int savedCount = processAndSaveItems(mappedItems);
          foundItemsSavedCounter.increment(savedCount);
          totalSaved += savedCount;
        } else {
          logger.warn("Received null or empty response from Police API for found items.");
        }
      } catch (org.springframework.web.client.HttpClientErrorException.TooManyRequests e) {
        logger.error("Too many requests error fetching found items: {}", e.getMessage(), e);
      } catch (Exception e) {
        logger.error("Error fetching or saving found items: {}", e.getMessage(), e);
      }
      return totalSaved;
    });
  }
  
  /**
   * 습득물 데이터를 처리하고 저장
   *
   * @param items 처리할 습득물 목록
   * @return 저장된 습득물 수
   */
  private int processAndSaveItems(List<FoundItem> items) {
    int saved = 0;
    
    for (FoundItem item : items) {
      if (!validator.isValidFoundItem(item)) {
        logger.warn("Skipping invalid item: {}", item);
        continue;
      }
      
      // 중복 검사
      if (!repository.existsByAtcId(item.getAtcId())) {
        repository.save(item);
        saved++;
      }
    }
    
    return saved;
  }
  
  /**
   * (스케줄러용) 중복 발견 전까지 습득물 데이터를 저장
   * @return 저장된 건수
   */
  public int collectAndSaveUniqueItems() {
    logger.info("[습득물] 중복 발견 전까지 저장 시작");
    int totalSaved = 0;
    LocalDate now = LocalDate.now();
    String endYmd = now.format(DateTimeFormatter.BASIC_ISO_DATE);
    String startYmd = now.minusDays(7).format(DateTimeFormatter.BASIC_ISO_DATE);
    int page = 1;
    boolean duplicateFound = false;
    while (!duplicateFound) {
      PoliceApiFoundItemResponse response = apiClient.fetchFoundItems(page, 100, startYmd, endYmd);
      if (response == null || response.getBody() == null || response.getBody().getItems() == null || response.getBody().getItems().isEmpty()) {
        break;
      }
      List<PoliceApiFoundItem> foundItems = response.getBody().getItems();
      List<FoundItem> mappedItems = mapper.mapList(foundItems);
      for (FoundItem item : mappedItems) {
        if (!validator.isValidFoundItem(item)) {
          logger.warn("Skipping invalid item: {}", item);
          continue;
        }
        if (repository.existsByAtcId(item.getAtcId())) {
          duplicateFound = true;
          break;
        }
        repository.save(item);
        totalSaved++;
      }
      if (!duplicateFound) page++;
    }
    logger.info("[습득물] 저장 완료: {}건", totalSaved);
    return totalSaved;
  }
}
