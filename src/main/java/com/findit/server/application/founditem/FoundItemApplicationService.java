package com.findit.server.application.founditem;

import com.findit.server.application.founditem.dto.FoundItemDto;
import com.findit.server.domain.founditem.FoundDate;
import com.findit.server.domain.founditem.FoundItem;
import com.findit.server.domain.founditem.FoundItemId;
import com.findit.server.domain.founditem.FoundItemRepository;
import com.findit.server.domain.shared.ItemCategory;
import com.findit.server.domain.shared.ItemName;
import com.findit.server.domain.shared.LocationName;
import com.findit.server.exception.InvalidRequestException;
import com.findit.server.exception.ResourceNotFoundException;
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
public class FoundItemApplicationService {

    private final FoundItemRepository foundItemRepository;

    @Transactional(readOnly = true)
    public List<FoundItemDto> getAllFoundItems() {
        return foundItemRepository.findAll(Sort.by(Sort.Direction.DESC, "atcId")).stream()
            .map(FoundItemDto::fromEntity)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Page<FoundItemDto> getAllFoundItems(int page, int size) {
        Pageable pageable = pageOf(page, size, Sort.by(Sort.Direction.DESC, "atcId"));
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
        try {
            FoundItem foundItem = FoundItem.create(
                FoundItemId.of(foundItemDto.atcId()),
                ItemName.of(foundItemDto.fdPrdtNm()),
                ItemCategory.of(foundItemDto.prdtClNm()),
                FoundDate.of(foundItemDto.fdYmd()),
                LocationName.of(foundItemDto.depPlace()),
                foundItemDto.fdSbjt(),
                null,
                foundItemDto.clrNm(),
                foundItemDto.fdSn()
            );

            FoundItem savedFoundItem = foundItemRepository.save(foundItem);
            return FoundItemDto.fromEntity(savedFoundItem);
        } catch (IllegalArgumentException e) {
            throw new InvalidRequestException(e.getMessage());
        }
    }

    @Transactional
    public FoundItemDto updateFoundItem(String atcId, FoundItemDto foundItemDto) {
        FoundItem existingFoundItem = foundItemRepository.findByAtcId(atcId)
            .orElseThrow(() -> new ResourceNotFoundException("FoundItem", "atcId", atcId));
        try {
            existingFoundItem.updateCoreDetails(
                ItemName.of(foundItemDto.fdPrdtNm()),
                ItemCategory.of(foundItemDto.prdtClNm()),
                FoundDate.of(foundItemDto.fdYmd()),
                LocationName.of(foundItemDto.depPlace()),
                foundItemDto.fdSbjt(),
                foundItemDto.clrNm(),
                foundItemDto.fdSn()
            );
        } catch (IllegalArgumentException e) {
            throw new InvalidRequestException(e.getMessage());
        }

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
        return foundItemRepository.findByPrdtClNm(itemType).stream()
            .map(FoundItemDto::fromEntity)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Page<FoundItemDto> findByItemType(String itemType, int page, int size) {
        Pageable pageable = pageOf(page, size);
        return foundItemRepository.findByPrdtClNm(itemType, pageable)
            .map(FoundItemDto::fromEntity);
    }

    @Transactional(readOnly = true)
    public List<FoundItemDto> findByLocation(String location) {
        return foundItemRepository.searchByKeyword(location).stream()
            .map(FoundItemDto::fromEntity)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Page<FoundItemDto> findByLocation(String location, int page, int size) {
        Pageable pageable = pageOf(page, size);
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
        Pageable pageable = pageOf(page, size);
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
    public Page<FoundItemDto> findByDateRange(LocalDateTime start, LocalDateTime end, int page, int size) {
        Pageable pageable = pageOf(page, size);
        String startStr = start.format(DateTimeFormatter.BASIC_ISO_DATE);
        String endStr = end.format(DateTimeFormatter.BASIC_ISO_DATE);
        return foundItemRepository.findByFdYmdBetween(startStr, endStr, pageable)
            .map(FoundItemDto::fromEntity);
    }

    @Transactional(readOnly = true)
    public Page<FoundItemDto> findRecentFoundItems(String itemType, int days, int page, int size) {
        LocalDateTime startDate = LocalDateTime.now().minusDays(days);
        String startDateStr = startDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        Pageable pageable = pageOf(page, size);
        return foundItemRepository.findRecentFoundItemsByType(itemType, startDateStr, pageable)
            .map(FoundItemDto::fromEntity);
    }

    private Pageable pageOf(int page, int size) {
        return PageRequest.of(page, size);
    }

    private Pageable pageOf(int page, int size, Sort sort) {
        return PageRequest.of(page, size, sort);
    }
}
