package com.centurylink.cloud.sdk.servers.services.domain.statistics.monitoring;

import com.centurylink.cloud.sdk.common.management.client.domain.datacenters.DataCenterMetadata;
import com.centurylink.cloud.sdk.common.management.services.DataCenterService;
import com.centurylink.cloud.sdk.common.management.services.domain.datacenters.filters.DataCenterFilter;
import com.centurylink.cloud.sdk.common.management.services.domain.datacenters.refs.DataCenter;
import com.centurylink.cloud.sdk.servers.client.domain.group.DiskUsageMetadata;
import com.centurylink.cloud.sdk.servers.client.domain.group.GroupMetadata;
import com.centurylink.cloud.sdk.servers.client.domain.group.GuestUsageMetadata;
import com.centurylink.cloud.sdk.servers.client.domain.group.SamplingEntry;
import com.centurylink.cloud.sdk.servers.client.domain.group.ServerMonitoringStatistics;
import com.centurylink.cloud.sdk.servers.client.domain.server.metadata.ServerMetadata;
import com.centurylink.cloud.sdk.servers.services.GroupService;
import com.centurylink.cloud.sdk.servers.services.ServerService;
import com.centurylink.cloud.sdk.servers.services.domain.account.AccountMetadata;
import com.centurylink.cloud.sdk.servers.services.domain.group.ServerMonitoringFilter;
import com.centurylink.cloud.sdk.servers.services.domain.group.filters.GroupFilter;
import com.centurylink.cloud.sdk.servers.services.domain.group.refs.Group;
import com.centurylink.cloud.sdk.servers.services.domain.server.filters.ServerFilter;
import com.centurylink.cloud.sdk.servers.services.domain.server.refs.Server;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static java.util.stream.Collectors.summingDouble;
import static java.util.stream.Collectors.summingInt;
import static java.util.stream.Collectors.toList;

/**
 * @author aliaksandr.krasitski
 */
public class MonitoringStatsEngine {

    private final ServerService serverService;
    private final GroupService groupService;
    private final DataCenterService dataCenterService;

    private GroupFilter groupFilter;
    private ServerFilter serverFilter;
    private DataCenterFilter dataCenterFilter;

    private ServerMonitoringFilter timeFilter;

    private boolean aggregateSubItems = false;
    private List<String> serverIdsRestrictions;

    private String accountAlias;

    private Predicate<ServerMonitoringStatistics> filterServers() {
        return (stat -> serverIdsRestrictions.isEmpty() || serverIdsRestrictions.contains(stat.getName()));
    }

    public MonitoringStatsEngine(ServerService serverService,
                             GroupService groupService,
                             DataCenterService dataCenterService,
                             String accountAlias) {
        this.serverService = serverService;
        this.groupService = groupService;
        this.dataCenterService = dataCenterService;
        this.accountAlias = accountAlias;
        initFilters();
    }

    private void initFilters() {
        this.serverFilter = new ServerFilter();
        this.groupFilter = new GroupFilter();
        this.dataCenterFilter = new DataCenterFilter();
        this.timeFilter = new ServerMonitoringFilter();
    }

    public MonitoringStatsEngine aggregateSubItems() {
        aggregateSubItems = true;
        return this;
    }

    public MonitoringStatsEngine forServers(ServerFilter serverFilter) {
        this.serverFilter = serverFilter;
        return this;
    }

    public MonitoringStatsEngine forGroups(GroupFilter groupFilter) {
        this.groupFilter = groupFilter;
        return this;
    }

    public MonitoringStatsEngine forDataCenters(DataCenterFilter dataCenterFilter) {
        this.dataCenterFilter = dataCenterFilter;
        return this;
    }

    public MonitoringStatsEngine forTime(ServerMonitoringFilter monitoringConfig) {
        this.timeFilter = monitoringConfig;
        return this;
    }

    public List<MonitoringStatsEntry<DataCenterMetadata>> groupByDataCenter() {
        List<ServerMonitoringStatistics> stats = getServerMonitoringStats();
        HashMap<String, List<MonitoringEntry>> plainGroupMap = new HashMap<>();

        stats.stream()
            .filter(filterServers())
            .forEach(stat ->
                fillPlainEntry(plainGroupMap,
                    serverService.findByRef(Server.refById(stat.getName())).getLocationId(),
                    stat.getStats()
                )
            );

        return group(convertToMonitoringEntries(plainGroupMap, DataCenterMetadata.class));
    }

    public List<MonitoringStatsEntry<AccountMetadata>> summarize() {
        List<ServerMonitoringStatistics> stats = getServerMonitoringStats();
        HashMap<String, List<MonitoringEntry>> plainGroupMap = new HashMap<>();

        List<MonitoringEntry> monitoringEntries = stats.stream()
            .filter(filterServers())
            .map(stat -> convertFromEntry(stat.getStats()))
            .flatMap(List::stream)
            .collect(toList());

        plainGroupMap.put(accountAlias, monitoringEntries);

        return group(convertToMonitoringEntries(plainGroupMap, AccountMetadata.class));
    }

