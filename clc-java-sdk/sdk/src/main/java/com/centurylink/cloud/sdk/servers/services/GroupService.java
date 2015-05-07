package com.centurylink.cloud.sdk.servers.services;

import com.centurylink.cloud.sdk.common.management.client.DataCentersClient;
import com.centurylink.cloud.sdk.common.management.client.QueueClient;
import com.centurylink.cloud.sdk.common.management.client.domain.datacenters.DataCenterMetadata;
import com.centurylink.cloud.sdk.common.management.services.DataCenterService;
import com.centurylink.cloud.sdk.common.management.services.domain.datacenters.refs.DataCenter;
import com.centurylink.cloud.sdk.common.management.services.domain.queue.future.OperationFuture;
import com.centurylink.cloud.sdk.common.management.services.domain.queue.future.job.JobFuture;
import com.centurylink.cloud.sdk.common.management.services.domain.queue.future.job.NoWaitingJobFuture;
import com.centurylink.cloud.sdk.common.management.services.domain.queue.future.job.ParallelJobsFuture;
import com.centurylink.cloud.sdk.core.client.ClcClientException;
import com.centurylink.cloud.sdk.core.client.domain.Link;
import com.centurylink.cloud.sdk.core.services.QueryService;
import com.centurylink.cloud.sdk.servers.client.ServerClient;
import com.centurylink.cloud.sdk.servers.client.domain.group.GroupMetadata;
import com.centurylink.cloud.sdk.servers.client.domain.server.BaseServerResponse;
import com.centurylink.cloud.sdk.servers.client.domain.server.CreateSnapshotRequest;
import com.centurylink.cloud.sdk.servers.services.domain.group.GroupConfig;
import com.centurylink.cloud.sdk.servers.services.domain.group.GroupConverter;
import com.centurylink.cloud.sdk.servers.services.domain.group.GroupHierarchyConfig;
import com.centurylink.cloud.sdk.servers.services.domain.group.InfrastructureConfig;
import com.centurylink.cloud.sdk.servers.services.domain.group.filters.GroupFilter;
import com.centurylink.cloud.sdk.servers.services.domain.group.refs.Group;
import com.centurylink.cloud.sdk.servers.services.domain.group.refs.GroupByIdRef;
import com.centurylink.cloud.sdk.servers.services.domain.server.CompositeServerConfig;
import com.centurylink.cloud.sdk.servers.services.domain.server.CreateServerConfig;
import com.google.inject.Inject;
import com.google.inject.Provider;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static com.centurylink.cloud.sdk.core.function.Predicates.*;
import static com.centurylink.cloud.sdk.core.services.filter.Filters.nullable;
import static com.google.common.base.Preconditions.checkNotNull;
import static java.util.stream.Collectors.toList;

/**
 * Service provide operations for query and manipulate groups of servers
 *
 * @author ilya.drabenia
 */
public class GroupService implements QueryService<Group, GroupFilter, GroupMetadata> {

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

