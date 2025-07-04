package com.findit.server.repository;

import com.findit.server.entity.FoundItem;
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
public class FoundItemRepositoryImpl implements FoundItemRepositoryCustom {

    private final JdbcTemplate jdbcTemplate;

    @Override
    @Transactional
    public void upsertBatch(List<FoundItem> items) {
        if (items.isEmpty()) return;
        final String sql = """
                INSERT INTO found_items (atc_id, fd_prdt_nm, prdt_cl_nm, fd_ymd, fd_sbjt, fd_file_path_img, dep_place, clr_nm, fd_sn)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
                ON DUPLICATE KEY UPDATE
                  fd_prdt_nm     = VALUES(fd_prdt_nm),
                  prdt_cl_nm     = VALUES(prdt_cl_nm),
                  fd_ymd         = VALUES(fd_ymd),
                  fd_sbjt        = VALUES(fd_sbjt),
                  fd_file_path_img = VALUES(fd_file_path_img),
                  dep_place      = VALUES(dep_place),
                  clr_nm         = VALUES(clr_nm),
                  fd_sn          = VALUES(fd_sn)
                """;
        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                FoundItem item = items.get(i);
                ps.setString(1, item.getAtcId());
                ps.setString(2, item.getFdPrdtNm());
                ps.setString(3, item.getPrdtClNm());
                ps.setString(4, item.getFdYmd());
                ps.setString(5, item.getFdSbjt());
                ps.setString(6, item.getFdFilePathImg());
                ps.setString(7, item.getDepPlace());
                ps.setString(8, item.getClrNm());
                ps.setString(9, item.getFdSn());
            }

            @Override
            public int getBatchSize() {
                return items.size();
            }
        });
    }
}
