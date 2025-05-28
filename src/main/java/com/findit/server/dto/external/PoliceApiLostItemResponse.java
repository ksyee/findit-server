package com.findit.server.dto.external;

import lombok.Data;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlElementWrapper;
import jakarta.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * 경찰청 API 분실물 응답 DTO
 */
@Data
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "OpenAPI_ServiceResponse")
public class PoliceApiLostItemResponse {

    @XmlElement(name = "header")
    private Header header;

    @XmlElement(name = "body")
    private Body body;

    // Convenience getters to access nested properties directly from the response object
    public List<PoliceApiLostItem> getItems() {
        return (body != null && body.items != null) ? body.items : java.util.Collections.emptyList();
    }

    public int getTotalCount() {
        return (body != null) ? body.totalCount : 0;
    }

    public int getPageNo() {
        return (body != null) ? body.pageNo : 0;
    }

    public int getNumOfRows() {
        return (body != null) ? body.numOfRows : 0;
    }
    
    public String getResultCode() {
        return (header != null) ? header.resultCode : null;
    }

    public String getResultMsg() {
        return (header != null) ? header.resultMsg : null;
    }

    // Setter for items, primarily for testing or manual construction
    public void setItems(List<PoliceApiLostItem> items) {
        if (this.body == null) {
            this.body = new Body();
        }
        this.body.items = items;
    }

    @Data
    @XmlAccessorType(XmlAccessType.FIELD)
    public static class Header {
        @XmlElement(name = "resultCode")
        private String resultCode;

        @XmlElement(name = "resultMsg")
        private String resultMsg;
    }

    @Data
    @XmlAccessorType(XmlAccessType.FIELD)
    public static class Body {
        @XmlElementWrapper(name = "items")
        @XmlElement(name = "item")
        private List<PoliceApiLostItem> items;

        @XmlElement(name = "totalCount")
        private int totalCount;

        @XmlElement(name = "pageNo")
        private int pageNo;

        @XmlElement(name = "numOfRows")
        private int numOfRows;
    }
}