    /**
     * {@inheritDoc}
     */
    @Override
    public Stream<GroupMetadata> findLazy(GroupFilter groupCriteria) {
        checkNotNull(groupCriteria, "Criteria must be not null");

        return
            groupCriteria.applyFindLazy(criteria -> {
                if (isAlwaysTruePredicate(criteria.getPredicate()) &&
                    isAlwaysTruePredicate(criteria.getDataCenterFilter().getPredicate()) &&
                    criteria.getIds().size() > 0) {
                    return
                        criteria.getIds().stream()
                            .map(nullable(curId -> client.getGroup(curId, true)))
                            .filter(notNull());
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

    /**
     * Create group
     * @param groupConfig group config
     * @return OperationFuture wrapper for Group
     */
    public OperationFuture<Group> create(GroupConfig groupConfig) {
        checkNotNull(groupConfig, "GroupConfig must be not null");
        checkNotNull(groupConfig.getName(), "Name of GroupConfig must be not null");
        checkNotNull(groupConfig.getParentGroup(), "ParentGroup of GroupConfig must be not null");

        GroupMetadata group = client.createGroup(
            converter.createGroupRequest(groupConfig, idByRef(groupConfig.getParentGroup())
        ));

        return new OperationFuture<>(
            Group.refById(group.getId()),
            new NoWaitingJobFuture()
        );
    }

    /**
     * Create group hierarchy based on {@link InfrastructureConfig} instance.<br/>
     * Existing groups are not override!
     *
     * @param configs group hierarchy configs
     * @return OperationFuture wrapper for list of root groups in each data center
     */
    public OperationFuture<List<Group>> defineInfrastructure(InfrastructureConfig... configs) {
        checkNotNull(configs, "array of InfrastructureConfig must be not null");
        List<OperationFuture<Group>> operationFutures = Arrays.asList(configs).stream()
            .map(cfg ->
                    cfg.getDataCenters().stream()
                        .map(dc ->
                                cfg.getSubitems().stream()
                                    .map(subCfg -> defineGroupHierarchy(dc, subCfg))
                                    .collect(toList())
                        )
                        .collect(toList())
            )
            .flatMap(list -> list.stream())
            .flatMap(list -> list.stream())
            .collect(toList());

        List<Group> groups = operationFutures.stream()
            .map(OperationFuture::getResult)
            .collect(toList());

        List<JobFuture> futures = operationFutures.stream()
            .map(OperationFuture::jobFuture)
            .collect(toList());

        return new OperationFuture<>(groups, new ParallelJobsFuture(futures));
    }

    /**
     * Create group hierarchy based on {@link GroupHierarchyConfig} instance
     * Existing groups are not override!
     *
     * @param dataCenter datacenter in which will be created group hierarchy
     * @param config group hierarchy config
     * @return OperationFuture wrapper for parent Group
     */
    public OperationFuture<Group> defineGroupHierarchy(DataCenter dataCenter, GroupHierarchyConfig config) {
        checkNotNull(dataCenter, "DataCenter must be not null");
        checkNotNull(config, "GroupHierarchyConfig must be not null");

        String parentGroupId = dataCenterService.findByRef(dataCenter).getGroup().getId();

        GroupMetadata rootGroup = findGroup(config, parentGroupId);

        if (rootGroup == null) {
            rootGroup = findByRef(create(converter.createGroupConfig(config, parentGroupId)).getResult());
        }

        createSubgroups(config, rootGroup.getId());

        return createServers(config, rootGroup.getId());
    }

    /**
     * Find group based on config instance and parent group identifier
     * @param config        group hierarchy config
     * @param parentGroupId parent group id
     * @return
     */
    private GroupMetadata findGroup(GroupHierarchyConfig config, String parentGroupId) {
        GroupMetadata parentGroup = findByRef(Group.refById(parentGroupId));

        return parentGroup.getGroups().stream()
            .filter(group -> group.getName().equals(config.getName()))
            .findFirst()
            .orElse(null);
    }

    /**
     * Create sub groups in group with {@code groupId}
     * @param config  group hierarchy config
     * @param groupId group id
     */
    private void createSubgroups(GroupHierarchyConfig config, String groupId) {
        config.getSubitems().forEach(cfg -> {
            if (cfg instanceof GroupHierarchyConfig) {
                GroupMetadata curGroup = findGroup((GroupHierarchyConfig) cfg, groupId);
                String subGroupId;
                if (curGroup != null) {
                    subGroupId = curGroup.getId();
                } else {
                    subGroupId = create(converter.createGroupConfig((GroupHierarchyConfig) cfg, groupId))
                        .getResult()
                        .as(GroupByIdRef.class)
                        .getId();
                }
                createSubgroups((GroupHierarchyConfig) cfg, subGroupId);
            } else if (cfg instanceof CreateServerConfig){
                ((CreateServerConfig) cfg).group(Group.refById(groupId));
            } else {
                ((CompositeServerConfig) cfg).getServer().group(Group.refById(groupId));
            }
        });
    }

    /**
     * Create servers based on {@link CreateServerConfig}
     * @param config      group hierarchy config
     * @param rootGroupId root group id
     * @return OperationFuture wrapper for root group
     */
    private OperationFuture<Group> createServers(GroupHierarchyConfig config, String rootGroupId) {
        List<CreateServerConfig> configs = config.getServerConfigs();

        List<JobFuture> futures = configs.stream()
            .map(serverConfig -> serverService().create(serverConfig).jobFuture())
            .collect(toList());

        return new OperationFuture<>(Group.refById(rootGroupId), new ParallelJobsFuture(futures));
    }

    /**
     * Update group
     * @param groupRef    group reference
     * @param groupConfig group config
     * @return OperationFuture wrapper for Group
     */
    public OperationFuture<Group> modify(Group groupRef, GroupConfig groupConfig) {
        checkNotNull(groupConfig, "GroupConfig must be not null");
        checkNotNull(groupRef, "Group reference must be not null");
        updateGroup(idByRef(groupRef), groupConfig);

        return new OperationFuture<>(
            groupRef,
            new NoWaitingJobFuture()
        );
    }

    /**
     * Update provided list of groups
     * @param groups      group list
     * @param groupConfig group config
     * @return OperationFuture wrapper for list of Group
     */
    public OperationFuture<List<Group>> modify(List<Group> groups, GroupConfig groupConfig) {
        checkNotNull(groupConfig, "GroupConfig must be not null");

        groups.stream()
            .forEach(group -> modify(group, groupConfig));

        return new OperationFuture<>(
            groups,
            new NoWaitingJobFuture()
        );
    }

    /**
     * Update provided list of groups
     * @param groupFilter search criteria
     * @param groupConfig group config
     * @return OperationFuture wrapper for list of Group
     */
    public OperationFuture<List<Group>> modify(GroupFilter groupFilter, GroupConfig groupConfig) {
        List<Group> groups = Arrays.asList(getRefsFromFilter(groupFilter));

        return modify(groups, groupConfig);
    }

    /**
     * Update group
     * @param groupId group id to update
     * @param groupConfig group config
     * @return <tt>true</tt> if update was successful, <br/>
     * <tt>false</tt> otherwise
     */
    private boolean updateGroup(String groupId, GroupConfig groupConfig) {
        return client
            .updateGroup(
                groupId,
                converter.createUpdateGroupRequest(
                    groupConfig,
                    groupConfig.getParentGroup() != null ? idByRef(groupConfig.getParentGroup()) : null)
            );
    }

    /**
     * Extract {@link Group} from {@link GroupFilter}
     * @param groupFilter group search criteria
     * @return array of groups
     */
    private Group[] getRefsFromFilter(GroupFilter groupFilter) {
        checkNotNull(groupFilter, "Group filter must be not null");
        List<Group> groupList = find(groupFilter).stream()
            .map(metadata -> Group.refById(metadata.getId()))
            .collect(toList());

        return groupList.toArray(new Group[groupList.size()]);
    }

    /**
     * Delete provided group
     * @param groupRef group reference
     * @return OperationFuture wrapper for Link
     */
    public OperationFuture<Link> delete(Group groupRef) {
        return deleteGroup(groupRef);
    }

    private OperationFuture<Link> deleteGroup(Group groupRef) {
        Link response = client.deleteGroup(idByRef(groupRef));

        return new OperationFuture<>(
            response,
            response.getId(),
            queueClient
        );
    }

    /**
     * Delete set of groups
     * @param groups groups array
     * @return OperationFuture wrapper for list of groups
     */
    public OperationFuture<List<Group>> delete(Group... groups) {
        List<Group> groupList = Arrays.asList(groups);

        List<JobFuture> jobs = groupList.stream()
            .map(group -> deleteGroup(group).jobFuture())
            .collect(toList());

        return new OperationFuture<>(
            groupList,
            new ParallelJobsFuture(jobs)
        );
    }

    /**
     * Delete all groups for group criteria
     * @param filter search criteria
     * @return OperationFuture wrapper for list of groups
     */
    public OperationFuture<List<Group>> delete(GroupFilter filter) {
        return delete(getRefsFromFilter(filter));
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
