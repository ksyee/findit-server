package com.findit.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter @Setter
@NoArgsConstructor
public class LostItem {

    @Id
    private String atcId; // 관리 ID (고유 식별자)

    private String clrNm;          // 색상명
    private String depPlace;       // 보관장소
    private String fdFilePathImg;  // 이미지 경로
    private String fdPrdtNm;       // 물품명
    private String fdSbjt;         // 설명
    private int fdSn;              // 습득 순번
    private String fdYmd;          // 습득일자
    private String prdtClNm;       // 물품분류명
}
