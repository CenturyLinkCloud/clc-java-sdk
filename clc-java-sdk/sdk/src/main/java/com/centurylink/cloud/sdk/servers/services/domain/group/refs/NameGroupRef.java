package com.centurylink.cloud.sdk.servers.services.domain.group.refs;

import com.centurylink.cloud.sdk.core.commons.services.domain.datacenters.refs.DataCenter;
import com.centurylink.cloud.sdk.servers.services.domain.group.filters.GroupFilter;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Reference that allow to find group by owned data center and group name
 *
 * @author ilya.drabenia
 */
public class NameGroupRef extends GroupRef {
    private final String name;

    NameGroupRef(DataCenter dataCenter, String name) {
        super(dataCenter);
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public NameGroupRef name(String name) {
        return new NameGroupRef(dataCenter, name);
    }

    public NameGroupRef dataCenter(DataCenter dataCenter) {
        return new NameGroupRef(dataCenter, name);
    }

    @Override
    public GroupFilter asFilter() {
        checkNotNull(name, "Name must be not null");

        return super.asFilter()
            .nameContains(name);
    }
}