    public List<MonitoringStatsEntry<ServerMetadata>> groupByServer() {
        List<ServerMonitoringStatistics> stats = getServerMonitoringStats();

        return stats.stream()
            .filter(filterServers())
            .map(stat -> new MonitoringStatsEntry<>(
                    serverService.findByRef(Server.refById(stat.getName())),
                    convertFromEntry(stat.getStats()))
            )
            .collect(toList());
    }

    public List<MonitoringStatsEntry<GroupMetadata>> groupByServerGroup() {
        List<ServerMonitoringStatistics> stats = getServerMonitoringStats();
        HashMap<String, List<MonitoringEntry>> plainGroupMap = new HashMap<>();
        stats.stream()
            .filter(filterServers())
            .forEach(stat ->
                        fillPlainEntry(plainGroupMap,
                            serverService.findByRef(Server.refById(stat.getName())).getGroupId(),
                            stat.getStats()
                        )
            );

        return group(convertToMonitoringEntries(plainGroupMap, GroupMetadata.class));
    }

    private void fillPlainEntry(HashMap<String, List<MonitoringEntry>> plainGrouping,
                                String key,
                                List<SamplingEntry> stats) {
        if (!plainGrouping.containsKey(key)) {
            plainGrouping.put(key, new ArrayList<>());
        }
        plainGrouping.get(key).addAll(convertFromEntry(stats));
    }

    private <T> MonitoringEntry aggregateMonitoringEntry(MonitoringStatsEntry<T> entry, OffsetDateTime time) {
        Supplier<Stream<MonitoringEntry>> streamSupplier =
            () -> entry.getStatistics().stream()
                .filter(e -> e.getTimestamp().equals(time));

        long count = streamSupplier.get().count();

        return new MonitoringEntry()
            .timestamp(time)
            .cpu(
                streamSupplier.get().collect(summingInt(MonitoringEntry::getCpu)))
            .cpuPercent(
                streamSupplier.get().collect(summingDouble(MonitoringEntry::getCpuPercent)) / count)
            .diskUsageTotalCapacityMB(
                streamSupplier.get().collect(summingInt(MonitoringEntry::getDiskUsageTotalCapacityMB)))
            .memoryMB(
                streamSupplier.get().collect(summingInt(MonitoringEntry::getMemoryMB)))
            .memoryPercent(
                streamSupplier.get().collect(summingDouble(MonitoringEntry::getMemoryPercent)) / count)
            .networkReceivedKBps(
                streamSupplier.get().collect(summingDouble(MonitoringEntry::getNetworkReceivedKBps)))
            .networkTransmittedKBps(
                streamSupplier.get().collect(summingDouble(MonitoringEntry::getNetworkTransmittedKBps)))
            .diskUsage(aggregateDiskUsage(
                streamSupplier.get().map(MonitoringEntry::getDiskUsage).flatMap(List::stream).collect(toList())))
            .guestDiskUsage(aggregateGuestUsage(
                streamSupplier.get().map(MonitoringEntry::getGuestDiskUsage).flatMap(List::stream).collect(toList())));
    }

    private List<DiskUsage> aggregateDiskUsage(List<DiskUsage> flatList) {
        HashMap<String, List<DiskUsage>> map = new HashMap<>();
        flatList.stream()
            .forEach(item -> {
                if (map.get(item.getId()) == null) {
                    map.put(item.getId(), new ArrayList<>());
                }
                map.get(item.getId()).add(new DiskUsage().id(item.getId()).capacityMB(item.getCapacityMB()));
            });

        return map.keySet().stream()
            .map(key ->
                new DiskUsage()
                    .id(key)
                    .capacityMB(
                        map.get(key).stream()
                            .collect(summingInt(DiskUsage::getCapacityMB)))
            )
            .collect(toList());
    }

    private List<GuestUsage> aggregateGuestUsage(List<GuestUsage> flatList) {
        HashMap<String, List<GuestUsage>> map = new HashMap<>();
        flatList.stream()
            .forEach(item -> {
                if (map.get(item.getPath()) == null) {
                    map.put(item.getPath(), new ArrayList<>());
                }
                map.get(item.getPath()).add(
                    new GuestUsage()
                        .path(item.getPath())
                        .capacityMB(item.getCapacityMB())
                        .consumedMB(item.getConsumedMB())
                );
            });

        return map.keySet().stream()
            .map(key ->
                    new GuestUsage()
                        .path(key)
                        .capacityMB(
                            map.get(key).stream()
                                .collect(summingInt(GuestUsage::getCapacityMB)))
                        .consumedMB(
                            map.get(key).stream()
                                .collect(summingInt(GuestUsage::getConsumedMB)))
            )
            .collect(toList());
    }

