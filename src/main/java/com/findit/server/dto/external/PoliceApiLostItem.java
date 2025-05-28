package com.findit.server.dto.external;

import lombok.Data;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * 경찰청 API 분실물 아이템 DTO
 */
@Data
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "item") // Assuming each lost item is wrapped in an <item> tag in the list
public class PoliceApiLostItem {

    @XmlElement(name = "atcId")
    private String id; // 관리ID (경찰서ID + 년도 + 순번)

    @XmlElement(name = "lstPrdtNm")
    private String itemName; // 분실물명

    @XmlElement(name = "prdtClNm")
    private String itemType; // 물품분류 (e.g., "가방 > 백팩") - PoliceApiSyncService may need to parse this

    @XmlElement(name = "lstPlace")
    private String location; // 분실장소

    @XmlElement(name = "lstYmd")
    private String lostDate; // 분실일자 (YYYYMMDD)

    @XmlElement(name = "csteSteNm")
    private String status; // 보관상태 (e.g., "보관중", "수사중", "종결")

    @XmlElement(name = "fdFilePathImg")
    private String imageUrl; // 분실물 이미지 경로

    @XmlElement(name = "lstSbjt")
    private String description; // 내용 (분실물 내용)

    @XmlElement(name = "orgNm")
    private String contactInfo; // 보관기관 연락처 또는 기관명 (e.g., "OO경찰서 02-XXX-XXXX")
                                 // API doc specifies orgNm (기관명) and tel (연락처) separately. We might need two fields or combine.
                                 // For now, mapping orgNm to contactInfo. If 'tel' is separate, add it.

    @XmlElement(name = "tel")
    private String telephone; // 연락처 (기관 전화번호)

    @XmlElement(name = "clrNm")
    private String color; // 색상

    @XmlElement(name = "lstGoodsSn")
    private String lstGoodsSn; // 분실물순번 (필수 for 상세조회)

    @XmlElement(name = "rnum")
    private String rnum; // 순번 또는 행번호

    // 기본 생성자
    public PoliceApiLostItem() {
    }

    // 모든 필드를 포함한 생성자
    public PoliceApiLostItem(String id, String itemName, String itemType, String location, 
                           String lostDate, String status, String imageUrl, 
                           String description, String contactInfo, String telephone,
                           String color, String lstGoodsSn, String rnum) {
        this.id = id;
        this.itemName = itemName;
        this.itemType = itemType;
        this.location = location;
        this.lostDate = lostDate;
        this.status = status;
        this.imageUrl = imageUrl;
        this.description = description;
        this.contactInfo = contactInfo;
        this.telephone = telephone;
        this.color = color;
        this.lstGoodsSn = lstGoodsSn;
        this.rnum = rnum;
    }

    // Getters
    public String getId() {
        return id;
    }

    public String getItemName() {
        return itemName;
    }

    public String getItemType() {
        return itemType;
    }

    public String getLocation() {
        return location;
    }

    public String getLostDate() {
        return lostDate;
    }

    public String getStatus() {
        return status;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getDescription() {
        return description;
    }

    public String getContactInfo() {
        return contactInfo;
    }

    public String getTelephone() {
        return telephone;
    }

    public String getColor() {
        return color;
    }

    public String getLstGoodsSn() {
        return lstGoodsSn;
    }

    // Setters
    public void setId(String id) {
        this.id = id;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setLostDate(String lostDate) {
        this.lostDate = lostDate;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setContactInfo(String contactInfo) {
        this.contactInfo = contactInfo;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public void setLstGoodsSn(String lstGoodsSn) {
        this.lstGoodsSn = lstGoodsSn;
    }

    // Builder 패턴
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String id;
        private String itemName;
        private String itemType;
        private String location;
        private String lostDate;
        private String status;
        private String imageUrl;
        private String description;
        private String contactInfo;
        private String telephone;
        private String color;
        private String lstGoodsSn;
        private String rnum;

        public Builder id(String id) {
            this.id = id;
            return this;
        }

        public Builder itemName(String itemName) {
            this.itemName = itemName;
            return this;
        }

        public Builder itemType(String itemType) {
            this.itemType = itemType;
            return this;
        }

        public Builder location(String location) {
            this.location = location;
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

        public Builder imageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder contactInfo(String contactInfo) {
            this.contactInfo = contactInfo;
            return this;
        }

        public Builder telephone(String telephone) {
            this.telephone = telephone;
            return this;
        }

        public Builder color(String color) {
            this.color = color;
            return this;
        }

        public Builder lstGoodsSn(String lstGoodsSn) {
            this.lstGoodsSn = lstGoodsSn;
            return this;
        }

        public Builder rnum(String rnum) {
            this.rnum = rnum;
            return this;
        }

        public PoliceApiLostItem build() {
            return new PoliceApiLostItem(id, itemName, itemType, location, lostDate, 
                                         status, imageUrl, description, contactInfo, 
                                         telephone, color, lstGoodsSn, rnum);
        }
    }
}
