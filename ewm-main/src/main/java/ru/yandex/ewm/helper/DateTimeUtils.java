package ru.yandex.ewm.helper;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class DateTimeUtils {
    public static final String PATTERN = "yyyy-MM-dd HH:mm:ss";
    public static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern(PATTERN);

    public static LocalDateTime parseOrNull(String s) {
        if (s == null || s.isBlank()) return null;
        return LocalDateTime.parse(s, FMT);
    }

    public static LocalDateTime or(LocalDateTime val, LocalDateTime fallback) {
        return Objects.requireNonNullElse(val, fallback);
    }
}