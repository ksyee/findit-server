package com.findit.server.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "lost_items")
public class LostItem {
    
    @Id
    @Column(name = "atc_id")
    private String atcId;
    
    @Column(name = "prdt_cl_nm")
    private String prdtClNm;
    
    @Column(name = "lst_place")
    private String lstPlace;
    
    @Column(name = "lst_ymd", length = 10)
    private String lstYmd;
    
    @Column(name = "lst_prdt_nm")
    private String lstPrdtNm;

    @Column(name = "lst_sbjt", columnDefinition = "TEXT")
    private String lstSbjt;
    
    @Column(name = "rnum")
    private String rnum;
    
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
    
    public String getLstPrdtNm() {
        return lstPrdtNm;
    }

    public String getLstSbjt() {
        return lstSbjt;
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
    
    public void setLstPrdtNm(String lstPrdtNm) {
        this.lstPrdtNm = lstPrdtNm;
    }

    public void setLstSbjt(String lstSbjt) {
        this.lstSbjt = lstSbjt;
    }
    
    public void setRnum(String rnum) {
        this.rnum = rnum;
    }
    
    // 기본 생성자
    public LostItem() {}

    // 모든 필드를 포함한 생성자
    public LostItem(String atcId, String prdtClNm, String lstPlace, String lstYmd, String lstPrdtNm, String lstSbjt, String rnum) {
        this.atcId = atcId;
        this.prdtClNm = prdtClNm;
        this.lstPlace = lstPlace;
        this.lstYmd = lstYmd;
        this.lstPrdtNm = lstPrdtNm;
        this.lstSbjt = lstSbjt;
        this.rnum = rnum;
    }
    
    // Builder 클래스
    public static Builder builder() {
        return new Builder();
    }
    
    public static class Builder {
        private String atcId;
        private String prdtClNm;
        private String lstYmd;
        private String lstPlace;
        private String lstPrdtNm;
        private String lstSbjt;
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
        
        public Builder lstPrdtNm(String lstPrdtNm) {
            this.lstPrdtNm = lstPrdtNm;
            return this;
        }

        public Builder lstSbjt(String lstSbjt) {
            this.lstSbjt = lstSbjt;
            return this;
        }
        
        public Builder rnum(String rnum) {
            this.rnum = rnum;
            return this;
        }
        
        public LostItem build() {
            LostItem lostItem = new LostItem();
            lostItem.atcId = this.atcId;
            lostItem.prdtClNm = this.prdtClNm;
            lostItem.lstPlace = this.lstPlace;
            lostItem.lstYmd = this.lstYmd;
            lostItem.lstPrdtNm = this.lstPrdtNm;
            lostItem.lstSbjt = this.lstSbjt;
            lostItem.rnum = this.rnum;
            return lostItem;
        }
    }
}
