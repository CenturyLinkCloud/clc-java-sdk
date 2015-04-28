package com.centurylink.cloud.sdk.core.commons.services.domain.datacenters.refs;

import com.centurylink.cloud.sdk.core.commons.services.domain.datacenters.filters.DataCenterFilter;

/**
 * Class allow to specify unique data center by ID
 *
 * @author ilya.drabenia
 */
public class DataCenterByIdRef extends DataCenter {
    private final String id;

    DataCenterByIdRef(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    @Override
    public DataCenterFilter asFilter() {
        return new DataCenterFilter().id(id);
    }
}
