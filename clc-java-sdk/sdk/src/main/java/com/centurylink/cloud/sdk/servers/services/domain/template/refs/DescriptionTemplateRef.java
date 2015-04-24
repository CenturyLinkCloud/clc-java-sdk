package com.centurylink.cloud.sdk.servers.services.domain.template.refs;

import com.centurylink.cloud.sdk.core.commons.services.domain.datacenters.refs.DataCenterRef;
import com.centurylink.cloud.sdk.servers.services.domain.template.filters.TemplateFilter;

/**
 * @author ilya.drabenia
 */
public class DescriptionTemplateRef extends TemplateRef {
    private final String description;

    public DescriptionTemplateRef(DataCenterRef dataCenter, String description) {
        super(dataCenter);
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public DescriptionTemplateRef description(String description) {
        return new DescriptionTemplateRef(getDataCenter(), description);
    }

    public DescriptionTemplateRef dataCenter(DataCenterRef dataCenter) {
        return new DescriptionTemplateRef(dataCenter, description);
    }

    @Override
    public TemplateFilter asFilter() {
        return (
            new TemplateFilter()
                .dataCenterIn(getDataCenter())
                .descriptionContains(description)
        );
    }
}
