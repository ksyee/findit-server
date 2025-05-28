package com.findit.server.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "lost_item")
public class LostItem {
    
    @Id
    @Column(name = "atc_id")
    private String atcId;
    
    @Column(name = "slt_prdt_nm")
    private String sltPrdtNm;
    
    @Column(name = "prdt_cl_nm")
    private String prdtClNm;
    
    @Column(name = "lst_place")
    private String lstPlace;
    
    @Column(name = "lst_ymd")
    private LocalDateTime lstYmd;
    
    @Lob
    @Column(name = "slt_sbjt", columnDefinition = "TEXT")
    private String sltSbjt;
    
    @Column(name = "rnum")
    private String rnum;
    
    // Getters
    public String getAtcId() {
        return atcId;
    }
    
    public String getSltPrdtNm() {
        return sltPrdtNm;
    }
    
    public String getPrdtClNm() {
        return prdtClNm;
    }
    
    public String getLstPlace() {
        return lstPlace;
    }
    
    public LocalDateTime getLstYmd() {
        return lstYmd;
    }
    
    public String getSltSbjt() {
        return sltSbjt;
    }
    
    public String getRnum() {
        return rnum;
    }
    
    // Setters
    public void setAtcId(String atcId) {
        this.atcId = atcId;
    }
    
    public void setSltPrdtNm(String sltPrdtNm) {
        this.sltPrdtNm = sltPrdtNm;
    }
    
    public void setPrdtClNm(String prdtClNm) {
        this.prdtClNm = prdtClNm;
    }
    
    public void setLstPlace(String lstPlace) {
        this.lstPlace = lstPlace;
    }
    
    public void setLstYmd(LocalDateTime lstYmd) {
        this.lstYmd = lstYmd;
    }
    
    public void setSltSbjt(String sltSbjt) {
        this.sltSbjt = sltSbjt;
    }
    
    public void setRnum(String rnum) {
        this.rnum = rnum;
    }
    
    // 기본 생성자
    public LostItem() {}

    // 모든 필드를 포함한 생성자
    public LostItem(String atcId, String sltPrdtNm, String prdtClNm, String lstPlace, 
                  LocalDateTime lstYmd, String sltSbjt, String rnum) {
        this.atcId = atcId;
        this.sltPrdtNm = sltPrdtNm;
        this.prdtClNm = prdtClNm;
        this.lstPlace = lstPlace;
        this.lstYmd = lstYmd;
        this.sltSbjt = sltSbjt;
        this.rnum = rnum;
    }
    
    // Builder 클래스
    public static Builder builder() {
        return new Builder();
    }
    
    public static class Builder {
        private String atcId;
        private String sltPrdtNm;
        private String prdtClNm;
        private String lstPlace;
        private LocalDateTime lstYmd;
        private String sltSbjt;
        private String rnum;
        
        public Builder atcId(String atcId) {
            this.atcId = atcId;
            return this;
        }
        
        public Builder sltPrdtNm(String sltPrdtNm) {
            this.sltPrdtNm = sltPrdtNm;
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
        
        public Builder lstYmd(LocalDateTime lstYmd) {
            this.lstYmd = lstYmd;
            return this;
        }
        
        public Builder sltSbjt(String sltSbjt) {
            this.sltSbjt = sltSbjt;
            return this;
        }

        public Builder rnum(String rnum) {
            this.rnum = rnum;
            return this;
        }
        
        public LostItem build() {
            LostItem lostItem = new LostItem();
            lostItem.atcId = this.atcId;
            lostItem.sltPrdtNm = this.sltPrdtNm;
            lostItem.prdtClNm = this.prdtClNm;
            lostItem.lstPlace = this.lstPlace;
            lostItem.lstYmd = this.lstYmd;
            lostItem.sltSbjt = this.sltSbjt;
            lostItem.rnum = this.rnum;
            return lostItem;
        }
    }
}
