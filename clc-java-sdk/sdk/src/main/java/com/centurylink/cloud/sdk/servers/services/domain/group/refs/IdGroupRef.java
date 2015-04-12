package com.centurylink.cloud.sdk.servers.services.domain.group.refs;

import com.centurylink.cloud.sdk.core.datacenters.services.domain.refs.DataCenterRef;
import com.centurylink.cloud.sdk.servers.services.domain.group.filters.GroupFilter;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * @author ilya.drabenia
 */
public class IdGroupRef extends GroupRef {
    private final String id;

    public IdGroupRef(DataCenterRef dataCenter, String id) {
        super(dataCenter);
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public IdGroupRef id(String id) {
        return new IdGroupRef(dataCenter, id);
    }

    public IdGroupRef dataCenter(DataCenterRef dataCenter) {
        return new IdGroupRef(dataCenter, id);
    }

    @Override
    public GroupFilter asFilter() {
        return super.asFilter()
            .idIn(checkNotNull(id));
    }
}
