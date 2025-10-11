package com.findit.server.domain.shared;

import java.util.Objects;

/**
 * 물품 분류명을 표현하는 값 객체.
 */
public final class ItemCategory {

    private static final int MAX_LENGTH = 100;

    private final String value;

    private ItemCategory(String value) {
        this.value = value;
    }

    public static ItemCategory of(String raw) {
        Objects.requireNonNull(raw, "물품 분류명은 null일 수 없습니다.");
        String trimmed = raw.trim();
        if (trimmed.isEmpty()) {
            throw new IllegalArgumentException("물품 분류명은 비어 있을 수 없습니다.");
        }
        if (trimmed.length() > MAX_LENGTH) {
            throw new IllegalArgumentException("물품 분류명은 " + MAX_LENGTH + "자를 초과할 수 없습니다.");
        }
        return new ItemCategory(trimmed);
    }

    public String value() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ItemCategory that = (ItemCategory) o;
        return value.equals(that.value);
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
