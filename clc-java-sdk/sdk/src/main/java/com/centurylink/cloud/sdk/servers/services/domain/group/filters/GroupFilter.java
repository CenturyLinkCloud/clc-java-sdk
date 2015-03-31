package com.centurylink.cloud.sdk.servers.services.domain.group.filters;

import com.centurylink.cloud.sdk.core.datacenters.client.domain.DataCenterMetadata;
import com.centurylink.cloud.sdk.core.datacenters.services.domain.refs.DataCenterRef;
import com.centurylink.cloud.sdk.servers.services.domain.group.Group;

import java.util.function.Predicate;

/**
 * @author Ilya Drabenia
 */
public class GroupFilter {

    public GroupFilter dataCenter(DataCenterRef dataCenterRef) {
        return this;
    }

    public GroupFilter dataCenters(DataCenterRef... dataCenters) {
        return this;
    }

    public GroupFilter dataCenter(Predicate<DataCenterMetadata> filter) {
        return this;
    }

    public GroupFilter group(Predicate<Group> filter) {
        return this;
    }

}
