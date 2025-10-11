package com.findit.server.application.lostitem;

import com.findit.server.application.lostitem.dto.LostItemDto;
import com.findit.server.domain.lostitem.LostDate;
import com.findit.server.domain.lostitem.LostItem;
import com.findit.server.domain.lostitem.LostItemId;
import com.findit.server.domain.lostitem.LostItemRepository;
import com.findit.server.domain.shared.ItemCategory;
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
public class LostItemApplicationService {

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
        try {
            LostItem lostItem = LostItem.create(
                LostItemId.of(lostItemDto.getAtcId()),
                ItemCategory.of(lostItemDto.getPrdtClNm()),
                LocationName.of(lostItemDto.getLstPlace()),
                LostDate.of(lostItemDto.getLstYmd()),
                null,
                null,
                lostItemDto.getRnum()
            );

            LostItem savedLostItem = lostItemRepository.save(lostItem);
            return LostItemDto.fromEntity(savedLostItem);
        } catch (IllegalArgumentException e) {
            throw new InvalidRequestException(e.getMessage());
        }
    }

    @Transactional
    public LostItemDto updateLostItem(String atcId, LostItemDto lostItemDto) {
        LostItem existingLostItem = lostItemRepository.findByAtcId(atcId)
            .orElseThrow(() -> new ResourceNotFoundException("LostItem", "atcId", atcId));
        try {
            existingLostItem.updateCoreDetails(
                ItemCategory.of(lostItemDto.getPrdtClNm()),
                LocationName.of(lostItemDto.getLstPlace()),
                LostDate.of(lostItemDto.getLstYmd()),
                lostItemDto.getRnum()
            );
        } catch (IllegalArgumentException e) {
            throw new InvalidRequestException(e.getMessage());
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
