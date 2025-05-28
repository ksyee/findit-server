package com.findit.server.mapper;

import com.findit.server.entity.FoundItem;
import com.findit.server.entity.LostItem;
import org.springframework.stereotype.Component;

/**
 * ub370uc774ud130 uc720ud6a8uc131 uac80uc99d uc720ud2f8ub9acud2f0
 */
@Component
public class DataValidator {
    
    /**
     * ubd84uc2e4ubb3c uc5d4ud2f0ud2f0uc758 uc720ud6a8uc131 uac80uc99d
     * 
     * @param item ubd84uc2e4ubb3c uc5d4ud2f0ud2f0
     * @return uc720ud6a8uc131 uac80uc99d uacb0uacfc
     */
    public boolean isValidLostItem(LostItem item) {
        return item != null &&
               item.getAtcId() != null &&
               item.getPrdtClNm() != null &&
               item.getLstYmd() != null;
    }
    
    /**
     * uc2b5ub4ddubb3c uc5d4ud2f0ud2f0uc758 uc720ud6a8uc131 uac80uc99d
     * 
     * @param item uc2b5ub4ddubb3c uc5d4ud2f0ud2f0
     * @return uc720ud6a8uc131 uac80uc99d uacb0uacfc
     */
    public boolean isValidFoundItem(FoundItem item) {
        return item != null &&
               item.getAtcId() != null &&
               item.getPrdtClNm() != null &&
               item.getFdYmd() != null;
    }
    
    /**
     * ubd84uc2e4ubb3c uc5d4ud2f0ud2f0uc758 uc0c1uc138 uc720ud6a8uc131 uac80uc99d
     * 
     * @param item ubd84uc2e4ubb3c uc5d4ud2f0ud2f0
     * @return uc720ud6a8uc131 uac80uc99d uacb0uacfc
     */
    public boolean isDetailedValidLostItem(LostItem item) {
        return isValidLostItem(item) &&
               item.getLstPlace() != null &&
               item.getSltSbjt() != null;
               // item.getStatus() != null; // LostItem 엔티티에 status 필드 없음
    }
    
    /**
     * uc2b5ub4ddubb3c uc5d4ud2f0ud2f0uc758 uc0c1uc138 uc720ud6a8uc131 uac80uc99d
     * 
     * @param item uc2b5ub4ddubb3c uc5d4ud2f0ud2f0
     * @return uc720ud6a8uc131 uac80uc99d uacb0uacfc
     */
    public boolean isDetailedValidFoundItem(FoundItem item) {
        return isValidFoundItem(item) &&
               item.getFdSbjt() != null &&
               item.getDepPlace() != null;
    }
}
