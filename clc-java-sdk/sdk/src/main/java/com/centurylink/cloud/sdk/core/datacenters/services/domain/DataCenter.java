package com.centurylink.cloud.sdk.core.datacenters.services.domain;

import com.centurylink.cloud.sdk.core.datacenters.services.domain.refs.IdDataCenterRef;
import com.centurylink.cloud.sdk.core.datacenters.services.domain.refs.NameDataCenterRef;

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
