package com.centurylink.cloud.sdk.servers.domain;

import com.centurylink.cloud.sdk.servers.domain.datacenter.DataCenters;

/**
 * @author ilya.drabenia
 */
public class Group {
    private String id;
    private String datacenter;
    private String name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Group id(String id) {
        setId(id);
        return this;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Group name(String name) {
        setName(name);
        return this;
    }

    public String getDatacenter() {
        return datacenter;
    }

    public void setDatacenter(String datacenter) {
        this.datacenter = datacenter;
    }

    public Group datacenter(String datacenter) {
        setDatacenter(datacenter);
        return this;
    }

    public Group datacenter(DataCenters datacenter) {
        setDatacenter(datacenter.getId());
        return this;
    }
}
