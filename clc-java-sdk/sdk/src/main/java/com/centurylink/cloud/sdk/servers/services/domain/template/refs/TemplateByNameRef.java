package com.centurylink.cloud.sdk.servers.services.domain.template.refs;

import com.centurylink.cloud.sdk.core.commons.services.domain.datacenters.refs.DataCenter;
import com.centurylink.cloud.sdk.servers.services.domain.template.filters.TemplateFilter;

/**
 * @author ilya.drabenia
 */
public class TemplateByNameRef extends Template {
    private final String name;

    public TemplateByNameRef(DataCenter dataCenter, String name) {
        super(dataCenter);
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public TemplateByNameRef name(String name) {
        return new TemplateByNameRef(getDataCenter(), name);
    }

    public TemplateByNameRef dataCenter(DataCenter dataCenter) {
        return new TemplateByNameRef(dataCenter, name);
    }

    @Override
    public TemplateFilter asFilter() {
        return
            new TemplateFilter()
                .dataCenters(getDataCenter())
                .names(name);
    }
}
