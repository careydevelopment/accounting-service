package us.careydevelopment.accounting.util;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class DateUtil {

    public static final ZoneId ZONE_ID = ZoneId.of("UTC");

    public static void main(String[] args) {
        System.err.println(currentTime());
    }

    public static Long currentTime() {
        final Long currentTime = Instant.now().toEpochMilli();
        return currentTime;
    }

    public static boolean isPreviousAccountingPeriod(final Long date) {
        final ZonedDateTime givenDateTime = ZonedDateTime.ofInstant(Instant.ofEpochMilli(date), ZONE_ID);
        final int givenYear = givenDateTime.getYear();

        final ZonedDateTime currentDateTime = ZonedDateTime.ofInstant(Instant.ofEpochMilli(currentTime()), ZONE_ID);
        final int currentYear = currentDateTime.getYear();

        return (currentYear > givenYear);
    }

    public static Long getStartOfAccountingPeriod(final Long asOf, final int startMonth) {
        final ZonedDateTime zonedDateTime = ZonedDateTime.ofInstant(Instant.ofEpochMilli(asOf), ZONE_ID);

        int currentYear = zonedDateTime.getYear();
        int currentMonth = zonedDateTime.getMonthValue();

        if (startMonth > currentMonth) {
            currentYear--;
        }

        final Long start = getDateAtMidnight(startMonth, 1, currentYear);
        return start;
    }

    public static Long getDateAtMidnight(final int month, final int day, final int year) {
        final LocalDateTime localDateTime = LocalDateTime.of(year, month, day, 0, 0, 0);
        final ZonedDateTime zonedDateTime = ZonedDateTime.of(localDateTime, ZONE_ID);

        final Long dateTime = zonedDateTime.toEpochSecond() * 1000;

        return dateTime;
    }
}
