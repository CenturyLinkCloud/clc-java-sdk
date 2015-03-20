package com.centurylink.cloud.sdk.servers.services.domain.template.refs;

import com.centurylink.cloud.sdk.core.domain.BaseRef;
import com.centurylink.cloud.sdk.core.datacenters.services.domain.datacenter.refs.DataCenterRef;

/**
 * @author ilya.drabenia
 */
public class TemplateRef extends BaseRef {
    private final DataCenterRef dataCenter;

    public TemplateRef(DataCenterRef dataCenter) {
        this.dataCenter = dataCenter;
    }

    public DataCenterRef getDataCenter() {
        return dataCenter;
    }
}
