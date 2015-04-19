package com.centurylink.cloud.sdk.servers.services.domain.server.filters;

import com.centurylink.cloud.sdk.core.commons.client.domain.datacenters.DataCenterMetadata;
import com.centurylink.cloud.sdk.core.commons.services.domain.datacenters.filters.DataCenterFilter;
import com.centurylink.cloud.sdk.core.commons.services.domain.datacenters.refs.DataCenterRef;
import com.centurylink.cloud.sdk.core.services.filter.Filters;
import com.centurylink.cloud.sdk.core.services.function.Predicates;
import com.centurylink.cloud.sdk.servers.client.domain.group.GroupMetadata;
import com.centurylink.cloud.sdk.servers.client.domain.server.metadata.ServerMetadata;
import com.centurylink.cloud.sdk.servers.services.domain.group.filters.GroupFilter;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.util.stream.Collectors.toList;

/**
 * @author Ilya Drabenia
 */
public class ServerFilter {
    private final GroupFilter groupFilters = new GroupFilter();
    private final Predicate<ServerMetadata> predicate = Predicates.alwaysTrue();

    /**
     * Method allow to restrict target groups using data centers in which this groups exists.
     *
     * @param dataCenters is not null list of data center references
     * @return {@link GroupFilter}
     */
    public ServerFilter dataCenterIn(DataCenterRef... dataCenters) {

        return this;
    }

    /**
     * Method allow to provide filtering predicate that restrict group by data centers that contains its.
     *
     * @param predicate is not null filtering predicate
     * @return {@link GroupFilter}
     */
    public ServerFilter dataCenter(Predicate<DataCenterMetadata> predicate) {
        return this;
    }

    /**
     * Method allow to provide data center filter that allow to restrict groups by data centers that contains its
     *
     * @param filter is not null data center filter
     * @return {@link GroupFilter}
     */
    public ServerFilter dataCenter(DataCenterFilter filter) {
        return this;
    }

    /**
     * Method allow to filter groups by its IDs. Matching will be strong and case sensitive.
     *
     * @param ids is not null list of group IDs
     * @return {@link GroupFilter}
     */
    public ServerFilter idIn(String... ids) {
        checkNotNull(ids, "List of ids must be not a null");
        return this;
    }

    /**
     * Method allow to filter groups by key phrase that contains in its name.
     * Filtering will be case insensitive and will use substring matching.
     *
     * @param subString is not null name of target group
     * @return {@link GroupFilter}
     */
    public ServerFilter nameContains(String subString) {
        checkNotNull(subString, "Name match criteria must be not a null");

        return this;
    }

    /**
     * Method allow to filter groups using predicate.
     *
     * @param filter is not null group filtering predicate
     * @return {@link GroupFilter}
     */
    public ServerFilter filter(Predicate<GroupMetadata> filter) {
        checkNotNull(filter, "Filter predicate must be not a null");

        return this;
    }


}
