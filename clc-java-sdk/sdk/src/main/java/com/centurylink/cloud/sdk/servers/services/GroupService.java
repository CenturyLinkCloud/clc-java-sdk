package com.centurylink.cloud.sdk.servers.services;

import com.centurylink.cloud.sdk.core.datacenters.client.DataCentersClient;
import com.centurylink.cloud.sdk.core.datacenters.client.domain.DataCenterMetadata;
import com.centurylink.cloud.sdk.core.datacenters.services.DataCenterService;
import com.centurylink.cloud.sdk.core.datacenters.services.domain.refs.DataCenterRef;
import com.centurylink.cloud.sdk.servers.client.ServerClient;
import com.centurylink.cloud.sdk.servers.client.domain.group.GroupMetadata;
import com.centurylink.cloud.sdk.servers.services.domain.group.Group;
import com.centurylink.cloud.sdk.servers.services.domain.group.GroupConverter;
import com.centurylink.cloud.sdk.servers.services.domain.group.filters.GroupFilter;
import com.centurylink.cloud.sdk.servers.services.domain.group.refs.GroupRef;
import com.google.inject.Inject;

import java.util.List;
import java.util.stream.Stream;

import static com.centurylink.cloud.sdk.core.services.refs.References.exceptionIfNotFound;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.collect.Iterables.getFirst;
import static java.util.stream.Collectors.toList;

/**
 * Service provide operations for query and manipulate groups of servers
 *
 * @author ilya.drabenia
 */
public class GroupService {
    private final ServerClient client;
    private final GroupConverter converter;
    private final DataCentersClient dataCentersClient;
    private final DataCenterService dataCenterService;

    @Inject
    public GroupService(ServerClient client, GroupConverter converter, DataCentersClient dataCentersClient,
                        DataCenterService dataCenterService) {
        this.client = client;
        this.converter = converter;
        this.dataCentersClient = dataCentersClient;
        this.dataCenterService = dataCenterService;
    }

    public GroupMetadata findByRef(GroupRef groupRef) {
        return exceptionIfNotFound(
            findFirst(groupRef.asFilter())
        );
    }

    public List<GroupMetadata> find(GroupFilter criteria) {
        return findLazy(criteria).collect(toList());
    }

    public Stream<GroupMetadata> findLazy(GroupFilter criteria) {
        checkNotNull(criteria, "Criteria must be not null");

        Stream<DataCenterMetadata> dataCenters;
        if (criteria.getDataCenters().size() > 0) {
            dataCenters =
                criteria
                    .getDataCenters().stream()
                    .flatMap(d -> dataCenterService.find(d).stream());
        } else {
            dataCenters =
                dataCentersClient
                    .findAllDataCenters()
                    .stream();
        }

        return
            dataCenters
                .map(d -> client.getGroup(d.getGroup().getId()))
                .flatMap(g -> g.getAllGroups().stream())
                .filter(criteria.getGroupFilter());
    }

    public GroupMetadata findFirst(GroupFilter criteria) {
        return getFirst(
            findLazy(criteria).limit(1).collect(toList()),
            null
        );
    }

    public List<Group> findByDataCenter(DataCenterRef dataCenter) {
        String rootGroupId = dataCentersClient
            .getDataCenter(
                dataCenterService.findByRef(dataCenter).getId()
            )
            .getGroup()
            .getId();

        GroupMetadata result = client.getGroup(rootGroupId);

        return converter.newGroupList(
            dataCenterService.findByRef(dataCenter).getId(),
            result.getAllGroups()
        );
    }

}
