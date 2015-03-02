package com.centurylinkcloud.auth;

import java.util.Date;

import static java.util.concurrent.TimeUnit.MINUTES;

/**
 * @author ilya.drabenia
 */
public class AuthToken {
    private final String value;
    private final Date receivedAt;

    public AuthToken(String value) {
        this.value = value;
        this.receivedAt = now();
    }

    public String value() {
        return value;
    }

    public Date receivedAt() {
        return receivedAt;
    }

    public boolean isExpired() {
        return now().getTime() - receivedAt.getTime() > MINUTES.toMicros(10);
    }

    public String toHeaderString() {
        return "Bearer " + value;
    }

    private Date now() {
        return new Date();
    }
}
