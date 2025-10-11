package com.findit.server.infrastructure.police.dto;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlElementWrapper;
import jakarta.xml.bind.annotation.XmlRootElement;
import java.util.Collections;
import java.util.List;

/**
 * 경찰청 API 분실물 응답 DTO
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "response")
public class PoliceApiLostItemResponse {

    @XmlElement(name = "header")
    private Header header;

    @XmlElement(name = "body")
    private Body body;

    public Header getHeader() {
        return header;
    }

    public void setHeader(Header header) {
        this.header = header;
    }

    public Body getBody() {
        return body;
    }

    public void setBody(Body body) {
        this.body = body;
    }

    // Convenience helpers to access nested data safely
    public List<PoliceApiLostItem> getItems() {
        return body != null && body.items != null ? body.items : Collections.emptyList();
    }

    public int getTotalCount() {
        return body != null ? body.totalCount : 0;
    }

    public int getPageNo() {
        return body != null ? body.pageNo : 0;
    }

    public int getNumOfRows() {
        return body != null ? body.numOfRows : 0;
    }

    public String getResultCode() {
        return header != null ? header.resultCode : null;
    }

    public String getResultMsg() {
        return header != null ? header.resultMsg : null;
    }

    public void setItems(List<PoliceApiLostItem> items) {
        if (this.body == null) {
            this.body = new Body();
        }
        this.body.items = items;
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    public static class Header {
        @XmlElement(name = "resultCode")
        private String resultCode;

        @XmlElement(name = "resultMsg")
        private String resultMsg;

        public String getResultCode() {
            return resultCode;
        }

        public void setResultCode(String resultCode) {
            this.resultCode = resultCode;
        }

        public String getResultMsg() {
            return resultMsg;
        }

        public void setResultMsg(String resultMsg) {
            this.resultMsg = resultMsg;
        }
    }

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

        public List<PoliceApiLostItem> getItems() {
            return items;
        }

        public void setItems(List<PoliceApiLostItem> items) {
            this.items = items;
        }

        public int getTotalCount() {
            return totalCount;
        }

        public void setTotalCount(int totalCount) {
            this.totalCount = totalCount;
        }

        public int getPageNo() {
            return pageNo;
        }

        public void setPageNo(int pageNo) {
            this.pageNo = pageNo;
        }

        public int getNumOfRows() {
            return numOfRows;
        }

        public void setNumOfRows(int numOfRows) {
            this.numOfRows = numOfRows;
        }
    }
}
