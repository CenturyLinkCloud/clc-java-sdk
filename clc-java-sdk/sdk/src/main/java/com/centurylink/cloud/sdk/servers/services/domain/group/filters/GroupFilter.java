package com.centurylink.cloud.sdk.servers.services.domain.group.filters;

import com.centurylink.cloud.sdk.core.datacenters.client.domain.DataCenterMetadata;
import com.centurylink.cloud.sdk.core.datacenters.services.domain.filters.DataCentersFilter;
import com.centurylink.cloud.sdk.core.services.filter.EmptyPredicate;
import com.centurylink.cloud.sdk.core.datacenters.services.domain.refs.DataCenterRef;
import com.centurylink.cloud.sdk.core.services.filter.Filters;
import com.centurylink.cloud.sdk.servers.client.domain.group.GroupMetadata;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.*;
import java.util.logging.Filter;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static java.util.stream.Collectors.toList;


/**
 * @author Ilya Drabenia
 */
public class GroupFilter {
    private List<DataCentersFilter> dataCenters = new ArrayList<>();
    private Predicate<GroupMetadata> groupFilter = new EmptyPredicate<>();

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

    public GroupFilter idIn(String... ids) {
        checkNotNull(ids, "List of ids must be not a null");

        this.groupFilter =
            Stream.of(ids)
                .filter(id -> id != null)
                .map(id -> (Predicate<GroupMetadata>) (m -> Filters.equals(m.getId(), id)))
                .reduce(new EmptyPredicate<>(),
                    (previousResult, item) -> previousResult.and(item)
                );

        return this;
    }

    public GroupFilter nameContains(String subString) {
        checkNotNull(subString, "Name match criteria must be not a null");

        this.groupFilter = this.groupFilter.and(
            group -> Filters.containsIgnoreCase(group.getName(), subString)
        );

        return this;
    }

    public GroupFilter filter(Predicate<GroupMetadata> filter) {
        checkNotNull(filter, "Filter predicate must be not a null");

        this.groupFilter = this.groupFilter.and(filter);

        return this;
    }

    public List<DataCentersFilter> getDataCenters() {
        return dataCenters;
    }

    public Predicate<GroupMetadata> getGroupFilter() {
        return groupFilter;
    }
}
