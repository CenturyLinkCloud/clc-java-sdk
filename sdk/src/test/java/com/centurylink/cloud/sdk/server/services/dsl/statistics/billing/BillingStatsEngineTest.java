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

package com.centurylink.cloud.sdk.server.services.dsl.statistics.billing;

import com.centurylink.cloud.sdk.base.services.client.domain.datacenters.DataCenterMetadata;
import com.centurylink.cloud.sdk.base.services.dsl.domain.datacenters.refs.DataCenter;
import com.centurylink.cloud.sdk.core.injector.Inject;
import com.centurylink.cloud.sdk.server.services.AbstractServersSdkTest;
import com.centurylink.cloud.sdk.server.services.client.domain.group.GroupMetadata;
import com.centurylink.cloud.sdk.server.services.client.domain.server.metadata.ServerMetadata;
import com.centurylink.cloud.sdk.server.services.dsl.StatisticsService;
import com.centurylink.cloud.sdk.server.services.dsl.domain.group.filters.GroupFilter;
import com.centurylink.cloud.sdk.server.services.dsl.domain.group.refs.Group;
import com.centurylink.cloud.sdk.server.services.dsl.domain.server.filters.ServerFilter;
import com.centurylink.cloud.sdk.server.services.dsl.domain.statistics.billing.BillingStatsEntry;
import com.centurylink.cloud.sdk.server.services.dsl.domain.statistics.billing.Statistics;
import com.centurylink.cloud.sdk.tests.recorded.WireMockFileSource;
import com.centurylink.cloud.sdk.tests.recorded.WireMockMixin;
import org.testng.annotations.Test;

import java.math.BigDecimal;
import java.util.List;

import static com.centurylink.cloud.sdk.tests.TestGroups.RECORDED;

@Test(groups = {RECORDED})
@SuppressWarnings("unchecked")
public class BillingStatsEngineTest extends AbstractServersSdkTest implements WireMockMixin {

    private static final String DEFAULT_GROUP_NAME = Group.DEFAULT_GROUP;

    @Inject
    StatisticsService statisticsService;

    private Group group = Group
        .refByName()
        .dataCenter(DataCenter.DE_FRANKFURT)
        .name(DEFAULT_GROUP_NAME);

    private void testStatsByDataCenters() {
        List<BillingStatsEntry> statsByDataCenter = statisticsService
            .billingStats()
            .forServers(new ServerFilter().nameContains("md-srv"))
            .groupByDataCenter();

        assertNotNull(statsByDataCenter);
        assertEquals(statsByDataCenter.size(), 1);

        BillingStatsEntry<DataCenterMetadata> entry = statsByDataCenter.get(0);

        assertEquals(entry.getEntity().getId(), DataCenter.DE_FRANKFURT.getId());
        assertEquals(entry.getStatistics().getTemplateCost(), BigDecimal.valueOf(0.0));
        assertEquals(entry.getStatistics().getArchiveCost(), BigDecimal.valueOf(0.0));
        assertEquals(entry.getStatistics().getMonthlyEstimate(), BigDecimal.valueOf(1.22));
        assertEquals(entry.getStatistics().getMonthToDate(), BigDecimal.valueOf(0.14));
        assertEquals(entry.getStatistics().getCurrentHour(), BigDecimal.valueOf(0.00357));
    }

    private void testStatsByServer() {
        testStatsByDataCenters();

        List<BillingStatsEntry> statsByServer = statisticsService
            .billingStats()
            .forServers(new ServerFilter().nameContains("md-srv"))
            .groupByServer();

        assertNotNull(statsByServer);
        assertEquals(statsByServer.size(), 1);

        BillingStatsEntry<ServerMetadata> entry1 = statsByServer.get(0);

        assertEquals(entry1.getStatistics().getTemplateCost(), BigDecimal.valueOf(0.0));
        assertEquals(entry1.getStatistics().getArchiveCost(), BigDecimal.valueOf(0.0));
        assertEquals(entry1.getStatistics().getMonthlyEstimate(), BigDecimal.valueOf(1.22));
        assertEquals(entry1.getStatistics().getMonthToDate(), BigDecimal.valueOf(0.14));
        assertEquals(entry1.getStatistics().getCurrentHour(), BigDecimal.valueOf(0.00357));
    }

    private void testStatsByGroup() {
        testStatsByServer();

        List<BillingStatsEntry> statsByGroup = statisticsService
            .billingStats()
            .forGroups(new GroupFilter().groups(group))
            .aggregateSubItems()
            .groupByGroup();

        assertNotNull(statsByGroup);
        assertEquals(statsByGroup.size(), 1);

        BillingStatsEntry<GroupMetadata> entry1 = statsByGroup.get(0);

        assertEquals(entry1.getEntity().getName(), DEFAULT_GROUP_NAME);
        assertEquals(entry1.getStatistics().getTemplateCost(), BigDecimal.valueOf(0.0));
        assertEquals(entry1.getStatistics().getArchiveCost(), BigDecimal.valueOf(0.0));
        assertEquals(entry1.getStatistics().getMonthlyEstimate(), BigDecimal.valueOf(1.22));
        assertEquals(entry1.getStatistics().getMonthToDate(), BigDecimal.valueOf(0.14));
        assertEquals(entry1.getStatistics().getCurrentHour(), BigDecimal.valueOf(0.00357));
    }

    private void testSummarize() {
        testStatsByGroup();

        Statistics summarize = statisticsService
            .billingStats()
            .forGroups(new GroupFilter().groups(group))
            .summarize();

        assertNotNull(summarize);

        assertEquals(summarize.getTemplateCost(), BigDecimal.valueOf(0.0));
        assertEquals(summarize.getArchiveCost(), BigDecimal.valueOf(0.0));
        assertEquals(summarize.getMonthlyEstimate(), BigDecimal.valueOf(1.22));
        assertEquals(summarize.getMonthToDate(), BigDecimal.valueOf(0.14));
        assertEquals(summarize.getCurrentHour(), BigDecimal.valueOf(0.00357));
    }

    @Test
    @WireMockFileSource
    public void testBillingStatsEngine() throws Exception {
        testSummarize();
    }
}