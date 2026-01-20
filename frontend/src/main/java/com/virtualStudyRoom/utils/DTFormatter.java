package com.virtualStudyRoom.utils;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class DTFormatter {

    public static Instant convertToUTC(String localTimeStr, String dateStr) {
        LocalTime localTime = LocalTime.parse(localTimeStr, DateTimeFormatter.ofPattern("H:m"));

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-M-d");
        LocalDate localDate = LocalDate.parse(dateStr, dateFormatter);

        ZoneId zoneId = ZoneId.systemDefault();

        ZonedDateTime zonedDateTime = ZonedDateTime.of(localDate, localTime, zoneId);

        ZonedDateTime utcZonedDateTime = zonedDateTime.withZoneSameInstant(ZoneOffset.UTC);

        return utcZonedDateTime.toInstant();
    }
}