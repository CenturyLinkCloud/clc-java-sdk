package com.centurylink.cloud.sdk.core.datacenters.services.domain.refs;

/**
 * @author ilya.drabenia
 */
public class IdDataCenterRef extends DataCenterRef {
    private final String id;

    public IdDataCenterRef(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }
}
