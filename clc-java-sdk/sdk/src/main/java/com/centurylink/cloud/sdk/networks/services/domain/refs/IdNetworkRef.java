package com.centurylink.cloud.sdk.networks.services.domain.refs;

import com.centurylink.cloud.sdk.core.commons.services.domain.datacenters.refs.DataCenterRef;

/**
 * @author ilya.drabenia
 */
public class IdNetworkRef extends NetworkRef {
    private final String id;

    public IdNetworkRef(DataCenterRef dataCenter, String id) {
        super(dataCenter);
        this.id = id;
    }

    public String getId() {
        return id;
    }
}
