package com.findit.server.domain.lostitem;

import com.findit.server.domain.shared.ItemCategory;
import com.findit.server.domain.shared.LocationName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class LostItemTest {

    @Test
    void createLostItem() {
        LostItem lostItem = LostItem.create(
            LostItemId.of("L20240526001"),
            ItemCategory.of("전자기기"),
            LocationName.of("서울시 강남구 역삼동"),
            LostDate.of("20240526"),
            "노트북",
            "맥북 프로",
            "1"
        );

        assertEquals("L20240526001", lostItem.getAtcId());
        assertEquals("전자기기", lostItem.getPrdtClNm());
        assertEquals("서울시 강남구 역삼동", lostItem.getLstPlace());
        assertEquals("20240526", lostItem.getLstYmd());
    }

    @Test
    void updateCoreDetails() {
        LostItem lostItem = LostItem.create(
            LostItemId.of("L20240526001"),
            ItemCategory.of("전자기기"),
            LocationName.of("서울시 강남구 역삼동"),
            LostDate.of("20240526"),
            null,
            null,
            "1"
        );

        lostItem.updateCoreDetails(
            ItemCategory.of("가방"),
            LocationName.of("서울시 서초구"),
            LostDate.of("20240527"),
            "2"
        );

        assertEquals("가방", lostItem.getPrdtClNm());
        assertEquals("서울시 서초구", lostItem.getLstPlace());
        assertEquals("20240527", lostItem.getLstYmd());
        assertEquals("2", lostItem.getRnum());
    }
}
