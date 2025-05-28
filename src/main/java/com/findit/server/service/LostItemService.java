package com.findit.server.service;

import com.findit.server.dto.LostItemDto;
import com.findit.server.entity.LostItem;
import com.findit.server.exception.ResourceNotFoundException;
import com.findit.server.repository.LostItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LostItemService {

    private final LostItemRepository lostItemRepository;
    
    @Transactional(readOnly = true)
    public List<LostItemDto> getAllLostItems() {
        return lostItemRepository.findAll().stream()
                .map(LostItemDto::fromEntity)
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public Page<LostItemDto> getAllLostItems(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
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
        lostItem.setSltPrdtNm(lostItemDto.getSltPrdtNm()); 
        lostItem.setPrdtClNm(lostItemDto.getPrdtClNm()); 
        lostItem.setLstPlace(lostItemDto.getLstPlace()); 
        // String → LocalDate 변환 (yyyy-MM-dd 포맷)
        lostItem.setLstYmd(lostItemDto.getLstYmd() != null ? java.time.LocalDate.parse(lostItemDto.getLstYmd()).atStartOfDay() : null);   
        lostItem.setSltSbjt(lostItemDto.getSltSbjt()); 
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
        
        existingLostItem.setSltPrdtNm(lostItemDto.getSltPrdtNm());
        existingLostItem.setPrdtClNm(lostItemDto.getPrdtClNm());
        existingLostItem.setLstPlace(lostItemDto.getLstPlace());
        // String → LocalDate 변환 (yyyy-MM-dd 포맷)
        existingLostItem.setLstYmd(lostItemDto.getLstYmd() != null ? java.time.LocalDate.parse(lostItemDto.getLstYmd()).atStartOfDay() : null);
        existingLostItem.setSltSbjt(lostItemDto.getSltSbjt());
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
        return lostItemRepository.findByLstYmdBetween(start, end, pageable)
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
        return lostItemRepository.findRecentLostItemsByType(prdtClNm, startDate).stream()
                .map(LostItemDto::fromEntity)
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public Page<LostItemDto> findRecentLostItems(String prdtClNm, int days, int page, int size) {
        LocalDateTime startDate = LocalDateTime.now().minusDays(days);
        Pageable pageable = PageRequest.of(page, size);
        return lostItemRepository.findRecentLostItemsByType(prdtClNm, startDate, pageable)
                .map(LostItemDto::fromEntity);
    }
}
