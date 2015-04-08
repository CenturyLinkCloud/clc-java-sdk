package com.centurylink.cloud.sdk.servers.services.domain.group.refs;

import com.centurylink.cloud.sdk.core.datacenters.services.domain.refs.DataCenterRef;
import com.centurylink.cloud.sdk.core.services.Reference;
import com.centurylink.cloud.sdk.servers.services.domain.group.filters.GroupFilter;

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
        return new GroupFilter()
            .dataCenter(getDataCenter());
    }
}
