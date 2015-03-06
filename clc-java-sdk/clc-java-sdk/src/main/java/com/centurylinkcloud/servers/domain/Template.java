package com.centurylinkcloud.servers.domain;

import com.centurylinkcloud.servers.domain.os.OperatingSystem;

/**
 * @author ilya.drabenia
 */
public class Template {
    private String name;
    private OperatingSystem os;

    public Template name(String name) {
        this.name = name;
        return this;
    }

    public String getName() {
        return name;
    }

    public Template os(OperatingSystem osType) {
        this.os = osType;
        return this;
    }

    public OperatingSystem getOs() {
        return os;
    }
}
