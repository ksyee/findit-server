package com.findit.server.service.external;

import com.findit.server.dto.external.PoliceApiFoundItem;
import com.findit.server.dto.external.PoliceApiFoundItemResponse;
import com.findit.server.dto.external.PoliceApiLostItem;
import com.findit.server.dto.external.PoliceApiLostItemResponse;
import com.findit.server.entity.FoundItem;
import com.findit.server.entity.LostItem;
import com.findit.server.repository.FoundItemRepository;
import com.findit.server.repository.LostItemRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

/**
 * 경찰청 API 데이터 동기화 서비스
 * 정기적으로 경찰청 API에서 분실물 및 습득물 데이터를 가져와 데이터베이스에 저장
 */
@Service
public class PoliceApiSyncService {
    private static final Logger logger = LoggerFactory.getLogger(PoliceApiSyncService.class);
    private static final DateTimeFormatter API_DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd");
    private static final DateTimeFormatter API_DATE_PARAM_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd");
    private static final int DEFAULT_NUM_OF_ROWS = 100;

    private final PoliceApiClient policeApiClient;
    private final LostItemRepository lostItemRepository;
    private final FoundItemRepository foundItemRepository;

    public PoliceApiSyncService(PoliceApiClient policeApiClient,
                               LostItemRepository lostItemRepository,
                               FoundItemRepository foundItemRepository) {
        this.policeApiClient = policeApiClient;
        this.lostItemRepository = lostItemRepository;
        this.foundItemRepository = foundItemRepository;
    }

    @Transactional
    public void syncLostItems() {
        logger.info("Starting daily lost items synchronization from Police API for yesterday's data.");
        LocalDate yesterday = LocalDate.now().minusDays(1);
        fetchAllLostItems(yesterday);
    }

    @Transactional
    public void syncFoundItems() {
        logger.info("Starting daily found items synchronization from Police API for yesterday's data.");
        LocalDate yesterday = LocalDate.now().minusDays(1);
        fetchAllFoundItems(yesterday);
    }

    private void fetchAllLostItems(LocalDate date) {
        String dateStr = date.format(DateTimeFormatter.BASIC_ISO_DATE);
        int pageNo = 1;
        int totalSavedCount = 0;
        int totalUpdatedCount = 0;
        boolean hasMoreData = true;
        logger.info("Fetching all lost items from Police API for period: {} to {}", dateStr, dateStr);

        while (hasMoreData) {
            try {
                PoliceApiLostItemResponse response = policeApiClient.fetchLostItems(pageNo, DEFAULT_NUM_OF_ROWS, dateStr, dateStr);
                if (response == null || response.getBody() == null || response.getBody().getItems() == null || response.getBody().getItems().isEmpty()) {
                    logger.info("No more lost items found or empty response at page {}. Ending sync.", pageNo);
                    hasMoreData = false;
                    continue;
                }

                List<PoliceApiLostItem> apiItems = response.getBody().getItems();
                int newInThisPage = 0;
                int updatedInThisPage = 0;

                for (PoliceApiLostItem apiItem : apiItems) {
                    if (!StringUtils.hasText(apiItem.getLstGoodsSn())) {
                        logger.warn("Skipping lost item with null or empty LST_GOODS_SN: {}", apiItem);
                        continue;
                    }
                    LostItem existingItem = lostItemRepository.findByAtcId(apiItem.getLstGoodsSn()).orElse(null);
                    LostItem lostItemToSave = (existingItem != null) ? existingItem : new LostItem();

                    lostItemToSave.setAtcId(apiItem.getLstGoodsSn());
                    lostItemToSave.setPrdtClNm(apiItem.getItemType()); 
                    lostItemToSave.setLstPlace(apiItem.getLocation()); 
                    
                    // PoliceApiLostItem의 lstYmd (분실일자) 매핑
                    String rawDate = apiItem.getLostDate();
                    if (rawDate != null && !rawDate.trim().isEmpty()) {
                        String trimmed = rawDate.trim();
                        lostItemToSave.setLstYmd(trimmed.length() >= 8 ? trimmed.substring(0, 8) : trimmed);
                    } else {
                        lostItemToSave.setLstYmd(null);
                    }

                    if (apiItem.getRnum() != null) { 
                        lostItemToSave.setRnum(apiItem.getRnum());
                    }

                    lostItemRepository.save(lostItemToSave);
                    if (existingItem == null) {
                        newInThisPage++;
                    } else {
                        updatedInThisPage++;
                    }
                }
                totalSavedCount += newInThisPage;
                totalUpdatedCount += updatedInThisPage;
                logger.info("Page {}: Processed {} lost items ({} new, {} updated).", pageNo, apiItems.size(), newInThisPage, updatedInThisPage);

                if (pageNo * DEFAULT_NUM_OF_ROWS >= response.getBody().getTotalCount() || apiItems.size() < DEFAULT_NUM_OF_ROWS) {
                    hasMoreData = false;
                }
                pageNo++;
            } catch (Exception e) {
                logger.error("Error during lost items synchronization at page {}: {}", pageNo, e.getMessage(), e);
                hasMoreData = false;
            }
        }
        logger.info("Finished lost items synchronization. Total new items: {}, Total updated items: {}.", totalSavedCount, totalUpdatedCount);
    }

