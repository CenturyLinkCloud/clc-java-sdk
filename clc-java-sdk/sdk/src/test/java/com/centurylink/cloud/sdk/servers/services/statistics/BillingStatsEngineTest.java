package com.centurylink.cloud.sdk.servers.services.statistics;

import com.centurylink.cloud.sdk.common.management.services.domain.datacenters.refs.DataCenter;
import com.centurylink.cloud.sdk.servers.AbstractServersSdkTest;
import com.centurylink.cloud.sdk.servers.services.StatisticsService;
import com.centurylink.cloud.sdk.servers.services.domain.group.filters.GroupFilter;
import com.centurylink.cloud.sdk.servers.services.domain.group.refs.Group;
import com.centurylink.cloud.sdk.servers.services.domain.server.filters.ServerFilter;
import com.centurylink.cloud.sdk.servers.services.domain.statistics.BillingStatsEntry;
import com.centurylink.cloud.sdk.servers.services.domain.statistics.Statistics;
import com.google.inject.Inject;
import org.testng.annotations.Test;

import java.util.List;

import static com.centurylink.cloud.sdk.tests.TestGroups.INTEGRATION;
import static com.centurylink.cloud.sdk.tests.TestGroups.LONG_RUNNING;

@Test(groups = {INTEGRATION, LONG_RUNNING})
public class BillingStatsEngineTest extends AbstractServersSdkTest {

    @Inject
    StatisticsService statisticsService;

    Group group = Group.refByName()
            .dataCenter(DataCenter.DE_FRANKFURT)
            .name(Group.DEFAULT_GROUP);

    @Test
    public void testBillingStatsEngine() throws Exception {
        /* Due to statistics isn't implemented immediately for new server/groups
           We should use default group to check results */

        Statistics summarize = statisticsService
                .billingStats()
                .forGroups(new GroupFilter().groups(group))
                .summarize();

        List<BillingStatsEntry> statsByGroup = statisticsService
                .billingStats()
                .forGroups(new GroupFilter().groups(group))
                .aggregateSubItems()
                .groupByGroup();

        List<BillingStatsEntry> statsByServer = statisticsService
                .billingStats()
                .forServers(new ServerFilter().nameContains("md-srv"))
                .groupByServer();

        List<BillingStatsEntry> statsByDataCenter = statisticsService
                .billingStats()
                .forServers(new ServerFilter().nameContains("md-uti"))
                .groupByDataCenter();

        assertNotNull(summarize);
        assertNotNull(statsByGroup);
        assertNotNull(statsByServer);
        assertNotNull(statsByDataCenter);
    }

}
