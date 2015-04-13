package com.centurylink.cloud.sdk.servers.services.domain.server;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;

/**
 * Created by aliaksandr.krasitski on 4/13/2015.
 */
public class TimeToLive {
    private ZonedDateTime zonedDateTime = ZonedDateTime.now();
    @JsonIgnore
    private boolean isDefined = false;

    public TimeToLive date(String date) {
        //date format is YYYY-MM-DD
        if (date != null && date.length() == 10) {
            int year = Integer.parseInt(date.substring(0,3));
            int month = Integer.parseInt(date.substring(5,7));
            int day = Integer.parseInt(date.substring(9));

            this.year(year)
                .month(month)
                .day(day);
        }

        return this;
    }

    public TimeToLive time(String time) {
        //time format is hh:mm
        if (time != null && time.length() == 5) {
            int hour = Integer.parseInt(time.substring(0,1));
            int minute = Integer.parseInt(time.substring(3,4));

            this.hour(hour)
                .minute(minute);
        }
        return this;
    }

    public TimeToLive year(int year) {
        isDefined = true;
        zonedDateTime.withYear(year);
        return this;
    }

    public TimeToLive month(int month) {
        isDefined = true;
        zonedDateTime.withMonth(month);
        return this;
    }

    public TimeToLive day(int day) {
        isDefined = true;
        zonedDateTime.withDayOfMonth(day);
        return this;
    }

    public TimeToLive hour(int hour) {
        isDefined = true;
        zonedDateTime.withHour(hour);
        return this;
    }

    public TimeToLive minute(int minute) {
        isDefined = true;
        zonedDateTime.withMinute(minute);
        return this;
    }

    public TimeToLive offset(String offset) {
        isDefined = true;
        zonedDateTime.withZoneSameLocal(ZoneOffset.of(offset));
        return this;
    }

    public boolean isDefined() {
        return isDefined;
    }

    public ZonedDateTime getZonedDateTime() {
        return zonedDateTime;
    }
}
