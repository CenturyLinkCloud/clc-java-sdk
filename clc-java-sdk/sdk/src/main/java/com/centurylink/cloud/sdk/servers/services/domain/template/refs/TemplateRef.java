package com.centurylink.cloud.sdk.servers.services.domain.template.refs;

import com.centurylink.cloud.sdk.core.datacenters.services.domain.refs.DataCenterRef;
import com.centurylink.cloud.sdk.core.domain.BaseRef;

/**
 * @author ilya.drabenia
 */
public class TemplateRef implements BaseRef {
    private final DataCenterRef dataCenter;

    public TemplateRef(DataCenterRef dataCenter) {
        this.dataCenter = dataCenter;
    }

    public DataCenterRef getDataCenter() {
        return dataCenter;
    }
}
