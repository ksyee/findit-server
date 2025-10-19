package com.findit.server.application.lostitem.dto;

import com.findit.server.domain.lostitem.LostItem;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "분실물 정보 DTO")
public record LostItemDto(
    @Schema(description = "분실물 관리 ID (경찰청 ATC_ID)", example = "L2024052512345")
    String atcId,

    @Schema(description = "물품분류", example = "휴대폰", required = true)
    @NotBlank(message = "물품분류는 필수입니다")
    @Size(max = 50, message = "물품분류는 50자를 초과할 수 없습니다")
    String prdtClNm,

    @Schema(description = "분실장소", example = "서울시 강남구 스타벅스", required = true)
    @NotBlank(message = "분실장소는 필수입니다")
    @Size(max = 200, message = "분실장소는 200자를 초과할 수 없습니다")
    String lstPlace,

    @Schema(description = "분실일자", example = "2025-05-20", required = true)
    @NotBlank(message = "분실일자는 필수입니다")
    String lstYmd,

    @Schema(description = "경찰청 API 결과 순번", example = "1")
    String rnum
) {

    public static LostItemDto fromEntity(LostItem lostItem) {
        if (lostItem == null) {
            return null;
        }
        return new LostItemDto(
            lostItem.getAtcId(),
            lostItem.getPrdtClNm(),
            lostItem.getLstPlace(),
            lostItem.getLstYmd(),
            lostItem.getRnum()
        );
    }
}
