package com.findit.server.application.collection;

import com.findit.server.domain.founditem.FoundItem;
import com.findit.server.domain.founditem.FoundItemRepository;
import com.findit.server.domain.lostitem.LostItem;
import com.findit.server.domain.lostitem.LostItemRepository;
import com.findit.server.infrastructure.police.client.PoliceApiClient;
import com.findit.server.infrastructure.police.dto.PoliceApiFoundItem;
import com.findit.server.infrastructure.police.dto.PoliceApiFoundItemResponse;
import com.findit.server.infrastructure.police.dto.PoliceApiLostItem;
import com.findit.server.infrastructure.police.dto.PoliceApiLostItemResponse;
import com.findit.server.infrastructure.police.mapper.FoundItemMapper;
import com.findit.server.infrastructure.police.mapper.LostItemMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * 경찰청 API 데이터 동기화 서비스
 */
@Service
public class PoliceApiSyncService {

    private static final Logger logger = LoggerFactory.getLogger(PoliceApiSyncService.class);
    private static final DateTimeFormatter API_DATE_PARAM_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd");
    private static final int DEFAULT_NUM_OF_ROWS = 100;

    private final PoliceApiClient policeApiClient;
    private final LostItemRepository lostItemRepository;
    private final FoundItemRepository foundItemRepository;
    private final LostItemMapper lostItemMapper;
    private final FoundItemMapper foundItemMapper;

    public PoliceApiSyncService(PoliceApiClient policeApiClient,
                               LostItemRepository lostItemRepository,
                               FoundItemRepository foundItemRepository,
                               LostItemMapper lostItemMapper,
                               FoundItemMapper foundItemMapper) {
        this.policeApiClient = policeApiClient;
        this.lostItemRepository = lostItemRepository;
        this.foundItemRepository = foundItemRepository;
        this.lostItemMapper = lostItemMapper;
        this.foundItemMapper = foundItemMapper;
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
        String dateStr = date.format(API_DATE_PARAM_FORMATTER);
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
                    if (!StringUtils.hasText(apiItem.getLostItemId())) {
                        logger.warn("Skipping lost item with null or empty atc_id: {}", apiItem);
                        continue;
                    }
                    try {
                        LostItem mapped = lostItemMapper.map(apiItem);
                        if (mapped == null) {
                            logger.debug("Skipping lost item due to incomplete data: {}", apiItem.getLostItemId());
                            continue;
                        }
                        boolean exists = lostItemRepository.existsByAtcId(mapped.getAtcId());
                        lostItemRepository.save(mapped);
                        if (exists) {
                            updatedInThisPage++;
                        } else {
                            newInThisPage++;
                        }
                    } catch (IllegalArgumentException ex) {
                        logger.warn("Skipping lost item [{}] due to invalid data: {}", apiItem.getLostItemId(), ex.getMessage());
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
        String dateStr = date.format(API_DATE_PARAM_FORMATTER);
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
                    if (!StringUtils.hasText(apiItem.getAtcId()) && !StringUtils.hasText(apiItem.getFdSn())) {
                        logger.warn("Skipping found item with missing identifiers: {}", apiItem);
                        continue;
                    }
                    try {
                        FoundItem mapped = foundItemMapper.map(apiItem);
                        if (mapped == null) {
                            logger.debug("Skipping found item due to incomplete data: {}", apiItem);
                            continue;
                        }
                        boolean exists = foundItemRepository.existsByAtcId(mapped.getAtcId());
                        foundItemRepository.save(mapped);
                        if (exists) {
                            updatedInThisPage++;
                        } else {
                            newInThisPage++;
                        }
                    } catch (IllegalArgumentException ex) {
                        logger.warn("Skipping found item [{}] due to invalid data: {}", apiItem.getAtcId(), ex.getMessage());
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
}
