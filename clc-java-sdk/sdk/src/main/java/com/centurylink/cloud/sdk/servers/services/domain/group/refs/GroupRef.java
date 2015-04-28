package com.centurylink.cloud.sdk.servers.services.domain.group.refs;

import com.centurylink.cloud.sdk.core.commons.services.domain.datacenters.refs.DataCenter;
import com.centurylink.cloud.sdk.core.services.refs.Reference;
import com.centurylink.cloud.sdk.servers.services.domain.group.filters.GroupFilter;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * @author ilya.drabenia
 */
public abstract class GroupRef implements Reference {
    protected final DataCenter dataCenter;

    GroupRef(DataCenter dataCenter) {
        this.dataCenter = dataCenter;
    }

    public DataCenter getDataCenter() {
        return dataCenter;
    }

    @Override
    public GroupFilter asFilter() {
        return new GroupFilter() {{
            if (getDataCenter() != null) {
                dataCenters(GroupRef.this.getDataCenter());
            }
        }};
    }

    public static IdGroupRef matchById() {
        return new IdGroupRef(null, null);
    }

    public static IdGroupRef matchById(String id) {
        return new IdGroupRef(null, id);
    }

    public static NameGroupRef matchByName() {
        return new NameGroupRef(null, null);
    }
}
