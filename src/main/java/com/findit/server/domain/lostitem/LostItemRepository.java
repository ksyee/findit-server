package com.findit.server.domain.lostitem;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;

public interface LostItemRepository {

    List<LostItem> findAll(Sort sort);

    Page<LostItem> findAll(Pageable pageable);

    Optional<LostItem> findByAtcId(String atcId);

    LostItem save(LostItem lostItem);

    void deleteByAtcId(String atcId);

    boolean existsByAtcId(String atcId);

    List<LostItem> findByPrdtClNm(String prdtClNm);

    Page<LostItem> findByPrdtClNm(String prdtClNm, Pageable pageable);

    Page<LostItem> findByLstPlaceContaining(String lstPlace, Pageable pageable);

    Page<LostItem> findByLstYmdBetween(String startYmd, String endYmd, Pageable pageable);

    List<LostItem> searchByKeyword(String keyword);

    Page<LostItem> searchByKeyword(String keyword, Pageable pageable);

    List<LostItem> findRecentLostItemsByType(String itemType, String startYmd);

    Page<LostItem> findRecentLostItemsByType(String itemType, String startYmd, Pageable pageable);

    void upsertBatch(List<LostItem> items);
}
