package com.centurylink.cloud.sdk.servers.services.domain.group.refs;

import com.centurylink.cloud.sdk.core.commons.services.domain.datacenters.refs.DataCenter;
import com.centurylink.cloud.sdk.servers.services.domain.group.filters.GroupFilter;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Reference that allow to find unique server group by it's ID and owned data center reference
 *
 * @author ilya.drabenia
 */
public class IdGroupRef extends GroupRef {
    private final String id;

    IdGroupRef(DataCenter dataCenter, String id) {
        super(dataCenter);
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public IdGroupRef id(String id) {
        return new IdGroupRef(dataCenter, id);
    }

    public IdGroupRef dataCenter(DataCenter dataCenter) {
        return new IdGroupRef(dataCenter, id);
    }

    @Override
    public GroupFilter asFilter() {
        checkNotNull(id, "Group ID must be not null");

        return super.asFilter().id(id);
    }
}
