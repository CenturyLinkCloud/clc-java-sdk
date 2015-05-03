package com.centurylink.cloud.sdk.servers.services;

import com.centurylink.cloud.sdk.core.client.ClcClientException;
import com.centurylink.cloud.sdk.core.client.domain.Link;
import com.centurylink.cloud.sdk.common.management.client.DataCentersClient;
import com.centurylink.cloud.sdk.common.management.client.QueueClient;
import com.centurylink.cloud.sdk.common.management.client.domain.datacenters.DataCenterMetadata;
import com.centurylink.cloud.sdk.common.management.services.DataCenterService;
import com.centurylink.cloud.sdk.common.management.services.domain.datacenters.refs.DataCenter;
import com.centurylink.cloud.sdk.common.management.services.domain.queue.future.OperationFuture;
import com.centurylink.cloud.sdk.common.management.services.domain.queue.future.job.NoWaitingJobFuture;
import com.centurylink.cloud.sdk.core.services.refs.Reference;
import com.centurylink.cloud.sdk.servers.client.ServerClient;
import com.centurylink.cloud.sdk.servers.client.domain.group.GroupMetadata;
import com.centurylink.cloud.sdk.servers.client.domain.server.BaseServerResponse;
import com.centurylink.cloud.sdk.servers.client.domain.server.CreateSnapshotRequest;
import com.centurylink.cloud.sdk.servers.services.domain.group.GroupConfig;
import com.centurylink.cloud.sdk.servers.services.domain.group.GroupConverter;
import com.centurylink.cloud.sdk.servers.services.domain.group.filters.GroupFilter;
import com.centurylink.cloud.sdk.servers.services.domain.group.refs.Group;
import com.centurylink.cloud.sdk.servers.services.domain.group.refs.GroupByIdRef;
import com.google.inject.Inject;
import com.google.inject.Provider;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static com.centurylink.cloud.sdk.core.function.Predicates.*;
import static com.centurylink.cloud.sdk.core.services.refs.Reference.evalUsingFindByFilter;
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
    private final Provider<ServerService> serverServiceProvider;
    private final QueueClient queueClient;

    @Inject
    public GroupService(ServerClient client, GroupConverter converter, DataCentersClient dataCentersClient,
                        DataCenterService dataCenterService, QueueClient queueClient,
                        Provider<ServerService> serverServiceProvider) {
        this.client = client;
        this.converter = converter;
        this.dataCentersClient = dataCentersClient;
        this.dataCenterService = dataCenterService;
        this.serverServiceProvider = serverServiceProvider;
        this.queueClient = queueClient;
    }

    public ServerService serverService() {
        return serverServiceProvider.get();
    }

    public GroupMetadata findByRef(Group groupRef) {
        return evalUsingFindByFilter(groupRef, this::find);
    }

    public List<GroupMetadata> find(GroupFilter criteria) {
        return findLazy(criteria).collect(toList());
    }

    public Stream<GroupMetadata> findLazy(GroupFilter groupCriteria) {
        checkNotNull(groupCriteria, "Criteria must be not null");

        return
            groupCriteria.applyFindLazy(criteria -> {
                if (isAlwaysTruePredicate(criteria.getPredicate()) &&
                    isAlwaysTruePredicate(criteria.getDataCenterFilter().getPredicate()) &&
                    criteria.getIds().size() > 0) {
                    return
                        criteria.getIds().stream()
                            .map(curId -> client.getGroup(curId, true));
                } else {
                    Stream<DataCenterMetadata> dataCenters =
                        dataCenterService
                            .findLazy(criteria.getDataCenterFilter());

                    return
                        dataCenters
                            .map(d -> client.getGroup(d.getGroup().getId(), true))
                            .flatMap(g -> g.getAllGroups().stream())
                            .filter(criteria.getPredicate())
                            .filter((criteria.getIds().size() > 0) ?
                                combine(GroupMetadata::getId, in(criteria.getIds())) : alwaysTrue()
                            );
                }
            });
    }

    public GroupMetadata findFirst(GroupFilter criteria) {
        return getFirst(
            findLazy(criteria).limit(1).collect(toList()),
            null
        );
    }

    public List<GroupMetadata> findByDataCenter(DataCenter dataCenter) {
        String rootGroupId = dataCentersClient
            .getDataCenter(
                dataCenterService.findByRef(dataCenter).getId()
            )
            .getGroup()
            .getId();

        try {
            return
                client
                    .getGroup(rootGroupId, false)
                    .getAllGroups();
        } catch (ClcClientException ex) {
            return new ArrayList<>();
        }
    }

    public OperationFuture<Group> create(GroupConfig groupConfig) {
        checkNotNull(groupConfig, "GroupConfig must be not null");
        checkNotNull(groupConfig.getName(), "Name of GroupConfig must be not null");
        checkNotNull(groupConfig.getParentGroup(), "ParentGroup of GroupConfig must be not null");

        GroupMetadata group = client.createGroup(
            converter.createGroupRequest(groupConfig, idByRef(groupConfig.getParentGroup()))
        );

        return new OperationFuture<>(
            Group.refById(group.getId()),
            new NoWaitingJobFuture()
        );
    }

    public OperationFuture<Group> update(Group groupRef, GroupConfig groupConfig) {
        checkNotNull(groupConfig, "GroupConfig must be not null");
        boolean updated = client
            .updateGroup(
                idByRef(groupRef),
                converter.createUpdateGroupRequest(groupConfig, idByRef(groupConfig.getParentGroup()))
            );

        return new OperationFuture<>(
            groupRef,
            new NoWaitingJobFuture()
        );
    }

    public OperationFuture<Link> delete(Group groupRef) {
        Link response = client.deleteGroup(idByRef(groupRef));

        return new OperationFuture<>(
            response,
            response.getId(),
            queueClient
        );
    }

    String idByRef(Group ref) {
        if (ref.is(GroupByIdRef.class)) {
            return ref.as(GroupByIdRef.class).getId();
        } else {
            return findByRef(ref).getId();
        }
    }

    /**
     * Power on groups of servers
     *
     * @param groupFilter search servers criteria by group filter
     * @return OperationFuture wrapper for BaseServerResponse list
     */
    public OperationFuture<List<BaseServerResponse>> powerOn(GroupFilter groupFilter) {
        return serverService().powerOperationResponse(
            client.powerOn(serverService().ids(groupFilter))
        );
    }

    /**
     * Power on groups of servers
     *
     * @param groupRefs groups references list
     * @return OperationFuture wrapper for BaseServerResponse list
     */
    public OperationFuture<List<BaseServerResponse>> powerOn(Group... groupRefs) {
        return serverService().powerOperationResponse(
            client.powerOn(serverService().ids(groupRefs))
        );
    }

    /**
     * Power off groups of servers
     *
     * @param groupFilter search servers criteria by group filter
     * @return OperationFuture wrapper for BaseServerResponse list
     */
    public OperationFuture<List<BaseServerResponse>> powerOff(GroupFilter groupFilter) {
        return serverService().powerOperationResponse(
            client.powerOff(serverService().ids(groupFilter))
        );
    }

    /**
     * Power off groups of servers
     *
     * @param groupRefs groups references list
     * @return OperationFuture wrapper for BaseServerResponse list
     */
    public OperationFuture<List<BaseServerResponse>> powerOff(Group... groupRefs) {
        return serverService().powerOperationResponse(
            client.powerOff(serverService().ids(groupRefs))
        );
    }

    /**
     * Start servers groups maintenance
     *
     * @param groupFilter search servers criteria by group filter
     * @return OperationFuture wrapper for BaseServerResponse list
     */
    public OperationFuture<List<BaseServerResponse>> startMaintenance(GroupFilter groupFilter) {
        return serverService().powerOperationResponse(
            client.startMaintenance(serverService().ids(groupFilter))
        );
    }

    /**
     * Start servers groups maintenance
     *
     * @param groupRefs groups references list
     * @return OperationFuture wrapper for BaseServerResponse list
     */
    public OperationFuture<List<BaseServerResponse>> startMaintenance(Group... groupRefs) {
        return serverService().powerOperationResponse(
            client.startMaintenance(serverService().ids(groupRefs))
        );
    }

    /**
     * Stop servers groups maintenance
     *
     * @param groupFilter search servers criteria by group filter
     * @return OperationFuture wrapper for BaseServerResponse list
     */
    public OperationFuture<List<BaseServerResponse>> stopMaintenance(GroupFilter groupFilter) {
        return serverService().powerOperationResponse(
            client.stopMaintenance(serverService().ids(groupFilter))
        );
    }

    /**
     * Stop servers groups maintenance
     *
     * @param groupRefs groups references list
     * @return OperationFuture wrapper for BaseServerResponse list
     */
    public OperationFuture<List<BaseServerResponse>> stopMaintenance(Group... groupRefs) {
        return serverService().powerOperationResponse(
            client.stopMaintenance(serverService().ids(groupRefs))
        );
    }

    /**
     * Pause groups of servers
     *
     * @param groupFilter search servers criteria by group filter
     * @return OperationFuture wrapper for BaseServerResponse list
     */
    public OperationFuture<List<BaseServerResponse>> pause(GroupFilter groupFilter) {
        return serverService().powerOperationResponse(
            client.pause(serverService().ids(groupFilter))
        );
    }

    /**
     * Pause groups of servers
     *
     * @param groupRefs groups references list
     * @return OperationFuture wrapper for BaseServerResponse list
     */
    public OperationFuture<List<BaseServerResponse>> pause(Group... groupRefs) {
        return serverService().powerOperationResponse(
            client.pause(serverService().ids(groupRefs))
        );
    }

    /**
     * Reboot groups of servers
     *
     * @param groupFilter search servers criteria by group filter
     * @return OperationFuture wrapper for BaseServerResponse list
     */
    public OperationFuture<List<BaseServerResponse>> reboot(GroupFilter groupFilter) {
        return serverService().powerOperationResponse(
            client.reboot(serverService().ids(groupFilter))
        );
    }

    /**
     * Reboot groups of servers
     *
     * @param groupRefs groups references list
     * @return OperationFuture wrapper for BaseServerResponse list
     */
    public OperationFuture<List<BaseServerResponse>> reboot(Group... groupRefs) {
        return serverService().powerOperationResponse(
            client.reboot(serverService().ids(groupRefs))
        );
    }

    /**
     * Reset groups of servers
     *
     * @param groupFilter search servers criteria by group filter
     * @return OperationFuture wrapper for BaseServerResponse list
     */
    public OperationFuture<List<BaseServerResponse>> reset(GroupFilter groupFilter) {
        return serverService().powerOperationResponse(
            client.reset(serverService().ids(groupFilter))
        );
    }

    /**
     * Reset groups of servers
     *
     * @param groupRefs groups references list
     * @return OperationFuture wrapper for BaseServerResponse list
     */
    public OperationFuture<List<BaseServerResponse>> reset(Group... groupRefs) {
        return serverService().powerOperationResponse(
            client.reset(serverService().ids(groupRefs))
        );
    }

    /**
     * Shut down groups of servers
     *
     * @param groupFilter search servers criteria by group filter
     * @return OperationFuture wrapper for BaseServerResponse list
     */
    public OperationFuture<List<BaseServerResponse>> shutDown(GroupFilter groupFilter) {
        return serverService().powerOperationResponse(
            client.shutDown(serverService().ids(groupFilter))
        );
    }

    /**
     * Shut down groups of servers
     *
     * @param groupRefs groups references list
     * @return OperationFuture wrapper for BaseServerResponse list
     */
    public OperationFuture<List<BaseServerResponse>> shutDown(Group... groupRefs) {
        return serverService().powerOperationResponse(
            client.shutDown(serverService().ids(groupRefs))
        );
    }

    /**
     * Archive groups of servers
     *
     * @param groupFilter search servers criteria by group filter
     * @return OperationFuture wrapper for BaseServerResponse list
     */
    public OperationFuture<List<BaseServerResponse>> archive(GroupFilter groupFilter) {
        return serverService().powerOperationResponse(
            client.archive(serverService().ids(groupFilter))
        );
    }

    /**
     * Archive groups of servers
     *
     * @param groupRefs groups references list
     * @return OperationFuture wrapper for BaseServerResponse list
     */
    public OperationFuture<List<BaseServerResponse>> archive(Group... groupRefs) {
        return serverService().powerOperationResponse(
            client.archive(serverService().ids(groupRefs))
        );
    }

    /**
     * Create snapshot of servers groups
     *
     * @param expirationDays expiration days (must be between 1 and 10)
     * @param groupFilter    search servers criteria by group filter
     * @return OperationFuture wrapper for BaseServerResponse list
     */
    public OperationFuture<List<BaseServerResponse>> createSnapshot(Integer expirationDays, GroupFilter groupFilter) {
        return
            serverService().powerOperationResponse(
                client.createSnapshot(
                    new CreateSnapshotRequest()
                        .snapshotExpirationDays(expirationDays)
                        .serverIds(serverService().ids(groupFilter))
                )
            );
    }

    /**
     * Create snapshot of servers groups
     *
     * @param expirationDays expiration days (must be between 1 and 10)
     * @param groupRef       groups references list
     * @return OperationFuture wrapper for BaseServerResponse list
     */
    public OperationFuture<List<BaseServerResponse>> createSnapshot(Integer expirationDays, Group... groupRef) {
        return
            serverService().powerOperationResponse(
                client.createSnapshot(
                    new CreateSnapshotRequest()
                        .snapshotExpirationDays(expirationDays)
                        .serverIds(serverService().ids(groupRef))
                )
            );
    }

}
