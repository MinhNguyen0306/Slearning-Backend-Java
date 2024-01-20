package com.example.Slearning.Backend.Java.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;

public class AppUtils {
    public static String formatYearMonthToString(YearMonth yearMonth) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("MM-yyyy");
        return yearMonth.format(dateTimeFormatter);
    }

    public static YearMonth formatYearMonth(YearMonth yearMonth) {
        return YearMonth.parse(formatYearMonthToString(yearMonth));
    }

    public static String formatDateToString(Date date, String pattern) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        return simpleDateFormat.format(date);
    }

    public static String formatTimeToString(LocalTime localTime) {
        ZoneId zoneId = ZoneId.of("Asia/Ho_Chi_Minh");
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("hh:mm:ssz").withZone(zoneId);
        return localTime.format(dateTimeFormatter);
    }

    public static String formatLocalDateTimeToString(LocalDateTime localDateTime) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm:ss a");
        return localDateTime.format(dateTimeFormatter);
    }
}
