package com.findit.server.repository;

import com.findit.server.entity.LostItem;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class LostItemRepositoryImpl implements LostItemRepositoryCustom {

    private final JdbcTemplate jdbcTemplate;

    @Override
    @Transactional
    public void upsertBatch(List<LostItem> items) {
        if (items.isEmpty()) return;
        final String sql = """
                INSERT INTO lost_items (atc_id, prdt_cl_nm, lst_place, lst_ymd, lst_prdt_nm, lst_sbjt, rnum)
                VALUES (?, ?, ?, ?, ?, ?, ?)
                ON DUPLICATE KEY UPDATE
                  prdt_cl_nm = VALUES(prdt_cl_nm),
                  lst_place  = VALUES(lst_place),
                  lst_ymd    = VALUES(lst_ymd),
                  lst_prdt_nm= VALUES(lst_prdt_nm),
                  lst_sbjt   = VALUES(lst_sbjt),
                  rnum       = VALUES(rnum)
                """;
        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                LostItem item = items.get(i);
                ps.setString(1, item.getAtcId());
                ps.setString(2, item.getPrdtClNm());
                ps.setString(3, item.getLstPlace());
                ps.setString(4, item.getLstYmd());
                ps.setString(5, item.getLstPrdtNm());
                ps.setString(6, item.getLstSbjt());
                ps.setString(7, item.getRnum());
            }

            @Override
            public int getBatchSize() {
                return items.size();
            }
        });
    }
}
