package com.findit.server.infrastructure.persistence.founditem;

import com.findit.server.domain.founditem.FoundItem;
import com.findit.server.domain.founditem.FoundItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class FoundItemRepositoryAdapter implements FoundItemRepository {

    private final FoundItemJpaRepository foundItemJpaRepository;

    @Override
    public List<FoundItem> findAll(Sort sort) {
        return foundItemJpaRepository.findAll(sort);
    }

    @Override
    public Page<FoundItem> findAll(Pageable pageable) {
        return foundItemJpaRepository.findAll(pageable);
    }

    @Override
    public Optional<FoundItem> findByAtcId(String atcId) {
        return foundItemJpaRepository.findByAtcId(atcId);
    }

    @Override
    public FoundItem save(FoundItem foundItem) {
        return foundItemJpaRepository.save(foundItem);
    }

    @Override
    public void deleteByAtcId(String atcId) {
        foundItemJpaRepository.deleteByAtcId(atcId);
    }

    @Override
    public boolean existsByAtcId(String atcId) {
        return foundItemJpaRepository.existsByAtcId(atcId);
    }

    @Override
    public List<FoundItem> findByPrdtClNm(String prdtClNm) {
        return foundItemJpaRepository.findByPrdtClNm(prdtClNm);
    }

    @Override
    public Page<FoundItem> findByPrdtClNm(String prdtClNm, Pageable pageable) {
        return foundItemJpaRepository.findByPrdtClNm(prdtClNm, pageable);
    }

    @Override
    public Page<FoundItem> findByFdYmdBetween(String start, String end, Pageable pageable) {
        return foundItemJpaRepository.findByFdYmdBetween(start, end, pageable);
    }

    @Override
    public List<FoundItem> searchByKeyword(String keyword) {
        return foundItemJpaRepository.searchByKeyword(keyword);
    }

    @Override
    public Page<FoundItem> searchByKeyword(String keyword, Pageable pageable) {
        return foundItemJpaRepository.searchByKeyword(keyword, pageable);
    }

    @Override
    public void upsertBatch(List<FoundItem> items) {
        foundItemJpaRepository.upsertBatch(items);
    }

    @Override
    public List<FoundItem> findRecentFoundItemsByType(String itemType, String startDate) {
        return foundItemJpaRepository.findRecentFoundItemsByType(itemType, startDate);
    }

    @Override
    public Page<FoundItem> findRecentFoundItemsByType(String itemType, String startDate, Pageable pageable) {
        return foundItemJpaRepository.findRecentFoundItemsByType(itemType, startDate, pageable);
    }
}
