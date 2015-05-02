package com.centurylink.cloud.sdk.servers.services.domain.server;

/**
 * @author Ilya Drabenia
 */
public enum ServerStatus {
    ACTIVE("active"),
    ARCHIVED("archived"),
    UNDER_CONSTRUCTION("underConstruction");

    private final String code;

    private ServerStatus(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
