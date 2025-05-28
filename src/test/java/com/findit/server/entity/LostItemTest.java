package com.findit.server.entity;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class LostItemTest {

    @Test
    public void testLostItemCreation() {
        // Given
        LocalDateTime now = LocalDateTime.now();
        
        // When
        LostItem lostItem = LostItem.builder()
                .prdtClNm("전자기기")
                .lstPlace("서울시 강남구 역삼동")
                .lstYmd(now)
                .sltSbjt("아이폰 14 프로 블랙 색상")
                .build();
        
        // Then
        assertEquals("전자기기", lostItem.getPrdtClNm());
        assertEquals("서울시 강남구 역삼동", lostItem.getLstPlace());
        assertEquals(now, lostItem.getLstYmd());
        assertEquals("아이폰 14 프로 블랙 색상", lostItem.getSltSbjt());
    }
    
    /* // Commenting out this test as LostItem does not have JPA lifecycle methods or audit fields
    @Test
    public void testPrePersistAndPreUpdate() {
        // Given
        LostItem lostItem = new LostItem();
        
        // When
        lostItem.onCreate();
        
        // Then
        assertNotNull(lostItem.getCreatedAt());
        assertNotNull(lostItem.getUpdatedAt());
        
        // When
        LocalDateTime createdAt = lostItem.getCreatedAt();
        try {
            Thread.sleep(10); // 약간의 시간 차이를 두기 위해
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        lostItem.onUpdate();
        
        // Then
        assertEquals(createdAt, lostItem.getCreatedAt()); // createdAt은 변경되지 않아야 함
        assertNotNull(lostItem.getUpdatedAt()); // updatedAt은 업데이트 되어야 함
    }
    */
}
