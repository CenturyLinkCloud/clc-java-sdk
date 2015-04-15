package com.centurylink.cloud.sdk.core.commons.services.domain.datacenters.refs;

import com.centurylink.cloud.sdk.core.commons.services.domain.datacenters.filters.DataCenterFilter;
import com.centurylink.cloud.sdk.core.services.refs.Reference;

/**
 * {@inheritDoc}
 *
 * @author ilya.drabenia
 */
public abstract class DataCenterRef implements Reference {

    /**
     * {@inheritDoc}
     */
    @Override
    public abstract DataCenterFilter asFilter();

}
