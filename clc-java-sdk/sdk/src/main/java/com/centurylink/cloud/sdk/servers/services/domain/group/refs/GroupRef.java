package com.centurylink.cloud.sdk.servers.services.domain.group.refs;

import com.centurylink.cloud.sdk.core.domain.BaseRef;
import com.centurylink.cloud.sdk.servers.services.domain.datacenter.refs.DataCenterRef;

/**
 * @author ilya.drabenia
 */
public class GroupRef extends BaseRef {
    protected final DataCenterRef dataCenter;

    public GroupRef(DataCenterRef dataCenter) {
        this.dataCenter = dataCenter;
    }

    public DataCenterRef getDataCenter() {
        return dataCenter;
    }
}
