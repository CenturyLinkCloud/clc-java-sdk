package com.centurylink.cloud.sdk.servers.services.domain.datacenter;

/**
 * Class represents CenturyLink data center in specified location
 *
 * @author ilya.drabenia
 */
public class DataCenter {
    private String id;
    private String description;

    public DataCenter(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public DataCenter description(String description) {
        this.description = description;
        return this;
    }
}
