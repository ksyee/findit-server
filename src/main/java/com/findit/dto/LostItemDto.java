package com.findit.dto;

import com.findit.domain.LostItem;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class LostItemDto {

    private String atcId;
    private String clrNm;
    private String depPlace;
    private String fdFilePathImg;
    private String fdPrdtNm;
    private String fdSbjt;
    private int fdSn;
    private String fdYmd;
    private String prdtClNm;
    private int rnum; // API 응답에 포함된 행 번호

    public LostItem toEntity() {
        LostItem item = new LostItem();
        item.setAtcId(atcId);
        item.setClrNm(clrNm);
        item.setDepPlace(depPlace);
        item.setFdFilePathImg(fdFilePathImg);
        item.setFdPrdtNm(fdPrdtNm);
        item.setFdSbjt(fdSbjt);
        item.setFdSn(fdSn);
        item.setFdYmd(fdYmd);
        item.setPrdtClNm(prdtClNm);
        return item;
    }
}