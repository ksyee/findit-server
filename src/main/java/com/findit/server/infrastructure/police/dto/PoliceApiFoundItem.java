package com.findit.server.infrastructure.police.dto;

import lombok.Data;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * uacbducc30uccad API uc2b5ub4ddubb3c uc544uc774ud15c DTO
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "item") 
public class PoliceApiFoundItem {

    @JsonProperty("atcId")
    @XmlElement(name = "atcId")
    private String atcId; 

    @JsonProperty("fdPrdtNm")
    @XmlElement(name = "fdPrdtNm")
    private String fdPrdtNm; 

    @JsonProperty("prdtClNm")
    @XmlElement(name = "prdtClNm")
    private String prdtClNm; 

    @JsonProperty("fdPlace")
    @XmlElement(name = "fdPlace")
    private String fdPlace; 

    @JsonProperty("fdYmd")
    @XmlElement(name = "fdYmd")
    private String fdYmd; 

    @JsonProperty("depPlace") 
    @XmlElement(name = "depPlace") 
    private String depPlace; 

    @JsonProperty("addr")
    @XmlElement(name = "addr")
    private String addr; 

    @JsonProperty("tel")
    @XmlElement(name = "tel")
    private String tel; 

    @JsonProperty("csteSteNm") 
                                  // 
                                  // Lost API 
    private String csteSteNm; 

    @JsonProperty("fdFilePathImg")
    @XmlElement(name = "fdFilePathImg")
    private String fdFilePathImg; 

    @JsonProperty("fdSbjt")
    @XmlElement(name = "fdSbjt")
    private String fdSbjt; 

    @JsonProperty("clrNm")
    @XmlElement(name = "clrNm")
    private String clrNm; 

    @JsonProperty("fdSn")
    @XmlElement(name = "fdSn")
    private String fdSn; 

    // uae30ubcf8 uc0dduc131uc790
    public PoliceApiFoundItem() {
    }
    
    // ubaa8ub4e0 ud544ub4dcub97c ud3ecud568ud55c uc0dduc131uc790
    public PoliceApiFoundItem(String atcId, String fdPrdtNm, String prdtClNm, String fdPlace, 
                             String fdYmd, String csteSteNm, String fdFilePathImg, 
                             String fdSbjt, String depPlace, 
                             String addr, String tel,
                             String clrNm, String fdSn) {
        this.atcId = atcId;
        this.fdPrdtNm = fdPrdtNm;
        this.prdtClNm = prdtClNm;
        this.fdPlace = fdPlace;
        this.fdYmd = fdYmd;
        this.csteSteNm = csteSteNm;
        this.fdFilePathImg = fdFilePathImg;
        this.fdSbjt = fdSbjt;
        this.depPlace = depPlace;
        this.addr = addr;
        this.tel = tel;
        this.clrNm = clrNm;
        this.fdSn = fdSn;
    }
    
    // Getters
    public String getAtcId() {
        return atcId;
    }
    
    public String getFdPrdtNm() {
        return fdPrdtNm;
    }
    
    public String getPrdtClNm() {
        return prdtClNm;
    }
    
    public String getFdPlace() {
        return fdPlace;
    }
    
    public String getFdYmd() {
        return fdYmd;
    }
    
    public String getCsteSteNm() {
        return csteSteNm;
    }
    
    public String getFdFilePathImg() {
        return fdFilePathImg;
    }
    
    public String getFdSbjt() {
        return fdSbjt;
    }
    
    public String getDepPlace() {
        return depPlace;
    }
    
    public String getAddr() {
        return addr;
    }
    
    public String getTel() {
        return tel;
    }
    
    public String getClrNm() {
        return clrNm;
    }
    
    public String getFdSn() {
        return fdSn;
    }
    
    // Setters
    public void setAtcId(String atcId) {
        this.atcId = atcId;
    }
    
    public void setFdPrdtNm(String fdPrdtNm) {
        this.fdPrdtNm = fdPrdtNm;
    }
    
    public void setPrdtClNm(String prdtClNm) {
        this.prdtClNm = prdtClNm;
    }
    
    public void setFdPlace(String fdPlace) {
        this.fdPlace = fdPlace;
    }
    
    public void setFdYmd(String fdYmd) {
        this.fdYmd = fdYmd;
    }
    
    public void setCsteSteNm(String csteSteNm) {
        this.csteSteNm = csteSteNm;
    }
    
    public void setFdFilePathImg(String fdFilePathImg) {
        this.fdFilePathImg = fdFilePathImg;
    }
    
    public void setFdSbjt(String fdSbjt) {
        this.fdSbjt = fdSbjt;
    }
    
    public void setDepPlace(String depPlace) {
        this.depPlace = depPlace;
    }
    
    public void setAddr(String addr) {
        this.addr = addr;
    }
    
    public void setTel(String tel) {
        this.tel = tel;
    }
    
    public void setClrNm(String clrNm) {
        this.clrNm = clrNm;
    }
    
    public void setFdSn(String fdSn) {
        this.fdSn = fdSn;
    }
    
    // Builder ud328ud134
    public static Builder builder() {
        return new Builder();
    }
    
    public static class Builder {
        private String atcId;
        private String fdPrdtNm;
        private String prdtClNm;
        private String fdPlace;
        private String fdYmd;
        private String csteSteNm;
        private String fdFilePathImg;
        private String fdSbjt;
        private String depPlace;
        private String addr;
        private String tel;
        private String clrNm;
        private String fdSn;
        
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
        
        public Builder fdPlace(String fdPlace) {
            this.fdPlace = fdPlace;
            return this;
        }
        
        public Builder fdYmd(String fdYmd) {
            this.fdYmd = fdYmd;
            return this;
        }
        
        public Builder csteSteNm(String csteSteNm) {
            this.csteSteNm = csteSteNm;
            return this;
        }
        
        public Builder fdFilePathImg(String fdFilePathImg) {
            this.fdFilePathImg = fdFilePathImg;
            return this;
        }
        
        public Builder fdSbjt(String fdSbjt) {
            this.fdSbjt = fdSbjt;
            return this;
        }
        
        public Builder depPlace(String depPlace) {
            this.depPlace = depPlace;
            return this;
        }
        
        public Builder addr(String addr) {
            this.addr = addr;
            return this;
        }
        
        public Builder tel(String tel) {
            this.tel = tel;
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
        
        public PoliceApiFoundItem build() {
            PoliceApiFoundItem item = new PoliceApiFoundItem();
            item.atcId = this.atcId;
            item.fdPrdtNm = this.fdPrdtNm;
            item.prdtClNm = this.prdtClNm;
            item.fdPlace = this.fdPlace;
            item.fdYmd = this.fdYmd;
            item.csteSteNm = this.csteSteNm;
            item.fdFilePathImg = this.fdFilePathImg;
            item.fdSbjt = this.fdSbjt;
            item.depPlace = this.depPlace;
            item.addr = this.addr;
            item.tel = this.tel;
            item.clrNm = this.clrNm;
            item.fdSn = this.fdSn;
            return item;
        }
    }
}
