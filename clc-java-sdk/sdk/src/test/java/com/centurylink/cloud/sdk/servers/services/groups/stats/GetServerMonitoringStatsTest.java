package com.centurylink.cloud.sdk.servers.services.groups.stats;

import com.centurylink.cloud.sdk.common.management.services.domain.datacenters.refs.DataCenter;
import com.centurylink.cloud.sdk.servers.AbstractServersSdkTest;
import com.centurylink.cloud.sdk.servers.client.domain.group.GroupMetadata;
import com.centurylink.cloud.sdk.servers.client.domain.group.SamplingEntry;
import com.centurylink.cloud.sdk.servers.client.domain.group.ServerMonitoringStatistics;
import com.centurylink.cloud.sdk.servers.client.domain.server.metadata.ServerMetadata;
import com.centurylink.cloud.sdk.servers.services.GroupService;
import com.centurylink.cloud.sdk.servers.services.domain.group.ServerMonitoringFilter;
import com.centurylink.cloud.sdk.servers.services.domain.group.filters.GroupFilter;
import com.centurylink.cloud.sdk.servers.services.domain.group.refs.Group;
import com.centurylink.cloud.sdk.servers.services.domain.group.refs.GroupByIdRef;
import com.google.inject.Inject;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.List;

import static com.centurylink.cloud.sdk.tests.TestGroups.INTEGRATION;
import static com.centurylink.cloud.sdk.tests.TestGroups.LONG_RUNNING;
import static java.util.stream.Collectors.toList;

@Test(groups = {INTEGRATION, LONG_RUNNING})
public class GetServerMonitoringStatsTest extends AbstractServersSdkTest {

    @Inject
    GroupService groupService;

    GroupByIdRef group;

    @BeforeMethod
    public void setUp() {
        List<GroupMetadata> metadata = groupService.find(new GroupFilter()
            .dataCenters(DataCenter.DE_FRANKFURT)
            .nameContains(Group.DEFAULT_GROUP));

        assertTrue(metadata.size() > 0);
        group = metadata.get(0).asRefById();
    }

    @Test
    public void testServerStats() {
        Duration sampleInterval = Duration.ofHours(1);
        GroupFilter groupFilter = new GroupFilter().groups(group);
        List<ServerMonitoringStatistics> result = groupService.getMonitoringStats(
            groupFilter,
            new ServerMonitoringFilter()
                .from(OffsetDateTime.now().minusDays(2))
        );

        List<GroupMetadata> m = groupService.find(groupFilter);

        assertNotNull(result);

        List<String> allServers = m.stream()
            .map(GroupMetadata::getServers)
            .flatMap(List::stream)
            .map(ServerMetadata::getId)
            .collect(toList());

        result.stream()
            .map(ServerMonitoringStatistics::getName)
            .forEach(serverId ->
                assertTrue(allServers.contains(serverId), "check that server exists")
            );

        result.stream()
                .forEach(metadata -> {

                    for (int i = 0; i < metadata.getStats().size() - 1; i++) {
                        SamplingEntry curStats = metadata.getStats().get(i);
                        SamplingEntry nextStats = metadata.getStats().get(i + 1);
                        assertEquals(Duration.between(curStats.getTimestamp(), nextStats.getTimestamp()), sampleInterval);

                        checkStats(curStats);
                    }
                });
    }

    private void checkStats(SamplingEntry stats) {
        boolean isEmpty = stats.getCpu().equals(0);

        checkStat(stats.getCpuPercent(), isEmpty);
        checkStat(stats.getMemoryPercent(), isEmpty);
        checkStat(stats.getNetworkReceivedKbps(), isEmpty);
        checkStat(stats.getNetworkTransmittedKbps(), isEmpty);
        checkStat(stats.getDiskUsageTotalCapacityMB(), isEmpty);
        checkStat(stats.getMemoryMB(), isEmpty);
        checkStat(stats.getDiskUsage().size(), isEmpty);
        checkStat(stats.getGuestDiskUsage().size(), isEmpty);
    }

    private void checkStat(Double value, boolean isEmpty) {
        Double zero = 0d;
        if (isEmpty) {
            assertTrue(value == zero);
        }
    }

    private void checkStat(Integer value, boolean isEmpty) {
        Integer zero = 0;
        assertTrue(isEmpty ? value == zero : value >=zero);
    }
}
