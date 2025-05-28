package com.findit.server.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * 습득물 정보를 나타내는 엔티티 클래스
 */
@Entity
@Table(name = "found_item")
public class FoundItem {
  
  /**
   * 습득물 관리 ID (PK)
   */
  @Id
  @Column(name = "atc_id", nullable = false, length = 50)
  private String atcId;
  
  /**
   * 습득물품명
   */
  @Column(name = "fd_prdt_nm", length = 255)
  private String fdPrdtNm;
  
  /**
   * 습득물 분류명
   */
  @Column(name = "prdt_cl_nm", length = 100)
  private String prdtClNm;
  
  /**
   * 습득일자
   */
  @Column(name = "fd_ymd", length = 8)
  private String fdYmd;
  
  /**
   * 습득물 제목
   */
  @Column(name = "fd_sbjt", columnDefinition = "TEXT")
  private String fdSbjt;
  
  /**
   * 습득물 이미지 경로
   */
  @Column(name = "fd_file_path_img", length = 500)
  private String fdFilePathImg;
  
  /**
   * 보관장소
   */
  @Column(name = "dep_place", length = 200)
  private String depPlace;
  
  /**
   * 색상명
   */
  @Column(name = "clr_nm", length = 50)
  private String clrNm;
  
  /**
   * 습득물 일련번호
   */
  @Column(name = "fd_sn", length = 50)
  private String fdSn;
  
  /**
   * 기본 생성자
   */
  public FoundItem() {
  }
  
  /**
   * 모든 필드를 포함한 생성자
   *
   * @param atcId         관리 ID
   * @param fdPrdtNm      습득물품명
   * @param prdtClNm      물품분류명
   * @param fdYmd         습득일자
   * @param fdSbjt        습득물 제목
   * @param fdFilePathImg 습득물 이미지 경로
   * @param depPlace      보관장소
   * @param clrNm         색상명
   * @param fdSn          습득물 일련번호
   */
  public FoundItem(String atcId, String fdPrdtNm, String prdtClNm,
    String fdYmd, String fdSbjt, String fdFilePathImg,
    String depPlace, String clrNm, String fdSn) {
    this.atcId = atcId;
    this.fdPrdtNm = fdPrdtNm;
    this.prdtClNm = prdtClNm;
    this.fdYmd = fdYmd;
    this.fdSbjt = fdSbjt;
    this.fdFilePathImg = fdFilePathImg;
    this.depPlace = depPlace;
    this.clrNm = clrNm;
    this.fdSn = fdSn;
  }
  
  /**
   * 빌더 패턴을 위한 정적 메서드
   *
   * @return 새로운 Builder 인스턴스
   */
  public static Builder builder() {
    return new Builder();
  }
  
  // Getters
  public String getAtcId() {
    return atcId;
  }
  
  // Setters
  public void setAtcId(String atcId) {
    this.atcId = atcId;
  }
  
  public String getFdPrdtNm() {
    return fdPrdtNm;
  }
  
  public void setFdPrdtNm(String fdPrdtNm) {
    this.fdPrdtNm = fdPrdtNm;
  }
  
  public String getPrdtClNm() {
    return prdtClNm;
  }
  
  public void setPrdtClNm(String prdtClNm) {
    this.prdtClNm = prdtClNm;
  }
  
  public String getFdYmd() {
    return fdYmd;
  }
  
  public void setFdYmd(String fdYmd) {
    this.fdYmd = fdYmd;
  }
  
  public String getFdSbjt() {
    return fdSbjt;
  }
  
  public void setFdSbjt(String fdSbjt) {
    this.fdSbjt = fdSbjt;
  }
  
  public String getFdFilePathImg() {
    return fdFilePathImg;
  }
  
  public void setFdFilePathImg(String fdFilePathImg) {
    this.fdFilePathImg = fdFilePathImg;
  }
  
  public String getDepPlace() {
    return depPlace;
  }
  
  public void setDepPlace(String depPlace) {
    this.depPlace = depPlace;
  }
  
  public String getClrNm() {
    return clrNm;
  }
  
  public void setClrNm(String clrNm) {
    this.clrNm = clrNm;
  }
  
  public String getFdSn() {
    return fdSn;
  }
  
  public void setFdSn(String fdSn) {
    this.fdSn = fdSn;
  }
  
  /**
   * 빌더 패턴을 위한 Builder 클래스
   */
  public static class Builder {
    
    private String atcId;
    private String fdPrdtNm;
    private String prdtClNm;
    private String fdYmd;
    private String fdSbjt;
    private String fdFilePathImg;
    private String depPlace;
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
    
    public Builder fdYmd(String fdYmd) {
      this.fdYmd = fdYmd;
      return this;
    }
    
    public Builder fdSbjt(String fdSbjt) {
      this.fdSbjt = fdSbjt;
      return this;
    }
    
    public Builder fdFilePathImg(String fdFilePathImg) {
      this.fdFilePathImg = fdFilePathImg;
      return this;
    }
    
    public Builder depPlace(String depPlace) {
      this.depPlace = depPlace;
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
    
    /**
     * FoundItem 인스턴스를 생성하여 반환
     *
     * @return 생성된 FoundItem 인스턴스
     */
    public FoundItem build() {
      return new FoundItem(
        atcId,
        fdPrdtNm,
        prdtClNm,
        fdYmd,
        fdSbjt,
        fdFilePathImg,
        depPlace,
        clrNm,
        fdSn
      );
    }
  }
}
