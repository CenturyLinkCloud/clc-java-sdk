package com.centurylink.cloud.sdk.servers.services.domain.group.refs;

import com.centurylink.cloud.sdk.core.datacenters.services.domain.refs.DataCenterRef;
import com.centurylink.cloud.sdk.core.domain.BaseRef;

/**
 * @author ilya.drabenia
 */
public class GroupRef implements BaseRef {
    protected final DataCenterRef dataCenter;

    public GroupRef(DataCenterRef dataCenter) {
        this.dataCenter = dataCenter;
    }

    public DataCenterRef getDataCenter() {
        return dataCenter;
    }
}
