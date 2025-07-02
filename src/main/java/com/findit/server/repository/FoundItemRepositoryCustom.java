package com.findit.server.repository;

import com.findit.server.entity.FoundItem;
import java.util.List;

/**
 * Custom repository interface for batch upsert operations on {@link FoundItem}.
 */
public interface FoundItemRepositoryCustom {
    /**
     * Batch upsert for found items.
     * @param items entities to insert or update
     */
    void upsertBatch(List<FoundItem> items);
}
