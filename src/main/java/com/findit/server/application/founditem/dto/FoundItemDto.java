package com.findit.server.application.founditem.dto;

import com.findit.server.domain.founditem.FoundItem;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "습득물 정보 DTO")
public record FoundItemDto(
    @Schema(description = "습득물 관리 ID (경찰청 ATC_ID)", example = "F2024052567890")
    String atcId,

    @Schema(description = "습득물품명", example = "갈색 가죽 지갑", required = true)
    @NotBlank(message = "습득물품명은 필수입니다")
    @Size(max = 100, message = "습득물품명은 100자를 초과할 수 없습니다")
    String fdPrdtNm,

    @Schema(description = "물품분류명", example = "지갑", required = true)
    @NotBlank(message = "물품분류명은 필수입니다")
    @Size(max = 50, message = "물품분류명은 50자를 초과할 수 없습니다")
    String prdtClNm,

    @Schema(description = "보관장소", example = "서울 지하철 2호선 시청역", required = true)
    @NotBlank(message = "보관장소는 필수입니다")
    @Size(max = 200, message = "보관장소는 200자를 초과할 수 없습니다")
    String depPlace,

    @Schema(description = "습득일자 (YYYY-MM-DD 형식 또는 YYYYMMDD)", example = "2025-05-20")
    @Size(max = 10, message = "습득일자는 10자를 초과할 수 없습니다")
    String fdYmd,

    @Schema(description = "습득물제목", example = "주인 찾아주세요, 갈색 지갑 습득")
    @Size(max = 1000, message = "습득물제목은 1000자를 초과할 수 없습니다")
    String fdSbjt,

    @Schema(description = "색상", example = "갈색")
    @Size(max = 50, message = "색상은 50자를 초과할 수 없습니다")
    String clrNm,

    @Schema(description = "습득순번 (경찰청 API)", example = "1")
    String fdSn,

    @Schema(description = "이미지 경로", example = "https://example.com/image.jpg")
    @Size(max = 500, message = "이미지 경로는 500자를 초과할 수 없습니다")
    String fdFilePathImg
) {

    public static FoundItemDto fromEntity(FoundItem foundItem) {
        if (foundItem == null) {
            return null;
        }
        return new FoundItemDto(
            foundItem.getAtcId(),
            foundItem.getFdPrdtNm(),
            foundItem.getPrdtClNm(),
            foundItem.getDepPlace(),
            foundItem.getFdYmd(),
            foundItem.getFdSbjt(),
            foundItem.getClrNm(),
            foundItem.getFdSn(),
            foundItem.getFdFilePathImg()
        );
    }
}
