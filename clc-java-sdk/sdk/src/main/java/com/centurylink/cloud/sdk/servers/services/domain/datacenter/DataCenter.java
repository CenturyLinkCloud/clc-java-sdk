package com.centurylink.cloud.sdk.servers.services.domain.datacenter;

import com.centurylink.cloud.sdk.servers.services.domain.datacenter.refs.DataCenterRef;
import com.centurylink.cloud.sdk.servers.services.domain.datacenter.refs.IdDataCenterRef;
import com.centurylink.cloud.sdk.servers.services.domain.datacenter.refs.NameDataCenterRef;
import com.centurylink.cloud.sdk.servers.services.domain.group.refs.GroupRef;
import com.centurylink.cloud.sdk.servers.services.domain.group.refs.IdGroupRef;
import com.centurylink.cloud.sdk.servers.services.domain.group.refs.NameGroupRef;

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

    public static IdDataCenterRef refById(String id) {
        return new IdDataCenterRef(id);
    }

    public static NameDataCenterRef refByName(String name) {
        return new NameDataCenterRef(name);
    }
}
