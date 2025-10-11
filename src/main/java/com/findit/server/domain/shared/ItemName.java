package com.findit.server.domain.shared;

import java.util.Objects;

/**
 * 물품 이름을 표현하는 값 객체.
 */
public final class ItemName {

    private static final int MAX_LENGTH = 255;

    private final String value;

    private ItemName(String value) {
        this.value = value;
    }

    public static ItemName of(String raw) {
        Objects.requireNonNull(raw, "물품 이름은 null일 수 없습니다.");
        String trimmed = raw.trim();
        if (trimmed.isEmpty()) {
            throw new IllegalArgumentException("물품 이름은 비어 있을 수 없습니다.");
        }
        if (trimmed.length() > MAX_LENGTH) {
            throw new IllegalArgumentException("물품 이름은 " + MAX_LENGTH + "자를 초과할 수 없습니다.");
        }
        return new ItemName(trimmed);
    }

    public String value() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ItemName itemName = (ItemName) o;
        return value.equals(itemName.value);
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
