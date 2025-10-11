package com.findit.server.infrastructure.police.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * 경찰청 API 분실물 아이템 DTO
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "item") // Assuming each lost item is wrapped in an <item> tag in the list
public class PoliceApiLostItem {

    @JsonProperty("atcId")
    @XmlElement(name = "atcId")
    private String lostItemId; // 관리ID (경찰서ID + 년도 + 순번)

    @JsonProperty("lstPrdtNm")
    @XmlElement(name = "lstPrdtNm")
    private String lostItemName; // 분실물명

    @JsonProperty("prdtClNm")
    @XmlElement(name = "prdtClNm")
    private String lostItemCategory; // 물품분류 (e.g., "가방 > 백팩") - PoliceApiSyncService may need to parse this

    @JsonProperty("lstPlace")
    @XmlElement(name = "lstPlace")
    private String lostPlace; // 분실장소

    @JsonProperty("lstYmd")
    @XmlElement(name = "lstYmd")
    private String lostDate; // 분실일자 (YYYYMMDD)

    @JsonProperty("csteSteNm")
    @XmlElement(name = "csteSteNm")
    private String status; // 보관상태 (e.g., "보관중", "수사중", "종결")

    @JsonProperty("fdFilePathImg")
    @XmlElement(name = "fdFilePathImg")
    private String lostItemImageUrl; // 분실물 이미지 경로

    @JsonProperty("lstSbjt")
    @XmlElement(name = "lstSbjt")
    private String lostItemDescription; // 내용 (분실물 내용)

    @JsonProperty("orgNm")
    @XmlElement(name = "orgNm")
    private String lostItemContactInfo; // 보관기관 연락처 또는 기관명 (e.g., "OO경찰서 02-XXX-XXXX")

    @JsonProperty("tel")
    @XmlElement(name = "tel")
    private String lostItemTelephone; // 연락처 (기관 전화번호)

    @JsonProperty("clrNm")
    @XmlElement(name = "clrNm")
    private String lostItemColor; // 색상

    @JsonProperty("rnum")
    @XmlElement(name = "rnum")
    private String lostItemRnum; // 순번 또는 행번호

    // 기본 생성자
    public PoliceApiLostItem() {
    }

    // 모든 필드를 포함한 생성자
    public PoliceApiLostItem(String lostItemId, String lostItemName, String lostItemCategory, String lostPlace, 
                           String lostDate, String status, String lostItemImageUrl, 
                           String lostItemDescription, String lostItemContactInfo, String lostItemTelephone,
                           String lostItemColor, String lostItemRnum) {
        this.lostItemId = lostItemId;
        this.lostItemName = lostItemName;
        this.lostItemCategory = lostItemCategory;
        this.lostPlace = lostPlace;
        this.lostDate = lostDate;
        this.status = status;
        this.lostItemImageUrl = lostItemImageUrl;
        this.lostItemDescription = lostItemDescription;
        this.lostItemContactInfo = lostItemContactInfo;
        this.lostItemTelephone = lostItemTelephone;
        this.lostItemColor = lostItemColor;
        this.lostItemRnum = lostItemRnum;
    }

    // Getters and Setters
    public String getLostItemId() {
        return lostItemId;
    }

    public void setLostItemId(String lostItemId) {
        this.lostItemId = lostItemId;
    }

    public String getLostItemName() {
        return lostItemName;
    }

    public void setLostItemName(String lostItemName) {
        this.lostItemName = lostItemName;
    }

    public String getLostItemCategory() {
        return lostItemCategory;
    }

    public void setLostItemCategory(String lostItemCategory) {
        this.lostItemCategory = lostItemCategory;
    }

    public String getLostPlace() {
        return lostPlace;
    }

    public void setLostPlace(String lostPlace) {
        this.lostPlace = lostPlace;
    }

    public String getLostDate() {
        return lostDate;
    }

    public void setLostDate(String lostDate) {
        this.lostDate = lostDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getLostItemImageUrl() {
        return lostItemImageUrl;
    }

    public void setLostItemImageUrl(String lostItemImageUrl) {
        this.lostItemImageUrl = lostItemImageUrl;
    }

    public String getLostItemDescription() {
        return lostItemDescription;
    }

    public void setLostItemDescription(String lostItemDescription) {
        this.lostItemDescription = lostItemDescription;
    }

    public String getLostItemContactInfo() {
        return lostItemContactInfo;
    }

    public void setLostItemContactInfo(String lostItemContactInfo) {
        this.lostItemContactInfo = lostItemContactInfo;
    }

    public String getLostItemTelephone() {
        return lostItemTelephone;
    }

    public void setLostItemTelephone(String lostItemTelephone) {
        this.lostItemTelephone = lostItemTelephone;
    }

    public String getLostItemColor() {
        return lostItemColor;
    }

    public void setLostItemColor(String lostItemColor) {
        this.lostItemColor = lostItemColor;
    }

    public String getLostItemRnum() {
        return lostItemRnum;
    }

    public void setLostItemRnum(String lostItemRnum) {
        this.lostItemRnum = lostItemRnum;
    }

    // Builder 패턴
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String lostItemId;
        private String lostItemName;
        private String lostItemCategory;
        private String lostPlace;
        private String lostDate;
        private String status;
        private String lostItemImageUrl;
        private String lostItemDescription;
        private String lostItemContactInfo;
        private String lostItemTelephone;
        private String lostItemColor;
        private String lostItemRnum;

        public Builder lostItemId(String lostItemId) {
            this.lostItemId = lostItemId;
            return this;
        }

        public Builder lostItemName(String lostItemName) {
            this.lostItemName = lostItemName;
            return this;
        }

        public Builder lostItemCategory(String lostItemCategory) {
            this.lostItemCategory = lostItemCategory;
            return this;
        }

        public Builder lostPlace(String lostPlace) {
            this.lostPlace = lostPlace;
            return this;
        }

        public Builder lostDate(String lostDate) {
            this.lostDate = lostDate;
            return this;
        }

        public Builder status(String status) {
            this.status = status;
            return this;
        }

        public Builder lostItemImageUrl(String lostItemImageUrl) {
            this.lostItemImageUrl = lostItemImageUrl;
            return this;
        }

        public Builder lostItemDescription(String lostItemDescription) {
            this.lostItemDescription = lostItemDescription;
            return this;
        }

        public Builder lostItemContactInfo(String lostItemContactInfo) {
            this.lostItemContactInfo = lostItemContactInfo;
            return this;
        }

        public Builder lostItemTelephone(String lostItemTelephone) {
            this.lostItemTelephone = lostItemTelephone;
            return this;
        }

        public Builder lostItemColor(String lostItemColor) {
            this.lostItemColor = lostItemColor;
            return this;
        }

        public Builder lostItemRnum(String lostItemRnum) {
            this.lostItemRnum = lostItemRnum;
            return this;
        }

        public PoliceApiLostItem build() {
            return new PoliceApiLostItem(lostItemId, lostItemName, lostItemCategory, lostPlace, lostDate, 
                                         status, lostItemImageUrl, lostItemDescription, lostItemContactInfo, 
                                         lostItemTelephone, lostItemColor, lostItemRnum);
        }
    }
}
