package com.centurylink.cloud.sdk.servers.services.domain.template;

import com.centurylink.cloud.sdk.servers.services.domain.os.OperatingSystem;

/**
 * @author ilya.drabenia
 */
public class Template {
    private String name;
    private String description;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Template description(String description) {
        setDescription(description);
        return this;
    }
}
