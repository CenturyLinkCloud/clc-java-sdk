package com.centurylink.cloud.sdk.servers.services.domain.server.filters;

import com.centurylink.cloud.sdk.core.commons.client.domain.datacenters.DataCenterMetadata;
import com.centurylink.cloud.sdk.core.commons.services.domain.datacenters.filters.DataCenterFilter;
import com.centurylink.cloud.sdk.core.commons.services.domain.datacenters.refs.DataCenterRef;
import com.centurylink.cloud.sdk.core.services.filter.Filter;
import com.centurylink.cloud.sdk.core.services.function.Predicates;
import com.centurylink.cloud.sdk.servers.client.domain.group.GroupMetadata;
import com.centurylink.cloud.sdk.servers.client.domain.server.metadata.ServerMetadata;
import com.centurylink.cloud.sdk.servers.services.domain.group.filters.GroupFilter;
import com.centurylink.cloud.sdk.servers.services.domain.group.refs.GroupRef;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import static com.centurylink.cloud.sdk.core.services.function.Predicates.combine;
import static com.centurylink.cloud.sdk.core.services.function.Predicates.in;
import static com.centurylink.cloud.sdk.core.services.function.Streams.map;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.collect.Sets.intersection;
import static com.google.common.collect.Sets.newHashSet;
import static java.util.Arrays.asList;

/**
 * @author Ilya Drabenia
 */
public class ServerFilter implements Filter<ServerFilter> {
    private List<String> serverIds = new ArrayList<>();
    private GroupFilter groupFilter = new GroupFilter(Predicates.alwaysTrue());
    private Predicate<ServerMetadata> predicate = Predicates.alwaysTrue();

    public ServerFilter() {
    }

    public ServerFilter(Predicate<ServerMetadata> predicate) {
        this.predicate = predicate;
    }

    /**
     * Method allow to restrict target groups using data centers in which this groups exists.
     *
     * @param dataCenters is not null list of data center references
     * @return {@link GroupFilter}
     */
    public ServerFilter dataCenters(DataCenterRef... dataCenters) {
        groupFilter.dataCenters(dataCenters);

        return this;
    }

    /**
     * Method allow to provide filtering predicate that restrict group by data centers that contains its.
     *
     * @param predicate is not null filtering predicate
     * @return {@link GroupFilter}
     */
    public ServerFilter dataCentersWhere(Predicate<DataCenterMetadata> predicate) {
        groupFilter.dataCentersWhere(predicate);

        return this;
    }

    /**
     * Method allow to provide data center filter that allow to restrict groups by data centers that contains its
     *
     * @param filter is not null data center filter
     * @return {@link GroupFilter}
     */
    public ServerFilter dataCentersWhere(DataCenterFilter filter) {
        groupFilter.dataCentersWhere(filter);

        return this;
    }

    /**
     * Method allow to filter groups by its IDs. Matching will be strong and case sensitive.
     *
     * @param ids is not null list of group IDs
     * @return {@link GroupFilter}
     */
    public ServerFilter groupId(String... ids) {
        groupFilter.id(ids);

        return this;
    }

    /**
     * Method allow to filter groups by key phrase that contains in its name.
     * Filtering will be case insensitive and will use substring matching.
     *
     * @param subString is not null name of target group
     * @return {@link GroupFilter}
     */
    public ServerFilter groupNameContains(String subString) {
        groupFilter.nameContains(subString);

        return this;
    }

    /**
     * Method allow to filter groups using predicate.
     *
     * @param filter is not null group filtering predicate
     * @return {@link GroupFilter}
     */
    public ServerFilter groupsWhere(Predicate<GroupMetadata> filter) {
        groupFilter.where(filter);

        return this;
    }

    public ServerFilter groupsWhere(GroupFilter filter) {
        groupFilter = groupFilter.and(filter);

        return this;
    }

    public ServerFilter groups(GroupRef... groups) {
        groupFilter = groupFilter.and(Filter.or(
            map(groups, GroupRef::asFilter)
        ));

        return this;
    }

    public ServerFilter where(Predicate<ServerMetadata> filter) {
        predicate = predicate.and(filter);

        return this;
    }

    public ServerFilter id(String... ids) {
        serverIds.addAll(asList(ids));

        return this;
    }

    public ServerFilter id(List<String> ids) {
        serverIds.addAll(ids);

        return this;
    }

    public ServerFilter onlyActive() {
        predicate = predicate.and(s -> s.getStatus().equals("active"));
        return this;
    }

    public ServerFilter status(String... statuses) {
        predicate = predicate.and(combine(
            ServerMetadata::getStatus, in(statuses)
        ));

        return this;
    }

    @Override
    public ServerFilter and(ServerFilter otherFilter) {
        return new ServerFilter()
            .id(new ArrayList<>(intersection(
                newHashSet(getServerIds()),
                newHashSet(otherFilter.getServerIds())
            )))
            .groupsWhere(
                groupFilter.and(otherFilter.groupFilter)
            )
            .where(
                predicate.and((otherFilter.predicate))
            );
    }

    @Override
    public ServerFilter or(ServerFilter otherFilter) {
        return new ServerFilter()
            .id(new ArrayList<String>() {{
                addAll(getServerIds());
                addAll(otherFilter.getServerIds());
            }})
            .groupsWhere(
                groupFilter.or(otherFilter.groupFilter)
            )
            .where(
                predicate.or(otherFilter.predicate)
            );
    }

    public GroupFilter getGroupFilter() {
        return groupFilter;
    }

    public Predicate<ServerMetadata> getPredicate() {
        return predicate;
    }

    public List<String> getServerIds() {
        return serverIds;
    }
}
