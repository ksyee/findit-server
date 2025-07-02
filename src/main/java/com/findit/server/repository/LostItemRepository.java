package com.findit.server.repository;

import com.findit.server.entity.LostItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LostItemRepository extends JpaRepository<LostItem, String>, LostItemRepositoryCustom {
    
    List<LostItem> findByPrdtClNm(String prdtClNm);
    
    Page<LostItem> findByPrdtClNm(String prdtClNm, Pageable pageable);
    
    List<LostItem> findByLstPlaceContaining(String lstPlace);
    
    Page<LostItem> findByLstPlaceContaining(String lstPlace, Pageable pageable);
    
    List<LostItem> findByLstYmdBetween(String startYmd, String endYmd);
    
    Page<LostItem> findByLstYmdBetween(String startYmd, String endYmd, Pageable pageable);
    
    Optional<LostItem> findByAtcId(String atcId); 
    
    boolean existsByAtcId(String atcId); 
    
    void deleteByAtcId(String atcId);
    
    @Query("SELECT l FROM LostItem l WHERE l.lstPlace LIKE %:keyword%")
    List<LostItem> searchByKeyword(@Param("keyword") String keyword);
    
    @Query("SELECT l FROM LostItem l WHERE l.lstPlace LIKE %:keyword%")
    Page<LostItem> searchByKeyword(@Param("keyword") String keyword, Pageable pageable);
    
    @Query("SELECT l FROM LostItem l WHERE l.prdtClNm = :itemType AND l.lstYmd >= :startYmd")
    List<LostItem> findRecentLostItemsByType(@Param("itemType") String itemType, @Param("startYmd") String startYmd);
    
    @Query("SELECT l FROM LostItem l WHERE l.prdtClNm = :itemType AND l.lstYmd >= :startYmd")
    Page<LostItem> findRecentLostItemsByType(@Param("itemType") String itemType, @Param("startYmd") String startYmd, Pageable pageable);
}
