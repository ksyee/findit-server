package com.findit.service;

import com.findit.domain.LostItem;
import com.findit.repository.LostItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class SaxXmlParserService {

    private final LostItemRepository lostItemRepository;

    @Transactional
    public void parseAndSaveWithSaxParser(String xmlString) {
        try {
            log.info("SAX 파서로 XML 파싱 시작");
            
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxParser = factory.newSAXParser();
            
            LostItemHandler handler = new LostItemHandler();
            
            saxParser.parse(
                new ByteArrayInputStream(xmlString.getBytes(StandardCharsets.UTF_8)), 
                handler
            );
            
            List<LostItem> lostItems = handler.getLostItems();
            log.info("SAX 파서로 파싱된 아이템 수: {}", lostItems.size());
            
            if (!lostItems.isEmpty()) {
                lostItemRepository.saveAll(lostItems);
                log.info("SAX 파서로 {}개의 아이템을 저장했습니다.", lostItems.size());
            } else {
                log.warn("SAX 파서로 파싱된 아이템이 없습니다.");
            }
            
        } catch (Exception e) {
            log.error("SAX 파서 처리 중 오류 발생: {}", e.getMessage(), e);
        }
    }
    
    private static class LostItemHandler extends DefaultHandler {
        private final List<LostItem> lostItems = new ArrayList<>();
        private LostItem currentItem = null;
        private StringBuilder currentValue = new StringBuilder();
        private boolean inItemElement = false;
        
        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
            currentValue.setLength(0);
            
            if ("item".equals(qName)) {
                inItemElement = true;
                currentItem = new LostItem();
            }
        }
        
        @Override
        public void endElement(String uri, String localName, String qName) throws SAXException {
            if (inItemElement) {
                switch (qName) {
                    case "atcId":
                        currentItem.setAtcId(currentValue.toString());
                        break;
                    case "clrNm":
                        currentItem.setClrNm(currentValue.toString());
                        break;
                    case "depPlace":
                        currentItem.setDepPlace(currentValue.toString());
                        break;
                    case "fdFilePathImg":
                        currentItem.setFdFilePathImg(currentValue.toString());
                        break;
                    case "fdPrdtNm":
                        currentItem.setFdPrdtNm(currentValue.toString());
                        break;
                    case "fdSbjt":
                        currentItem.setFdSbjt(currentValue.toString());
                        break;
                    case "fdSn":
                        try {
                            currentItem.setFdSn(Integer.parseInt(currentValue.toString()));
                        } catch (NumberFormatException e) {
                            currentItem.setFdSn(0);
                        }
                        break;
                    case "fdYmd":
                        currentItem.setFdYmd(currentValue.toString());
                        break;
                    case "prdtClNm":
                        currentItem.setPrdtClNm(currentValue.toString());
                        break;
                    case "item":
                        inItemElement = false;
                        if (currentItem.getAtcId() != null && !currentItem.getAtcId().isEmpty()) {
                            lostItems.add(currentItem);
                        }
                        break;
                }
            }
        }
        
        @Override
        public void characters(char[] ch, int start, int length) throws SAXException {
            currentValue.append(ch, start, length);
        }
        
        public List<LostItem> getLostItems() {
            return lostItems;
        }
    }
}