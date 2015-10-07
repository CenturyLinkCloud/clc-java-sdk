package com.centurylink.cloud.sdk.server.services.dsl.statistics;

import com.centurylink.cloud.sdk.base.services.client.domain.datacenters.DataCenterMetadata;
import com.centurylink.cloud.sdk.base.services.dsl.DataCenterService;
import com.centurylink.cloud.sdk.base.services.dsl.domain.datacenters.filters.DataCenterFilter;
import com.centurylink.cloud.sdk.base.services.dsl.domain.datacenters.refs.DataCenter;
import com.centurylink.cloud.sdk.core.injector.Inject;
import com.centurylink.cloud.sdk.server.services.AbstractServersSdkTest;
import com.centurylink.cloud.sdk.server.services.client.domain.group.DiskUsageMetadata;
import com.centurylink.cloud.sdk.server.services.client.domain.group.GroupMetadata;
import com.centurylink.cloud.sdk.server.services.client.domain.group.GuestUsageMetadata;
import com.centurylink.cloud.sdk.server.services.client.domain.group.SamplingEntry;
import com.centurylink.cloud.sdk.server.services.client.domain.group.ServerMonitoringStatistics;
import com.centurylink.cloud.sdk.server.services.client.domain.server.metadata.ServerMetadata;
import com.centurylink.cloud.sdk.server.services.dsl.GroupService;
import com.centurylink.cloud.sdk.server.services.dsl.StatisticsService;
import com.centurylink.cloud.sdk.server.services.dsl.domain.account.AccountMetadata;
import com.centurylink.cloud.sdk.server.services.dsl.domain.group.ServerMonitoringFilter;
import com.centurylink.cloud.sdk.server.services.dsl.domain.group.filters.GroupFilter;
import com.centurylink.cloud.sdk.server.services.dsl.domain.group.refs.Group;
import com.centurylink.cloud.sdk.server.services.dsl.domain.server.filters.ServerFilter;
import com.centurylink.cloud.sdk.server.services.dsl.domain.statistics.monitoring.GuestUsage;
import com.centurylink.cloud.sdk.server.services.dsl.domain.statistics.monitoring.MonitoringEntry;
import com.centurylink.cloud.sdk.server.services.dsl.domain.statistics.monitoring.MonitoringStatsEntry;
import com.centurylink.cloud.sdk.tests.recorded.WireMockFileSource;
import com.centurylink.cloud.sdk.tests.recorded.WireMockMixin;
import org.testng.annotations.Test;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static com.centurylink.cloud.sdk.tests.TestGroups.RECORDED;
import static java.util.stream.Collectors.summingDouble;
import static java.util.stream.Collectors.summingInt;
import static java.util.stream.Collectors.toList;

/**
 * @author aliaksandr.krasitski
 */
@Test(groups = {RECORDED})
@SuppressWarnings("unchecked")
@WireMockFileSource("monitoring")
public class MonitoringStatisticsTest extends AbstractServersSdkTest implements WireMockMixin{

    @Inject
    StatisticsService statisticsService;

    @Inject
    GroupService groupService;

    @Inject
    DataCenterService dataCenterService;

    ServerMonitoringFilter timeFilter = new ServerMonitoringFilter().last(Duration.ofDays(2));

    private DataCenter[] dataCenters = {DataCenter.DE_FRANKFURT};
    private String groupName = Group.DEFAULT_GROUP;

    private static final double DELTA = 0.5;

    List<String> serverIds = Arrays.asList("de1altdweb318", "de1altdweb424", "de1altdweb426", "de1altdweb443",
        "de1altdmd-srv324", "de1altdtcrt523", "de1altdtcrt738", "de1altdtcrt940");

    @Test
    public void testGroupByDataCenter() {
        DataCenterFilter filter = new DataCenterFilter().dataCenters(dataCenters);
        List<MonitoringStatsEntry> resultList = statisticsService.monitoringStats()
            .forDataCenters(filter)
            .forTime(timeFilter)
            .groupByDataCenter();

        assertEquals(resultList.size(), dataCenters.length, "check count of grouping rows");

        MonitoringStatsEntry<DataCenterMetadata> result = resultList.get(0);

        assertEquals(DataCenter.DE_FRANKFURT.getId(), result.getEntity().getId(), "check group by field");

        List<ServerMonitoringStatistics> stats = groupService.getMonitoringStats(
            new GroupFilter().dataCentersWhere(filter),
            timeFilter
        );

        List<SamplingEntry> allSamplingEntries = getSamplingEntries(stats);

        checkSamplingCount(allSamplingEntries, result);

        result.getStatistics().stream()
            .forEach(entry ->
                checkStatisticsEntry(entry, allSamplingEntries)
            );
    }

    @Test
    public void testGroupByGroup() {
        GroupFilter groupFilter = new GroupFilter()
            .dataCenters(dataCenters)
            .nameContains(groupName);

        List<MonitoringStatsEntry> resultList = statisticsService.monitoringStats()
            .forGroups(groupFilter)
            .forTime(timeFilter)
            .aggregateSubItems()
            .groupByServerGroup();

        assertEquals(resultList.size(), 1, "check count of grouping rows");

        List<ServerMonitoringStatistics> stats = groupService.getMonitoringStats(groupFilter, timeFilter);

        List<SamplingEntry> allSamplingEntries = getSamplingEntries(stats);

        MonitoringStatsEntry<GroupMetadata> result = resultList.get(0);

        checkSamplingCount(allSamplingEntries, result);

        result.getStatistics().stream()
            .forEach(entry ->
                checkStatisticsEntry((MonitoringEntry) entry, allSamplingEntries)
            );
    }

