package com.centurylink.cloud.sdk.servers.services.domain.server;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by aliaksandr.krasitski on 4/13/2015.
 */
public class TimeToLive {
    private ZonedDateTime zonedDateTime;

    public TimeToLive(ZonedDateTime zonedDateTime) {
        this.zonedDateTime = zonedDateTime.withNano(0).withSecond(0);
    }

    public TimeToLive(String dateTime) {
        parse(dateTime);
    }

    public TimeToLive(String dateTime, String pattern) {
        parse(dateTime, pattern);
    }

    public TimeToLive(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        this.zonedDateTime = ZonedDateTime.ofInstant(calendar.toInstant(), ZoneId.of(calendar.getTimeZone().getID()));
    }

    public TimeToLive(Calendar calendar) {
        this(calendar.getTime());
    }

    private TimeToLive parse(String dateTime) {
        //dateTime format is YYYY-MM-DDThh:mm+hh:mm
        if (dateTime != null) {
            try {
                zonedDateTime = ZonedDateTime.parse(dateTime, DateTimeFormatter.ISO_DATE_TIME).withNano(0).withSecond(0);
            } catch (DateTimeParseException ex) {
                throw new DateTimeParseException("dateTime should follow format: yyyy-MM-dd'T'HH:mmXXX", ex.getParsedString(), ex.getErrorIndex(), ex);
            }
        }

        return this;
    }

    private TimeToLive parse(String dateTime, String pattern) {
        if (dateTime != null) {
            try {
                zonedDateTime = ZonedDateTime.parse(dateTime, DateTimeFormatter.ofPattern(pattern)).withNano(0).withSecond(0);
            } catch (DateTimeParseException ex) {
                throw new DateTimeParseException("dateTime should follow format: "+ pattern, ex.getParsedString(), ex.getErrorIndex(), ex);
            }
        }

        return this;
    }

    @Override
    public String toString() {
        return zonedDateTime != null ? zonedDateTime.format(DateTimeFormatter.ISO_DATE_TIME): null;
    }

    public String format() {
        return zonedDateTime != null ? zonedDateTime.toOffsetDateTime().toString(): null;
    }
}
