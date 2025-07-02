package com.findit.server.repository;

import com.findit.server.entity.LostItem;
import java.util.List;

/**
 * Custom repository interface for batch upsert operations on {@link LostItem}.
 */
public interface LostItemRepositoryCustom {
    /**
     * Inserts new rows or updates existing rows matched by primary/unique key.
     *
     * @param items list of {@link LostItem} entities to upsert in batch
     */
    void upsertBatch(List<LostItem> items);
}
