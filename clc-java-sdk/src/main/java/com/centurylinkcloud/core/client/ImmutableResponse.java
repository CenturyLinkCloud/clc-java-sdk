package com.centurylinkcloud.core.client;

/**
 * @author ilya.drabenia
 */
public class ImmutableResponse {
    private final String message;

    public ImmutableResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
