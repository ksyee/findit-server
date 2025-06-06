package com.findit.server.repository;

import com.findit.server.entity.LostItem;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
public class LostItemRepositoryTest {
  
  @Autowired
  private TestEntityManager entityManager;
  
  @Autowired
  private LostItemRepository lostItemRepository;
  
  @Test
  void contextLoads() {
    // Simple test to verify that the test context loads
    assertNotNull(entityManager);
    assertNotNull(lostItemRepository);
  }
  
  @Test
  void testSaveAndFindLostItem() {
    // Given
    LostItem item = new LostItem();
    item.setAtcId("TEST123");
    item.setPrdtClNm("전자기기");
    item.setLstPlace("테스트 장소");
    // 문자열(yyyyMMdd) 형태의 날짜 저장
    String todayYmd = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
    item.setLstYmd(todayYmd);
    item.setRnum("1");
    
    // When
    LostItem savedItem = lostItemRepository.save(item);
    LostItem foundItem = entityManager.find(LostItem.class, savedItem.getAtcId());
    
    // Then
    assertNotNull(foundItem);
    assertEquals("TEST123", foundItem.getAtcId());
    assertEquals("전자기기", foundItem.getPrdtClNm());
  }
}