    @Test
    public void testGroupByServer() {
        GroupFilter groupFilter = new GroupFilter()
            .dataCenters(dataCenters)
            .nameContains(groupName);

        List<MonitoringStatsEntry> resultList = statisticsService.monitoringStats()
            .forServers(new ServerFilter().id(serverIds))
            .forTime(timeFilter)
            .groupByServer();

        List<ServerMonitoringStatistics> stats = groupService.getMonitoringStats(groupFilter, timeFilter);

        assertEquals(
            resultList.size(),
            stats.stream()
                .map(ServerMonitoringStatistics::getName)
                .distinct()
                .collect(toList())
                .size(),
            "check count of grouping rows"
        );

        resultList.forEach(entry -> {
            String serverId = ((ServerMetadata) entry.getEntity()).getId();
            assertTrue(serverIds.contains(serverId));


            List<SamplingEntry> allSamplingEntries = stats.stream()
                .filter(stat -> stat.getName().equals(serverId))
                .map(ServerMonitoringStatistics::getStats)
                .flatMap(List::stream)
                .collect(toList());

            checkSamplingCount(allSamplingEntries, entry);

            entry.getStatistics().stream()
                .forEach(st ->
                    checkStatisticsEntry((MonitoringEntry) st, allSamplingEntries)
                );
        });
    }

    @Test
    public void testSummarize() {
        GroupFilter groupFilter = new GroupFilter()
            .dataCenters(dataCenters)
            .nameContains(groupName);

        List<MonitoringStatsEntry> resultList = statisticsService.monitoringStats()
            .forGroups(groupFilter)
            .forTime(timeFilter)
            .aggregateSubItems()
            .summarize();

        assertEquals(
            resultList.size(),
            1,
            "check count of grouping rows"
        );

        MonitoringStatsEntry<AccountMetadata> result = resultList.get(0);

        Map<Group, List<ServerMonitoringStatistics>> stats = groupService
            .getMonitoringStatsAsMap(groupFilter, timeFilter);

        result.getStatistics().forEach(stat -> {
            List<SamplingEntry> allSamplingEntries = stats.values().stream()
                .flatMap(List::stream)
                .map(ServerMonitoringStatistics::getStats)
                .flatMap(List::stream)
                .filter(entry -> entry.getTimestamp().equals(stat.getTimestamp()))
                .collect(toList());

            checkStatisticsEntry(stat, allSamplingEntries);
        });
    }

    @Test
    public void testAggregateSubItems() {
        //find group, that can't contain any servers
        String rootGroupId = dataCenterService.findByRef(dataCenters[0]).getGroup().getId();
        GroupFilter groupFilter = new GroupFilter().id(rootGroupId);

        List<MonitoringStatsEntry> resultList = statisticsService.monitoringStats()
            .forGroups(groupFilter)
            .forTime(timeFilter)
            .groupByServerGroup();

        assertEquals(resultList.size(), 0, "check result rows count");

        resultList = statisticsService.monitoringStats()
            .forGroups(groupFilter)
            .forTime(timeFilter)
            .aggregateSubItems()
            .groupByServerGroup();

        assertEquals(resultList.size(), 1, "check result rows count");
    }

    private List<SamplingEntry> getSamplingEntries(List<ServerMonitoringStatistics> stats) {
        return stats.stream()
            .map(ServerMonitoringStatistics::getStats)
            .flatMap(List::stream)
            .collect(toList());
    }

    private void checkSamplingCount(List<SamplingEntry> allSamplingEntries, MonitoringStatsEntry entry) {
        long sampleCount = allSamplingEntries.stream()
            .map(SamplingEntry::getTimestamp)
            .distinct()
            .count();

        assertTrue(sampleCount >= entry.getStatistics().size(), "check time points count");
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

        assertDoubleValues(
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

        assertDoubleValues(
            entry.getMemoryPercent(),
            samplingEntries.stream().collect(summingDouble(SamplingEntry::getMemoryPercent)) / samplingEntries.size(),
            "check memory percent average"
        );

        assertDoubleValues(
            entry.getNetworkReceivedKBps(),
            samplingEntries.stream().collect(summingDouble(SamplingEntry::getNetworkReceivedKbps)),
            "check sum network received traffic"
        );

        assertDoubleValues(
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

        entry.getGuestDiskUsage().forEach(
            diskUsage -> {
                assertEquals(
                    diskUsage.getCapacityMB(),
                    fetchGuestUsageMetadataStream(samplingEntries, diskUsage)
                        .collect(summingInt(GuestUsageMetadata::getCapacityMB)),
                    "check sum guest disk capacity usage"
                );

                assertEquals(
                    diskUsage.getConsumedMB(),
                    fetchGuestUsageMetadataStream(samplingEntries, diskUsage)
                        .collect(summingInt(GuestUsageMetadata::getConsumedMB)),
                    "check sum guest disk consumed usage"
                );
            }
        );
    }

    private Stream<GuestUsageMetadata> fetchGuestUsageMetadataStream(
        List<SamplingEntry> samplingEntries,
        GuestUsage guestUsage
    ) {
        return
            samplingEntries.stream()
                .map(SamplingEntry::getGuestDiskUsage)
                .flatMap(List::stream)
                .filter(metadata -> metadata.getPath().equals(guestUsage.getPath()));
    }

    private void assertDoubleValues(double expected, double actual, String assertMessage) {
        assertEquals(expected, actual, DELTA, assertMessage);
    }
}
