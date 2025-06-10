package com.findit.server.mapper;

import com.findit.server.dto.external.PoliceApiLostItem;
import com.findit.server.entity.LostItem;
import org.springframework.stereotype.Component;

/**
 * 경찰청 API 분실물 데이터를 내부 엔티티로 매핑하는 매퍼
 */
@Component
public class LostItemMapper implements ApiMapper<PoliceApiLostItem, LostItem> {
    
    @Override
    public LostItem map(PoliceApiLostItem source) {
        if (source == null) {
            return null;
        }
        
        LostItem lostItem = new LostItem();
        
        // PoliceApiLostItem의 atcId (경찰청 관리 ID) 를 LostItem의 atcId (기본키)로 매핑합니다.
        lostItem.setAtcId(source.getId());
        
        // PoliceApiLostItem의 prdtClNm (물품분류)을 LostItem의 prdtClNm으로 매핑합니다.
        lostItem.setPrdtClNm(source.getItemType());
        
        // PoliceApiLostItem의 lstPlace (분실장소)을 LostItem의 lstPlace으로 매핑합니다.
        lostItem.setLstPlace(source.getLocation());
        
        // PoliceApiLostItem의 lstPrdtNm (분실물명)을 LostItem의 lstPrdtNm으로 매핑합니다.
        lostItem.setLstPrdtNm(source.getItemName());
        
        // PoliceApiLostItem의 lstSbjt (내용)을 LostItem의 lstSbjt으로 매핑합니다.
        lostItem.setLstSbjt(source.getDescription());
        
        // 분실일자 문자열(yyyy-MM-dd) 형식 그대로 저장
        String rawDate = source.getLostDate();
        if (rawDate != null && !rawDate.trim().isEmpty()) {
            String trimmed = rawDate.trim();
            lostItem.setLstYmd(trimmed.length() >= 10 ? trimmed.substring(0, 10) : trimmed);
        } else {
            lostItem.setLstYmd(null);
        }
        
        // LostItem 엔티티에 없는 필드들은 주석 처리합니다.
        // lostItem.setImageUrl(source.getImageUrl()); // LostItem 엔티티에 imageUrl 필드 없음 (PoliceApiLostItem에는 fdFilePathImg로 존재)
        // lostItem.setColor(source.getColor()); // LostItem 엔티티에 color 필드 없음 (PoliceApiLostItem에는 clrNm으로 존재)
        // lostItem.setContactInfo(source.getContactInfo()); // LostItem 엔티티에 contactInfo 필드 없음 (PoliceApiLostItem에는 orgNm으로 존재)
        // lostItem.setTelephone(source.getTelephone()); // LostItem 엔티티에 telephone 필드 없음 (PoliceApiLostItem에는 tel로 존재)
        // lostItem.setStatus(source.getStatus()); // LostItem 엔티티에 status 필드 없음 (PoliceApiLostItem에는 csteSteNm으로 존재)

        // PoliceApiLostItem의 rnum (행번호)을 LostItem의 rnum으로 매핑합니다.
        lostItem.setRnum(source.getRnum());

        // 경찰청 API DTO id, itemType, location, lostDate가 잘 매핑됩니다.

        return lostItem;
    }
}
