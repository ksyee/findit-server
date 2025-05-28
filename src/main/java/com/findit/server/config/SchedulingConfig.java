package com.findit.server.config;

import com.findit.server.service.collection.FoundItemCollectionService;
import com.findit.server.service.collection.LostItemCollectionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

/**
 * uc2a4ucf00uc904ub9c1 uad6cuc131 ud074ub798uc2a4
 * uc815uae30uc801uc778 ub370uc774ud130 uc218uc9d1 uc791uc5c5uc744 uc2a4ucf00uc904ub9c1
 */
@Configuration
@EnableScheduling
public class SchedulingConfig {
    private final LostItemCollectionService lostItemService;
    private final FoundItemCollectionService foundItemService;
    private final Logger logger = LoggerFactory.getLogger(SchedulingConfig.class);
    
    /**
     * uc0dduc131uc790
     * 
     * @param lostItemService ubd84uc2e4ubb3c uc218uc9d1 uc11cube44uc2a4
     * @param foundItemService uc2b5ub4ddubb3c uc218uc9d1 uc11cube44uc2a4
     */
    public SchedulingConfig(LostItemCollectionService lostItemService,
                           FoundItemCollectionService foundItemService) {
        this.lostItemService = lostItemService;
        this.foundItemService = foundItemService;
    }
    
    /**
     * 매일 0,3,6,9,12,15,18,21시에 분실물/습득물 데이터를 중복 없이 저장
     * 조회 범위: (오늘-7일)~오늘
     */
    @EventListener(ApplicationReadyEvent.class)
    @Scheduled(cron = "0 0 0,3,6,9,12,15,18,21 * * *")
    public void collectLostAndFoundItems() {
        logger.info("[스케줄] 분실물/습득물 데이터 수집 시작");
        try {
            int lostSaved = lostItemService.collectAndSaveUniqueItems();
            int foundSaved = foundItemService.collectAndSaveUniqueItems();
            logger.info("[스케줄] 저장 완료: 분실물 {}건, 습득물 {}건", lostSaved, foundSaved);
        } catch (Exception e) {
            logger.error("[스케줄] 분실물/습득물 데이터 수집 중 오류: {}", e.getMessage(), e);
        }
    }
}
