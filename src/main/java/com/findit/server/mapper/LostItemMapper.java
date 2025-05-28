package com.findit.server.mapper;

import com.findit.server.dto.external.PoliceApiLostItem;
import com.findit.server.entity.LostItem;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * 경찰청 API 분실물 데이터를 내부 엔티티로 매핑하는 매퍼
 */
@Component
public class LostItemMapper implements ApiMapper<PoliceApiLostItem, LostItem> {
    
    // PoliceApiLostItem DTO의 lostDate 필드는 "yyyyMMdd" 형식의 문자열입니다.
    private static final DateTimeFormatter API_DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd");
    
    @Override
    public LostItem map(PoliceApiLostItem source) {
        if (source == null) {
            return null;
        }
        
        LostItem lostItem = new LostItem();
        
        // PoliceApiLostItem의 lstGoodsSn (분실물순번)을 LostItem의 atcId (기본키)로 매핑합니다.
        lostItem.setAtcId(source.getLstGoodsSn());
        
        // PoliceApiLostItem의 sltPrdtNm (분실물명)을 LostItem의 sltPrdtNm으로 매핑합니다.
        lostItem.setSltPrdtNm(source.getItemName()); 
        
        // PoliceApiLostItem의 prdtClNm (물품분류)을 LostItem의 prdtClNm으로 매핑합니다.
        lostItem.setPrdtClNm(source.getItemType());
        
        // PoliceApiLostItem의 lstPlace (분실장소)을 LostItem의 lstPlace으로 매핑합니다.
        lostItem.setLstPlace(source.getLocation());
        
        // PoliceApiLostItem의 lstYmd (분실일자, YYYYMMDD 문자열)를 LostItem의 lstYmd (LocalDateTime)로 변환하여 매핑합니다.
        lostItem.setLstYmd(parseDate(source.getLostDate()));
        
        // PoliceApiLostItem의 sltSbjt (내용)을 LostItem의 sltSbjt으로 매핑합니다.
        lostItem.setSltSbjt(source.getDescription());

        // LostItem 엔티티에 없는 필드들은 주석 처리합니다.
        // lostItem.setImageUrl(source.getImageUrl()); // LostItem 엔티티에 imageUrl 필드 없음 (PoliceApiLostItem에는 fdFilePathImg로 존재)
        // lostItem.setColor(source.getColor()); // LostItem 엔티티에 color 필드 없음 (PoliceApiLostItem에는 clrNm으로 존재)
        // lostItem.setContactInfo(source.getContactInfo()); // LostItem 엔티티에 contactInfo 필드 없음 (PoliceApiLostItem에는 orgNm으로 존재)
        // lostItem.setTelephone(source.getTelephone()); // LostItem 엔티티에 telephone 필드 없음 (PoliceApiLostItem에는 tel로 존재)
        // lostItem.setStatus(source.getStatus()); // LostItem 엔티티에 status 필드 없음 (PoliceApiLostItem에는 csteSteNm으로 존재)

        // LostItem 엔티티의 rnum 필드는 PoliceApiLostItem DTO에서 직접 매핑할 필드가 현재 없습니다.
        // 필요시 API 응답을 확인하여 추가 매핑이 필요할 수 있습니다.
        lostItem.setRnum(source.getRnum());

        return lostItem;
    }
    
    /**
     * 문자열 날짜(yyyyMMdd)를 LocalDateTime으로 변환합니다.
     * API 응답의 날짜는 시간 정보가 없으므로 자정(00:00:00)으로 설정합니다.
     * 
     * @param dateStrYyyyMmDd 날짜 문자열 (yyyyMMdd 형식)
     * @return 변환된 LocalDateTime 객체 (자정), 변환 실패 또는 입력값이 null이거나 비어있으면 null 반환
     */
    private LocalDateTime parseDate(String dateStrYyyyMmDd) {
        if (dateStrYyyyMmDd == null || dateStrYyyyMmDd.trim().isEmpty()) {
            return null;
        }
        try {
            LocalDate date = LocalDate.parse(dateStrYyyyMmDd.trim(), API_DATE_FORMATTER);
            return date.atStartOfDay(); // LocalDate를 LocalDateTime 자정으로 변환
        } catch (DateTimeParseException e) {
            // 실제 운영 환경에서는 로깅 프레임워크(예: SLF4J) 사용을 권장합니다.
            System.err.println("LostItemMapper: 날짜 문자열 파싱 오류 '" + dateStrYyyyMmDd + "': " + e.getMessage());
            return null;
        }
    }
}
