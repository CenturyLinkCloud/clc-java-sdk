package com.centurylink.cloud.sdk.servers.services.domain.template.refs;

import com.centurylink.cloud.sdk.core.commons.services.domain.datacenters.refs.DataCenter;
import com.centurylink.cloud.sdk.core.services.refs.Reference;
import com.centurylink.cloud.sdk.servers.services.domain.template.filters.TemplateFilter;

/**
 * @author ilya.drabenia
 */
public abstract class Template implements Reference {
    private final DataCenter dataCenter;

    Template(DataCenter dataCenter) {
        this.dataCenter = dataCenter;
    }

    public DataCenter getDataCenter() {
        return dataCenter;
    }

    @Override
    public abstract TemplateFilter asFilter();
}
