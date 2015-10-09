/*
 * (c) 2015 CenturyLink. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.centurylink.cloud.sdk.server.services.dsl;

import com.centurylink.cloud.sdk.base.services.client.QueueClient;
import com.centurylink.cloud.sdk.base.services.client.domain.datacenters.DataCenterMetadata;
import com.centurylink.cloud.sdk.base.services.dsl.DataCenterService;
import com.centurylink.cloud.sdk.base.services.dsl.domain.datacenters.refs.DataCenter;
import com.centurylink.cloud.sdk.base.services.dsl.domain.queue.OperationFuture;
import com.centurylink.cloud.sdk.base.services.dsl.domain.queue.job.future.JobFuture;
import com.centurylink.cloud.sdk.base.services.dsl.domain.queue.job.future.NoWaitingJobFuture;
import com.centurylink.cloud.sdk.base.services.dsl.domain.queue.job.future.ParallelJobsFuture;
import com.centurylink.cloud.sdk.core.client.domain.Link;
import com.centurylink.cloud.sdk.core.services.QueryService;
import com.centurylink.cloud.sdk.server.services.client.ServerClient;
import com.centurylink.cloud.sdk.server.services.client.domain.group.GroupMetadata;
import com.centurylink.cloud.sdk.server.services.client.domain.group.ServerMonitoringStatistics;
import com.centurylink.cloud.sdk.server.services.dsl.domain.InfrastructureConfig;
import com.centurylink.cloud.sdk.server.services.dsl.domain.group.*;
import com.centurylink.cloud.sdk.server.services.dsl.domain.group.filters.GroupFilter;
import com.centurylink.cloud.sdk.server.services.dsl.domain.group.refs.Group;
import com.centurylink.cloud.sdk.server.services.dsl.domain.group.refs.GroupByIdRef;
import com.centurylink.cloud.sdk.server.services.dsl.domain.server.CompositeServerConfig;
import com.centurylink.cloud.sdk.server.services.dsl.domain.server.CreateServerConfig;
import com.centurylink.cloud.sdk.server.services.dsl.domain.server.filters.ServerFilter;
import com.centurylink.cloud.sdk.server.services.dsl.domain.server.refs.Server;

import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static com.centurylink.cloud.sdk.core.function.Predicates.alwaysTrue;
import static com.centurylink.cloud.sdk.core.function.Predicates.combine;
import static com.centurylink.cloud.sdk.core.function.Predicates.in;
import static com.centurylink.cloud.sdk.core.function.Predicates.isAlwaysTruePredicate;
import static com.centurylink.cloud.sdk.core.function.Predicates.notNull;
import static com.centurylink.cloud.sdk.core.services.filter.Filters.nullable;
import static com.centurylink.cloud.sdk.core.preconditions.Preconditions.checkNotNull;
import static java.util.stream.Collectors.toList;

/**
 * Service provide operations for query and manipulate groups of servers
 *
 * @author ilya.drabenia
 */
public class GroupService implements QueryService<Group, GroupFilter, GroupMetadata> {

    private final ServerClient client;
    private final GroupConverter converter;
    private final DataCenterService dataCenterService;
    private final Supplier<ServerService> serverServiceProvider;
    private final QueueClient queueClient;

    public interface ServerServiceSupplier extends Supplier<ServerService> {}

    public GroupService(ServerClient client, GroupConverter converter,
                        DataCenterService dataCenterService, QueueClient queueClient,
                        ServerServiceSupplier serverServiceProvider) {
        this.client = client;
        this.converter = converter;
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
                converter.createGroupRequest(groupConfig, idByRef(groupConfig.getParentGroup()),
                    groupConfig.getCustomFields() == null || groupConfig.getCustomFields().size() == 0 ?
                        null : client.getCustomFields()
                ));