    private <T> List<MonitoringStatsEntry<T>> convertToMonitoringEntries(
        HashMap<String, List<MonitoringEntry>> plainGroupMap,
        Class entityClass) {
        return plainGroupMap.keySet().stream()
            .map(key -> {
                T resultEntity;
                if (entityClass.equals(DataCenterMetadata.class)) {
                    resultEntity = (T) dataCenterService.findByRef(DataCenter.refById(key));
                } else if (entityClass.equals(ServerMetadata.class)) {
                    resultEntity = (T) serverService.findByRef(Server.refById(key));
                } else if (entityClass.equals(GroupMetadata.class)) {
                    resultEntity = (T) groupService.findByRef(Group.refById(key));
                } else if (entityClass.equals(AccountMetadata.class)) {
                    resultEntity = (T) new AccountMetadata(key);
                } else {
                    throw new IllegalArgumentException("Unknown result class");
                }
                return new MonitoringStatsEntry<>(
                    resultEntity,
                    plainGroupMap.get(key));
                }
            )
            .collect(toList());
    }

    private List<ServerMonitoringStatistics> getServerMonitoringStats() {
        return groupService.getMonitoringStats(collectFilters(), timeFilter);
    }

    private <T> List<MonitoringStatsEntry<T>> group(List<MonitoringStatsEntry<T>> plainEntries) {
        List<MonitoringStatsEntry<T>> result = aggregate(plainEntries);

        if (aggregateSubItems && plainEntries.size() > 0 && plainEntries.get(0).getEntity() instanceof GroupMetadata) {
            List<MonitoringStatsEntry<T>> aggregatedResult = new ArrayList<>();
            result.stream()
                .forEach(entry -> {
                    List<String> groupIds = collectSubGroups(((GroupMetadata) entry.getEntity())).stream()
                        .map(GroupMetadata::getId)
                        .collect(toList());

                    List<MonitoringEntry> subgroupsStats = result.stream()
                        .filter(en -> groupIds.contains(((GroupMetadata) en.getEntity()).getId()))
                        .map(MonitoringStatsEntry::getStatistics)
                        .flatMap(List::stream)
                        .collect(toList());

                    List<MonitoringStatsEntry<T>> monitoringStatsEntries =
                        Arrays.asList(new MonitoringStatsEntry<>(entry.getEntity(), subgroupsStats));
                    aggregatedResult.add(aggregate(monitoringStatsEntries).get(0));
                });

            return aggregatedResult;
        }

        return result;
    }
    
    private <T> List<MonitoringStatsEntry<T>> aggregate(List<MonitoringStatsEntry<T>> plainEntries) {
        return plainEntries.stream()
            .map(entry -> {
                List<MonitoringEntry> resultEntries = entry.getStatistics().stream()
                    //select distinct timestamps
                    .map(MonitoringEntry::getTimestamp)
                    .distinct()
                    .collect(toList())

                    .stream()
                    .map(time -> aggregateMonitoringEntry(entry, time))
                    .collect(toList());

                return new MonitoringStatsEntry<>(entry.getEntity(), resultEntries);
            })
            .collect(toList());
    }

    private List<GroupMetadata> collectSubGroups(GroupMetadata rootGroup) {
        return rootGroup.getAllGroups();
    }

    private GroupFilter collectFilters() {
        serverIdsRestrictions = serverFilter.getServerIds();
        List<String> groupIds = serverIdsRestrictions.stream()
            .map(serverId ->
                serverService.findByRef(Server.refById(serverId)).getGroupId()
            )
            .collect(toList());

        return groupFilter.dataCentersWhere(dataCenterFilter).id(groupIds);
    }

    private List<MonitoringEntry> convertFromEntry(List<SamplingEntry> entries) {
        return entries.stream()
            .map(entry -> new MonitoringEntry()
                .timestamp(entry.getTimestamp())
                .cpu(entry.getCpu())
                .cpuPercent(entry.getCpuPercent())
                .diskUsageTotalCapacityMB(entry.getDiskUsageTotalCapacityMB())
                .memoryMB(entry.getMemoryMB())
                .memoryPercent(entry.getMemoryPercent())
                .networkReceivedKBps(entry.getNetworkReceivedKbps())
                .networkTransmittedKBps(entry.getNetworkTransmittedKbps())
                .diskUsage(convertFromDiskUsage(entry.getDiskUsage()))
                .guestDiskUsage(convertFromGuestUsage(entry.getGuestDiskUsage()))
            )
            .collect(toList());
    }

    private List<DiskUsage> convertFromDiskUsage(List<DiskUsageMetadata> list) {
        return list.stream()
            .map(metadata -> new DiskUsage()
                .id(metadata.getId())
                .capacityMB(metadata.getCapacityMB()))
            .collect(toList());
    }

    private List<GuestUsage> convertFromGuestUsage(List<GuestUsageMetadata> list) {
        return list.stream()
            .map(metadata -> new GuestUsage()
                .path(metadata.getPath())
                .capacityMB(metadata.getCapacityMB())
                .consumedMB(metadata.getConsumedMB()))
            .collect(toList());
    }

}
