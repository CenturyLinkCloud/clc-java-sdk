package com.centurylink.cloud.sdk.servers.services.domain.group;

import com.centurylink.cloud.sdk.servers.services.domain.datacenter.DataCenter;

/**
 * @author ilya.drabenia
 */
public class Group {
    private String id;
    private String dataCenter;
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

    public String getDataCenter() {
        return dataCenter;
    }

    public void setDataCenter(String datacenter) {
        this.dataCenter = datacenter;
    }

    public Group dataCenter(String datacenter) {
        setDataCenter(datacenter);
        return this;
    }

    public Group dataCenter(DataCenter datacenter) {
        setDataCenter(datacenter.getId());
        return this;
    }
}
