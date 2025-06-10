-- 분실물 테이블에 새로운 컬럼 추가
ALTER TABLE lost_items
ADD COLUMN external_id VARCHAR(255) NULL,
ADD COLUMN item_name VARCHAR(255) NULL,
ADD COLUMN source VARCHAR(50) NULL;

-- 습득물 테이블에 새로운 컬럼 추가
ALTER TABLE found_items
ADD COLUMN external_id VARCHAR(255) NULL,
ADD COLUMN item_name VARCHAR(255) NULL,
ADD COLUMN storage_place_name VARCHAR(255) NULL,
ADD COLUMN storage_address VARCHAR(255) NULL,
ADD COLUMN storage_contact VARCHAR(100) NULL,
ADD COLUMN source VARCHAR(50) NULL;

-- external_id에 인덱스 추가
CREATE INDEX idx_lost_items_external_id ON lost_items(external_id);
CREATE INDEX idx_found_items_external_id ON found_items(external_id);

-- source에 인덱스 추가
CREATE INDEX idx_lost_items_source ON lost_items(source);
CREATE INDEX idx_found_items_source ON found_items(source);
