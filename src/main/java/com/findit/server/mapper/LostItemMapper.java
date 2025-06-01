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
        
        // PoliceApiLostItem의 lstGoodsSn (분실물순번)을 LostItem의 atcId (기본키)로 매핑합니다.
        lostItem.setAtcId(source.getLstGoodsSn());
        
        // PoliceApiLostItem의 prdtClNm (물품분류)을 LostItem의 prdtClNm으로 매핑합니다.
        lostItem.setPrdtClNm(source.getItemType());
        
        // PoliceApiLostItem의 lstPlace (분실장소)을 LostItem의 lstPlace으로 매핑합니다.
        lostItem.setLstPlace(source.getLocation());
        
        // 분실일자 문자열(yyyyMMdd)만 잘라서 저장
        String rawDate = source.getLostDate();
        if (rawDate != null && !rawDate.trim().isEmpty()) {
            String trimmed = rawDate.trim();
            lostItem.setLstYmd(trimmed.length() >= 8 ? trimmed.substring(0, 8) : trimmed);
        } else {
            lostItem.setLstYmd(null);
        }
        
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
}
