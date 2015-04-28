package com.centurylink.cloud.sdk.servers.services.domain.group.refs;

import com.centurylink.cloud.sdk.core.services.refs.Reference;
import com.centurylink.cloud.sdk.servers.services.domain.group.GroupByNameRef;
import com.centurylink.cloud.sdk.servers.services.domain.group.filters.GroupFilter;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * @author ilya.drabenia
 */
public abstract class Group implements Reference {
    public final static String ARCHIVE = "Archive";
    public final static String TEMPLATES = "Templates";
    public final static String DEFAULT_GROUP = "Default Group";

    @Override
    public abstract GroupFilter asFilter();

    public static GroupByIdRef refById(String id) {
        return new GroupByIdRef(id);
    }

    public static GroupByNameRef refByName() {
        return new GroupByNameRef(null, null);
    }
}
