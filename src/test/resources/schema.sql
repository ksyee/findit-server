DROP TABLE IF EXISTS lost_item;

CREATE TABLE lost_item (
    atc_id VARCHAR(20) PRIMARY KEY,
    slt_prdt_nm VARCHAR(255),
    prdt_cl_nm VARCHAR(100),
    lst_place VARCHAR(255),
    lst_ymd TIMESTAMP,
    slt_sbjt TEXT,
    rnum VARCHAR(10),
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP
);
