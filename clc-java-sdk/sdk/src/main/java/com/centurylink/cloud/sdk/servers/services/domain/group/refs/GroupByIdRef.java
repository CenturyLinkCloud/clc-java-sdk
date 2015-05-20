package com.centurylink.cloud.sdk.servers.services.domain.group.refs;

import com.centurylink.cloud.sdk.servers.services.domain.group.filters.GroupFilter;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Reference that allow to find unique server group by it's ID and owned data center reference
 *
 * @author ilya.drabenia
 */
public class GroupByIdRef extends Group {
    private final String id;

    GroupByIdRef(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    @Override
    public GroupFilter asFilter() {
        checkNotNull(id, "Group ID must be not null");

        return new GroupFilter().id(id);
    }
}
