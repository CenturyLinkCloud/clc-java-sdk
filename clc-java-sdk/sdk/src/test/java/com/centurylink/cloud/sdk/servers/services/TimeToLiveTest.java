package com.centurylink.cloud.sdk.servers.services;

import com.centurylink.cloud.sdk.servers.services.domain.server.TimeToLive;
import com.centurylink.cloud.sdk.servers.services.domain.server.TimeToLiveParseException;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.text.SimpleDateFormat;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

/**
 * @author Aliaksandr Krasitski
 */
public class TimeToLiveTest{

    private static final String TTL_PATTERN = "yyyy-MM-dd'T'HH:mmXXX";

    private static final SimpleDateFormat DATE_TIME_FORMAT = new SimpleDateFormat(TTL_PATTERN);

    @Test
    public void testConstructorDate() {
        Date date = Calendar.getInstance().getTime();
        TimeToLive ttl = new TimeToLive(date);
        Assert.assertEquals(ttl.format(), DATE_TIME_FORMAT.format(date));
    }

    @Test
    public void testConstructorCalendar() {
        Calendar calendar = Calendar.getInstance();
        TimeToLive ttl = new TimeToLive(calendar);
        Assert.assertEquals(ttl.format(), DATE_TIME_FORMAT.format(calendar.getTime()));
    }

    @Test
    public void testConstructorZonedDateTime() {
        ZonedDateTime dt = ZonedDateTime.now();
        TimeToLive ttl = new TimeToLive(dt);
        Assert.assertEquals(ttl.format(), dt.format(DateTimeFormatter.ofPattern(TTL_PATTERN)));
    }

    @Test(expectedExceptions = TimeToLiveParseException.class)
    public void testConstructorStringFail1() {
        new TimeToLive("2015-12-31");
    }

    @Test(expectedExceptions = TimeToLiveParseException.class)
    public void testConstructorStringFail2() {
        new TimeToLive("2015-12-31T23:00");
    }

    @Test
    public void testConstructorString() {
        String date = "2015-12-31T23:00+03:00";
        TimeToLive ttl = new TimeToLive(date);
        Assert.assertEquals(date, ttl.format());
    }

    @Test
    public void testConstructorStringPattern() {
        String date = "2015/12/31T23:00+03:00";
        String pattern = "yyyy/MM/dd'T'HH:mmXXX";
        TimeToLive ttl = new TimeToLive(date, pattern);
        Assert.assertEquals(ttl.format(),
                ZonedDateTime.parse(date, DateTimeFormatter.ofPattern(pattern))
                        .format(DateTimeFormatter.ofPattern(TTL_PATTERN)));
    }
}
