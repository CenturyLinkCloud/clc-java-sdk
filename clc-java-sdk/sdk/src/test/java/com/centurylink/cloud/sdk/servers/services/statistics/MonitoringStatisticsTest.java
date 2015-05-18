package com.centurylink.cloud.sdk.servers.services.statistics;

import com.centurylink.cloud.sdk.common.management.client.domain.datacenters.DataCenterMetadata;
import com.centurylink.cloud.sdk.common.management.services.domain.datacenters.refs.DataCenter;
import com.centurylink.cloud.sdk.servers.AbstractServersSdkTest;
import com.centurylink.cloud.sdk.servers.client.domain.group.DiskUsageMetadata;
import com.centurylink.cloud.sdk.servers.client.domain.group.GroupMetadata;
import com.centurylink.cloud.sdk.servers.client.domain.group.GuestUsageMetadata;
import com.centurylink.cloud.sdk.servers.client.domain.group.SamplingEntry;
import com.centurylink.cloud.sdk.servers.client.domain.group.ServerMonitoringStatistics;
import com.centurylink.cloud.sdk.servers.services.GroupService;
import com.centurylink.cloud.sdk.servers.services.StatisticsService;
import com.centurylink.cloud.sdk.servers.services.domain.group.ServerMonitoringFilter;
import com.centurylink.cloud.sdk.servers.services.domain.group.filters.GroupFilter;
import com.centurylink.cloud.sdk.servers.services.domain.group.refs.Group;
import com.centurylink.cloud.sdk.servers.services.domain.statistics.monitoring.MonitoringEntry;
import com.centurylink.cloud.sdk.servers.services.domain.statistics.monitoring.MonitoringStatsEntry;
import com.google.inject.Inject;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.time.Duration;
import java.util.List;

import static com.centurylink.cloud.sdk.tests.TestGroups.INTEGRATION;
import static com.centurylink.cloud.sdk.tests.TestGroups.LONG_RUNNING;
import static java.util.stream.Collectors.summingDouble;
import static java.util.stream.Collectors.summingInt;
import static java.util.stream.Collectors.toList;

/**
 * @author aliaksandr.krasitski
 */
@Test(groups = {INTEGRATION, LONG_RUNNING})
public class MonitoringStatisticsTest extends AbstractServersSdkTest {

    @Inject
    StatisticsService statisticsService;

    @Inject
    GroupService groupService;

    ServerMonitoringFilter timeFilter;
    GroupFilter groupFilter;

    private DataCenter[] dataCenters = {DataCenter.DE_FRANKFURT};
    private String groupName = Group.DEFAULT_GROUP;

    @BeforeClass
    private void setup() {
        timeFilter = new ServerMonitoringFilter()
            .last(Duration.ofDays(2));
        groupFilter = new GroupFilter().dataCenters(dataCenters).nameContains(groupName);
    }

    @Test
    public void testGroupByDataCenter() {
        List<MonitoringStatsEntry<DataCenterMetadata>> resultList = statisticsService.monitoringStats()
            .forGroups(groupFilter)
            .forTime(timeFilter)
            .groupByDataCenter();

        assertEquals(resultList.size(), dataCenters.length, "check count of grouping rows");

        MonitoringStatsEntry<DataCenterMetadata> result = resultList.get(0);

        assertEquals(DataCenter.DE_FRANKFURT.getId(),result.getEntity().getId(), "check group by field");

        List<ServerMonitoringStatistics> stats = groupService.getMonitoringStats(groupFilter, timeFilter);

        List<SamplingEntry> allSamplingEntries = stats.stream()
            .map(ServerMonitoringStatistics::getStats)
            .flatMap(List::stream)
            .collect(toList());


        long sampleCount = allSamplingEntries.stream()
            .map(stat -> stat.getTimestamp())
            .distinct()
            .count();

        assertEquals(sampleCount, result.getStatistics().size(), "check time points count");

        result.getStatistics().stream()
            .forEach(entry ->
                checkStatisticsEntry(entry, allSamplingEntries)
            );
    }

