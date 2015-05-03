package com.centurylink.cloud.sdk.servers.services.domain.group.refs;

import com.centurylink.cloud.sdk.core.ToStringMixin;
import com.centurylink.cloud.sdk.core.services.refs.Reference;
import com.centurylink.cloud.sdk.servers.services.domain.group.filters.GroupFilter;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * @author ilya.drabenia
 */
public abstract class Group implements Reference<GroupFilter> {
    public final static String ARCHIVE = "Archive";
    public final static String TEMPLATES = "Templates";
    public final static String DEFAULT_GROUP = "Default Group";

    public static GroupByIdRef refById(String id) {
        return new GroupByIdRef(id);
    }

    public static GroupNameRef refByName() {
        return new GroupNameRef(null, null);
    }

    @Override
    public String toString() {
        return this.toReadableString();
    }
}
