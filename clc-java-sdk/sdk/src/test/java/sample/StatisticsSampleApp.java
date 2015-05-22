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

package sample;

import com.centurylink.cloud.sdk.ClcSdk;
import com.centurylink.cloud.sdk.common.management.services.domain.datacenters.filters.DataCenterFilter;
import com.centurylink.cloud.sdk.common.management.services.domain.datacenters.refs.DataCenter;
import com.centurylink.cloud.sdk.core.auth.services.domain.credentials.DefaultCredentialsProvider;
import com.centurylink.cloud.sdk.servers.services.GroupService;
import com.centurylink.cloud.sdk.servers.services.ServerService;
import com.centurylink.cloud.sdk.servers.services.StatisticsService;
import com.centurylink.cloud.sdk.servers.services.domain.group.MonitoringType;
import com.centurylink.cloud.sdk.servers.services.domain.group.ServerMonitoringFilter;
import com.centurylink.cloud.sdk.servers.services.domain.group.filters.GroupFilter;
import com.centurylink.cloud.sdk.servers.services.domain.server.CreateServerConfig;
import com.centurylink.cloud.sdk.servers.services.domain.server.Machine;
import com.centurylink.cloud.sdk.servers.services.domain.server.filters.ServerFilter;
import com.centurylink.cloud.sdk.servers.services.domain.statistics.billing.BillingStatsEntry;
import com.centurylink.cloud.sdk.servers.services.domain.statistics.billing.Statistics;
import com.centurylink.cloud.sdk.servers.services.domain.statistics.monitoring.MonitoringStatsEntry;
import com.centurylink.cloud.sdk.servers.services.domain.template.refs.Template;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.List;

import static com.centurylink.cloud.sdk.common.management.services.domain.datacenters.refs.DataCenter.DE_FRANKFURT;
import static com.centurylink.cloud.sdk.common.management.services.domain.datacenters.refs.DataCenter.US_EAST_STERLING;
import static com.centurylink.cloud.sdk.servers.services.domain.InfrastructureConfig.dataCenter;
import static com.centurylink.cloud.sdk.servers.services.domain.group.GroupHierarchyConfig.group;
import static com.centurylink.cloud.sdk.servers.services.domain.server.ServerStatus.ACTIVE;
import static com.centurylink.cloud.sdk.servers.services.domain.server.ServerType.STANDARD;
import static com.centurylink.cloud.sdk.servers.services.domain.template.filters.os.CpuArchitecture.x86_64;
import static com.centurylink.cloud.sdk.servers.services.domain.template.filters.os.OsType.CENTOS;
import static com.centurylink.cloud.sdk.tests.TestGroups.SAMPLES;

public class StatisticsSampleApp extends Assert {

    private ServerService serverService;
    private GroupService groupService;
    private StatisticsService statisticsService;

    public StatisticsSampleApp() {
        ClcSdk sdk = new ClcSdk(
            new DefaultCredentialsProvider("centurylink-clc-sdk-uat.properties")
        );

        serverService = sdk.serverService();
        groupService = sdk.groupService();
        statisticsService = sdk.statisticsService();
    }

    @BeforeClass(groups = {SAMPLES})
    public void init() {
        clearAll();

        groupService
            .defineInfrastructure(
                dataCenter(DE_FRANKFURT).subitems(
                    group(
                        "uat-s1",
                        "uat-s1 group description"
                    )
                    .subitems(
                        centOsServer("sr-de1", DE_FRANKFURT),
                        centOsServer("sr-de2", DE_FRANKFURT)
                    )
                ),

                dataCenter(US_EAST_STERLING).subitems(
                    group(
                        "uat-s2",
                        "uat-s2 group description"
                    )
                    .subitems(
                        centOsServer("sr-va1", US_EAST_STERLING)
                    )
                )
            )
            .waitUntilComplete();
    }

    @AfterClass(groups = {SAMPLES})
    public void deleteServers() {
        clearAll();
    }

    public static CreateServerConfig centOsServer(String name, DataCenter dataCenter) {
        return new CreateServerConfig()
            .name(name)
            .description(name)
            .type(STANDARD)
            .machine(new Machine()
                .cpuCount(1)
                .ram(2)
            )
            .template(Template.refByOs()
                .dataCenter(dataCenter)
                .type(CENTOS)
                .version("6")
                .architecture(x86_64)
            )
            .timeToLive(
                ZonedDateTime.now().plusHours(2)
            );
    }

