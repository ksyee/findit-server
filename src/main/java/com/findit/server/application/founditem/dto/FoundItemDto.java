package com.findit.server.application.founditem.dto;

import com.findit.server.domain.founditem.FoundItem;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "습득물 정보 DTO")
public class FoundItemDto {
    @Schema(description = "습득물 관리 ID (경찰청 ATC_ID)", example = "F2024052567890")
    private String atcId;

    @Schema(description = "습득물품명", example = "갈색 가죽 지갑", required = true)
    @NotBlank(message = "습득물품명은 필수입니다")
    @Size(max = 100, message = "습득물품명은 100자를 초과할 수 없습니다")
    private String fdPrdtNm;

    @Schema(description = "물품분류명", example = "지갑", required = true)
    @NotBlank(message = "물품분류명은 필수입니다")
    @Size(max = 50, message = "물품분류명은 50자를 초과할 수 없습니다")
    private String prdtClNm;

    @Schema(description = "보관장소", example = "서울 지하철 2호선 시청역", required = true)
    @NotBlank(message = "보관장소는 필수입니다")
    @Size(max = 200, message = "보관장소는 200자를 초과할 수 없습니다")
    private String depPlace;

    @Schema(description = "습득일자 (YYYY-MM-DD 형식 또는 YYYYMMDD)", example = "2025-05-20")
    @Size(max = 10, message = "습득일자는 10자를 초과할 수 없습니다")
    private String fdYmd;

    @Schema(description = "습득물제목", example = "주인 찾아주세요, 갈색 지갑 습득")
    @Size(max = 1000, message = "습득물제목은 1000자를 초과할 수 없습니다")
    private String fdSbjt;

    @Schema(description = "색상", example = "갈색")
    @Size(max = 50, message = "색상은 50자를 초과할 수 없습니다")
    private String clrNm;

    @Schema(description = "습득순번 (경찰청 API)", example = "1")
    private String fdSn;

    @Schema(description = "이미지 경로", example = "https://example.com/image.jpg")
    @Size(max = 500, message = "이미지 경로는 500자를 초과할 수 없습니다")
    private String fdFilePathImg;

    public FoundItemDto() {
    }

    public FoundItemDto(String atcId, String fdPrdtNm, String prdtClNm, String depPlace, String fdYmd, String fdSbjt, String clrNm, String fdSn) {
        this.atcId = atcId;
        this.fdPrdtNm = fdPrdtNm;
        this.prdtClNm = prdtClNm;
        this.depPlace = depPlace;
        this.fdYmd = fdYmd;
        this.fdSbjt = fdSbjt;
        this.clrNm = clrNm;
        this.fdSn = fdSn;
    }

    public FoundItemDto(String atcId, String fdPrdtNm, String prdtClNm, String depPlace, String fdYmd, String fdSbjt, String clrNm, String fdSn, String fdFilePathImg) {
        this.atcId = atcId;
        this.fdPrdtNm = fdPrdtNm;
        this.prdtClNm = prdtClNm;
        this.depPlace = depPlace;
        this.fdYmd = fdYmd;
        this.fdSbjt = fdSbjt;
        this.clrNm = clrNm;
        this.fdSn = fdSn;
        this.fdFilePathImg = fdFilePathImg;
    }

    public static FoundItemDto fromEntity(FoundItem foundItem) {
        if (foundItem == null) {
            return null;
        }
        return FoundItemDto.builder()
                .atcId(foundItem.getAtcId())
                .fdPrdtNm(foundItem.getFdPrdtNm())
                .prdtClNm(foundItem.getPrdtClNm())
                .depPlace(foundItem.getDepPlace())
                .fdYmd(foundItem.getFdYmd())
                .fdSbjt(foundItem.getFdSbjt())
                .fdFilePathImg(foundItem.getFdFilePathImg())
                .clrNm(foundItem.getClrNm())
                .fdSn(foundItem.getFdSn())
                .build();
    }

    public String getAtcId() {
        return atcId;
    }

    public String getFdPrdtNm() {
        return fdPrdtNm;
    }

    public String getPrdtClNm() {
        return prdtClNm;
    }

    public String getDepPlace() {
        return depPlace;
    }

    public String getFdYmd() {
        return fdYmd;
    }

    public String getFdSbjt() {
        return fdSbjt;
    }

    public String getClrNm() {
        return clrNm;
    }

    public String getFdSn() {
        return fdSn;
    }

    public String getFdFilePathImg() {
        return fdFilePathImg;
    }

    public void setAtcId(String atcId) {
        this.atcId = atcId;
    }

    public void setFdPrdtNm(String fdPrdtNm) {
        this.fdPrdtNm = fdPrdtNm;
    }

    public void setPrdtClNm(String prdtClNm) {
        this.prdtClNm = prdtClNm;
    }

    public void setDepPlace(String depPlace) {
        this.depPlace = depPlace;
    }

    public void setFdYmd(String fdYmd) {
        this.fdYmd = fdYmd;
    }

    public void setFdSbjt(String fdSbjt) {
        this.fdSbjt = fdSbjt;
    }

    public void setClrNm(String clrNm) {
        this.clrNm = clrNm;
    }

    public void setFdSn(String fdSn) {
        this.fdSn = fdSn;
    }

    public void setFdFilePathImg(String fdFilePathImg) {
        this.fdFilePathImg = fdFilePathImg;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String atcId;
        private String fdPrdtNm;
        private String prdtClNm;
        private String depPlace;
        private String fdYmd;
        private String fdSbjt;
        private String clrNm;
        private String fdSn;
        private String fdFilePathImg;

        public Builder atcId(String atcId) {
            this.atcId = atcId;
            return this;
        }

        public Builder fdPrdtNm(String fdPrdtNm) {
            this.fdPrdtNm = fdPrdtNm;
            return this;
        }

        public Builder prdtClNm(String prdtClNm) {
            this.prdtClNm = prdtClNm;
            return this;
        }

        public Builder depPlace(String depPlace) {
            this.depPlace = depPlace;
            return this;
        }

        public Builder fdYmd(String fdYmd) {
            this.fdYmd = fdYmd;
            return this;
        }

        public Builder fdSbjt(String fdSbjt) {
            this.fdSbjt = fdSbjt;
            return this;
        }

        public Builder clrNm(String clrNm) {
            this.clrNm = clrNm;
            return this;
        }

        public Builder fdSn(String fdSn) {
            this.fdSn = fdSn;
            return this;
        }

        public Builder fdFilePathImg(String fdFilePathImg) {
            this.fdFilePathImg = fdFilePathImg;
            return this;
        }

        public FoundItemDto build() {
            FoundItemDto dto = new FoundItemDto();
            dto.setAtcId(this.atcId);
            dto.setFdPrdtNm(this.fdPrdtNm);
            dto.setPrdtClNm(this.prdtClNm);
            dto.setDepPlace(this.depPlace);
            dto.setFdYmd(this.fdYmd);
            dto.setFdSbjt(this.fdSbjt);
            dto.setClrNm(this.clrNm);
            dto.setFdSn(this.fdSn);
            dto.setFdFilePathImg(this.fdFilePathImg);
            return dto;
        }
    }
}
