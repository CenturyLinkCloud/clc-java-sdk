package com.centurylink.cloud.sdk.servers.services.domain.template.refs;

import com.centurylink.cloud.sdk.common.management.services.domain.datacenters.refs.DataCenter;
import com.centurylink.cloud.sdk.servers.services.domain.template.filters.TemplateFilter;

/**
 * @author ilya.drabenia
 */
public class TemplateByDescriptionRef extends Template {
    private final String description;

    TemplateByDescriptionRef(DataCenter dataCenter, String description) {
        super(dataCenter);
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public TemplateByDescriptionRef description(String description) {
        return new TemplateByDescriptionRef(getDataCenter(), description);
    }

    public TemplateByDescriptionRef dataCenter(DataCenter dataCenter) {
        return new TemplateByDescriptionRef(dataCenter, description);
    }

    @Override
    public TemplateFilter asFilter() {
        return (
            new TemplateFilter()
                .dataCenters(getDataCenter())
                .descriptionContains(description)
        );
    }
}
