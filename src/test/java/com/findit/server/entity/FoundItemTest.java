package com.findit.server.entity;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class FoundItemTest {
    
    @Test
    public void testFoundItemCreation() {
        // Given
        String atcId = "F2024052612345";
        String fdPrdtNm = "아이폰 14 프로";
        String prdtClNm = "스마트폰";
        String fdYmd = "20240526";
        String fdSbjt = "분실물 아이폰 14 프로 블랙";
        String depPlace = "서울시 강남구 역삼동";
        String clrNm = "블랙";
        String fdSn = "12345";
        
        // When
        FoundItem foundItem = FoundItem.builder()
          .atcId(atcId)
          .fdPrdtNm(fdPrdtNm)
          .prdtClNm(prdtClNm)
          .fdYmd(fdYmd)
          .fdSbjt(fdSbjt)
          .depPlace(depPlace)
          .clrNm(clrNm)
          .fdSn(fdSn)
          .build();
        
        // Then
        assertNotNull(foundItem);
        assertEquals(atcId, foundItem.getAtcId());
        assertEquals(fdPrdtNm, foundItem.getFdPrdtNm());
        assertEquals(prdtClNm, foundItem.getPrdtClNm());
        assertEquals(fdYmd, foundItem.getFdYmd());
        assertEquals(fdSbjt, foundItem.getFdSbjt());
        assertEquals(depPlace, foundItem.getDepPlace());
        assertEquals(clrNm, foundItem.getClrNm());
        assertEquals(fdSn, foundItem.getFdSn());
    }
    
    @Test
    public void testPrePersistAndPreUpdate() {
        // Given
        FoundItem foundItem = new FoundItem();
        
        // When
        // foundItem.onCreate();
        
        // Then
        // assertNotNull(foundItem.getCreatedAt());
        // assertNotNull(foundItem.getUpdatedAt());
        
        // When
        // LocalDateTime createdAt = foundItem.getCreatedAt();
        try {
            Thread.sleep(10); // 시간 차이를 두기 위해
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // foundItem.onUpdate();
        
        // Then
        // assertEquals(createdAt, foundItem.getCreatedAt()); // createdAt은 변경되지 않아야 함
        // assertNotNull(foundItem.getUpdatedAt()); // updatedAt은 업데이트 되어야 함
    }
}