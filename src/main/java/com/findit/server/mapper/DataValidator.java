package com.findit.server.mapper;

import com.findit.server.entity.FoundItem;
import com.findit.server.entity.LostItem;
import org.springframework.stereotype.Component;

/**
 * 데이터 유효성 검증 유틸리티
 */
@Component
public class DataValidator {
    
    /**
     * 분실물 엔티티의 유효성 검증
     * 
     * @param item 분실물 엔티티
     * @return 유효성 검증 결과
     */
    public boolean isValidLostItem(LostItem item) {
        return item != null &&
               item.getAtcId() != null &&
               item.getPrdtClNm() != null &&
               item.getLstYmd() != null;
    }
    
    /**
     * 습득물 엔티티의 유효성 검증
     * 
     * @param item 습득물 엔티티
     * @return 유효성 검증 결과
     */
    public boolean isValidFoundItem(FoundItem item) {
        return item != null &&
               item.getAtcId() != null &&
               item.getPrdtClNm() != null &&
               item.getFdYmd() != null;
    }
    
    /**
     * 분실물 엔티티의 상세 유효성 검증
     * 
     * @param item 분실물 엔티티
     * @return 유효성 검증 결과
     */
    public boolean isDetailedValidLostItem(LostItem item) {
        return isValidLostItem(item) &&
               item.getLstPlace() != null;
               // item.getStatus() != null; // LostItem 엔티티에 status 필드 없음
    }
    
    /**
     * 습득물 엔티티의 상세 유효성 검증
     * 
     * @param item 습득물 엔티티
     * @return 유효성 검증 결과
     */
    public boolean isDetailedValidFoundItem(FoundItem item) {
        return isValidFoundItem(item) &&
               item.getFdSbjt() != null &&
               item.getDepPlace() != null;
    }
}
