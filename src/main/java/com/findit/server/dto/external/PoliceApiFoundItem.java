package com.findit.server.dto.external;

import lombok.Data;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * uacbducc30uccad API uc2b5ub4ddubb3c uc544uc774ud15c DTO
 */
@Data
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "item") 
public class PoliceApiFoundItem {

    @XmlElement(name = "atcId")
    private String id; 

    @XmlElement(name = "fdPrdtNm")
    private String itemName; 

    @XmlElement(name = "prdtClNm")
    private String itemType; 

    @XmlElement(name = "fdPlace")
    private String location; 

    @XmlElement(name = "fdYmd")
    private String foundDate; 

    @XmlElement(name = "depPlace") 
    private String storagePlaceName; 

    @XmlElement(name = "addr")
    private String storageAddress; 

    @XmlElement(name = "tel")
    private String storageContact; 

    @XmlElement(name = "csteSteNm") 
                                  // 
                                  // Lost API 
    private String status; 

    @XmlElement(name = "fdFilePathImg")
    private String imageUrl; 

    @XmlElement(name = "fdSbjt")
    private String description; 

    @XmlElement(name = "clrNm")
    private String color; 

    @XmlElement(name = "fdSn")
    private String fdSn; 

    // uae30ubcf8 uc0dduc131uc790
    public PoliceApiFoundItem() {
    }
    
    // ubaa8ub4e0 ud544ub4dcub97c ud3ecud568ud55c uc0dduc131uc790
    public PoliceApiFoundItem(String id, String itemName, String itemType, String location, 
                             String foundDate, String status, String imageUrl, 
                             String description, String storagePlaceName, 
                             String storageAddress, String storageContact,
                             String color, String fdSn) {
        this.id = id;
        this.itemName = itemName;
        this.itemType = itemType;
        this.location = location;
        this.foundDate = foundDate;
        this.status = status;
        this.imageUrl = imageUrl;
        this.description = description;
        this.storagePlaceName = storagePlaceName;
        this.storageAddress = storageAddress;
        this.storageContact = storageContact;
        this.color = color;
        this.fdSn = fdSn;
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
    
    public String getFoundDate() {
        return foundDate;
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
    
    public String getStoragePlaceName() {
        return storagePlaceName;
    }
    
    public String getStorageAddress() {
        return storageAddress;
    }
    
    public String getStorageContact() {
        return storageContact;
    }
    
    public String getColor() {
        return color;
    }
    
    public String getFdSn() {
        return fdSn;
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
    
    public void setFoundDate(String foundDate) {
        this.foundDate = foundDate;
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
    
    public void setStoragePlaceName(String storagePlaceName) {
        this.storagePlaceName = storagePlaceName;
    }
    
    public void setStorageAddress(String storageAddress) {
        this.storageAddress = storageAddress;
    }
    
    public void setStorageContact(String storageContact) {
        this.storageContact = storageContact;
    }
    
    public void setColor(String color) {
        this.color = color;
    }
    
    public void setFdSn(String fdSn) {
        this.fdSn = fdSn;
    }
    
    // Builder ud328ud134
    public static Builder builder() {
        return new Builder();
    }
    
    public static class Builder {
        private String id;
        private String itemName;
        private String itemType;
        private String location;
        private String foundDate;
        private String status;
        private String imageUrl;
        private String description;
        private String storagePlaceName;
        private String storageAddress;
        private String storageContact;
        private String color;
        private String fdSn;
        
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
        
        public Builder foundDate(String foundDate) {
            this.foundDate = foundDate;
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
        
        public Builder storagePlaceName(String storagePlaceName) {
            this.storagePlaceName = storagePlaceName;
            return this;
        }
        
        public Builder storageAddress(String storageAddress) {
            this.storageAddress = storageAddress;
            return this;
        }
        
        public Builder storageContact(String storageContact) {
            this.storageContact = storageContact;
            return this;
        }
        
        public Builder color(String color) {
            this.color = color;
            return this;
        }
        
        public Builder fdSn(String fdSn) {
            this.fdSn = fdSn;
            return this;
        }
        
        public PoliceApiFoundItem build() {
            PoliceApiFoundItem item = new PoliceApiFoundItem();
            item.id = this.id;
            item.itemName = this.itemName;
            item.itemType = this.itemType;
            item.location = this.location;
            item.foundDate = this.foundDate;
            item.status = this.status;
            item.imageUrl = this.imageUrl;
            item.description = this.description;
            item.storagePlaceName = this.storagePlaceName;
            item.storageAddress = this.storageAddress;
            item.storageContact = this.storageContact;
            item.color = this.color;
            item.fdSn = this.fdSn;
            return item;
        }
    }
}
