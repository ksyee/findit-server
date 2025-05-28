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
    
    /**
     * 생성자
     * 
     * @param apiClient 경찰청 API 클라이언트
     * @param repository 분실물 저장소
     * @param mapper 분실물 매퍼
     * @param validator 데이터 유효성 검증기
     * @param registry 메트릭 레지스트리
     */
    public LostItemCollectionService(PoliceApiClient apiClient, 
                                    LostItemRepository repository,
                                    LostItemMapper mapper,
                                    DataValidator validator,
                                    MeterRegistry registry) {
        this.apiClient = apiClient;
        this.repository = repository;
        this.mapper = mapper;
        this.validator = validator;
        
        // 메트릭 등록
        this.lostItemsFetchedCounter = registry.counter("api.lost_items.fetched");
        this.lostItemsSavedCounter = registry.counter("api.lost_items.saved");
        this.fetchTimer = registry.timer("api.lost_items.fetch_duration");
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
                String endYmdStr = LocalDate.now().minusDays(1).format(DateTimeFormatter.BASIC_ISO_DATE);
                String startYmdStr = LocalDate.now().minusDays(7).format(DateTimeFormatter.BASIC_ISO_DATE);
                // 사용자 요청: 페이지번호, 행수, 시작/종료일자 외 파라미터 사용 안함
                PoliceApiLostItemResponse response = apiClient.fetchLostItems(1, 100, startYmdStr, endYmdStr);

                if (response != null && response.getBody() != null && response.getBody().getItems() != null) {
                    List<PoliceApiLostItem> lostItems = response.getBody().getItems();
                    lostItemsFetchedCounter.increment(lostItems.size());
                    
                    List<LostItem> mappedItems = mapper.mapList(lostItems);
                    
                    int savedCount = processAndSaveItems(mappedItems);
                    lostItemsSavedCounter.increment(savedCount);
                    totalSaved += savedCount;
                } else {
                    logger.warn("Received null or empty response from Police API for lost items.");
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
        boolean duplicateFound = false;
        while (!duplicateFound) {
            PoliceApiLostItemResponse response = apiClient.fetchLostItems(page, 100, startYmd, endYmd);
            if (response == null || response.getBody() == null || response.getBody().getItems() == null || response.getBody().getItems().isEmpty()) {
                break;
            }
            List<PoliceApiLostItem> lostItems = response.getBody().getItems();
            List<LostItem> mappedItems = mapper.mapList(lostItems);
            for (LostItem item : mappedItems) {
                if (!validator.isValidLostItem(item)) {
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
        logger.info("[분실물] 저장 완료: {}건", totalSaved);
        return totalSaved;
    }
}
