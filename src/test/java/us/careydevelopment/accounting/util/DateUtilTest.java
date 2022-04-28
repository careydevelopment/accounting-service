package us.careydevelopment.accounting.util;

import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DateUtilTest {

    private static DateTimeFormatter FORMAT = DateTimeFormatter.ofPattern("MM/dd/yy hh:mm:ss");

    @Test
    public void testGetDateAtMidnight() {
        final Long dateAtMidnight = DateUtil.getDateAtMidnight(1, 1, 2022);
        final ZonedDateTime zonedDateTime = ZonedDateTime.ofInstant(Instant.ofEpochMilli(dateAtMidnight), DateUtil.ZONE_ID);

        final String dateStr = zonedDateTime.format(FORMAT);
        assertEquals("01/01/22 12:00:00", dateStr);
    }

    @Test
    public void testGetStartOfAccountingPeriodCurrentYear() {
        final Long currentDate = 1651013295663l;
        final Integer startMonth = 1;

        final Long startOfAccountingPeriod = DateUtil.getStartOfAccountingPeriod(currentDate, startMonth);

        final ZonedDateTime zonedDateTime = ZonedDateTime.ofInstant(Instant.ofEpochMilli(startOfAccountingPeriod), DateUtil.ZONE_ID);

        final String dateStr = zonedDateTime.format(FORMAT);
        assertEquals("01/01/22 12:00:00", dateStr);
    }

    @Test
    public void testGetStartOfAccountingPeriodPreviousYear() {
        final Long currentDate = 1651013295663l;
        final Integer startMonth = 12;

        final Long startOfAccountingPeriod = DateUtil.getStartOfAccountingPeriod(currentDate, startMonth);

        final ZonedDateTime zonedDateTime = ZonedDateTime.ofInstant(Instant.ofEpochMilli(startOfAccountingPeriod), DateUtil.ZONE_ID);

        final String dateStr = zonedDateTime.format(FORMAT);
        assertEquals("12/01/21 12:00:00", dateStr);
    }
}