    private void fetchAllFoundItems(LocalDate date) {
        String dateStr = date.format(DateTimeFormatter.BASIC_ISO_DATE);
        int pageNo = 1;
        int totalSavedCount = 0;
        int totalUpdatedCount = 0;
        boolean hasMoreData = true;
        logger.info("Fetching all found items from Police API for period: {} to {}", dateStr, dateStr);

        while (hasMoreData) {
            try {
                PoliceApiFoundItemResponse response = policeApiClient.fetchFoundItems(pageNo, DEFAULT_NUM_OF_ROWS, dateStr, dateStr);
                if (response == null || response.getBody() == null || response.getBody().getItems() == null || response.getBody().getItems().isEmpty()) {
                    logger.info("No more found items found or empty response at page {}. Ending sync.", pageNo);
                    hasMoreData = false;
                    continue;
                }

                List<PoliceApiFoundItem> apiItems = response.getBody().getItems();
                int newInThisPage = 0;
                int updatedInThisPage = 0;

                for (PoliceApiFoundItem apiItem : apiItems) {
                     if (!StringUtils.hasText(apiItem.getFdSn())) {
                        logger.warn("Skipping found item with null or empty FD_SN: {}", apiItem);
                        continue;
                    }
                    // 기존 항목이 있는지 확인
                    boolean isNew = !foundItemRepository.existsByAtcId(apiItem.getFdSn());
                    
                    // API 응답에서 필요한 데이터를 엔티티에 매핑
                    FoundItem foundItemToSave = FoundItem.builder()
                        .atcId(apiItem.getFdSn()) // FD_SN을 atcId로 사용
                        .fdPrdtNm(apiItem.getItemName())
                        .prdtClNm(apiItem.getItemType())
                        .fdYmd(apiItem.getFoundDate()) // 문자열 형식 그대로 저장
                        .fdSbjt(apiItem.getDescription())
                        .fdFilePathImg(apiItem.getImageUrl())
                        .depPlace(apiItem.getStoragePlaceName())
                        .clrNm(apiItem.getColor())
                        .fdSn(apiItem.getFdSn())
                        .build();

                    foundItemRepository.save(foundItemToSave);
                    
                    if (isNew) {
                        newInThisPage++;
                    } else {
                        updatedInThisPage++;
                    }
                }
                totalSavedCount += newInThisPage;
                totalUpdatedCount += updatedInThisPage;
                logger.info("Page {}: Processed {} found items ({} new, {} updated).", pageNo, apiItems.size(), newInThisPage, updatedInThisPage);

                if (pageNo * DEFAULT_NUM_OF_ROWS >= response.getBody().getTotalCount() || apiItems.size() < DEFAULT_NUM_OF_ROWS) {
                    hasMoreData = false;
                }
                pageNo++;
            } catch (Exception e) {
                logger.error("Error during found items synchronization at page {}: {}", pageNo, e.getMessage(), e);
                hasMoreData = false;
            }
        }
        logger.info("Finished found items synchronization. Total new items: {}, Total updated items: {}.", totalSavedCount, totalUpdatedCount);
    }

    private LocalDateTime parseDate(String dateStr, String contextInfo) {
        if (!StringUtils.hasText(dateStr)) {
            logger.warn("Date string is null or empty for {}. Returning null.", contextInfo);
            return null;
        }
        try {
            LocalDate date = LocalDate.parse(dateStr.trim(), API_DATE_FORMATTER);
            return date.atStartOfDay(); 
        } catch (DateTimeParseException e) {
            logger.error("Error parsing date string '{}' for {}: {}. Returning null.", dateStr, contextInfo, e.getMessage());
            return null;
        }
    }
}
