package com.centurylink.cloud.sdk.servers.services.domain.template.refs;

import com.centurylink.cloud.sdk.servers.services.domain.datacenter.refs.DataCenterRef;

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
}
