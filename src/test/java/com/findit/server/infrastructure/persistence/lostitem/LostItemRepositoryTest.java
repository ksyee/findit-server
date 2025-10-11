package com.findit.server.infrastructure.persistence.lostitem;

import com.findit.server.domain.lostitem.LostDate;
import com.findit.server.domain.lostitem.LostItem;
import com.findit.server.domain.lostitem.LostItemId;
import com.findit.server.domain.shared.ItemCategory;
import com.findit.server.domain.shared.LocationName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
public class LostItemRepositoryTest {
  
  @Autowired
  private TestEntityManager entityManager;
  
  @Autowired
  private LostItemJpaRepository lostItemRepository;
  
  @Test
  void contextLoads() {
    assertNotNull(entityManager);
    assertNotNull(lostItemRepository);
  }
  
  @Test
  void testSaveAndFindLostItem() {
    String todayYmd = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
    LostItem item = LostItem.create(
      LostItemId.of("TEST123"),
      ItemCategory.of("전자기기"),
      LocationName.of("테스트 장소"),
      LostDate.of(todayYmd),
      null,
      null,
      "1"
    );
    
    LostItem savedItem = lostItemRepository.save(item);
    LostItem foundItem = entityManager.find(LostItem.class, savedItem.getAtcId());
    
    assertNotNull(foundItem);
    assertEquals("TEST123", foundItem.getAtcId());
    assertEquals("전자기기", foundItem.getPrdtClNm());
  }
}
