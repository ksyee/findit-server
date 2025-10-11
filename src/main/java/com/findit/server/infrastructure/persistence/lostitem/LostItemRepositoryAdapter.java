package com.findit.server.infrastructure.persistence.lostitem;

import com.findit.server.domain.lostitem.LostItem;
import com.findit.server.domain.lostitem.LostItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class LostItemRepositoryAdapter implements LostItemRepository {

    private final LostItemJpaRepository lostItemJpaRepository;

    @Override
    public List<LostItem> findAll(Sort sort) {
        return lostItemJpaRepository.findAll(sort);
    }

    @Override
    public Page<LostItem> findAll(Pageable pageable) {
        return lostItemJpaRepository.findAll(pageable);
    }

    @Override
    public Optional<LostItem> findByAtcId(String atcId) {
        return lostItemJpaRepository.findByAtcId(atcId);
    }

    @Override
    public LostItem save(LostItem lostItem) {
        return lostItemJpaRepository.save(lostItem);
    }

    @Override
    public void deleteByAtcId(String atcId) {
        lostItemJpaRepository.deleteByAtcId(atcId);
    }

    @Override
    public boolean existsByAtcId(String atcId) {
        return lostItemJpaRepository.existsByAtcId(atcId);
    }

    @Override
    public List<LostItem> findByPrdtClNm(String prdtClNm) {
        return lostItemJpaRepository.findByPrdtClNm(prdtClNm);
    }

    @Override
    public Page<LostItem> findByPrdtClNm(String prdtClNm, Pageable pageable) {
        return lostItemJpaRepository.findByPrdtClNm(prdtClNm, pageable);
    }

    @Override
    public Page<LostItem> findByLstPlaceContaining(String lstPlace, Pageable pageable) {
        return lostItemJpaRepository.findByLstPlaceContaining(lstPlace, pageable);
    }

    @Override
    public Page<LostItem> findByLstYmdBetween(String startYmd, String endYmd, Pageable pageable) {
        return lostItemJpaRepository.findByLstYmdBetween(startYmd, endYmd, pageable);
    }

    @Override
    public List<LostItem> searchByKeyword(String keyword) {
        return lostItemJpaRepository.searchByKeyword(keyword);
    }

    @Override
    public Page<LostItem> searchByKeyword(String keyword, Pageable pageable) {
        return lostItemJpaRepository.searchByKeyword(keyword, pageable);
    }

    @Override
    public List<LostItem> findRecentLostItemsByType(String itemType, String startYmd) {
        return lostItemJpaRepository.findRecentLostItemsByType(itemType, startYmd);
    }

    @Override
    public Page<LostItem> findRecentLostItemsByType(String itemType, String startYmd, Pageable pageable) {
        return lostItemJpaRepository.findRecentLostItemsByType(itemType, startYmd, pageable);
    }

    @Override
    public void upsertBatch(List<LostItem> items) {
        lostItemJpaRepository.upsertBatch(items);
    }
}
