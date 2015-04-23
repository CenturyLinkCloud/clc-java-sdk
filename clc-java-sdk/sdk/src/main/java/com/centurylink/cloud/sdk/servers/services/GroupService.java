package com.centurylink.cloud.sdk.servers.services;

import com.centurylink.cloud.sdk.core.client.domain.Link;
import com.centurylink.cloud.sdk.core.commons.client.DataCentersClient;
import com.centurylink.cloud.sdk.core.commons.client.domain.datacenters.DataCenterMetadata;
import com.centurylink.cloud.sdk.core.commons.services.DataCenterService;
import com.centurylink.cloud.sdk.core.commons.services.domain.datacenters.refs.DataCenterRef;
import com.centurylink.cloud.sdk.core.commons.services.domain.queue.future.OperationFuture;
import com.centurylink.cloud.sdk.core.commons.services.domain.queue.future.job.NoWaitingJobFuture;
import com.centurylink.cloud.sdk.servers.client.ServerClient;
import com.centurylink.cloud.sdk.servers.client.domain.group.GroupMetadata;
import com.centurylink.cloud.sdk.servers.services.domain.group.Group;
import com.centurylink.cloud.sdk.servers.services.domain.group.GroupConfig;
import com.centurylink.cloud.sdk.servers.services.domain.group.GroupConverter;
import com.centurylink.cloud.sdk.servers.services.domain.group.filters.GroupFilter;
import com.centurylink.cloud.sdk.servers.services.domain.group.refs.GroupRef;
import com.centurylink.cloud.sdk.servers.services.domain.group.refs.IdGroupRef;
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

        Stream<DataCenterMetadata> dataCenters =
            dataCenterService
                .findLazy(criteria.getDataCenterFilter());

        return
            dataCenters
                .map(d -> client.getGroup(d.getGroup().getId()))
                .flatMap(g -> g.getAllGroups().stream())
                .filter(criteria.getPredicate());
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

    public OperationFuture<GroupRef> create(GroupConfig groupConfig) {
        checkNotNull(groupConfig, "GroupConfig must be not null");
        checkNotNull(groupConfig.getName(), "Name of GroupConfig must be not null");
        checkNotNull(groupConfig.getParentGroup(), "ParentGroup of GroupConfig must be not null");

        GroupMetadata group = client.createGroup(converter.createGroupRequest(groupConfig, idByRef(groupConfig.getParentGroup())));

        return new OperationFuture<>(
                new IdGroupRef(groupConfig.getParentGroup().getDataCenter(), group.getId()),
                new NoWaitingJobFuture()
        );
    }

    public GroupMetadata get(GroupRef groupRef) {
        return client.getGroup(idByRef(groupRef));
    }

    public Link delete(GroupRef groupRef) {
        return client.deleteGroup(idByRef(groupRef));
    }

    String idByRef(GroupRef ref) {
        if (ref.is(IdGroupRef.class)) {
            return ref.as(IdGroupRef.class).getId();
        } else {
            return findByRef(ref).getId();
        }
    }

}