        return new OperationFuture<>(
            Group.refById(group.getId()),
            new NoWaitingJobFuture()
        );
    }

    /**
     * Create group hierarchy based on {@link InfrastructureConfig} instance.<br>
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
     * @return GroupMetadata
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
            } else if (cfg instanceof CreateServerConfig) {
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
                    groupConfig.getParentGroup() != null ? idByRef(groupConfig.getParentGroup()) : null,
                    groupConfig.getCustomFields() == null || groupConfig.getCustomFields().size() == 0 ?
                        null : client.getCustomFields())
            );
    }

    /**
     * Extract {@link Group} from {@link GroupFilter}
     * @param groupFilter group search criteria
     * @return array of groups
     */
    private Group[] getRefsFromFilter(GroupFilter groupFilter) {
        checkNotNull(groupFilter, "Group filter must be not null");

        List<Group> groupList = findLazy(groupFilter)
            .map(metadata -> Group.refById(metadata.getId()))
            .collect(toList());

        return groupList.toArray(new Group[groupList.size()]);
    }

    /**
     * Delete provided group
     *
     * @param group group reference
     * @return OperationFuture wrapper for group
     */
    public OperationFuture<Group> delete(Group group) {
        Link response = client.deleteGroup(idByRef(group));

        return new OperationFuture<>(
                group,
                response.getId(),
                queueClient
        );
    }

    /**
     * Delete set of groups
     *
     * @param groups groups array
     * @return OperationFuture wrapper for list of groups
     */
    public OperationFuture<List<Group>> delete(Group... groups) {
        List<Group> groupList = Arrays.asList(groups);

        List<JobFuture> jobs = groupList.stream()
            .map(group -> delete(group).jobFuture())
            .collect(toList());

        return new OperationFuture<>(
            groupList,
            new ParallelJobsFuture(jobs)
        );
    }

    /**
     * Delete all groups for group criteria
     *
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
     * @return OperationFuture wrapper for Server list
     */
    public OperationFuture<List<Server>> powerOn(GroupFilter groupFilter) {
        return serverService().powerOn(
                getServerSearchCriteria(groupFilter)
        );
    }

    /**
     * Power on groups of servers
     *
     * @param groups groups references list
     * @return OperationFuture wrapper for Server list
     */
    public OperationFuture<List<Server>> powerOn(Group... groups) {
        return serverService().powerOn(
            getServerSearchCriteria(groups)
        );
    }

    /**
     * Power off groups of servers
     *
     * @param groupFilter search servers criteria by group filter
     * @return OperationFuture wrapper for Server list
     */
    public OperationFuture<List<Server>> powerOff(GroupFilter groupFilter) {
        return serverService().powerOff(
                getServerSearchCriteria(groupFilter)
        );
    }

    /**
     * Power off groups of servers
     *
     * @param groups groups references list
     * @return OperationFuture wrapper for Server list
     */
    public OperationFuture<List<Server>> powerOff(Group... groups) {
        return serverService().powerOff(
            getServerSearchCriteria(groups)
        );
    }

    /**
     * Start servers groups maintenance
     *
     * @param groupFilter search servers criteria by group filter
     * @return OperationFuture wrapper for Server list
     */
    public OperationFuture<List<Server>> startMaintenance(GroupFilter groupFilter) {
        return serverService().startMaintenance(
            getServerSearchCriteria(groupFilter)
        );
    }

    /**
     * Start servers groups maintenance
     *
     * @param groups groups references list
     * @return OperationFuture wrapper for Server list
     */
    public OperationFuture<List<Server>> startMaintenance(Group... groups) {
        return serverService().startMaintenance(
            getServerSearchCriteria(groups)
        );
    }

    /**
     * Stop servers groups maintenance
     *
     * @param groupFilter search servers criteria by group filter
     * @return OperationFuture wrapper for Server list
     */
    public OperationFuture<List<Server>> stopMaintenance(GroupFilter groupFilter) {
        return serverService().stopMaintenance(
            getServerSearchCriteria(groupFilter)
        );
    }

    /**
     * Stop servers groups maintenance
     *
     * @param groups groups references list
     * @return OperationFuture wrapper for Server list
     */
    public OperationFuture<List<Server>> stopMaintenance(Group... groups) {
        return serverService().stopMaintenance(
            getServerSearchCriteria(groups)
        );
    }

    /**
     * Pause groups of servers
     *
     * @param groupFilter search servers criteria by group filter
     * @return OperationFuture wrapper for Server list
     */
    public OperationFuture<List<Server>> pause(GroupFilter groupFilter) {
        return serverService().pause(
            getServerSearchCriteria(groupFilter)
        );
    }

    /**
     * Pause groups of servers
     *
     * @param groups groups references list
     * @return OperationFuture wrapper for Server list
     */
    public OperationFuture<List<Server>> pause(Group... groups) {
        return serverService().pause(
            getServerSearchCriteria(groups)
        );
    }

    /**
     * Reboot groups of servers
     *
     * @param groupFilter search servers criteria by group filter
     * @return OperationFuture wrapper for Server list
     */
    public OperationFuture<List<Server>> reboot(GroupFilter groupFilter) {
        return serverService().reboot(
            getServerSearchCriteria(groupFilter)
        );
    }

    /**
     * Reboot groups of servers
     *
     * @param groups groups references list
     * @return OperationFuture wrapper for Server list
     */
    public OperationFuture<List<Server>> reboot(Group... groups) {
        return serverService().reboot(
            getServerSearchCriteria(groups)
        );
    }

    /**
     * Reset groups of servers
     *
     * @param groupFilter search servers criteria by group filter
     * @return OperationFuture wrapper for Server list
     */
    public OperationFuture<List<Server>> reset(GroupFilter groupFilter) {
        return serverService().reset(
            getServerSearchCriteria(groupFilter)
        );
    }

    /**
     * Reset groups of servers
     *
     * @param groups groups references list
     * @return OperationFuture wrapper for Server list
     */
    public OperationFuture<List<Server>> reset(Group... groups) {
        return serverService().reset(
            getServerSearchCriteria(groups)
        );
    }

    /**
     * Shut down groups of servers
     *
     * @param groupFilter search servers criteria by group filter
     * @return OperationFuture wrapper for Server list
     */
    public OperationFuture<List<Server>> shutDown(GroupFilter groupFilter) {
        return serverService().shutDown(
            getServerSearchCriteria(groupFilter)
        );
    }

    /**
     * Shut down groups of servers
     *
     * @param groups groups references list
     * @return OperationFuture wrapper for Server list
     */
    public OperationFuture<List<Server>> shutDown(Group... groups) {
        return serverService().shutDown(
            getServerSearchCriteria(groups)
        );
    }

    /**
     * Archive groups of servers
     *
     * @param groupFilter search servers criteria by group filter
     * @return OperationFuture wrapper for Server list
     */
    public OperationFuture<List<Server>> archive(GroupFilter groupFilter) {
        return serverService().archive(
            getServerSearchCriteria(groupFilter)
        );
    }

    /**
     * Archive groups of servers
     *
     * @param groups groups references list
     * @return OperationFuture wrapper for Server list
     */
    public OperationFuture<List<Server>> archive(Group... groups) {
        return serverService().archive(
            getServerSearchCriteria(groups)
        );
    }

    /**
     * Create snapshot of servers groups
     *
     * @param expirationDays expiration days (must be between 1 and 10)
     * @param groupFilter    search servers criteria by group filter
     * @return OperationFuture wrapper for Server list
     */
    public OperationFuture<List<Server>> createSnapshot(
            Integer expirationDays,
            GroupFilter groupFilter
    ) {
        return serverService().createSnapshot(
            expirationDays,
            getServerSearchCriteria(groupFilter)
        );
    }

    /**
     * Create snapshot of servers groups
     *
     * @param expirationDays expiration days (must be between 1 and 10)
     * @param groups groups references list
     * @return OperationFuture wrapper for Server list
     */
    public OperationFuture<List<Server>> createSnapshot(Integer expirationDays, Group... groups) {
        return serverService().createSnapshot(
            expirationDays,
            getServerSearchCriteria(groups)
        );
    }

    /**
     * Get billing stats by single group
     *
     * @param group Group
     * @return Group billing stats
     */
    public BillingStats getBillingStats(Group group) {
        return converter.convertBillingStats(
            client.getGroupBillingStats(idByRef(group))
        );
    }

    /**
     * Get billing stats by Groups
     *
     * @param groups groups array
     * @return List of Group billing stats
     */
    public List<BillingStats> getBillingStats(Group... groups) {
        List<BillingStats> result = new ArrayList<>();
        List<Group> groupList = Arrays.asList(groups);

        groupList.forEach(
            group -> result.add(
                converter.convertBillingStats(
                    client.getGroupBillingStats(idByRef(group))
                )
            )
        );

        return result;
    }

    /**
     * Get billing stats by GroupFilter
     * @param groupFilter group filter
     * @return List of Group billing stats
     */
    public List<BillingStats> getBillingStats(GroupFilter groupFilter) {
        return findLazy(groupFilter)
            .map(
                groupMetadata -> converter.convertBillingStats(
                    client.getGroupBillingStats(groupMetadata.getId())
                )
            )
            .collect(toList());
    }

    /**
     * Retrieve the resource usage of servers within a group hierarchy statistics.
     * @param group  Group reference
     * @param config configuration for statistics entries
     * @return the statistics list
     */
    public List<ServerMonitoringStatistics> getMonitoringStats(Group group, ServerMonitoringFilter config) {
        checkNotNull(config, "Config must be not a null");
        return client.getMonitoringStatistics(
            idByRef(group),
            converter.createMonitoringStatisticRequest(config));
    }

    /**
     * Retrieve the resource usage of servers within a group hierarchy statistics.
     * @param groups the list of Group references
     * @param config configuration for statistics entries
     * @return the statistics list
     */
    public List<ServerMonitoringStatistics> getMonitoringStats(List<Group> groups, ServerMonitoringFilter config) {
        List<ServerMonitoringStatistics> rawStats = groups.stream()
            .map(group -> getMonitoringStats(group, config))
            .flatMap(List::stream)
            .collect(toList());

        Map<String, ServerMonitoringStatistics> distinctMap = new HashMap<>();

        rawStats.stream()
            .forEach(stat -> {
                if (!distinctMap.containsKey(stat.getName())) {
                    distinctMap.put(stat.getName(), stat);
                }
            });

        return new ArrayList<>(distinctMap.values());
    }

    /**
     * Retrieve the resource usage of servers within a group hierarchy statistics.
     * @param groupFilter group filter
     * @param config      configuration for statistics entries
     * @return the statistics list
     */
    public List<ServerMonitoringStatistics> getMonitoringStats(GroupFilter groupFilter, ServerMonitoringFilter config) {
        return getMonitoringStats(Arrays.asList(getRefsFromFilter(groupFilter)), config);
    }

    /**
     * Retrieve the resource usage of servers within a group hierarchy statistics.
     * @param groups the list of Group references
     * @param config configuration for statistics entries
     * @return the statistics map
     */
    public Map<Group, List<ServerMonitoringStatistics>> getMonitoringStatsAsMap(List<Group> groups, ServerMonitoringFilter config) {
        Map<Group, List<ServerMonitoringStatistics>> result = new HashMap<>(groups.size());
        groups.stream()
            .forEach(group -> result.put(group, getMonitoringStats(group, config)));

        return result;
    }

    /**
     * Retrieve the resource usage of servers within a group hierarchy statistics.
     * @param groupFilter group filter
     * @param config      configuration for statistics entries
     * @return the statistics map
     */
    public Map<Group, List<ServerMonitoringStatistics>> getMonitoringStatsAsMap(GroupFilter groupFilter, ServerMonitoringFilter config) {
        return getMonitoringStatsAsMap(Arrays.asList(getRefsFromFilter(groupFilter)), config);
    }

    private ServerFilter getServerSearchCriteria(GroupFilter groupFilter) {
        return new ServerFilter().groupsWhere(groupFilter);
    }

    private ServerFilter getServerSearchCriteria(Group... groups) {
        return new ServerFilter().groups(groups);
    }
}
