package com.centurylink.cloud.sdk.servers.services.domain.group.filters;

import com.centurylink.cloud.sdk.core.commons.client.domain.datacenters.DataCenterMetadata;
import com.centurylink.cloud.sdk.core.commons.services.domain.datacenters.filters.DataCenterFilter;
import com.centurylink.cloud.sdk.core.services.filter.Filter;
import com.centurylink.cloud.sdk.core.services.function.Predicates;
import com.centurylink.cloud.sdk.core.commons.services.domain.datacenters.refs.DataCenterRef;
import com.centurylink.cloud.sdk.servers.client.domain.group.GroupMetadata;

import java.util.function.*;

import static com.centurylink.cloud.sdk.core.services.filter.Filter.and;
import static com.centurylink.cloud.sdk.core.services.filter.Filter.or;
import static com.centurylink.cloud.sdk.core.services.function.Streams.map;
import static com.centurylink.cloud.sdk.core.services.function.Predicates.combine;
import static com.centurylink.cloud.sdk.core.services.function.Predicates.containsIgnoreCase;
import static com.centurylink.cloud.sdk.core.services.function.Predicates.in;
import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;


/**
 * Class that used to filter groups of servers
 *
 * @author Ilya Drabenia
 */
public class GroupFilter implements Filter<GroupFilter> {
    private DataCenterFilter dataCenterFilter = new DataCenterFilter(Predicates.alwaysTrue());
    private Predicate<GroupMetadata> predicate = Predicates.alwaysTrue();

    public GroupFilter() {

    }

    public GroupFilter(Predicate<GroupMetadata> groupFilter) {
        this.predicate = groupFilter;
    }

    /**
     * Method allow to restrict target groups using data centers in which this groups exists.
     *
     * @param dataCenters is not null list of data center references
     * @return {@link GroupFilter}
     */
    public GroupFilter dataCenterIn(DataCenterRef... dataCenters) {
        dataCenterFilter = dataCenterFilter.and(Filter.or(
            map(dataCenters, DataCenterRef::asFilter)
        ));

        return this;
    }

    /**
     * Method allow to provide filtering predicate that restrict group by data centers that contains its.
     *
     * @param predicate is not null filtering predicate
     * @return {@link GroupFilter}
     */
    public GroupFilter dataCenterWhere(Predicate<DataCenterMetadata> predicate) {
        dataCenterFilter.where(
            dataCenterFilter.getPredicate().or(predicate)
        );

        return this;
    }

    /**
     * Method allow to provide data center filter that allow to restrict groups by data centers that contains its
     *
     * @param filter is not null data center filter
     * @return {@link GroupFilter}
     */
    public GroupFilter dataCenterWhere(DataCenterFilter filter) {
        dataCenterFilter = dataCenterFilter.and(filter);
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

        this.predicate = predicate.and(
            combine(GroupMetadata::getId, in(ids))
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

        predicate = predicate.and(combine(
            GroupMetadata::getName, containsIgnoreCase(subString)
        ));

        return this;
    }

    /**
     * Method allow to filter groups using predicate.
     *
     * @param filter is not null group filtering predicate
     * @return {@link GroupFilter}
     */
    public GroupFilter where(Predicate<GroupMetadata> filter) {
        checkNotNull(filter, "Filter predicate must be not a null");

        this.predicate = this.predicate.and(filter);

        return this;
    }

    @Override
    public GroupFilter and(GroupFilter otherFilter) {
        checkNotNull(otherFilter, "Other filter must be not a null");

        return new GroupFilter()
            .dataCenterWhere(
                dataCenterFilter.and(otherFilter.dataCenterFilter)
            )
            .where(
                predicate.and(otherFilter.predicate)
            );
    }

    @Override
    public GroupFilter or(GroupFilter otherFilter) {
        checkNotNull(otherFilter, "Other filter must be not a null");

        return new GroupFilter()
            .dataCenterWhere(
                dataCenterFilter.or(otherFilter.dataCenterFilter)
            )
            .where(
                predicate.or(otherFilter.predicate)
            );
    }

    public DataCenterFilter getDataCenterFilter() {
        return dataCenterFilter;
    }

    public Predicate<GroupMetadata> getPredicate() {
        return predicate;
    }

}
