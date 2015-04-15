package com.centurylink.cloud.sdk.servers.services.domain.group.refs;

import com.centurylink.cloud.sdk.core.commons.services.domain.datacenters.refs.DataCenterRef;
import com.centurylink.cloud.sdk.core.services.refs.Reference;
import com.centurylink.cloud.sdk.servers.services.domain.group.filters.GroupFilter;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * @author ilya.drabenia
 */
public abstract class GroupRef implements Reference {
    protected final DataCenterRef dataCenter;

    public GroupRef(DataCenterRef dataCenter) {
        this.dataCenter = dataCenter;
    }

    public DataCenterRef getDataCenter() {
        return dataCenter;
    }

    @Override
    public GroupFilter asFilter() {
        checkNotNull(dataCenter, "Data center reference must be not a null");

        return new GroupFilter()
            .dataCenterIn(getDataCenter());
    }
}
