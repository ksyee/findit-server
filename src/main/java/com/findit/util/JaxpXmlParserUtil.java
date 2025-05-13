package com.findit.util;

import com.findit.dto.LostItemDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class JaxpXmlParserUtil {

    public List<LostItemDto> parseLostItems(String xml) {
        List<LostItemDto> result = new ArrayList<>();
        
        try {
            log.info("JAXP 파서로 XML 파싱 시작");
            
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(false); // 네임스페이스 무시
            factory.setValidating(false); // 유효성 검사 무시
            factory.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
            
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8)));
            
            // root -> response -> body -> items -> item 노드 탐색
            NodeList itemList = doc.getElementsByTagName("item");
            log.info("JAXP: item 요소 개수: {}", itemList.getLength());
            
            for (int i = 0; i < itemList.getLength(); i++) {
                Element itemElement = (Element) itemList.item(i);
                LostItemDto dto = new LostItemDto();
                
                // 각 필드 추출
                dto.setAtcId(getTextContent(itemElement, "atcId"));
                dto.setClrNm(getTextContent(itemElement, "clrNm"));
                dto.setDepPlace(getTextContent(itemElement, "depPlace"));
                dto.setFdFilePathImg(getTextContent(itemElement, "fdFilePathImg"));
                dto.setFdPrdtNm(getTextContent(itemElement, "fdPrdtNm"));
                dto.setFdSbjt(getTextContent(itemElement, "fdSbjt"));
                
                try {
                    dto.setFdSn(Integer.parseInt(getTextContent(itemElement, "fdSn")));
                } catch (NumberFormatException e) {
                    dto.setFdSn(0);
                    log.warn("fdSn 값 변환 실패: {}", e.getMessage());
                }
                
                dto.setFdYmd(getTextContent(itemElement, "fdYmd"));
                dto.setPrdtClNm(getTextContent(itemElement, "prdtClNm"));
                
                try {
                    dto.setRnum(Integer.parseInt(getTextContent(itemElement, "rnum")));
                } catch (NumberFormatException e) {
                    dto.setRnum(0);
                    log.warn("rnum 값 변환 실패: {}", e.getMessage());
                }
                
                log.info("JAXP 파싱된 아이템 {}: id={}, 물품명={}", i+1, dto.getAtcId(), dto.getFdPrdtNm());
                result.add(dto);
            }
            
        } catch (Exception e) {
            log.error("JAXP 파싱 중 오류 발생: {}", e.getMessage(), e);
        }
        
        log.info("JAXP 파싱 완료, 아이템 수: {}", result.size());
        return result;
    }
    
    private String getTextContent(Element element, String tagName) {
        NodeList nodeList = element.getElementsByTagName(tagName);
        if (nodeList != null && nodeList.getLength() > 0) {
            return nodeList.item(0).getTextContent();
        }
        return "";
    }
}