-- Indexes to optimize frequent search patterns
CREATE INDEX IF NOT EXISTS idx_lost_items_prdt_cl_nm ON lost_items (prdt_cl_nm);
CREATE INDEX IF NOT EXISTS idx_lost_items_lst_place ON lost_items (lst_place);
CREATE INDEX IF NOT EXISTS idx_lost_items_lst_ymd ON lost_items (lst_ymd);

CREATE INDEX IF NOT EXISTS idx_found_items_prdt_cl_nm ON found_items (prdt_cl_nm);
CREATE INDEX IF NOT EXISTS idx_found_items_fd_ymd ON found_items (fd_ymd);
CREATE INDEX IF NOT EXISTS idx_found_items_dep_place ON found_items (dep_place);
