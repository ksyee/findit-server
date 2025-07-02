package com.findit.server.service;

import com.findit.server.dto.LostItemDto;
import com.findit.server.entity.LostItem;
import com.findit.server.exception.ResourceNotFoundException;
import com.findit.server.repository.LostItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LostItemService {

    private final LostItemRepository lostItemRepository;
    
    @Transactional(readOnly = true)
    public List<LostItemDto> getAllLostItems() {
        return lostItemRepository.findAll(Sort.by(Sort.Direction.DESC, "atcId")).stream()
                .map(LostItemDto::fromEntity)
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public Page<LostItemDto> getAllLostItems(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "atcId"));
        return lostItemRepository.findAll(pageable)
                .map(LostItemDto::fromEntity);
    }
    
    @Transactional(readOnly = true)
    public LostItemDto getLostItemByAtcId(String atcId) {
        LostItem lostItem = lostItemRepository.findByAtcId(atcId)
                .orElseThrow(() -> new ResourceNotFoundException("LostItem", "atcId", atcId));
        return LostItemDto.fromEntity(lostItem);
    }
    
    @Transactional
    public LostItemDto createLostItem(LostItemDto lostItemDto) {
        LostItem lostItem = new LostItem();
        lostItem.setAtcId(lostItemDto.getAtcId());
        lostItem.setPrdtClNm(lostItemDto.getPrdtClNm()); 
        lostItem.setLstPlace(lostItemDto.getLstPlace()); 
        // 문자열(yyyyMMdd) 그대로 저장
        lostItem.setLstYmd(lostItemDto.getLstYmd());
        if (lostItemDto.getRnum() != null) { 
            lostItem.setRnum(lostItemDto.getRnum());
        }
        
        LostItem savedLostItem = lostItemRepository.save(lostItem);
        return LostItemDto.fromEntity(savedLostItem);
    }
    
    @Transactional
    public LostItemDto updateLostItem(String atcId, LostItemDto lostItemDto) {
        LostItem existingLostItem = lostItemRepository.findByAtcId(atcId)
                .orElseThrow(() -> new ResourceNotFoundException("LostItem", "atcId", atcId));
        
        existingLostItem.setPrdtClNm(lostItemDto.getPrdtClNm());
        existingLostItem.setLstPlace(lostItemDto.getLstPlace());
        // 문자열(yyyyMMdd) 그대로 저장
        existingLostItem.setLstYmd(lostItemDto.getLstYmd());
        if (lostItemDto.getRnum() != null) { 
            existingLostItem.setRnum(lostItemDto.getRnum());
        }
        
        LostItem updatedLostItem = lostItemRepository.save(existingLostItem);
        return LostItemDto.fromEntity(updatedLostItem);
    }
    
    @Transactional
    public void deleteLostItem(String atcId) {
        if (!lostItemRepository.existsByAtcId(atcId)) {
            throw new ResourceNotFoundException("LostItem", "atcId", atcId);
        }
        lostItemRepository.deleteByAtcId(atcId);
    }
    
    @Transactional(readOnly = true)
    public List<LostItemDto> findByPrdtClNm(String prdtClNm) {
        return lostItemRepository.findByPrdtClNm(prdtClNm).stream()
                .map(LostItemDto::fromEntity)
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public Page<LostItemDto> findByPrdtClNm(String prdtClNm, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return lostItemRepository.findByPrdtClNm(prdtClNm, pageable)
                .map(LostItemDto::fromEntity);
    }
    
    @Transactional(readOnly = true)
    public Page<LostItemDto> findByLstPlaceContaining(String lstPlace, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return lostItemRepository.findByLstPlaceContaining(lstPlace, pageable)
                .map(LostItemDto::fromEntity);
    }
    
    @Transactional(readOnly = true)
    public Page<LostItemDto> findByLstYmdBetween(LocalDateTime start, LocalDateTime end, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        String startYmd = start.format(DateTimeFormatter.BASIC_ISO_DATE);
        String endYmd = end.format(DateTimeFormatter.BASIC_ISO_DATE);
        return lostItemRepository.findByLstYmdBetween(startYmd, endYmd, pageable)
                .map(LostItemDto::fromEntity);
    }
    
    @Transactional(readOnly = true)
    public List<LostItemDto> searchByKeyword(String keyword) {
        return lostItemRepository.searchByKeyword(keyword).stream()
                .map(LostItemDto::fromEntity)
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public Page<LostItemDto> searchByKeyword(String keyword, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return lostItemRepository.searchByKeyword(keyword, pageable)
                .map(LostItemDto::fromEntity);
    }
    
    @Transactional(readOnly = true)
    public List<LostItemDto> findRecentLostItems(String prdtClNm, int days) {
        LocalDateTime startDate = LocalDateTime.now().minusDays(days);
        String startYmd = startDate.format(DateTimeFormatter.BASIC_ISO_DATE);
        return lostItemRepository.findRecentLostItemsByType(prdtClNm, startYmd).stream()
                .map(LostItemDto::fromEntity)
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public Page<LostItemDto> findRecentLostItems(String prdtClNm, int days, int page, int size) {
        LocalDateTime startDate = LocalDateTime.now().minusDays(days);
        String startYmd = startDate.format(DateTimeFormatter.BASIC_ISO_DATE);
        Pageable pageable = PageRequest.of(page, size);
        return lostItemRepository.findRecentLostItemsByType(prdtClNm, startYmd, pageable)
                .map(LostItemDto::fromEntity);
    }
}
