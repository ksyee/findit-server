package com.findit.server.mapper;

import com.findit.server.dto.external.PoliceApiFoundItem;
import com.findit.server.entity.FoundItem;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * 경찰청 API 습득물 데이터를 내부 엔티티로 매핑하는 매퍼
 */
@Component
public class FoundItemMapper implements ApiMapper<PoliceApiFoundItem, FoundItem> {
  
  // PoliceApiFoundItem.foundDate is "yyyyMMdd"
  private static final DateTimeFormatter API_DATE_FORMATTER = DateTimeFormatter.ofPattern(
    "yyyyMMdd");
  
  @Override
  public FoundItem map(PoliceApiFoundItem source) {
    if (source == null) {
      return null;
    }
    
    FoundItem foundItem = new FoundItem();
    
    // PoliceApiFoundItem의 id (atcId에 해당)를 FoundItem의 atcId (기본 키)에 매핑
    foundItem.setAtcId(source.getId());
    // PoliceApiFoundItem의 fdSn을 FoundItem의 fdSn 필드에 매핑
    foundItem.setFdSn(source.getFdSn());
    
    foundItem.setFdPrdtNm(source.getItemName());
    foundItem.setPrdtClNm(source.getItemType()); // This is the raw string like "가방 > 백팩"
    foundItem.setDepPlace(source.getStoragePlaceName());
    
    // 저장할 날짜 부분만 추출 (yyyyMMdd)
    String rawDate = source.getFoundDate();
    if (rawDate != null && !rawDate.trim().isEmpty()) {
      String trimmed = rawDate.trim();
      foundItem.setFdYmd(trimmed.length() >= 8 ? trimmed.substring(0, 8) : trimmed);
    } else {
      foundItem.setFdYmd(null);
    }
    
    foundItem.setFdFilePathImg(source.getImageUrl());
    foundItem.setFdSbjt(source.getDescription());
    foundItem.setClrNm(source.getColor());
    
    return foundItem;
  }
  
  /**
   * 문자열 날짜(yyyyMMdd)를 LocalDateTime으로 변환
   *
   * @param dateStr 날짜 문자열 (yyyyMMdd 형식)
   * @return 변환된 LocalDateTime 객체 (자정), 변환 실패 시 null 반환
   */
  private LocalDateTime parseDate(String dateStr) {
    if (dateStr == null || dateStr.isEmpty()) {
      return null;
    }
    try {
      LocalDate date = LocalDate.parse(dateStr.trim(), API_DATE_FORMATTER);
      return date.atStartOfDay(); // 날짜만 있으므로 자정으로 설정
    } catch (DateTimeParseException e) {
      // Log this error or handle it as per application's error strategy
      System.err.println(
        "Error parsing date string for FoundItem: " + dateStr + " - " + e.getMessage());
      return null; // Or throw an exception, or return a default
    }
  }
}
