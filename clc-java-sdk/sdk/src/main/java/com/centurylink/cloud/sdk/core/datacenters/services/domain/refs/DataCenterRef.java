package com.centurylink.cloud.sdk.core.datacenters.services.domain.refs;

import com.centurylink.cloud.sdk.core.datacenters.services.domain.filters.DataCentersFilter;
import com.centurylink.cloud.sdk.core.services.Reference;

/**
 * @author ilya.drabenia
 */
public abstract class DataCenterRef implements Reference {

    @Override
    public abstract DataCentersFilter asFilter();

}
