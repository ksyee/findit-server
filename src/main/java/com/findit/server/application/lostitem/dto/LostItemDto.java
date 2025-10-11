package com.findit.server.application.lostitem.dto;

import com.findit.server.domain.lostitem.LostItem;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "분실물 정보 DTO")
public class LostItemDto {
    @Schema(description = "분실물 관리 ID (경찰청 ATC_ID)", example = "L2024052512345")
    private String atcId;
    
    @Schema(description = "물품분류", example = "휴대폰", required = true)
    @NotBlank(message = "물품분류는 필수입니다")
    @Size(max = 50, message = "물품분류는 50자를 초과할 수 없습니다")
    private String prdtClNm;
    
    @Schema(description = "분실장소", example = "서울시 강남구 스타벅스", required = true)
    @NotBlank(message = "분실장소는 필수입니다")
    @Size(max = 200, message = "분실장소는 200자를 초과할 수 없습니다")
    private String lstPlace;
    
    @Schema(description = "분실일자", example = "2025-05-20", required = true)
    @NotBlank(message = "분실일자는 필수입니다")
    private String lstYmd;
    
    @Schema(description = "경찰청 API 결과 순번", example = "1")
    private String rnum;
    
    // 기본 생성자
    public LostItemDto() {
    }
    
    // 모든 필드를 포함한 생성자
    public LostItemDto(String atcId, String prdtClNm, String lstPlace, String lstYmd, String rnum) {
        this.atcId = atcId;
        this.prdtClNm = prdtClNm;
        this.lstPlace = lstPlace;
        this.lstYmd = lstYmd;
        this.rnum = rnum;
    }

    // LostItem 엔티티를 LostItemDto로 변환하는 정적 메서드
    public static LostItemDto fromEntity(LostItem lostItem) {
        if (lostItem == null) {
            return null;
        }
        return LostItemDto.builder()
                .atcId(lostItem.getAtcId())
                .prdtClNm(lostItem.getPrdtClNm())
                .lstPlace(lostItem.getLstPlace())
                .lstYmd(lostItem.getLstYmd())
                .rnum(lostItem.getRnum())
                .build();
    }
    
    // Getters
    public String getAtcId() {
        return atcId;
    }
    
    public String getPrdtClNm() {
        return prdtClNm;
    }
    
    public String getLstPlace() {
        return lstPlace;
    }
    
    public String getLstYmd() {
        return lstYmd;
    }
    
    public String getRnum() {
        return rnum;
    }
    
    // Setters
    public void setAtcId(String atcId) {
        this.atcId = atcId;
    }
    
    public void setPrdtClNm(String prdtClNm) {
        this.prdtClNm = prdtClNm;
    }
    
    public void setLstPlace(String lstPlace) {
        this.lstPlace = lstPlace;
    }
    
    public void setLstYmd(String lstYmd) {
        this.lstYmd = lstYmd;
    }
    
    public void setRnum(String rnum) {
        this.rnum = rnum;
    }
    
    // Builder 패턴 구현
    public static Builder builder() {
        return new Builder();
    }
    
    public static class Builder {
        private String atcId;
        private String prdtClNm;
        private String lstPlace;
        private String lstYmd;
        private String rnum;
        
        public Builder atcId(String atcId) {
            this.atcId = atcId;
            return this;
        }
        
        public Builder prdtClNm(String prdtClNm) {
            this.prdtClNm = prdtClNm;
            return this;
        }
        
        public Builder lstPlace(String lstPlace) {
            this.lstPlace = lstPlace;
            return this;
        }
        
        public Builder lstYmd(String lstYmd) {
            this.lstYmd = lstYmd;
            return this;
        }
        
        public Builder rnum(String rnum) {
            this.rnum = rnum;
            return this;
        }
        
        public LostItemDto build() {
            LostItemDto dto = new LostItemDto();
            dto.setAtcId(this.atcId);
            dto.setPrdtClNm(this.prdtClNm);
            dto.setLstPlace(this.lstPlace);
            dto.setLstYmd(this.lstYmd);
            dto.setRnum(this.rnum);
            return dto;
        }
    }
}
