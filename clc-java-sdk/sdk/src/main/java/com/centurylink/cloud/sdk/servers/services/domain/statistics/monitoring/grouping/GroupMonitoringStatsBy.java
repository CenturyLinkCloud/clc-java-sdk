package com.centurylink.cloud.sdk.servers.services.domain.statistics.monitoring.grouping;

import com.centurylink.cloud.sdk.servers.client.domain.group.DiskUsageMetadata;
import com.centurylink.cloud.sdk.servers.client.domain.group.GuestUsageMetadata;
import com.centurylink.cloud.sdk.servers.client.domain.group.SamplingEntry;
import com.centurylink.cloud.sdk.servers.client.domain.group.ServerMonitoringStatistics;
import com.centurylink.cloud.sdk.servers.services.domain.group.refs.Group;
import com.centurylink.cloud.sdk.servers.services.domain.statistics.monitoring.DiskUsage;
import com.centurylink.cloud.sdk.servers.services.domain.statistics.monitoring.GuestUsage;
import com.centurylink.cloud.sdk.servers.services.domain.statistics.monitoring.MonitoringEntry;
import com.centurylink.cloud.sdk.servers.services.domain.statistics.monitoring.MonitoringStatsEntry;
import com.centurylink.cloud.sdk.servers.services.domain.statistics.monitoring.filter.MonitoringStatsFilter;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.util.stream.Collectors.summingDouble;
import static java.util.stream.Collectors.summingInt;
import static java.util.stream.Collectors.toList;

public abstract class GroupMonitoringStatsBy {

    protected MonitoringStatsFilter statsFilter;

    public abstract List<MonitoringStatsEntry> group(Map<Group, List<ServerMonitoringStatistics>> monitoringStats);

    public GroupMonitoringStatsBy(MonitoringStatsFilter statsFilter) {
        checkNotNull(statsFilter, "Filter must be not a null");
        this.statsFilter = statsFilter;
    }

    protected List<MonitoringEntry> convertEntry(List<SamplingEntry> entries) {
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
                .diskUsage(convertDiskUsage(entry.getDiskUsage()))
                .guestDiskUsage(convertGuestUsage(entry.getGuestDiskUsage()))
            )
            .collect(toList());
    }

    protected List<DiskUsage> convertDiskUsage(List<DiskUsageMetadata> list) {
        return list.stream()
            .map(metadata -> new DiskUsage()
                .id(metadata.getId())
                .capacityMB(metadata.getCapacityMB()))
            .collect(toList());
    }

    protected List<GuestUsage> convertGuestUsage(List<GuestUsageMetadata> list) {
        return list.stream()
            .map(metadata -> new GuestUsage()
                .path(metadata.getPath())
                .capacityMB(metadata.getCapacityMB())
                .consumedMB(metadata.getConsumedMB()))
            .collect(toList());
    }

    protected void collectStats(Map<String, List<MonitoringEntry>> plainGrouping,
                                String key,
                                List<SamplingEntry> stats,
                                boolean distinct) {
        if (!plainGrouping.containsKey(key)) {
            plainGrouping.put(key, convertEntry(stats));
            return;
        }
        if (!distinct) {
            plainGrouping.get(key).addAll(convertEntry(stats));
        }
    }

    protected List<ServerMonitoringStatistics> selectServersStatsDistinct(
        Map<Group, List<ServerMonitoringStatistics>> stats) {

        Map<String, ServerMonitoringStatistics> distinctMap = new HashMap<>();
        stats.values().stream()
            .flatMap(List::stream)
            .forEach(stat -> {
                if (!distinctMap.containsKey(stat.getName())) {
                    distinctMap.put(stat.getName(), stat);
                }
            });

        return new ArrayList<>(distinctMap.values());
    }

    @SuppressWarnings("unchecked")
    protected <T> MonitoringStatsEntry createMonitoringStatsEntry(T metadata, List<MonitoringEntry> entries) {
        return new MonitoringStatsEntry<>(metadata, entries);
    }

    @SuppressWarnings("unchecked")
    protected List<MonitoringStatsEntry> aggregate(List<MonitoringStatsEntry> plainEntries) {
        return plainEntries.stream()
            .map(entry -> {
                List<MonitoringEntry> resultEntries = ((List<OffsetDateTime>) entry.getStatistics().stream()
                    //select distinct timestamps
                    .map(e -> ((MonitoringEntry) e).getTimestamp())
                    .distinct()
                    .collect(toList()))

                    .stream()
                    .map(time -> aggregateMonitoringEntry(entry, time))
                    .collect(toList());

                return new MonitoringStatsEntry(entry.getEntity(), resultEntries);
            })
            .collect(toList());
    }

    @SuppressWarnings("unchecked")
    private MonitoringEntry aggregateMonitoringEntry(MonitoringStatsEntry entry, OffsetDateTime time) {
        Supplier<Stream<MonitoringEntry>> streamSupplier =
            () -> entry.getStatistics().stream()
                .filter(e -> ((MonitoringEntry) e).getTimestamp().equals(time));

        long entriesCount = streamSupplier.get().count();

        return new MonitoringEntry()
            .timestamp(time)
            .cpu(
                streamSupplier.get().collect(summingInt(MonitoringEntry::getCpu)))
            .cpuPercent(
                streamSupplier.get().collect(summingDouble(MonitoringEntry::getCpuPercent)) / entriesCount)
            .diskUsageTotalCapacityMB(
                streamSupplier.get().collect(summingInt(MonitoringEntry::getDiskUsageTotalCapacityMB)))
            .memoryMB(
                streamSupplier.get().collect(summingInt(MonitoringEntry::getMemoryMB)))
            .memoryPercent(
                streamSupplier.get().collect(summingDouble(MonitoringEntry::getMemoryPercent)) / entriesCount)
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
}
