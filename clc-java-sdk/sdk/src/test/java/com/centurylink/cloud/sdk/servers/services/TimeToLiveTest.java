package com.centurylink.cloud.sdk.servers.services;

import com.centurylink.cloud.sdk.servers.services.domain.server.TimeToLive;
import com.centurylink.cloud.sdk.servers.services.domain.server.TimeToLiveParseException;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.time.ZonedDateTime;
import java.util.Calendar;

/**
 * @author Aliaksandr Krasitski
 */
public class TimeToLiveTest extends Assert{

    @Test
    public void testConstructorDate() {
        new TimeToLive(Calendar.getInstance().getTime());
    }

    @Test
    public void testConstructorCalendar() {
        new TimeToLive(Calendar.getInstance());
    }

    @Test
    public void testConstructorZonedDateTime() {
        new TimeToLive(ZonedDateTime.now());
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
        TimeToLive ttl = new TimeToLive("2015-12-31T23:00+03:00");
        assertEquals(date, ttl.format());
    }

    @Test
    public void testConstructorStringPattern() {
        new TimeToLive("2015-12-31T23:00+03:00", "yyyy-MM-dd'T'HH:mmXXX");
    }
}
