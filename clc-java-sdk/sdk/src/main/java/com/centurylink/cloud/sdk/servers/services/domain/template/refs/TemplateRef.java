package com.centurylink.cloud.sdk.servers.services.domain.template.refs;

import com.centurylink.cloud.sdk.core.commons.services.domain.datacenters.refs.DataCenterRef;
import com.centurylink.cloud.sdk.core.services.refs.Reference;

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
