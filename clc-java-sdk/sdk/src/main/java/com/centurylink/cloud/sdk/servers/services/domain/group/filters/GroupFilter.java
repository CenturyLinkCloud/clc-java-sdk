package com.centurylink.cloud.sdk.servers.services.domain.group.filters;

import com.centurylink.cloud.sdk.core.datacenters.client.domain.DataCenterMetadata;
import com.centurylink.cloud.sdk.core.datacenters.services.domain.refs.DataCenterRef;
import com.centurylink.cloud.sdk.servers.client.domain.group.GroupMetadata;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import static java.util.Arrays.asList;

/**
 * @author Ilya Drabenia
 */
public class GroupFilter {
    private List<DataCenterRef> dataCenters = new ArrayList<>();
    private Predicate<DataCenterMetadata> filter;
    private List<Predicate<GroupMetadata>> groupFilter;

    public GroupFilter dataCenter(DataCenterRef dataCenterRef) {
        dataCenters.add(dataCenterRef);
        return this;
    }

    public GroupFilter dataCenters(DataCenterRef... dataCenters) {
        this.dataCenters.addAll(asList(dataCenters));
        return this;
    }

    public GroupFilter dataCenter(Predicate<DataCenterMetadata> filter) {
        this.filter = filter;
        return this;
    }

    public GroupFilter group(Predicate<GroupMetadata> filter) {
        this.groupFilter.add(filter);
        return this;
    }

}
