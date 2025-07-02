package com.findit.server.mapper;

import com.findit.server.dto.external.PoliceApiFoundItem;
import com.findit.server.entity.FoundItem;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 경찰청 API 습득물 데이터를 내부 엔티티로 매핑하는 매퍼
 */
@Component
public class FoundItemMapper implements ApiMapper<PoliceApiFoundItem, FoundItem> {
  
  private static final Logger logger = LoggerFactory.getLogger(FoundItemMapper.class);
  
  // PoliceApiFoundItem.foundDate is "yyyyMMdd"
  private static final DateTimeFormatter API_DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd");
  private static final DateTimeFormatter TARGET_DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
  
  // 다양한 날짜 형식을 인식하기 위한 패턴들
  private static final Pattern YYYY_MM_DD_PATTERN = Pattern.compile("(\\d{4})[-./](\\d{1,2})[-./](\\d{1,2})");
  private static final Pattern YYYY_MM_PATTERN = Pattern.compile("(\\d{4})[-./](\\d{1,2})[-]?");
  private static final Pattern YYYYMMDD_PATTERN = Pattern.compile("(\\d{4})(\\d{2})(\\d{2})");
  private static final Pattern YYYYMM_PATTERN = Pattern.compile("(\\d{4})(\\d{2})");
  
  @Override
  public FoundItem map(PoliceApiFoundItem source) {
    if (source == null) {
      return null;
    }
    
    FoundItem foundItem = new FoundItem();
    
    // PoliceApiFoundItem의 atcId를 FoundItem의 atcId (기본 키)에 매핑
    foundItem.setAtcId(source.getAtcId());
    // PoliceApiFoundItem의 fdSn을 FoundItem의 fdSn 필드에 매핑
    foundItem.setFdSn(source.getFdSn());
    
    foundItem.setFdPrdtNm(source.getFdPrdtNm());
    foundItem.setPrdtClNm(source.getPrdtClNm()); // This is the raw string like "가방 > 백팩"
    foundItem.setDepPlace(source.getDepPlace());
    
    // 개선된 날짜 처리 로직
    String rawDate = source.getFdYmd();
    foundItem.setFdYmd(normalizeDateString(rawDate));
    
    foundItem.setFdFilePathImg(source.getFdFilePathImg());
    foundItem.setFdSbjt(source.getFdSbjt());
    foundItem.setClrNm(source.getClrNm());
    
    return foundItem;
  }
  
  /**
   * 다양한 형식의 날짜 문자열을 표준 형식(yyyy-MM-dd)으로 정규화
   * 
   * @param dateStr 처리할 날짜 문자열
   * @return 정규화된 날짜 문자열 (yyyy-MM-dd 형식), 변환 실패 시 원본 문자열 또는 null 반환
   */
  private String normalizeDateString(String dateStr) {
    if (dateStr == null || dateStr.trim().isEmpty()) {
      return null;
    }
    
    String trimmed = dateStr.trim();
    LocalDate date = null;
    
    try {
      // 1. "yyyy-MM-dd" 또는 "yyyy.MM.dd" 또는 "yyyy/MM/dd" 형식 처리
      Matcher ymdMatcher = YYYY_MM_DD_PATTERN.matcher(trimmed);
      if (ymdMatcher.matches()) {
        int year = Integer.parseInt(ymdMatcher.group(1));
        int month = Integer.parseInt(ymdMatcher.group(2));
        int day = Integer.parseInt(ymdMatcher.group(3));
        
        if (isValidDate(year, month, day)) {
          date = LocalDate.of(year, month, day);
        }
      } 
      // 2. "yyyy-MM-" 또는 "yyyy.MM" 형식 처리 (일자 누락)
      else if (YYYY_MM_PATTERN.matcher(trimmed).matches()) {
        Matcher ymMatcher = YYYY_MM_PATTERN.matcher(trimmed);
        if (ymMatcher.find()) {
          int year = Integer.parseInt(ymMatcher.group(1));
          int month = Integer.parseInt(ymMatcher.group(2));
          // 현재 날짜의 일자 사용
          int day = LocalDate.now().getDayOfMonth();
          
          if (isValidDate(year, month, day)) {
            date = LocalDate.of(year, month, day);
            logger.info("일자가 누락된 날짜를 수정: {} → {}", trimmed, date.format(TARGET_DATE_FORMATTER));
          }
        }
      }
      // 3. "yyyyMMdd" 형식 처리
      else if (YYYYMMDD_PATTERN.matcher(trimmed).matches()) {
        Matcher compactMatcher = YYYYMMDD_PATTERN.matcher(trimmed);
        if (compactMatcher.find()) {
          int year = Integer.parseInt(compactMatcher.group(1));
          int month = Integer.parseInt(compactMatcher.group(2));
          int day = Integer.parseInt(compactMatcher.group(3));
          
          if (isValidDate(year, month, day)) {
            date = LocalDate.of(year, month, day);
          }
        }
      }
      // 4. "yyyyMM" 형식 처리
      else if (YYYYMM_PATTERN.matcher(trimmed).matches()) {
        Matcher compactYmMatcher = YYYYMM_PATTERN.matcher(trimmed);
        if (compactYmMatcher.find()) {
          int year = Integer.parseInt(compactYmMatcher.group(1));
          int month = Integer.parseInt(compactYmMatcher.group(2));
          // 현재 날짜의 일자 사용
          int day = LocalDate.now().getDayOfMonth();
          
          if (isValidDate(year, month, day)) {
            date = LocalDate.of(year, month, day);
            logger.info("일자가 누락된 날짜를 수정: {} → {}", trimmed, date.format(TARGET_DATE_FORMATTER));
          }
        }
      }
      // 5. 기타 형식은 원본 그대로 반환
      else {
        logger.warn("인식할 수 없는 날짜 형식: {}", trimmed);
        return trimmed.length() > 10 ? trimmed.substring(0, 10) : trimmed;
      }
      
      // 변환된 날짜가 있으면 표준 형식으로 반환
      if (date != null) {
        return date.format(TARGET_DATE_FORMATTER);
      } else {
        logger.warn("유효하지 않은 날짜 값: {}", trimmed);
        return trimmed.length() > 10 ? trimmed.substring(0, 10) : trimmed;
      }
      
    } catch (Exception e) {
      logger.error("날짜 정규화 중 오류 발생: {} - {}", trimmed, e.getMessage());
      // 오류 발생 시 원본 값 반환 (길이 제한)
      return trimmed.length() > 10 ? trimmed.substring(0, 10) : trimmed;
    }
  }
  
  /**
   * 날짜 값이 유효한지 검사
   * 
   * @param year 연도
   * @param month 월
   * @param day 일
   * @return 유효한 날짜인지 여부
   */
  private boolean isValidDate(int year, int month, int day) {
    if (year < 1900 || year > 2100) return false;
    if (month < 1 || month > 12) return false;
    
    try {
      LocalDate.of(year, month, day);
      return true;
    } catch (Exception e) {
      return false;
    }
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
      logger.error("Error parsing date string for FoundItem: {} - {}", dateStr, e.getMessage());
      return null; // Or throw an exception, or return a default
    }
  }
}
