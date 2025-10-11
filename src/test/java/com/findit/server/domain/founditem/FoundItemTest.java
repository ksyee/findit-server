package com.findit.server.domain.founditem;

import com.findit.server.domain.shared.ItemCategory;
import com.findit.server.domain.shared.ItemName;
import com.findit.server.domain.shared.LocationName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FoundItemTest {

    @Test
    void createFoundItem() {
        FoundItem foundItem = FoundItem.create(
            FoundItemId.of("F20240526001"),
            ItemName.of("아이폰 14 프로"),
            ItemCategory.of("전자기기"),
            FoundDate.of("20240526"),
            LocationName.of("서울시 강남구"),
            "아이폰 14 프로 블랙 분실물",
            null,
            "블랙",
            "SN-100"
        );

        assertEquals("F20240526001", foundItem.getAtcId());
        assertEquals("아이폰 14 프로", foundItem.getFdPrdtNm());
        assertEquals("전자기기", foundItem.getPrdtClNm());
        assertEquals("20240526", foundItem.getFdYmd());
        assertEquals("서울시 강남구", foundItem.getDepPlace());
    }

    @Test
    void updateCoreDetails() {
        FoundItem foundItem = FoundItem.create(
            FoundItemId.of("F20240526002"),
            ItemName.of("지갑"),
            ItemCategory.of("패션잡화"),
            FoundDate.of("20240520"),
            LocationName.of("서울시 중구"),
            "갈색 지갑 발견",
            null,
            "브라운",
            "SN-200"
        );

        foundItem.updateCoreDetails(
            ItemName.of("지갑"),
            ItemCategory.of("패션잡화"),
            FoundDate.of("20240521"),
            LocationName.of("서울시 종로구"),
            "갈색 지갑 발견 - 종로",
            "다크브라운",
            "SN-200"
        );

        assertEquals("20240521", foundItem.getFdYmd());
        assertEquals("서울시 종로구", foundItem.getDepPlace());
        assertEquals("다크브라운", foundItem.getClrNm());
        assertEquals("갈색 지갑 발견 - 종로", foundItem.getFdSbjt());
    }
}
