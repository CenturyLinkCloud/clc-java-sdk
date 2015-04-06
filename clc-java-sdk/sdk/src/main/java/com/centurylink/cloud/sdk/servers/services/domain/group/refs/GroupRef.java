package com.centurylink.cloud.sdk.servers.services.domain.group.refs;

import com.centurylink.cloud.sdk.core.datacenters.services.domain.refs.DataCenterRef;
import com.centurylink.cloud.sdk.core.services.Reference;

/**
 * @author ilya.drabenia
 */
public class GroupRef implements Reference {
    protected final DataCenterRef dataCenter;

    public GroupRef(DataCenterRef dataCenter) {
        this.dataCenter = dataCenter;
    }

    public DataCenterRef getDataCenter() {
        return dataCenter;
    }

    @Override
    public Object asFilter() {
        throw new UnsupportedOperationException();
    }
}
