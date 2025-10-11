package com.findit.server.domain.lostitem;

import java.util.Objects;

/**
 * 분실물 관리 ID 값 객체.
 */
public final class LostItemId {

    private final String value;

    private LostItemId(String value) {
        this.value = value;
    }

    public static LostItemId of(String raw) {
        Objects.requireNonNull(raw, "분실물 ID는 null일 수 없습니다.");
        String trimmed = raw.trim();
        if (trimmed.isEmpty()) {
            throw new IllegalArgumentException("분실물 ID는 비어 있을 수 없습니다.");
        }
        return new LostItemId(trimmed);
    }

    public String value() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LostItemId lostItemId = (LostItemId) o;
        return value.equals(lostItemId.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public String toString() {
        return value;
    }
}
