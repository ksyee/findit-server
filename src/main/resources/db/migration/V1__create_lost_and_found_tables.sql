-- Baseline schema for core domain tables
CREATE TABLE IF NOT EXISTS lost_items (
    atc_id VARCHAR(50) PRIMARY KEY,
    prdt_cl_nm VARCHAR(255),
    lst_place VARCHAR(255),
    lst_ymd VARCHAR(10),
    lst_prdt_nm VARCHAR(255),
    lst_sbjt TEXT,
    rnum VARCHAR(100)
);

CREATE TABLE IF NOT EXISTS found_items (
    atc_id VARCHAR(50) PRIMARY KEY,
    fd_prdt_nm VARCHAR(255),
    prdt_cl_nm VARCHAR(100),
    fd_ymd VARCHAR(10),
    fd_sbjt TEXT,
    fd_file_path_img VARCHAR(500),
    dep_place VARCHAR(200),
    clr_nm VARCHAR(50),
    fd_sn VARCHAR(50)
);
