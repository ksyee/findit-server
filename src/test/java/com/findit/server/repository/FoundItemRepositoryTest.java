package com.findit.server.repository;

import com.findit.server.entity.FoundItem;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
public class FoundItemRepositoryTest {
  
  @Autowired
  private TestEntityManager entityManager;
  
  @Autowired
  private FoundItemRepository foundItemRepository;
  
  @Test
  public void testFindByPrdtClNm() { 
    // Given
    String now = "20240526";

    FoundItem item1 = FoundItem.builder()
      .atcId("F_TEST_001")
      .fdPrdtNm("아이폰 14")
      .prdtClNm("전자기기")
      .depPlace("서울시 강남구")
      .fdYmd(now)
      .fdSbjt("아이폰 14 분실물")
      .clrNm("블랙")
      .fdSn("1")
      .build();
    
    FoundItem item2 = FoundItem.builder()
      .atcId("F_TEST_002")
      .fdPrdtNm("가죽지갑 검정색")
      .prdtClNm("지갑")
      .depPlace("서울시 송파구")
      .fdYmd(now)
      .fdSbjt("검정색 가죽 지갑 분실물")
      .clrNm("검정")
      .fdSn("2")
      .build();
    
    FoundItem item3 = FoundItem.builder()
      .atcId("F_TEST_003")
      .fdPrdtNm("어쿠스틱 기타")
      .prdtClNm("전자기기")
      .depPlace("서울시 종로구")
      .fdYmd(now)
      .fdSbjt("어쿠스틱 기타 분실물")
      .clrNm("브라운")
      .fdSn("3")
      .build();
    
    entityManager.persist(item1);
    entityManager.persist(item2);
    entityManager.persist(item3);
    entityManager.flush();
    
    // When
    List<FoundItem> foundItems = foundItemRepository.findByPrdtClNm("전자기기"); 
    
    // Then
    assertEquals(2, foundItems.size());
    assertEquals("전자기기", foundItems.get(0).getPrdtClNm());
    assertEquals("전자기기", foundItems.get(1).getPrdtClNm());
  }
  
  @Test
  public void testSearchByKeyword() {
    // Given
    String now = "20240526";

    FoundItem item1 = FoundItem.builder()
      .atcId("F_TEST_007")
      .fdPrdtNm("아이폰 14 프로")
      .prdtClNm("전자기기")
      .depPlace("서울시 강남구")
      .fdYmd(now)
      .fdSbjt("아이폰 14 프로 블랙 색상, 강남 습득")
      .clrNm("블랙")
      .fdSn("7")
      .build();
    
    FoundItem item2 = FoundItem.builder()
      .atcId("F_TEST_008")
      .fdPrdtNm("가죽지갑")
      .prdtClNm("지갑")
      .depPlace("서울시 송파구")
      .fdYmd(now)
      .fdSbjt("가죽지갑 검정색, 송파 습득")
      .clrNm("검정")
      .fdSn("8")
      .build();
    
    FoundItem item3 = FoundItem.builder()
      .atcId("F_TEST_009")
      .fdPrdtNm("삼성 갤럭시 폰")
      .prdtClNm("전자기기")
      .depPlace("경기도 성남시")
      .fdYmd(now)
      .fdSbjt("삼성 갤럭시 최신폰, 성남 습득")
      .clrNm("화이트")
      .fdSn("9")
      .build();
    
    entityManager.persist(item1);
    entityManager.persist(item2);
    entityManager.persist(item3);
    entityManager.flush();
    
    // When
    List<FoundItem> foundItems = foundItemRepository.searchByKeyword("아이폰");
    
    // Then
    assertEquals(1, foundItems.size());
    assertEquals("아이폰 14 프로 블랙 색상, 강남 습득", foundItems.get(0).getFdSbjt());
  }
}
