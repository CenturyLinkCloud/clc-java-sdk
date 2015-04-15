package com.centurylink.cloud.sdk.servers.services.domain.group;

import com.centurylink.cloud.sdk.core.commons.services.domain.datacenters.DataCenter;
import com.centurylink.cloud.sdk.servers.services.domain.group.refs.IdGroupRef;
import com.centurylink.cloud.sdk.servers.services.domain.group.refs.NameGroupRef;

/**
 * @author ilya.drabenia
 */
public class Group {
    private String id;
    private DataCenter dataCenter;
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

    public DataCenter getDataCenter() {
        return dataCenter;
    }

    public void setDataCenter(DataCenter datacenter) {
        this.dataCenter = datacenter;
    }

    public Group dataCenter(DataCenter datacenter) {
        setDataCenter(datacenter);
        return this;
    }

    public static IdGroupRef refById() {
        return new IdGroupRef(null, null);
    }

    public static NameGroupRef refByName() {
        return new NameGroupRef(null, null);
    }
}
