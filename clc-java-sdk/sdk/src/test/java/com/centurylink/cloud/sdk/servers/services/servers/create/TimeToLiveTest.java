/*
 * (c) 2015 CenturyLink. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.centurylink.cloud.sdk.servers.services.servers.create;

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
