package com.centurylink.cloud.sdk.servers.services.groups.stats;

import com.centurylink.cloud.sdk.servers.AbstractServersSdkTest;
import com.centurylink.cloud.sdk.servers.client.domain.group.SamplingEntry;
import com.centurylink.cloud.sdk.servers.client.domain.group.ServerMonitoringStatistics;
import com.centurylink.cloud.sdk.servers.client.domain.server.metadata.ServerMetadata;
import com.centurylink.cloud.sdk.servers.services.GroupService;
import com.centurylink.cloud.sdk.servers.services.ServerService;
import com.centurylink.cloud.sdk.servers.services.domain.group.MonitoringType;
import com.centurylink.cloud.sdk.servers.services.domain.group.ServerMonitoringConfig;
import com.centurylink.cloud.sdk.servers.services.domain.group.filters.GroupFilter;
import com.centurylink.cloud.sdk.servers.services.domain.group.refs.Group;
import com.centurylink.cloud.sdk.servers.services.domain.group.refs.GroupByIdRef;
import com.centurylink.cloud.sdk.servers.services.domain.server.refs.Server;
import com.centurylink.cloud.sdk.tests.fixtures.SingleServerFixture;
import com.google.inject.Inject;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.List;

import static com.centurylink.cloud.sdk.tests.TestGroups.INTEGRATION;
import static com.centurylink.cloud.sdk.tests.TestGroups.LONG_RUNNING;

@Test(groups = {INTEGRATION, LONG_RUNNING})
public class GetServerMonitoringStatsTest extends AbstractServersSdkTest {

    @Inject
    ServerService serverService;

    @Inject
    GroupService groupService;

    GroupByIdRef group;

    Server server;
    ServerMetadata serverMetadata;

    @BeforeMethod
    public void setUp() {
        server = SingleServerFixture.server();
        serverMetadata = serverService.findByRef(server);
        group = Group.refById(serverMetadata.getGroupId());
    }

    @Test
    public void testServerStats() {
        Duration sampleInterval = Duration.ofHours(1);
        List<ServerMonitoringStatistics> result = groupService.getMonitoringStats(
            new GroupFilter().groups(group),
            new ServerMonitoringConfig()
                .from(OffsetDateTime.now().minusDays(5))
                .to(OffsetDateTime.now())
                .type(MonitoringType.HOURLY)
                .interval(sampleInterval)
        );

        result.stream()
            .forEach(metadata -> {
                ServerMetadata srvMetadata = serverService.findByRef(Server.refById(metadata.getName()));

                for (int i = 0; i < metadata.getStats().size()-1; i++) {
                    SamplingEntry curStats = metadata.getStats().get(i);
                    SamplingEntry nextStats = metadata.getStats().get(i+1);
                    assertEquals(Duration.between(curStats.getTimestamp(), nextStats.getTimestamp()), sampleInterval);

                    //if server prepared to delete - it hasn't details
                    if (srvMetadata.getDetails() != null) {
                        assertEquals(srvMetadata.getDetails().getCpu(), curStats.getCpu());
                        assertEquals(srvMetadata.getDetails().getMemoryMB(), curStats.getMemoryMB());
                        assertEquals(srvMetadata.getDetails().getDiskCount().intValue(), curStats.getDiskUsage().size());
                        // if server has swap partition, user can't use it ->
                        //assertEquals(srvMetadata.getDetails().getPartitions().size() - 1, curStats.getGuestDiskUsage().size());

                        assertTrue(
                            srvMetadata.getDetails().getStorageGB() * 1024 > curStats.getDiskUsageTotalCapacityMB().intValue()
                        );
                    }
                }
            });
    }

}
