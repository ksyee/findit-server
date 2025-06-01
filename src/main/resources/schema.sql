-- 테이블 삭제 (이미 존재하는 경우에만)
DROP TABLE IF EXISTS lost_item;
DROP TABLE IF EXISTS found_item;

-- 테이블 생성 (이미 존재하지 않는 경우에만)
CREATE TABLE IF NOT EXISTS lost_item (
    atc_id VARCHAR(50) PRIMARY KEY,
    slt_prdt_nm VARCHAR(255),
    prdt_cl_nm VARCHAR(255),
    lst_place VARCHAR(255),
    lst_ymd VARCHAR(8),
    slt_sbjt TEXT,
    rnum VARCHAR(100)
);

CREATE TABLE IF NOT EXISTS found_item (
    atc_id VARCHAR(50) PRIMARY KEY,
    fd_prdt_nm VARCHAR(255),
    prdt_cl_nm VARCHAR(100),
    fd_ymd VARCHAR(8),
    fd_sbjt TEXT,
    fd_file_path_img VARCHAR(500),
    dep_place VARCHAR(200),
    clr_nm VARCHAR(50),
    fd_sn VARCHAR(50)
);

-- 인덱스 추가 (이미 존재하지 않는 경우에만)
CREATE INDEX IF NOT EXISTS idx_lost_item_atc_id ON lost_item(atc_id);
CREATE INDEX IF NOT EXISTS idx_found_item_atc_id ON found_item(atc_id);

CREATE INDEX IF NOT EXISTS idx_lost_item_prdt_cl_nm ON lost_item(prdt_cl_nm);
CREATE INDEX IF NOT EXISTS idx_found_item_prdt_cl_nm ON found_item(prdt_cl_nm);

CREATE INDEX IF NOT EXISTS idx_lost_item_lst_ymd ON lost_item(lst_ymd);
CREATE INDEX IF NOT EXISTS idx_found_item_fd_ymd ON found_item(fd_ymd);
