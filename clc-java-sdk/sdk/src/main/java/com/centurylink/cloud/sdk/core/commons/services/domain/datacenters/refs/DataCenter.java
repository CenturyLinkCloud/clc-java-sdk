package com.centurylink.cloud.sdk.core.commons.services.domain.datacenters.refs;

import com.centurylink.cloud.sdk.core.commons.services.domain.datacenters.filters.DataCenterFilter;
import com.centurylink.cloud.sdk.core.services.refs.Reference;

/**
 * {@inheritDoc}
 *
 * @author ilya.drabenia
 */
public abstract class DataCenter implements Reference {

    public static DataCenterByIdRef refById(String id) {
        return new DataCenterByIdRef(id);
    }

    public static DataCenterByNameRef refByName(String name) {
        return new DataCenterByNameRef(name);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public abstract DataCenterFilter asFilter();

}
