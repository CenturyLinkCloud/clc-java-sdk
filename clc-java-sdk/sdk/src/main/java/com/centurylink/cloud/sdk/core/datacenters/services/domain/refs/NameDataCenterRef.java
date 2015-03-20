package com.centurylink.cloud.sdk.core.datacenters.services.domain.refs;

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
