package com.findit.server.service.collection;

import com.findit.server.dto.external.PoliceApiLostItem;
import com.findit.server.dto.external.PoliceApiLostItemResponse;
import com.findit.server.entity.LostItem;
import com.findit.server.mapper.DataValidator;
import com.findit.server.mapper.LostItemMapper;
import com.findit.server.repository.LostItemRepository;
import com.findit.server.service.external.PoliceApiClient;
import io.micrometer.core.annotation.Timed;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * 분실물 데이터 수집 서비스
 * 경찰청 API에서 분실물 데이터를 수집하여 데이터베이스에 저장
 */
@Service
public class LostItemCollectionService {
    private static final Logger logger = LoggerFactory.getLogger(LostItemCollectionService.class);
    
    private final PoliceApiClient apiClient;
    private final LostItemRepository repository;
    private final LostItemMapper mapper;
    private final DataValidator validator;
    private final Counter lostItemsFetchedCounter;
    private final Counter lostItemsSavedCounter;
    private final Timer fetchTimer;
    private final ObjectMapper objectMapper;
    
    /**
     * 생성자
     * 
     * @param apiClient 경찰청 API 클라이언트
     * @param repository 분실물 저장소
     * @param mapper 분실물 매퍼
     * @param validator 데이터 유효성 검증기
     * @param registry 메트릭 레지스트리
     * @param objectMapper JSON 객체 매퍼
     */
    public LostItemCollectionService(PoliceApiClient apiClient, 
                                    LostItemRepository repository,
                                    LostItemMapper mapper,
                                    DataValidator validator,
                                    MeterRegistry registry,
                                    ObjectMapper objectMapper) {
        this.apiClient = apiClient;
        this.repository = repository;
        this.mapper = mapper;
        this.validator = validator;
        
        // 메트릭 등록
        this.lostItemsFetchedCounter = registry.counter("api.lost_items.fetched");
        this.lostItemsSavedCounter = registry.counter("api.lost_items.saved");
        this.fetchTimer = registry.timer("api.lost_items.fetch_duration");
        // JSON 로깅용 ObjectMapper: null일 경우 기본 인스턴스 사용
        this.objectMapper = (objectMapper != null ? objectMapper : new com.fasterxml.jackson.databind.ObjectMapper());
    }
    
    /**
     * 경찰청 API에서 새로운 분실물 데이터를 가져와 저장
     * 
     * @return 저장된 분실물 수
     */
    @Timed(value = "lost_items.collection", description = "Time taken to collect lost items")
    public int fetchAndSaveNewItems() {
        logger.info("Fetching new lost items from Police API");
        
        return fetchTimer.record(() -> {
            int totalSaved = 0;
            
            try {
                // 날짜 범위 설정
                String endYmdStr = LocalDate.now().minusDays(1).format(DateTimeFormatter.BASIC_ISO_DATE);
                String startYmdStr = LocalDate.now().minusDays(7).format(DateTimeFormatter.BASIC_ISO_DATE);
                // API 호출 및 응답 처리
                PoliceApiLostItemResponse response = apiClient.fetchLostItems(1, 100, startYmdStr, endYmdStr);
                try {
                    String json = objectMapper.writeValueAsString(response);
                    logger.info("[분실물 DTO JSON]: {}", json);
                } catch (JsonProcessingException e) {
                    logger.error("분실물 DTO JSON 변환 오류: {}", e.getMessage(), e);
                }
                List<PoliceApiLostItem> lostItems = response.getItems();
                logger.info("[분실물] API에서 받은 총 데이터 수: {}건", lostItems.size());
                if (!lostItems.isEmpty()) {
                    lostItemsFetchedCounter.increment(lostItems.size());
                    List<LostItem> mappedItems = mapper.mapList(lostItems);
                    int savedCount = processAndSaveItems(mappedItems);
                    lostItemsSavedCounter.increment(savedCount);
                    totalSaved += savedCount;
                } else {
                    logger.warn("[분실물] API에서 빈 목록을 반환했습니다.");
                }
            } catch (org.springframework.web.client.HttpClientErrorException.TooManyRequests e) {
                logger.error("Too many requests error fetching lost items: {}", e.getMessage(), e);
            } catch (Exception e) {
                logger.error("Error fetching or saving lost items: {}", e.getMessage(), e);
            }
            return totalSaved;
        });
    }
    
    /**
     * 분실물 데이터를 처리하고 저장
     * 
     * @param items 처리할 분실물 목록
     * @return 저장된 분실물 수
     */
    private int processAndSaveItems(List<LostItem> items) {
        int saved = 0;
        
        for (LostItem item : items) {
            if (!validator.isValidLostItem(item)) {
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
     * (스케줄러용) 중복 발견 전까지 분실물 데이터를 저장
     * @return 저장된 건수
     */
    public int collectAndSaveUniqueItems() {
        logger.info("[분실물] 중복 발견 전까지 저장 시작");
        int totalSaved = 0;
        LocalDate now = LocalDate.now();
        String endYmd = now.format(DateTimeFormatter.BASIC_ISO_DATE);
        String startYmd = now.minusDays(7).format(DateTimeFormatter.BASIC_ISO_DATE);
        int page = 1;
        while (true) {
            PoliceApiLostItemResponse response = apiClient.fetchLostItems(page, 100, startYmd, endYmd);
            try {
                String json = objectMapper.writeValueAsString(response);
                logger.info("[분실물 DTO JSON, page {}]: {}", page, json);
            } catch (JsonProcessingException e) {
                logger.error("분실물 DTO JSON 변환 오류: {}", e.getMessage(), e);
            }
            List<PoliceApiLostItem> lostItems = response.getItems();
            logger.info("[분실물] 페이지{} API 반환 데이터 수: {}건", page, lostItems.size());
            if (lostItems.isEmpty()) break;
            List<LostItem> mappedItems = mapper.mapList(lostItems);
            int savedThisPage = 0;
            for (LostItem item : mappedItems) {
                if (!validator.isValidLostItem(item)) {
                    logger.warn("Skipping invalid item: {}", item);
                    continue;
                }
                if (repository.existsByAtcId(item.getAtcId())) continue;
                repository.save(item);
                totalSaved++;
                savedThisPage++;
            }
            if (savedThisPage == 0) break;
            page++;
        }
        logger.info("[분실물] 저장 완료: {}건", totalSaved);
        return totalSaved;
    }
}
