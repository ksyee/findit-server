package com.findit.service;

import com.findit.domain.LostItem;
import com.findit.repository.LostItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class DirectXmlParserService {

    private final LostItemRepository lostItemRepository;

    @Transactional
    public void parseAndSaveWithDomParser(String xmlString) {
        try {
            log.info("DOM 파서로 XML 파싱 시작");
            
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new ByteArrayInputStream(xmlString.getBytes(StandardCharsets.UTF_8)));
            
            doc.getDocumentElement().normalize();
            log.info("루트 요소: {}", doc.getDocumentElement().getNodeName());
            
            NodeList itemList = doc.getElementsByTagName("item");
            log.info("item 요소 개수: {}", itemList.getLength());
            
            List<LostItem> lostItems = new ArrayList<>();
            
            for (int i = 0; i < itemList.getLength(); i++) {
                Node itemNode = itemList.item(i);
                
                if (itemNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element itemElement = (Element) itemNode;
                    
                    LostItem item = new LostItem();
                    item.setAtcId(getElementText(itemElement, "atcId"));
                    item.setClrNm(getElementText(itemElement, "clrNm"));
                    item.setDepPlace(getElementText(itemElement, "depPlace"));
                    item.setFdFilePathImg(getElementText(itemElement, "fdFilePathImg"));
                    item.setFdPrdtNm(getElementText(itemElement, "fdPrdtNm"));
                    item.setFdSbjt(getElementText(itemElement, "fdSbjt"));
                    
                    try {
                        item.setFdSn(Integer.parseInt(getElementText(itemElement, "fdSn")));
                    } catch (NumberFormatException e) {
                        item.setFdSn(0);
                        log.warn("fdSn 값 변환 실패: {}", e.getMessage());
                    }
                    
                    item.setFdYmd(getElementText(itemElement, "fdYmd"));
                    item.setPrdtClNm(getElementText(itemElement, "prdtClNm"));
                    
                    log.info("파싱된 아이템 {}: id={}, 물품명={}", i+1, item.getAtcId(), item.getFdPrdtNm());
                    lostItems.add(item);
                }
            }
            
            if (!lostItems.isEmpty()) {
                lostItemRepository.saveAll(lostItems);
                log.info("DOM 파서로 {}개의 아이템을 저장했습니다.", lostItems.size());
            } else {
                log.warn("DOM 파서로 파싱된 아이템이 없습니다.");
            }
            
        } catch (Exception e) {
            log.error("DOM 파서 처리 중 오류 발생: {}", e.getMessage(), e);
        }
    }
    
    private String getElementText(Element element, String tagName) {
        NodeList nodeList = element.getElementsByTagName(tagName);
        if (nodeList.getLength() > 0) {
            return nodeList.item(0).getTextContent();
        }
        return "";
    }
}