    @Test
    public void testGroupByGroup() {
        List<MonitoringStatsEntry<GroupMetadata>> resultList = statisticsService.monitoringStats()
            .forGroups(groupFilter)
            .forTime(timeFilter)
            .groupByServerGroup();

        assertEquals(resultList.size(), 1, "check count of grouping rows");

        MonitoringStatsEntry<GroupMetadata> result = resultList.get(0);

        assertEquals(groupName,result.getEntity().getName(), "check group by field");

        List<ServerMonitoringStatistics> stats = groupService.getMonitoringStats(groupFilter, timeFilter);

        List<SamplingEntry> allSamplingEntries = stats.stream()
            .map(ServerMonitoringStatistics::getStats)
            .flatMap(List::stream)
            .collect(toList());

        long sampleCount = allSamplingEntries.stream()
            .map(stat -> stat.getTimestamp())
            .distinct()
            .count();

        assertEquals(sampleCount, result.getStatistics().size(), "check time points count");

        result.getStatistics().stream()
        .forEach(entry ->
                checkStatisticsEntry(entry, allSamplingEntries)
        );
    }

    private void checkStatisticsEntry(MonitoringEntry entry, List<SamplingEntry> allSamplingEntries) {
        List<SamplingEntry> samplingEntries = allSamplingEntries.stream()
            .filter(e -> e.getTimestamp().equals(entry.getTimestamp()))
            .collect(toList());

        assertEquals(
            entry.getCpu(),
            samplingEntries.stream().collect(summingInt(SamplingEntry::getCpu)),
            "check cpu count"
        );

        assertEquals(
            entry.getCpuPercent(),
            samplingEntries.stream().collect(summingDouble(SamplingEntry::getCpuPercent)) / samplingEntries.size(),
            "check cpu percent average"
        );

        assertEquals(
            entry.getMemoryMB(),
            samplingEntries.stream().collect(summingInt(SamplingEntry::getMemoryMB)),
            "check sum memory mb"
        );

        assertEquals(
            entry.getDiskUsageTotalCapacityMB(),
            samplingEntries.stream().collect(summingInt(SamplingEntry::getDiskUsageTotalCapacityMB)),
            "check sum disk usage total capacity"
        );

        assertEquals(
            entry.getMemoryPercent(),
            samplingEntries.stream().collect(summingDouble(SamplingEntry::getMemoryPercent)) / samplingEntries.size(),
            "check memory percent average"
        );

        assertEquals(
            entry.getNetworkReceivedKBps(),
            samplingEntries.stream().collect(summingDouble(SamplingEntry::getNetworkReceivedKbps)),
            "check sum network received traffic"
        );

        assertEquals(
            entry.getNetworkTransmittedKBps(),
            samplingEntries.stream().collect(summingDouble(SamplingEntry::getNetworkTransmittedKbps)),
            "check sum network transmitted traffic"
        );

        entry.getDiskUsage().stream()
            .forEach(diskUsage ->
                assertEquals(
                    diskUsage.getCapacityMB(),
                    samplingEntries.stream()
                        .map(SamplingEntry::getDiskUsage)
                        .flatMap(List::stream)
                        .filter(metadata -> metadata.getId().equals(diskUsage.getId()))
                        .collect(summingInt(DiskUsageMetadata::getCapacityMB)),
                    "check sum disk usage"
                )
            );

        entry.getGuestDiskUsage().stream()
            .forEach(diskUsage -> {
                    assertEquals(
                        diskUsage.getCapacityMB(),
                        samplingEntries.stream()
                            .map(SamplingEntry::getGuestDiskUsage)
                            .flatMap(List::stream)
                            .filter(metadata -> metadata.getPath().equals(diskUsage.getPath()))
                            .collect(summingInt(GuestUsageMetadata::getCapacityMB)),
                        "check sum guest disk capacity usage"
                    );

                    assertEquals(
                        diskUsage.getConsumedMB(),
                        samplingEntries.stream()
                            .map(SamplingEntry::getGuestDiskUsage)
                            .flatMap(List::stream)
                            .filter(metadata -> metadata.getPath().equals(diskUsage.getPath()))
                            .collect(summingInt(GuestUsageMetadata::getConsumedMB)),
                        "check sum guest disk consumed usage"
                    );
                }
            );
    }
}
