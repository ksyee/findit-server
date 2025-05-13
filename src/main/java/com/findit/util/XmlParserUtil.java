package com.findit.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.findit.dto.LostItemDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class XmlParserUtil {

    public List<LostItemDto> parseLostItems(String xml) throws IOException {
        XmlMapper xmlMapper = new XmlMapper();
        log.info("XML 파싱 시작");
        
        JsonNode root = xmlMapper.readTree(xml);
        log.info("XML 루트 노드 타입: {}", root.getNodeType());
        
        // 응답 구조 디버깅
        JsonNode responseNode = root.path("response");
        log.info("response 노드 존재: {}", !responseNode.isMissingNode());
        
        JsonNode bodyNode = responseNode.path("body");
        log.info("body 노드 존재: {}", !bodyNode.isMissingNode());
        
        JsonNode itemsNode = bodyNode.path("items");
        log.info("items 노드 존재: {}", !itemsNode.isMissingNode());
        
        JsonNode itemNode = itemsNode.path("item");
        log.info("item 노드 존재: {}, 노드 타입: {}", !itemNode.isMissingNode(), itemNode.getNodeType());
        
        if (itemNode.isArray()) {
            log.info("item 배열 크기: {}", itemNode.size());
        }
        
        List<LostItemDto> result = new ArrayList<>();
        
        if (itemNode.isArray()) {
            for (JsonNode node : itemNode) {
                try {
                    LostItemDto dto = xmlMapper.treeToValue(node, LostItemDto.class);
                    log.info("파싱된 아이템: id={}, 물품명={}", dto.getAtcId(), dto.getFdPrdtNm());
                    result.add(dto);
                } catch (Exception e) {
                    log.error("아이템 파싱 중 오류: {}", e.getMessage(), e);
                }
            }
        } else if (itemNode.isObject()) {
            // 단일 item인 경우
            try {
                LostItemDto dto = xmlMapper.treeToValue(itemNode, LostItemDto.class);
                log.info("단일 아이템 파싱: id={}, 물품명={}", dto.getAtcId(), dto.getFdPrdtNm());
                result.add(dto);
            } catch (Exception e) {
                log.error("단일 아이템 파싱 중 오류: {}", e.getMessage(), e);
            }
        } else {
            log.warn("item 노드가 배열이나 객체가 아닙니다. 노드 타입: {}", itemNode.getNodeType());
            // 직접 XML 내용을 확인
            log.info("XML 내용 일부: {}", xml.substring(0, Math.min(500, xml.length())));
        }
        
        log.info("파싱 완료, 아이템 수: {}", result.size());
        return result;
    }
}