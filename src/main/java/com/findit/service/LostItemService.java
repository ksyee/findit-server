package com.findit.service;

import com.findit.domain.LostItem;
import com.findit.dto.LostItemDto;
import com.findit.repository.LostItemRepository;
import com.findit.util.JaxpXmlParserUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class LostItemService {

    private final LostItemRepository lostItemRepository;
    private final JaxpXmlParserUtil jaxpXmlParserUtil;

    @Autowired
    public LostItemService(LostItemRepository lostItemRepository, JaxpXmlParserUtil jaxpXmlParserUtil) {
        this.lostItemRepository = lostItemRepository;
        this.jaxpXmlParserUtil = jaxpXmlParserUtil;
    }

    @Transactional
    public void saveLostItems(String xmlResponse) {
        try {
            List<LostItemDto> lostItemDtos = jaxpXmlParserUtil.parseLostItems(xmlResponse);
            log.info("파싱된 분실물 데이터 개수: {}", lostItemDtos.size());
            
            List<LostItem> lostItems = lostItemDtos.stream()
                    .map(LostItemDto::toEntity)
                    .collect(Collectors.toList());
            
            lostItemRepository.saveAll(lostItems);
            log.info("분실물 데이터 저장 완료: {} 건", lostItems.size());
            
        } catch (Exception e) {
            log.error("분실물 데이터 저장 중 오류 발생: {}", e.getMessage(), e);
        }
    }

    public List<LostItem> getAllLostItems() {
        return lostItemRepository.findAll();
    }
}