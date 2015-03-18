package com.centurylink.cloud.sdk.servers.services.domain.datacenter.refs;

/**
 * @author ilya.drabenia
 */
public class NameDataCenterRef extends DataCenterRef {
    private final String name;

    public NameDataCenterRef(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
