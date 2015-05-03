package com.centurylink.cloud.sdk.networks.services.domain.refs;

import com.centurylink.cloud.sdk.commons.services.domain.datacenters.refs.DataCenter;
import com.centurylink.cloud.sdk.base.services.refs.Reference;

/**
 * @author ilya.drabenia
 */
public class NetworkRef implements Reference {
    private DataCenter dataCenter;

    public NetworkRef(DataCenter dataCenter) {
        this.dataCenter = dataCenter;
    }

    public DataCenter getDataCenter() {
        return dataCenter;
    }

    @Override
    public Object asFilter() {
        // TODO: hey, implement me!
        throw new UnsupportedOperationException();
    }
}
