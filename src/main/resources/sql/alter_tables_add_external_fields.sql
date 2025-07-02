-- external_id에 인덱스 추가
CREATE INDEX idx_lost_items_external_id ON lost_items(external_id);
CREATE INDEX idx_found_items_external_id ON found_items(external_id);

-- source에 인덱스 추가
CREATE INDEX idx_lost_items_source ON lost_items(source);
CREATE INDEX idx_found_items_source ON found_items(source);
