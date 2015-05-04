package com.centurylink.cloud.sdk.servers.services.domain.group.refs;

import com.centurylink.cloud.sdk.common.management.services.domain.datacenters.refs.DataCenter;
import com.centurylink.cloud.sdk.core.services.refs.Reference;
import com.centurylink.cloud.sdk.servers.services.domain.group.filters.GroupFilter;

/**
 * {@inheritDoc}
 */
public abstract class Group implements Reference<GroupFilter> {
    public final static String ARCHIVE = "Archive";
    public final static String TEMPLATES = "Templates";
    public final static String DEFAULT_GROUP = "Default Group";

    /**
     * Method allow to refer group by it's ID. Comparison is by full match and case sensitive.
     *
     * @param id is not null ID of group
     * @return {@link com.centurylink.cloud.sdk.servers.services.domain.group.refs.GroupByIdRef}
     */
    public static GroupByIdRef refById(String id) {
        return new GroupByIdRef(id);
    }

    /**
     * Method allow to refer group by name. Filtering is by full match. Comparison is case insensitive.
     *
     * @return {@link com.centurylink.cloud.sdk.servers.services.domain.group.refs.GroupNameRef}
     */
    public static GroupNameRef refByName() {
        return new GroupNameRef(null, null);
    }

    public static GroupNameRef refByName(DataCenter dataCenter, String name) {
        return new GroupNameRef(dataCenter, name);
    }

    @Override
    public String toString() {
        return this.toReadableString();
    }
}
