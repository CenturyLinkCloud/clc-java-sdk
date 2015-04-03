package com.centurylink.cloud.sdk.servers.services.domain.template.refs;

import com.centurylink.cloud.sdk.core.datacenters.services.domain.refs.DataCenterRef;
import com.centurylink.cloud.sdk.core.domain.Reference;

/**
 * @author ilya.drabenia
 */
public class TemplateRef implements Reference {
    private final DataCenterRef dataCenter;

    public TemplateRef(DataCenterRef dataCenter) {
        this.dataCenter = dataCenter;
    }

    public DataCenterRef getDataCenter() {
        return dataCenter;
    }

    @Override
    public Object asFilter() {
        return null;
    }
}
