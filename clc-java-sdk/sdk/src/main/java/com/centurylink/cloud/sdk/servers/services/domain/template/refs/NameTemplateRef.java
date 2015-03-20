package com.centurylink.cloud.sdk.servers.services.domain.template.refs;

import com.centurylink.cloud.sdk.core.datacenters.services.domain.refs.DataCenterRef;

/**
 * @author ilya.drabenia
 */
public class NameTemplateRef extends TemplateRef {
    private final String name;

    public NameTemplateRef(DataCenterRef dataCenter, String name) {
        super(dataCenter);
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public NameTemplateRef name(String name) {
        return new NameTemplateRef(getDataCenter(), name);
    }

    public NameTemplateRef dataCenter(DataCenterRef dataCenter) {
        return new NameTemplateRef(dataCenter, name);
    }
}
