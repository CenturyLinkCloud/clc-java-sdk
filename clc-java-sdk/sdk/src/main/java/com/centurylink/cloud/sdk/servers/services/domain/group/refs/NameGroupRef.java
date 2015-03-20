package com.centurylink.cloud.sdk.servers.services.domain.group.refs;

import com.centurylink.cloud.sdk.core.datacenters.services.domain.datacenter.refs.DataCenterRef;

/**
 * @author ilya.drabenia
 */
public class NameGroupRef extends GroupRef {
    private final String name;

    public NameGroupRef(DataCenterRef dataCenter, String name) {
        super(dataCenter);
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public NameGroupRef name(String name) {
        return new NameGroupRef(dataCenter, name);
    }

    public NameGroupRef dataCenter(DataCenterRef dataCenter) {
        return new NameGroupRef(dataCenter, name);
    }
}
