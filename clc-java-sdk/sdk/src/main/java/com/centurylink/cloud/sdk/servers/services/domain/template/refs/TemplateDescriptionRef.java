package com.centurylink.cloud.sdk.servers.services.domain.template.refs;

import com.centurylink.cloud.sdk.core.commons.services.domain.datacenters.refs.DataCenter;
import com.centurylink.cloud.sdk.servers.services.domain.template.filters.TemplateFilter;

/**
 * @author ilya.drabenia
 */
public class TemplateDescriptionRef extends Template {
    private final String description;

    TemplateDescriptionRef(DataCenter dataCenter, String description) {
        super(dataCenter);
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public TemplateDescriptionRef description(String description) {
        return new TemplateDescriptionRef(getDataCenter(), description);
    }

    public TemplateDescriptionRef dataCenter(DataCenter dataCenter) {
        return new TemplateDescriptionRef(dataCenter, description);
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
