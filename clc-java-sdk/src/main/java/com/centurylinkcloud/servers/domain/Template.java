package com.centurylinkcloud.servers.domain;

/**
 * @author ilya.drabenia
 */
public class Template {
    private String name;
    private String osType;

    public Template name(String name) {
        this.name = name;
        return this;
    }

    public String getName() {
        return name;
    }

    public Template osType(String osType) {
        this.osType = osType;
        return this;
    }

    public String getOsType() {
        return osType;
    }
}
