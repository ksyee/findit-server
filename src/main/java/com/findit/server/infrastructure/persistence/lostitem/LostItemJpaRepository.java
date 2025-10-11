package com.findit.server.infrastructure.persistence.lostitem;

import com.findit.server.domain.lostitem.LostItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LostItemJpaRepository extends JpaRepository<LostItem, String>, LostItemJpaRepositoryCustom {
    
    List<LostItem> findByPrdtClNm(String prdtClNm);
    
    Page<LostItem> findByPrdtClNm(String prdtClNm, Pageable pageable);
    
    List<LostItem> findByLstPlaceContaining(String lstPlace);
    
    Page<LostItem> findByLstPlaceContaining(String lstPlace, Pageable pageable);
    
    List<LostItem> findByLstYmdBetween(String startYmd, String endYmd);
    
    Page<LostItem> findByLstYmdBetween(String startYmd, String endYmd, Pageable pageable);
    
    Optional<LostItem> findByAtcId(String atcId); 
    
    boolean existsByAtcId(String atcId); 
    
    void deleteByAtcId(String atcId);
    
    @Query("SELECT l FROM LostItem l WHERE l.lstPlace LIKE %:keyword% OR l.lstPrdtNm LIKE %:keyword% OR l.lstSbjt LIKE %:keyword% OR l.prdtClNm LIKE %:keyword%")
    List<LostItem> searchByKeyword(@Param("keyword") String keyword);
    
    @Query("SELECT l FROM LostItem l WHERE l.lstPlace LIKE %:keyword% OR l.lstPrdtNm LIKE %:keyword% OR l.lstSbjt LIKE %:keyword% OR l.prdtClNm LIKE %:keyword%")
    Page<LostItem> searchByKeyword(@Param("keyword") String keyword, Pageable pageable);
    
    @Query("SELECT l FROM LostItem l WHERE l.prdtClNm = :itemType AND l.lstYmd >= :startYmd")
    List<LostItem> findRecentLostItemsByType(@Param("itemType") String itemType, @Param("startYmd") String startYmd);
    
    @Query("SELECT l FROM LostItem l WHERE l.prdtClNm = :itemType AND l.lstYmd >= :startYmd")
    Page<LostItem> findRecentLostItemsByType(@Param("itemType") String itemType, @Param("startYmd") String startYmd, Pageable pageable);
}
