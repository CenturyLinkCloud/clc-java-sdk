package com.centurylink.cloud.sdk.servers.services.domain.group.filters;

import com.centurylink.cloud.sdk.core.commons.client.domain.datacenters.DataCenterMetadata;
import com.centurylink.cloud.sdk.core.commons.services.domain.datacenters.filters.DataCenterFilter;
import com.centurylink.cloud.sdk.core.services.predicates.Predicates;
import com.centurylink.cloud.sdk.core.commons.services.domain.datacenters.refs.DataCenterRef;
import com.centurylink.cloud.sdk.core.services.filter.Filters;
import com.centurylink.cloud.sdk.servers.client.domain.group.GroupMetadata;

import java.util.ArrayList;
import java.util.List;
import java.util.function.*;
import java.util.stream.Stream;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static java.util.stream.Collectors.toList;


/**
 * Class that used to filter groups of servers
 *
 * @author Ilya Drabenia
 */
public class GroupFilter {
    private List<DataCenterFilter> dataCenters = new ArrayList<>();
    private Predicate<GroupMetadata> groupFilter = Predicates.alwaysTrue();

    /**
     * Method allow to restrict target groups using data centers in which this groups exists.
     *
     * @param dataCenters is not null list of data center references
     * @return {@link GroupFilter}
     */
    public GroupFilter dataCenterIn(DataCenterRef... dataCenters) {
        this.dataCenters.addAll(
            Stream.of(checkNotNull(dataCenters))
                .map(DataCenterRef::asFilter)
                .collect(toList())
        );

        return this;
    }

    /**
     * Method allow to provide filtering predicate that restrict group by data centers that contains its.
     *
     * @param predicate is not null filtering predicate
     * @return {@link GroupFilter}
     */
    public GroupFilter dataCenter(Predicate<DataCenterMetadata> predicate) {
        this.dataCenters.add(new DataCenterFilter(checkNotNull(predicate)));
        return this;
    }

    /**
     * Method allow to provide data center filter that allow to restrict groups by data centers that contains its
     *
     * @param filter is not null data center filter
     * @return {@link GroupFilter}
     */
    public GroupFilter dataCenter(DataCenterFilter filter) {
        this.dataCenters.add(checkNotNull(filter));
        return this;
    }

    /**
     * Method allow to filter groups by its IDs. Matching will be strong and case sensitive.
     *
     * @param ids is not null list of group IDs
     * @return {@link GroupFilter}
     */
    public GroupFilter idIn(String... ids) {
        checkNotNull(ids, "List of ids must be not a null");

        this.groupFilter =
            Stream.of(ids)
                .filter(id -> id != null)
                .map(id -> (Predicate<GroupMetadata>) (m -> Filters.equals(m.getId(), id)))
                .reduce(Predicates.alwaysFalse(),
                    (previousResult, item) -> previousResult.or(item)
                );

        return this;
    }

    /**
     * Method allow to filter groups by key phrase that contains in its name.
     * Filtering will be case insensitive and will use substring matching.
     *
     * @param subString is not null name of target group
     * @return {@link GroupFilter}
     */
    public GroupFilter nameContains(String subString) {
        checkNotNull(subString, "Name match criteria must be not a null");

        this.groupFilter = this.groupFilter.and(
            group -> Filters.containsIgnoreCase(group.getName(), subString)
        );

        return this;
    }

    /**
     * Method allow to filter groups using predicate.
     *
     * @param filter is not null group filtering predicate
     * @return {@link GroupFilter}
     */
    public GroupFilter filter(Predicate<GroupMetadata> filter) {
        checkNotNull(filter, "Filter predicate must be not a null");

        this.groupFilter = this.groupFilter.and(filter);

        return this;
    }

    public List<DataCenterFilter> getDataCenters() {
        return dataCenters;
    }

    public Predicate<GroupMetadata> getGroupFilter() {
        return groupFilter;
    }

}
