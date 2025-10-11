package com.findit.server.domain.founditem;

import java.util.Objects;

/**
 * 습득물 관리 ID 값 객체.
 */
public final class FoundItemId {

    private final String value;

    private FoundItemId(String value) {
        this.value = value;
    }

    public static FoundItemId of(String raw) {
        Objects.requireNonNull(raw, "습득물 ID는 null일 수 없습니다.");
        String trimmed = raw.trim();
        if (trimmed.isEmpty()) {
            throw new IllegalArgumentException("습득물 ID는 비어 있을 수 없습니다.");
        }
        return new FoundItemId(trimmed);
    }

    public String value() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FoundItemId foundItemId = (FoundItemId) o;
        return value.equals(foundItemId.value);
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
