package com.centurylink.cloud.sdk.servers.services.domain.group.filters;

import com.centurylink.cloud.sdk.core.datacenters.client.domain.DataCenterMetadata;
import com.centurylink.cloud.sdk.core.datacenters.services.domain.filters.DataCentersFilter;
import com.centurylink.cloud.sdk.core.datacenters.services.domain.refs.DataCenterRef;
import com.centurylink.cloud.sdk.servers.client.domain.group.GroupMetadata;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.util.stream.Collectors.toList;


/**
 * @author Ilya Drabenia
 */
public class GroupFilter {
    private List<DataCentersFilter> dataCenters = new ArrayList<>();
    private Predicate<GroupMetadata> groupFilter;

    public GroupFilter dataCenterIn(DataCenterRef... dataCenters) {
        this.dataCenters.addAll(
            Stream.of(checkNotNull(dataCenters))
                .map(DataCenterRef::asFilter)
                .collect(toList())
        );

        return this;
    }

    public GroupFilter dataCenter(Predicate<DataCenterMetadata> predicate) {
        this.dataCenters.add(new DataCentersFilter(checkNotNull(predicate)));
        return this;
    }

    public GroupFilter dataCenter(DataCentersFilter filter) {
        this.dataCenters.add(checkNotNull(filter));
        return this;
    }

    public GroupFilter filter(Predicate<GroupMetadata> filter) {
        this.groupFilter = (this.groupFilter != null) ? this.groupFilter.and(filter) : filter;
        return this;
    }

    public List<DataCentersFilter> getDataCenters() {
        return dataCenters;
    }

    public Predicate<GroupMetadata> getGroupFilter() {
        return groupFilter;
    }
}
