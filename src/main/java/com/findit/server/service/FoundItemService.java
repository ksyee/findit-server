package com.findit.server.service;

import com.findit.server.dto.FoundItemDto;
import com.findit.server.entity.FoundItem;
import com.findit.server.exception.ResourceNotFoundException;
import com.findit.server.repository.FoundItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FoundItemService {

    private final FoundItemRepository foundItemRepository;

    private static final DateTimeFormatter INPUT_FORMATTER_YYYYMMDD = DateTimeFormatter.ofPattern("yyyyMMdd");
    private static final DateTimeFormatter INPUT_FORMATTER_YYYY_MM_DD = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    // Helper method to parse date strings (yyyyMMdd or yyyy-MM-dd) to LocalDateTime (at start of day)
    private LocalDateTime parseStringToLocalDateTime(String dateStr) {
        if (dateStr == null || dateStr.trim().isEmpty()) {
            return null;
        }
        dateStr = dateStr.trim();
        LocalDate parsedDate = null;
        try {
            if (dateStr.contains("-")) {
                parsedDate = LocalDate.parse(dateStr, INPUT_FORMATTER_YYYY_MM_DD);
            } else {
                parsedDate = LocalDate.parse(dateStr, INPUT_FORMATTER_YYYYMMDD);
            }
            return parsedDate != null ? parsedDate.atStartOfDay() : null;
        } catch (DateTimeParseException e) {
            System.err.println("Invalid date format for LocalDateTime: " + dateStr + ". Expected yyyy-MM-dd or yyyyMMdd. Error: " + e.getMessage());
            return null; 
        }
    }
    
    // formatDateToDbStandard is likely not needed for setting LocalDateTime entity fields.
    // If it's used for query parameters that expect a string date, it can be kept.
    // For now, we assume it's not directly used for FoundItem entity's foundDate.
    private String formatDateToDbStandard(String dateStr) {
        if (dateStr == null || dateStr.trim().isEmpty()) {
            return null;
        }
        dateStr = dateStr.trim();
        LocalDate parsedDate = null;
        try {
            if (dateStr.contains("-")) {
                parsedDate = LocalDate.parse(dateStr, INPUT_FORMATTER_YYYY_MM_DD);
            } else {
                parsedDate = LocalDate.parse(dateStr, INPUT_FORMATTER_YYYYMMDD);
            }
            return parsedDate.format(DateTimeFormatter.ISO_LOCAL_DATE); // Returns YYYY-MM-DD string
        } catch (DateTimeParseException e) {
            System.err.println("Invalid date format: " + dateStr + ". Expected yyyy-MM-dd or yyyyMMdd. Error: " + e.getMessage());
            return null; 
        }
    }
    
    @Transactional(readOnly = true)
    public List<FoundItemDto> getAllFoundItems() {
        return foundItemRepository.findAll(Sort.by(Sort.Direction.DESC, "atcId")).stream()
                .map(FoundItemDto::fromEntity)
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public Page<FoundItemDto> getAllFoundItems(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "atcId"));
        return foundItemRepository.findAll(pageable)
                .map(FoundItemDto::fromEntity);
    }
    
    @Transactional(readOnly = true)
    public FoundItemDto getFoundItemByAtcId(String atcId) {
        FoundItem foundItem = foundItemRepository.findByAtcId(atcId)
                .orElseThrow(() -> new ResourceNotFoundException("FoundItem", "atcId", atcId));
        return FoundItemDto.fromEntity(foundItem);
    }
    
    @Transactional
    public FoundItemDto createFoundItem(FoundItemDto foundItemDto) {
        FoundItem foundItem = FoundItem.builder()
                .atcId(foundItemDto.getAtcId())
                .fdPrdtNm(foundItemDto.getFdPrdtNm())
                .prdtClNm(foundItemDto.getPrdtClNm())
                .fdYmd(foundItemDto.getFdYmd()) // 이미 문자열 형식이므로 그대로 사용
                .fdSbjt(foundItemDto.getFdSbjt())
                .fdFilePathImg(null) // 이미지 경로는 DTO에 없으므로 null 처리
                .depPlace(foundItemDto.getDepPlace())
                .clrNm(foundItemDto.getClrNm())
                .fdSn(foundItemDto.getFdSn())
                .build();

        FoundItem savedFoundItem = foundItemRepository.save(foundItem);
        return FoundItemDto.fromEntity(savedFoundItem);
    }
    
    @Transactional
    public FoundItemDto updateFoundItem(String atcId, FoundItemDto foundItemDto) {
        FoundItem existingFoundItem = foundItemRepository.findByAtcId(atcId)
                .orElseThrow(() -> new ResourceNotFoundException("FoundItem", "atcId", atcId));
        
        // ID는 경로에서 가져온 것을 사용 (일반적으로 경로의 ID가 권한이 있음)
        existingFoundItem.setFdPrdtNm(foundItemDto.getFdPrdtNm());
        existingFoundItem.setPrdtClNm(foundItemDto.getPrdtClNm());
        existingFoundItem.setDepPlace(foundItemDto.getDepPlace());
        existingFoundItem.setFdYmd(foundItemDto.getFdYmd()); // 이미 문자열 형식이므로 그대로 사용
        existingFoundItem.setFdSbjt(foundItemDto.getFdSbjt());
        existingFoundItem.setClrNm(foundItemDto.getClrNm());
        existingFoundItem.setFdSn(foundItemDto.getFdSn());
        
        FoundItem updatedFoundItem = foundItemRepository.save(existingFoundItem);
        return FoundItemDto.fromEntity(updatedFoundItem);
    }
    
    @Transactional
    public void deleteFoundItem(String atcId) {
        if (!foundItemRepository.existsByAtcId(atcId)) {
            throw new ResourceNotFoundException("FoundItem", "atcId", atcId);
        }
        foundItemRepository.deleteByAtcId(atcId);
    }
    
    @Transactional(readOnly = true)
    public List<FoundItemDto> findByItemType(String itemType) {
        // Repository's findByPrdtClNm is searching by prdtClNm
        return foundItemRepository.findByPrdtClNm(itemType).stream()
                .map(FoundItemDto::fromEntity)
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public Page<FoundItemDto> findByItemType(String itemType, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        // Repository's findByPrdtClNm is searching by prdtClNm
        return foundItemRepository.findByPrdtClNm(itemType, pageable)
                .map(FoundItemDto::fromEntity);
    }
    
    @Transactional(readOnly = true)
    public List<FoundItemDto> findByLocation(String location) {
        // 'location' (FoundItem의 storagePlaceName)으로 검색하기 위해 searchByKeyword 사용
        return foundItemRepository.searchByKeyword(location).stream()
                .map(FoundItemDto::fromEntity)
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public Page<FoundItemDto> findByLocation(String location, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        // 'location' (FoundItem의 storagePlaceName)으로 검색하기 위해 searchByKeyword 사용
        return foundItemRepository.searchByKeyword(location, pageable)
                .map(FoundItemDto::fromEntity);
    }
    
    @Transactional(readOnly = true)
    public List<FoundItemDto> searchByKeyword(String keyword) {
        return foundItemRepository.searchByKeyword(keyword).stream()
                .map(FoundItemDto::fromEntity)
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public Page<FoundItemDto> searchByKeyword(String keyword, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return foundItemRepository.searchByKeyword(keyword, pageable)
                .map(FoundItemDto::fromEntity);
    }
    
    @Transactional(readOnly = true)
    public List<FoundItemDto> findRecentFoundItems(String itemType, int days) {
        LocalDateTime startDate = LocalDateTime.now().minusDays(days);
        String startDateStr = startDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        return foundItemRepository.findRecentFoundItemsByType(itemType, startDateStr).stream()
                .map(FoundItemDto::fromEntity)
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public Page<FoundItemDto> findRecentFoundItems(String itemType, int days, int page, int size) {
        LocalDateTime startDate = LocalDateTime.now().minusDays(days);
        String startDateStr = startDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        Pageable pageable = PageRequest.of(page, size);
        return foundItemRepository.findRecentFoundItemsByType(itemType, startDateStr, pageable)
                .map(FoundItemDto::fromEntity);
    }
    
    @Transactional(readOnly = true)
    public Page<FoundItemDto> findByDateRange(LocalDateTime start, LocalDateTime end, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        String startStr = start.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        String endStr = end.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        return foundItemRepository.findByFdYmdBetween(startStr, endStr, pageable)
                .map(FoundItemDto::fromEntity);
    }
}
