package com.findit.server.infrastructure.persistence.founditem;

import com.findit.server.domain.founditem.FoundItem;
import java.util.List;

/**
 * Custom repository interface for batch upsert operations on {@link FoundItem}.
 */
public interface FoundItemJpaRepositoryCustom {
    /**
     * Batch upsert for found items.
     * @param items entities to insert or update
     */
    void upsertBatch(List<FoundItem> items);
}
