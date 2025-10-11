package com.findit.server.domain.shared;

import java.util.Objects;

/**
 * 위치(장소)명을 나타내는 값 객체.
 */
public final class LocationName {

    private static final int MAX_LENGTH = 255;

    private final String value;

    private LocationName(String value) {
        this.value = value;
    }

    public static LocationName of(String raw) {
        Objects.requireNonNull(raw, "장소 정보는 null일 수 없습니다.");
        String trimmed = raw.trim();
        if (trimmed.isEmpty()) {
            throw new IllegalArgumentException("장소 정보는 비어 있을 수 없습니다.");
        }
        if (trimmed.length() > MAX_LENGTH) {
            throw new IllegalArgumentException("장소 정보는 " + MAX_LENGTH + "자를 초과할 수 없습니다.");
        }
        return new LocationName(trimmed);
    }

    public String value() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LocationName that = (LocationName) o;
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
