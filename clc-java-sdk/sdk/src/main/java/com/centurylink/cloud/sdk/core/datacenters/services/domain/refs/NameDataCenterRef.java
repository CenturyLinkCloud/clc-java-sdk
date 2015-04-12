package com.centurylink.cloud.sdk.core.datacenters.services.domain.refs;

import com.centurylink.cloud.sdk.core.datacenters.services.domain.filters.DataCenterFilter;

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

    @Override
    public DataCenterFilter asFilter() {
        return new DataCenterFilter().nameContains(name);
    }
}
