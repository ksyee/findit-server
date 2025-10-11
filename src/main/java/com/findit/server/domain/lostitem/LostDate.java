package com.findit.server.domain.lostitem;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Objects;

/**
 * 분실 일자를 표현하는 값 객체.
 */
public final class LostDate {

    private static final DateTimeFormatter BASIC_FORMAT = DateTimeFormatter.BASIC_ISO_DATE;
    private static final DateTimeFormatter ISO_FORMAT = DateTimeFormatter.ISO_LOCAL_DATE;

    private final LocalDate value;

    private LostDate(LocalDate value) {
        this.value = value;
    }

    public static LostDate of(String raw) {
        Objects.requireNonNull(raw, "분실 일자는 null일 수 없습니다.");
        String trimmed = raw.trim();
        if (trimmed.isEmpty()) {
            throw new IllegalArgumentException("분실 일자는 비어 있을 수 없습니다.");
        }
        return new LostDate(parse(trimmed));
    }

    public static LostDate of(LocalDate date) {
        Objects.requireNonNull(date, "분실 일자는 null일 수 없습니다.");
        return new LostDate(date);
    }

    private static LocalDate parse(String raw) {
        try {
            if (raw.matches("\\d{8}")) {
                return LocalDate.parse(raw, BASIC_FORMAT);
            }
            return LocalDate.parse(raw, ISO_FORMAT);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("지원하지 않는 분실 일자 형식입니다: " + raw, e);
        }
    }

    public LocalDate toLocalDate() {
        return value;
    }

    public String asDatabaseValue() {
        return value.format(BASIC_FORMAT);
    }

    public String asIsoDate() {
        return value.format(ISO_FORMAT);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LostDate lostDate = (LostDate) o;
        return value.equals(lostDate.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public String toString() {
        return asIsoDate();
    }
}
