package com.findit.server.domain.founditem;

import com.findit.server.domain.shared.ItemCategory;
import com.findit.server.domain.shared.ItemName;
import com.findit.server.domain.shared.LocationName;
import jakarta.persistence.Access;
import jakarta.persistence.AccessType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * 습득물 애그리게이트 루트.
 */
@Entity
@Table(name = "found_items")
@Access(AccessType.FIELD)
public class FoundItem {

    @Id
    @Column(name = "atc_id", nullable = false, length = 50)
    private String atcId;

    @Column(name = "fd_prdt_nm", length = 255, nullable = false)
    private String fdPrdtNm;

    @Column(name = "prdt_cl_nm", length = 100, nullable = false)
    private String prdtClNm;

    @Column(name = "fd_ymd", length = 10, nullable = false)
    private String fdYmd;

    @Column(name = "fd_sbjt", columnDefinition = "TEXT")
    private String fdSbjt;

    @Column(name = "fd_file_path_img", length = 500)
    private String fdFilePathImg;

    @Column(name = "dep_place", length = 200, nullable = false)
    private String depPlace;

    @Column(name = "clr_nm", length = 50)
    private String clrNm;

    @Column(name = "fd_sn", length = 50)
    private String fdSn;

    protected FoundItem() {
        // for JPA
    }

    private FoundItem(FoundItemId id,
                      ItemName itemName,
                      ItemCategory category,
                      FoundDate foundDate,
                      LocationName location,
                      String subject,
                      String imagePath,
                      String colorName,
                      String serialNumber) {
        this.atcId = id.value();
        this.fdPrdtNm = itemName.value();
        this.prdtClNm = category.value();
        this.fdYmd = foundDate.asDatabaseValue();
        this.depPlace = location.value();
        this.fdSbjt = normalizeOptional(subject);
        this.fdFilePathImg = normalizeOptional(imagePath);
        this.clrNm = normalizeOptional(colorName);
        this.fdSn = normalizeOptional(serialNumber);
    }

    public static FoundItem create(FoundItemId id,
                                   ItemName itemName,
                                   ItemCategory category,
                                   FoundDate foundDate,
                                   LocationName location,
                                   String subject,
                                   String imagePath,
                                   String colorName,
                                   String serialNumber) {
        return new FoundItem(id, itemName, category, foundDate, location, subject, imagePath, colorName, serialNumber);
    }

    public void updateCoreDetails(ItemName itemName,
                                  ItemCategory category,
                                  FoundDate foundDate,
                                  LocationName location,
                                  String subject,
                                  String colorName,
                                  String serialNumber) {
        this.fdPrdtNm = itemName.value();
        this.prdtClNm = category.value();
        this.fdYmd = foundDate.asDatabaseValue();
        this.depPlace = location.value();
        if (subject != null) {
            this.fdSbjt = normalizeOptional(subject);
        }
        if (colorName != null) {
            this.clrNm = normalizeOptional(colorName);
        }
        if (serialNumber != null) {
            this.fdSn = normalizeOptional(serialNumber);
        }
    }

    public void updateImagePath(String imagePath) {
        if (imagePath != null) {
            this.fdFilePathImg = normalizeOptional(imagePath);
        }
    }

    public FoundItemId getFoundItemId() {
        return FoundItemId.of(atcId);
    }

    public ItemName getItemName() {
        return ItemName.of(fdPrdtNm);
    }

    public ItemCategory getCategory() {
        return ItemCategory.of(prdtClNm);
    }

    public FoundDate getFoundDateValue() {
        return FoundDate.of(fdYmd);
    }

    public LocationName getLocation() {
        return LocationName.of(depPlace);
    }

    public String getAtcId() {
        return atcId;
    }

    public String getFdPrdtNm() {
        return fdPrdtNm;
    }

    public String getPrdtClNm() {
        return prdtClNm;
    }

    public String getFdYmd() {
        return fdYmd;
    }

    public String getFdSbjt() {
        return fdSbjt;
    }

    public String getFdFilePathImg() {
        return fdFilePathImg;
    }

    public String getDepPlace() {
        return depPlace;
    }

    public String getClrNm() {
        return clrNm;
    }

    public String getFdSn() {
        return fdSn;
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

        public FoundItem build() {
            return FoundItem.create(
                FoundItemId.of(atcId),
                ItemName.of(fdPrdtNm),
                ItemCategory.of(prdtClNm),
                FoundDate.of(fdYmd),
                LocationName.of(depPlace),
                fdSbjt,
                fdFilePathImg,
                clrNm,
                fdSn
            );
        }
    }
}
