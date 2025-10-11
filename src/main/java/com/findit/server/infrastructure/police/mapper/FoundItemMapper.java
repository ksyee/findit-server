package com.findit.server.infrastructure.police.mapper;

import com.findit.server.domain.founditem.FoundDate;
import com.findit.server.domain.founditem.FoundItem;
import com.findit.server.domain.founditem.FoundItemId;
import com.findit.server.domain.shared.ItemCategory;
import com.findit.server.domain.shared.ItemName;
import com.findit.server.domain.shared.LocationName;
import com.findit.server.infrastructure.police.dto.PoliceApiFoundItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 경찰청 API 습득물 데이터를 내부 애그리게이트로 변환하는 매퍼.
 */
@Component
public class FoundItemMapper implements ApiMapper<PoliceApiFoundItem, FoundItem> {

    private static final Logger logger = LoggerFactory.getLogger(FoundItemMapper.class);

    private static final Pattern YYYY_MM_DD_PATTERN = Pattern.compile("(\\d{4})[-./](\\d{1,2})[-./](\\d{1,2})");
    private static final Pattern YYYY_MM_PATTERN = Pattern.compile("(\\d{4})[-./](\\d{1,2})[-]?");
    private static final Pattern YYYYMMDD_PATTERN = Pattern.compile("(\\d{4})(\\d{2})(\\d{2})");
    private static final Pattern YYYYMM_PATTERN = Pattern.compile("(\\d{4})(\\d{2})");

    private static final DateTimeFormatter TARGET_DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Override
    public FoundItem map(PoliceApiFoundItem source) {
        if (source == null) {
            return null;
        }

        String identifier = resolveIdentifier(source);
        if (isBlank(identifier) || isBlank(source.getFdPrdtNm())
            || isBlank(source.getPrdtClNm()) || isBlank(source.getDepPlace())) {
            return null;
        }

        String normalizedDate = normalizeDateString(source.getFdYmd());
        if (normalizedDate == null) {
            return null;
        }

        return FoundItem.create(
            FoundItemId.of(identifier),
            ItemName.of(source.getFdPrdtNm()),
            ItemCategory.of(source.getPrdtClNm()),
            FoundDate.of(normalizedDate),
            LocationName.of(source.getDepPlace()),
            source.getFdSbjt(),
            source.getFdFilePathImg(),
            source.getClrNm(),
            source.getFdSn()
        );
    }

    private String resolveIdentifier(PoliceApiFoundItem source) {
        if (!isBlank(source.getAtcId())) {
            return source.getAtcId().trim();
        }
        if (!isBlank(source.getFdSn())) {
            return source.getFdSn().trim();
        }
        return null;
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }

    private String normalizeDateString(String dateStr) {
        if (dateStr == null || dateStr.trim().isEmpty()) {
            return null;
        }

        String trimmed = dateStr.trim();
        LocalDate date = tryParseDate(trimmed);
        if (date != null) {
            return date.format(TARGET_DATE_FORMATTER);
        }

        logger.warn("인식할 수 없는 날짜 형식: {}", trimmed);
        return trimmed.length() > 10 ? trimmed.substring(0, 10) : trimmed;
    }

    private LocalDate tryParseDate(String raw) {
        Matcher ymdMatcher = YYYY_MM_DD_PATTERN.matcher(raw);
        if (ymdMatcher.matches()) {
            return buildDate(ymdMatcher.group(1), ymdMatcher.group(2), ymdMatcher.group(3));
        }

        Matcher ymMatcher = YYYY_MM_PATTERN.matcher(raw);
        if (ymMatcher.matches()) {
            LocalDate date = buildDate(ymMatcher.group(1), ymMatcher.group(2), String.valueOf(LocalDate.now().getDayOfMonth()));
            if (date != null) {
                logger.info("일자가 누락된 날짜를 보정: {} → {}", raw, date.format(TARGET_DATE_FORMATTER));
            }
            return date;
        }

        Matcher compactMatcher = YYYYMMDD_PATTERN.matcher(raw);
        if (compactMatcher.matches()) {
            return buildDate(compactMatcher.group(1), compactMatcher.group(2), compactMatcher.group(3));
        }

        Matcher compactYmMatcher = YYYYMM_PATTERN.matcher(raw);
        if (compactYmMatcher.matches()) {
            LocalDate date = buildDate(compactYmMatcher.group(1), compactYmMatcher.group(2), String.valueOf(LocalDate.now().getDayOfMonth()));
            if (date != null) {
                logger.info("일자가 누락된 날짜를 보정: {} → {}", raw, date.format(TARGET_DATE_FORMATTER));
            }
            return date;
        }

        try {
            return LocalDate.parse(raw, DateTimeFormatter.ISO_LOCAL_DATE);
        } catch (Exception ignored) {
            return null;
        }
    }

    private LocalDate buildDate(String year, String month, String day) {
        try {
            int y = Integer.parseInt(year);
            int m = Integer.parseInt(month);
            int d = Integer.parseInt(day);
            return LocalDate.of(y, m, d);
        } catch (Exception e) {
            logger.warn("유효하지 않은 날짜 값: {}-{}-{}", year, month, day);
            return null;
        }
    }
}
