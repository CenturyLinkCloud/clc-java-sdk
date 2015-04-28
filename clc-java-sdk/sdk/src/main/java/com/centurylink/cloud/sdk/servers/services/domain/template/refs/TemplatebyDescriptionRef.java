package com.centurylink.cloud.sdk.servers.services.domain.template.refs;

import com.centurylink.cloud.sdk.core.commons.services.domain.datacenters.refs.DataCenter;
import com.centurylink.cloud.sdk.servers.services.domain.template.filters.TemplateFilter;

/**
 * @author ilya.drabenia
 */
public class TemplatebyDescriptionRef extends Template {
    private final String description;

    public TemplatebyDescriptionRef(DataCenter dataCenter, String description) {
        super(dataCenter);
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public TemplatebyDescriptionRef description(String description) {
        return new TemplatebyDescriptionRef(getDataCenter(), description);
    }

    public TemplatebyDescriptionRef dataCenter(DataCenter dataCenter) {
        return new TemplatebyDescriptionRef(dataCenter, description);
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
