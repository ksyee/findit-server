package com.findit.server.repository;

import com.findit.server.entity.FoundItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FoundItemRepository extends JpaRepository<FoundItem, String>, FoundItemRepositoryCustom {
    
    List<FoundItem> findByPrdtClNm(String prdtClNm);
    
    Page<FoundItem> findByPrdtClNm(String prdtClNm, Pageable pageable);
    
    List<FoundItem> findByFdYmdBetween(String start, String end);
    
    Page<FoundItem> findByFdYmdBetween(String start, String end, Pageable pageable);
    
    Optional<FoundItem> findByAtcId(String atcId); // 새로운 ID 필드용 메서드 추가
    
    boolean existsByAtcId(String atcId); // 새로운 ID 필드용 메서드 추가
    
    void deleteByAtcId(String atcId); // Added method for deleting by atcId
    
    @Query("SELECT f FROM FoundItem f WHERE f.fdSbjt LIKE %:keyword% OR f.depPlace LIKE %:keyword%") 
    List<FoundItem> searchByKeyword(@Param("keyword") String keyword);
    
    @Query("SELECT f FROM FoundItem f WHERE f.fdSbjt LIKE %:keyword% OR f.depPlace LIKE %:keyword%")
    Page<FoundItem> searchByKeyword(@Param("keyword") String keyword, Pageable pageable);
    
    @Query("SELECT f FROM FoundItem f WHERE f.prdtClNm = :prdtClNm AND f.fdYmd >= :startDate")
    List<FoundItem> findRecentFoundItemsByType(@Param("prdtClNm") String prdtClNm, @Param("startDate") String startDate);
    
    @Query("SELECT f FROM FoundItem f WHERE f.prdtClNm = :prdtClNm AND f.fdYmd >= :startDate")
    Page<FoundItem> findRecentFoundItemsByType(@Param("prdtClNm") String prdtClNm, @Param("startDate") String startDate, Pageable pageable);
}
