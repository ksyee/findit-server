package com.findit.server.config;

import com.findit.server.domain.founditem.FoundItem;
import com.findit.server.domain.lostitem.LostItem;
import com.findit.server.application.collection.founditem.FoundItemCollectionService;
import com.findit.server.application.collection.lostitem.LostItemCollectionService;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

/**
 * 스케줄링 구성 클래스 정기적인 데이터 수집 작업을 스케줄링
 */
@Configuration
@EnableScheduling
public class SchedulingConfig {
  
  private final LostItemCollectionService lostItemService;
  private final FoundItemCollectionService foundItemService;
  private final Logger logger = LoggerFactory.getLogger(SchedulingConfig.class);
  
  /*
   * 생성자
   *
   * @param lostItemService  분실물 수집 서비스
   * @param foundItemService 습득물 수집 서비스
   */
  public SchedulingConfig(LostItemCollectionService lostItemService,
    FoundItemCollectionService foundItemService) {
    this.lostItemService = lostItemService;
    this.foundItemService = foundItemService;
  }
  
  /**
   * 매일 0,12시에 분실물/습득물 데이터를 중복 없이 저장 조회 범위: (오늘-7일)~오늘
   */
  @EventListener(ApplicationReadyEvent.class)
  @Scheduled(cron = "0 0 0,12 * * *")
  public void collectLostAndFoundItems() {
    if (!lostItemService.isCollectionEnabled() || !foundItemService.isCollectionEnabled()) {
      logger.info("Police API 비활성화 상태로 스케줄 데이터 수집을 건너뜁니다.");
      return;
    }
    logger.info("[스케줄] 분실물/습득물 데이터 수집 시작");
    try {
      List<LostItem> lostSaved = lostItemService.collectAndSaveUniqueItems();
      List<FoundItem> foundSaved = foundItemService.collectAndSaveUniqueItems();
      logger.info("[스케줄] 저장 완료: 분실물 {}건, 습득물 {}건", lostSaved.size(), foundSaved.size());
    } catch (Exception e) {
      logger.error("[스케줄] 분실물/습득물 데이터 수집 중 오류: {}", e.getMessage(), e);
    }
  }
}
