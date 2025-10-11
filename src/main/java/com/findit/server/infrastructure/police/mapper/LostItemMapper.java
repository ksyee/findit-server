package com.findit.server.infrastructure.police.mapper;

import com.findit.server.domain.lostitem.LostDate;
import com.findit.server.domain.lostitem.LostItem;
import com.findit.server.domain.lostitem.LostItemId;
import com.findit.server.domain.shared.ItemCategory;
import com.findit.server.domain.shared.LocationName;
import com.findit.server.infrastructure.police.dto.PoliceApiLostItem;
import org.springframework.stereotype.Component;

/**
 * 경찰청 API 분실물 데이터를 내부 엔티티로 매핑하는 매퍼
 */
@Component
public class LostItemMapper implements ApiMapper<PoliceApiLostItem, LostItem> {

    @Override
    public LostItem map(PoliceApiLostItem source) {
        if (source == null) {
            return null;
        }

        String lostDate = source.getLostDate();
        if (isBlank(source.getLostItemId()) || isBlank(source.getLostItemCategory())
            || isBlank(source.getLostPlace()) || isBlank(lostDate)) {
            return null;
        }

        return LostItem.create(
            LostItemId.of(source.getLostItemId()),
            ItemCategory.of(source.getLostItemCategory()),
            LocationName.of(source.getLostPlace()),
            LostDate.of(lostDate),
            source.getLostItemName(),
            source.getLostItemDescription(),
            source.getLostItemRnum()
        );
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}
