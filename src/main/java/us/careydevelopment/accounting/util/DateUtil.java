package us.careydevelopment.accounting.util;

import java.time.Instant;
import java.time.ZoneId;

public class DateUtil {

    private static final ZoneId ZONE_ID = ZoneId.of("UTC");

    public static void main(String[] args) {
        currentTime();
    }

    public static Long currentTime() {
        final Long currentTime = Instant.now().toEpochMilli();
        return currentTime;
    }
}
