package com.centurylink.cloud.sdk.servers.services.domain.template.filters.os;

/**
 * @author ilya.drabenia
 */
public enum OsType {
    CENTOS("centOS"),
    RHEL("redHat"),
    DEBIAN("debian"),
    WINDOWS("windows"),
    UBUNTU("ubuntu");

    private final String code;

    private OsType(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
