package com.findit.server.repository;

import com.findit.server.entity.LostItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface LostItemRepository extends JpaRepository<LostItem, String> {
    
    List<LostItem> findByPrdtClNm(String prdtClNm);
    
    Page<LostItem> findByPrdtClNm(String prdtClNm, Pageable pageable);
    
    List<LostItem> findByLstPlaceContaining(String lstPlace);
    
    Page<LostItem> findByLstPlaceContaining(String lstPlace, Pageable pageable);
    
    List<LostItem> findByLstYmdBetween(LocalDateTime start, LocalDateTime end);
    
    Page<LostItem> findByLstYmdBetween(LocalDateTime start, LocalDateTime end, Pageable pageable);
    
    Optional<LostItem> findByAtcId(String atcId); 
    
    boolean existsByAtcId(String atcId); 
    
    void deleteByAtcId(String atcId);
    
    @Query("SELECT l FROM LostItem l WHERE l.sltSbjt LIKE %:keyword% OR l.lstPlace LIKE %:keyword% OR l.sltPrdtNm LIKE %:keyword%")
    List<LostItem> searchByKeyword(@Param("keyword") String keyword);
    
    @Query("SELECT l FROM LostItem l WHERE l.sltSbjt LIKE %:keyword% OR l.lstPlace LIKE %:keyword% OR l.sltPrdtNm LIKE %:keyword%")
    Page<LostItem> searchByKeyword(@Param("keyword") String keyword, Pageable pageable);
    
    @Query("SELECT l FROM LostItem l WHERE l.prdtClNm = :itemType AND l.lstYmd >= :startDate")
    List<LostItem> findRecentLostItemsByType(@Param("itemType") String itemType, @Param("startDate") LocalDateTime startDate);
    
    @Query("SELECT l FROM LostItem l WHERE l.prdtClNm = :itemType AND l.lstYmd >= :startDate")
    Page<LostItem> findRecentLostItemsByType(@Param("itemType") String itemType, @Param("startDate") LocalDateTime startDate, Pageable pageable);
}
