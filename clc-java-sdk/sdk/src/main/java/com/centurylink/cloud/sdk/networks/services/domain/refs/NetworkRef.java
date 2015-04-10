package com.centurylink.cloud.sdk.networks.services.domain.refs;

import com.centurylink.cloud.sdk.core.datacenters.services.domain.refs.DataCenterRef;
import com.centurylink.cloud.sdk.core.services.refs.Reference;

/**
 * @author ilya.drabenia
 */
public class NetworkRef implements Reference {
    private DataCenterRef dataCenter;

    public NetworkRef(DataCenterRef dataCenter) {
        this.dataCenter = dataCenter;
    }

    public DataCenterRef getDataCenter() {
        return dataCenter;
    }

    @Override
    public Object asFilter() {
        // TODO: hey, implement me!
        throw new UnsupportedOperationException();
    }
}
