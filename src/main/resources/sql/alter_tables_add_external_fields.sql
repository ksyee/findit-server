-- 분실물 테이블에 새로운 컬럼 추가
ALTER TABLE lost_item
ADD COLUMN external_id VARCHAR(255) NULL,
ADD COLUMN item_name VARCHAR(255) NULL,
ADD COLUMN source VARCHAR(50) NULL;

-- 습득물 테이블에 새로운 컬럼 추가
ALTER TABLE find_item
ADD COLUMN external_id VARCHAR(255) NULL,
ADD COLUMN item_name VARCHAR(255) NULL,
ADD COLUMN storage_place_name VARCHAR(255) NULL,
ADD COLUMN storage_address VARCHAR(255) NULL,
ADD COLUMN storage_contact VARCHAR(100) NULL,
ADD COLUMN source VARCHAR(50) NULL;

-- external_id에 인덱스 추가
CREATE INDEX idx_lost_item_external_id ON lost_item(external_id);
CREATE INDEX idx_find_item_external_id ON find_item(external_id);

-- source에 인덱스 추가
CREATE INDEX idx_lost_item_source ON lost_item(source);
CREATE INDEX idx_find_item_source ON find_item(source);
