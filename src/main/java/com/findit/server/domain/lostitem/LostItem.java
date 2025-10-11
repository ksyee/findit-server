package com.findit.server.domain.lostitem;

import com.findit.server.domain.shared.ItemCategory;
import com.findit.server.domain.shared.LocationName;
import jakarta.persistence.Access;
import jakarta.persistence.AccessType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * 분실물 애그리게이트 루트.
 */
@Entity
@Table(name = "lost_items")
@Access(AccessType.FIELD)
public class LostItem {

    @Id
    @Column(name = "atc_id", nullable = false)
    private String atcId;

    @Column(name = "prdt_cl_nm", nullable = false)
    private String prdtClNm;

    @Column(name = "lst_place", nullable = false)
    private String lstPlace;

    @Column(name = "lst_ymd", length = 10, nullable = false)
    private String lstYmd;

    @Column(name = "lst_prdt_nm")
    private String lstPrdtNm;

    @Column(name = "lst_sbjt", columnDefinition = "TEXT")
    private String lstSbjt;

    @Column(name = "rnum")
    private String rnum;

    protected LostItem() {
        // for JPA
    }

    private LostItem(LostItemId id,
                     ItemCategory category,
                     LocationName location,
                     LostDate lostDate,
                     String productName,
                     String subject,
                     String rnum) {
        this.atcId = id.value();
        this.prdtClNm = category.value();
        this.lstPlace = location.value();
        this.lstYmd = lostDate.asDatabaseValue();
        this.lstPrdtNm = normalizeOptional(productName);
        this.lstSbjt = normalizeOptional(subject);
        this.rnum = normalizeOptional(rnum);
    }

    public static LostItem create(LostItemId id,
                                  ItemCategory category,
                                  LocationName location,
                                  LostDate lostDate,
                                  String productName,
                                  String subject,
                                  String rnum) {
        return new LostItem(id, category, location, lostDate, productName, subject, rnum);
    }

    public void updateCoreDetails(ItemCategory category,
                                  LocationName location,
                                  LostDate lostDate,
                                  String rnum) {
        this.prdtClNm = category.value();
        this.lstPlace = location.value();
        this.lstYmd = lostDate.asDatabaseValue();
        if (rnum != null) {
            this.rnum = normalizeOptional(rnum);
        }
    }

    public void updateAdditionalDetails(String productName, String subject) {
        if (productName != null) {
            this.lstPrdtNm = normalizeOptional(productName);
        }
        if (subject != null) {
            this.lstSbjt = normalizeOptional(subject);
        }
    }

    public LostItemId getLostItemId() {
        return LostItemId.of(atcId);
    }

    public ItemCategory getCategory() {
        return ItemCategory.of(prdtClNm);
    }

    public LocationName getLocation() {
        return LocationName.of(lstPlace);
    }

    public LostDate getLostDateValue() {
        return LostDate.of(lstYmd);
    }

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

    private String normalizeOptional(String value) {
        if (value == null) {
            return null;
        }
        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String atcId;
        private String prdtClNm;
        private String lstPlace;
        private String lstYmd;
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
            return LostItem.create(
                LostItemId.of(atcId),
                ItemCategory.of(prdtClNm),
                LocationName.of(lstPlace),
                LostDate.of(lstYmd),
                lstPrdtNm,
                lstSbjt,
                rnum
            );
        }
    }
}
