package com.centurylink.cloud.sdk.servers.services.domain.server;

/**
 * @author ilya.drabenia
 */
public enum ServerType {
    STANDARD("standard"),
    HYPERSCALE("hyperscale");

    private String code;

    private ServerType(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
