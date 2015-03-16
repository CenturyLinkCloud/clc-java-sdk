package com.centurylink.cloud.sdk.servers.services.domain.datacenter;

/**
 * Class represents CenturyLink data center in specified location
 *
 * @author ilya.drabenia
 */
public class DataCenter {
    private String id;
    private String name;

    public DataCenter() {
    }

    public DataCenter(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public DataCenter id(String id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public DataCenter name(String name) {
        this.name = name;
        return this;
    }
}
