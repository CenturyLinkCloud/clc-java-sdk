package com.centurylink.cloud.sdk.servers.services.domain.server;

import com.google.common.base.Preconditions;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Calendar;
import java.util.Date;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.time.format.DateTimeFormatter.ISO_DATE_TIME;

/**
 * Class that represents server expiration time
 *
 * @author Aliaksandr Krasitski
 */
public class TimeToLive {
    private final ZonedDateTime zonedDateTime;

    /**
     * Constructor that allow to provide server expiration time using {@link ZonedDateTime}
     *
     * @param zonedDateTime not null representation of expiration time
     */
    public TimeToLive(ZonedDateTime zonedDateTime) {
        checkNotNull(zonedDateTime);

        this.zonedDateTime = zonedDateTime.withNano(0).withSecond(0);
    }

    /**
     * Constructor that allow to provide server expiration time as {@link String} in ISO date format
     *
     * @param dateTime is not null ISO formatter string that contains server expiration date
     * @throws TimeToLiveParseException when application is not able to parse dateTime
     * @throws NullPointerException
     */
    public TimeToLive(String dateTime) {
        checkNotNull(dateTime);

        zonedDateTime = parse(dateTime);
    }

    /**
     * Constructor that allow to provide server expiration time as formatted date {@link String}
     *
     * @param dateTime is not null formatted string that contains date
     * @param pattern is pattern which describe structure of dateTime parameter
     * @throws TimeToLiveParseException when application is not able to parse dateTime
     */
    public TimeToLive(String dateTime, String pattern) {
        checkNotNull(dateTime);
        Preconditions.checkNotNull(pattern, "Time to live date pattern must be not a null");

        zonedDateTime = parse(dateTime, pattern);
    }

    /**
     * Constructor that allow to provide server expiration time as {@link Date}
     *
     * @param date is not null value of server expiration time
     */
    public TimeToLive(Date date) {
        checkNotNull(date);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        this.zonedDateTime = ZonedDateTime.ofInstant(
            calendar.toInstant(),
            ZoneId.of(calendar.getTimeZone().getID())
        );
    }

    /**
     * Constructor that allow to provide server expiration time as {@link Calendar}
     *
     * @param calendar is not null value of server expiration time
     */
    public TimeToLive(Calendar calendar) {
        this(checkNotNull(calendar).getTime());
    }

    private ZonedDateTime parse(String dateTime) {
        //dateTime format is YYYY-MM-DDThh:mm+hh:mm
        return parse(dateTime, ISO_DATE_TIME);
    }

    private ZonedDateTime parse(String dateTime, String pattern) {
        return parse(dateTime, DateTimeFormatter.ofPattern(pattern));
    }

    private ZonedDateTime parse(String dateTime, DateTimeFormatter pattern) {
        try {
            return
                ZonedDateTime
                    .parse(dateTime, pattern)
                    .withNano(0)
                    .withSecond(0);
        } catch (DateTimeParseException ex) {
            throw new TimeToLiveParseException("DateTime should follow format %s", pattern, ex);
        }
    }

    private static <T> T checkNotNull(T value) {
        return Preconditions.checkNotNull(value, "Time to live date must be not a null");
    }

    @Override
    public String toString() {
        return zonedDateTime != null ? zonedDateTime.format(ISO_DATE_TIME): null;
    }

    public String format() {
        return zonedDateTime != null ? zonedDateTime.toOffsetDateTime().toString(): null;
    }
}
