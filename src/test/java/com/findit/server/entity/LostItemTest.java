package com.findit.server.entity;

import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class LostItemTest {
  
  @Test
  public void testLostItemCreation() {
    // Given
    LocalDateTime now = LocalDateTime.now();
    // 테스트용 날짜 문자열 형식 yyyyMMdd
    String nowYmd = now.toLocalDate().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
    
    // When
    LostItem lostItem = LostItem.builder()
      .prdtClNm("전자기기")
      .lstPlace("서울시 강남구 역삼동")
      .lstYmd(nowYmd)
      .build();
    
    // Then
    assertEquals("전자기기", lostItem.getPrdtClNm());
    assertEquals("서울시 강남구 역삼동", lostItem.getLstPlace());
    assertEquals(nowYmd, lostItem.getLstYmd());
  }
}
