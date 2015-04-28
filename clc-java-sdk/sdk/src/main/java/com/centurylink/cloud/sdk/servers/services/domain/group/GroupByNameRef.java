package com.centurylink.cloud.sdk.servers.services.domain.group;

import com.centurylink.cloud.sdk.core.commons.services.domain.datacenters.refs.DataCenter;
import com.centurylink.cloud.sdk.servers.services.domain.group.filters.GroupFilter;
import com.centurylink.cloud.sdk.servers.services.domain.group.refs.Group;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Reference that allow to find group by owned data center and group name
 *
 * @author ilya.drabenia
 */
public class GroupByNameRef extends Group {
    private final DataCenter dataCenter;
    private final String name;

    GroupByNameRef(DataCenter dataCenter, String name) {
        this.dataCenter = dataCenter;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public GroupByNameRef name(String name) {
        return new GroupByNameRef(dataCenter, name);
    }

    public GroupByNameRef dataCenter(DataCenter dataCenter) {
        return new GroupByNameRef(dataCenter, name);
    }

    @Override
    public GroupFilter asFilter() {
        checkNotNull(name, "Name must be not null");

        return new GroupFilter()
            .dataCenters(dataCenter)
            .nameContains(name);
    }
}
