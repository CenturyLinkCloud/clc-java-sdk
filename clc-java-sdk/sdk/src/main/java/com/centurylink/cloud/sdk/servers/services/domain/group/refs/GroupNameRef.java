package com.centurylink.cloud.sdk.servers.services.domain.group.refs;

import com.centurylink.cloud.sdk.common.management.services.domain.datacenters.refs.DataCenter;
import com.centurylink.cloud.sdk.servers.services.domain.group.filters.GroupFilter;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Reference that allow to find group by owned data center and group name
 *
 * @author ilya.drabenia
 */
public class GroupNameRef extends Group {
    private final DataCenter dataCenter;
    private final String name;

    GroupNameRef(DataCenter dataCenter, String name) {
        this.dataCenter = dataCenter;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public GroupNameRef name(String name) {
        return new GroupNameRef(dataCenter, name);
    }

    public GroupNameRef dataCenter(DataCenter dataCenter) {
        return new GroupNameRef(dataCenter, name);
    }

    @Override
    public GroupFilter asFilter() {
        checkNotNull(name, "Name must be not null");

        return new GroupFilter()
            .dataCenters(dataCenter)
            .nameContains(name);
    }
}
