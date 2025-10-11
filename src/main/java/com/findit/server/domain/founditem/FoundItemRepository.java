package com.findit.server.domain.founditem;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;

public interface FoundItemRepository {

    List<FoundItem> findAll(Sort sort);

    Page<FoundItem> findAll(Pageable pageable);

    Optional<FoundItem> findByAtcId(String atcId);

    FoundItem save(FoundItem foundItem);

    void deleteByAtcId(String atcId);

    boolean existsByAtcId(String atcId);

    List<FoundItem> findByPrdtClNm(String prdtClNm);

    Page<FoundItem> findByPrdtClNm(String prdtClNm, Pageable pageable);

    Page<FoundItem> findByFdYmdBetween(String start, String end, Pageable pageable);

    List<FoundItem> searchByKeyword(String keyword);

    Page<FoundItem> searchByKeyword(String keyword, Pageable pageable);

    void upsertBatch(List<FoundItem> items);

    List<FoundItem> findRecentFoundItemsByType(String itemType, String startDate);

    Page<FoundItem> findRecentFoundItemsByType(String itemType, String startDate, Pageable pageable);
}
