package com.centurylink.cloud.sdk.servers.services.domain.server;

/**
 * Server power states
 *
 * @author Ilya Drabenia
 */
public enum PowerState {
    STARTED("started"),
    PAUSED("paused"),
    STOPPED("stopped");

    private final String code;

    private PowerState(String codeValue) {
        code = codeValue;
    }

    public String getCode() {
        return code;
    }
}