    private void clearAll() {
        serverService
            .delete(new ServerFilter()
                .dataCenters(DE_FRANKFURT, US_EAST_STERLING)
                .status(ACTIVE)
            )
            .waitUntilComplete();

        groupService
            .delete(new GroupFilter().nameContains("uat-s"))
            .waitUntilComplete();
    }

    /**
     * Step 1. App query total billing statistics by all datacenters
     */
    @Test(groups = {SAMPLES})
    public void getBillingStatisticsByAllDatacenters() {
        Statistics summarize = statisticsService
            .billingStats()
            .forDataCenters(
                new DataCenterFilter()
                    .dataCenters(DE_FRANKFURT, US_EAST_STERLING)
            )
            .summarize();

        assertNotNull(summarize);
    }

    /**
     * Step 2. App query billing statistics grouped by datacenters
     */
    @Test(groups = {SAMPLES})
    public void getBillingStatisticsGroupedByDatacenters() {
        List<BillingStatsEntry> stats = statisticsService
            .billingStats()
            .forGroups(
                new GroupFilter().nameContains("uat-s")
            )
            .groupByDataCenter();

        assertNotNull(stats);
    }

    /**
     * Step 3. App query billing statistics grouped by servers within DE1 Datacenter
     */
    @Test(groups = {SAMPLES})
    public void getDE1BillingStatsGroupedByServers() {
        List<BillingStatsEntry> stats = statisticsService
            .billingStats()
            .forDataCenters(
                new DataCenterFilter()
                    .dataCenters(DE_FRANKFURT)
            )
            .groupByServer();

        assertNotNull(stats);
    }

    /**
     * Step 4. App query total monitoring statistics by all datacenters
     */
    @Test(groups = {SAMPLES})
    public void getMonitoringStatisticsByAllDatacenters() {
        List<MonitoringStatsEntry> summarize = statisticsService
            .monitoringStats()
            .forDataCenters(
                new DataCenterFilter()
                    .dataCenters(DE_FRANKFURT, US_EAST_STERLING)
            )
            .forTime(new ServerMonitoringFilter().last(Duration.ofDays(2)))
            .summarize();

        assertNotNull(summarize);
    }

    /**
     * Step 5. App query monitoring statistics grouped by datacenters
     */
    @Test(groups = {SAMPLES})
    public void getMonitoringStatisticsGroupedByDatacenters() {
        List<MonitoringStatsEntry> stats = statisticsService
            .monitoringStats()
            .forGroups(
                new GroupFilter().nameContains("uat-s")
            )
            .forTime(new ServerMonitoringFilter().last(Duration.ofDays(2)))
            .groupByDataCenter();

        assertNotNull(stats);
    }

    /**
     * Step 6. App query monitoring statistics grouped by servers within DE1 Datacenter
     */
    @Test(groups = {SAMPLES})
    public void getDE1MonitoringStatsGroupedByServers() {
        List<MonitoringStatsEntry> stats = statisticsService
            .monitoringStats()
            .forDataCenters(
                new DataCenterFilter()
                    .dataCenters(DE_FRANKFURT)
            )
            .forTime(new ServerMonitoringFilter().last(Duration.ofDays(2)))
            .groupByServer();

        groupService.findByDataCenter(DE_FRANKFURT);

        assertNotNull(stats);
    }

    /**
     * Step 7. App query monitoring statistics for last hour grouped by DataCenter
     */
    @Test(groups = {SAMPLES})
    public void getDE1MonitoringStatsForLastHourGroupedByServers() {
        List<MonitoringStatsEntry> stats = statisticsService
            .monitoringStats()
            .forDataCenters(
                new DataCenterFilter()
                    .dataCenters(DE_FRANKFURT)
            )
            .forTime(new ServerMonitoringFilter()
                .last(Duration.ofHours(1))
                .type(MonitoringType.REALTIME)
                .interval(Duration.ofMinutes(10)))
            .groupByDataCenter();

        assertNotNull(stats);
    }
}
