package com.centurylink.cloud.sdk.servers.services.domain.server.filters;

import com.centurylink.cloud.sdk.core.commons.client.domain.datacenters.DataCenterMetadata;
import com.centurylink.cloud.sdk.core.commons.services.domain.datacenters.filters.DataCenterFilter;
import com.centurylink.cloud.sdk.core.commons.services.domain.datacenters.refs.DataCenter;
import com.centurylink.cloud.sdk.core.services.filter.AbstractResourceFilter;
import com.centurylink.cloud.sdk.core.services.filter.Filter;
import com.centurylink.cloud.sdk.core.services.filter.evaluation.AndEvaluation;
import com.centurylink.cloud.sdk.core.services.filter.evaluation.OrEvaluation;
import com.centurylink.cloud.sdk.core.services.filter.evaluation.SingleFilterEvaluation;
import com.centurylink.cloud.sdk.core.services.function.Predicates;
import com.centurylink.cloud.sdk.servers.client.domain.group.GroupMetadata;
import com.centurylink.cloud.sdk.servers.client.domain.server.metadata.ServerMetadata;
import com.centurylink.cloud.sdk.servers.services.domain.group.filters.GroupFilter;
import com.centurylink.cloud.sdk.servers.services.domain.group.refs.Group;
import com.centurylink.cloud.sdk.servers.services.domain.server.refs.Server;

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
 * Class allow to select by specified search conditions needed subset of account servers
 *
 * @author Ilya Drabenia
 */
public class ServerFilter extends AbstractResourceFilter<ServerFilter> {
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
    public ServerFilter dataCenters(DataCenter... dataCenters) {
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

    /**
     * Method allow to specify {@link GroupFilter} for restrict server groups
     *
     * @param filter is not a null group filter object
     * @return {@link GroupFilter}
     * @throws NullPointerException
     */
    public ServerFilter groupsWhere(GroupFilter filter) {
        groupFilter = groupFilter.and(filter);
        return this;
    }

    /**
     * Method allow to restrict searched servers by groups
     *
     * @param groups is list of group references
     * @return {@link GroupFilter}
     */
    public ServerFilter groups(Group... groups) {
        groupFilter = groupFilter.and(Filter.or(
            map(groups, Group::asFilter)
        ));

        return this;
    }

    /**
     * Method allow to specify custom search servers predicate
     *
     * @param filter is not null custom filtering predicate
     * @return {@link GroupFilter}
     * @throws NullPointerException
     */
    public ServerFilter where(Predicate<ServerMetadata> filter) {
        checkNotNull(filter, "Filter must be not a null");

        predicate = predicate.and(filter);

        return this;
    }

    /**
     * Method allow to restrict servers by target IDs
     *
     * @param ids is a list of string ID representations
     * @return {@link GroupFilter}
     */
    public ServerFilter id(String... ids) {
        serverIds.addAll(asList(ids));

        return this;
    }

    public ServerFilter id(List<String> ids) {
        serverIds.addAll(ids);

        return this;
    }

    /**
     * Method allow to select only active servers
     *
     * @return {@link GroupFilter}
     */
    public ServerFilter onlyActive() {
        predicate = predicate.and(s -> s.getStatus().equals("active"));
        return this;
    }

    /**
     * Method allow to restrict status of target servers
     *
     * @param statuses is a list target server statuses
     * @return {@link GroupFilter}
     */
    public ServerFilter status(String... statuses) {
        predicate = predicate.and(combine(
            ServerMetadata::getStatus, in(statuses)
        ));

        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ServerFilter and(ServerFilter otherFilter) {
        if (this.filtersChain instanceof SingleFilterEvaluation &&
            otherFilter.filtersChain instanceof SingleFilterEvaluation) {
            return
                new ServerFilter()
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
        } else {
            filtersChain = new AndEvaluation<>(filtersChain, otherFilter, ServerMetadata::getId);

            return this;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ServerFilter or(ServerFilter otherFilter) {
        filtersChain = new OrEvaluation<ServerFilter>(filtersChain, otherFilter, ServerMetadata::getId);

        return this;
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
