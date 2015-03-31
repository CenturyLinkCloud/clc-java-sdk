package com.centurylink.cloud.sdk.networks.services.domain.refs;

import com.centurylink.cloud.sdk.core.datacenters.services.domain.refs.DataCenterRef;
import com.centurylink.cloud.sdk.core.domain.BaseRef;

/**
 * @author ilya.drabenia
 */
public class NetworkRef implements BaseRef {
    private DataCenterRef dataCenter;

    public NetworkRef(DataCenterRef dataCenter) {
        this.dataCenter = dataCenter;
    }

    public DataCenterRef getDataCenter() {
        return dataCenter;
    }
}
