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
        
        // PoliceApiLostItem의 atc_id를 LostItem의 atcId (기본키)로 매핑합니다.
        lostItem.setAtcId(source.getLostItemId());
        
        // PoliceApiLostItem의 lst_prdt_nm (물품분류)을 LostItem의 prdtClNm으로 매핑합니다.
        lostItem.setPrdtClNm(source.getLostItemCategory());
        
        // PoliceApiLostItem의 lst_place (분실장소)을 LostItem의 lstPlace으로 매핑합니다.
        lostItem.setLstPlace(source.getLostPlace());
        
        // PoliceApiLostItem의 lst_prdt_nm (분실물명)을 LostItem의 lstPrdtNm으로 매핑합니다.
        lostItem.setLstPrdtNm(source.getLostItemName());
        
        // PoliceApiLostItem의 lst_sbjt (내용)을 LostItem의 lstSbjt으로 매핑합니다.
        lostItem.setLstSbjt(source.getLostItemDescription());
        
        // 분실일자 문자열(yyyy-MM-dd) 형식 그대로 저장
        String rawDate = source.getLostDate();
        if (rawDate != null && !rawDate.trim().isEmpty()) {
            String trimmed = rawDate.trim();
            lostItem.setLstYmd(trimmed.length() >= 10 ? trimmed.substring(0, 10) : trimmed);
        } else {
            lostItem.setLstYmd(null);
        }
        
        // LostItem 엔티티에 없는 필드들은 주석 처리합니다.
        // lostItem.setImageUrl(source.getLostItemImageUrl()); // LostItem 엔티티에 imageUrl 필드 없음 (PoliceApiLostItem에는 fdFilePathImg로 존재)
        // lostItem.setColor(source.getLostItemColor()); // LostItem 엔티티에 color 필드 없음 (PoliceApiLostItem에는 clrNm으로 존재)
        // lostItem.setContactInfo(source.getLostItemContactInfo()); // LostItem 엔티티에 contactInfo 필드 없음 (PoliceApiLostItem에는 orgNm으로 존재)
        // lostItem.setTelephone(source.getLostItemTelephone()); // LostItem 엔티티에 telephone 필드 없음 (PoliceApiLostItem에는 tel로 존재)
        // lostItem.setStatus(source.getStatus()); // LostItem 엔티티에 status 필드 없음 (PoliceApiLostItem에는 csteSteNm으로 존재)

        // PoliceApiLostItem의 rnum (행번호)을 LostItem의 rnum으로 매핑합니다.
        lostItem.setRnum(source.getLostItemRnum());

        return lostItem;
    }
}
