package com.centurylink.cloud.sdk.servers.services.domain.template.refs;

import com.centurylink.cloud.sdk.common.management.services.domain.datacenters.refs.DataCenter;
import com.centurylink.cloud.sdk.core.services.refs.Reference;
import com.centurylink.cloud.sdk.servers.services.domain.template.filters.TemplateFilter;

/**
 * {@inheritDoc}
 */
public abstract class Template implements Reference<TemplateFilter> {
    private final DataCenter dataCenter;

    Template(DataCenter dataCenter) {
        this.dataCenter = dataCenter;
    }

    /**
     * Method allow refer to required resource by it's ID.
     *
     * @return {@link com.centurylink.cloud.sdk.servers.services.domain.template.refs.TemplateByNameRef}
     */
    public static TemplateByNameRef refByName() {
        return new TemplateByNameRef(null, null);
    }

    /**
     * Method allow refer to resolve required template by it's operating system characteristics.
     * All parameters are optional.
     *
     * @return {@link TemplateByOsRef}
     */
    public static TemplateByOsRef refByOs() {
        return new TemplateByOsRef(null, null, null, null, null);
    }

    /**
     * Method allow to refer resource by keyword contains in description of this resource.
     * All parameters are required. If it will be found zero or multiple resource satisfied provided
     * criteria - it will throw exception.
     *
     * @return {@link TemplateByDescriptionRef}
     */
    public static TemplateByDescriptionRef refByDescription() {
        return new TemplateByDescriptionRef(null, null);
    }

    public DataCenter getDataCenter() {
        return dataCenter;
    }

    @Override
    public String toString() {
        return this.toReadableString();
    }
}
