package com.centurylink.cloud.sdk.servers.services.statistics.billing;

import com.centurylink.cloud.sdk.common.management.client.domain.datacenters.DataCenterMetadata;
import com.centurylink.cloud.sdk.common.management.services.domain.datacenters.refs.DataCenter;
import com.centurylink.cloud.sdk.servers.AbstractServersSdkTest;
import com.centurylink.cloud.sdk.servers.client.ServerClient;
import com.centurylink.cloud.sdk.servers.client.domain.ChangeInfo;
import com.centurylink.cloud.sdk.servers.client.domain.group.ClientBillingStats;
import com.centurylink.cloud.sdk.servers.client.domain.group.GroupMetadata;
import com.centurylink.cloud.sdk.servers.client.domain.server.Details;
import com.centurylink.cloud.sdk.servers.client.domain.server.metadata.ServerMetadata;
import com.centurylink.cloud.sdk.servers.services.GroupService;
import com.centurylink.cloud.sdk.servers.services.ServerService;
import com.centurylink.cloud.sdk.servers.services.StatisticsService;
import com.centurylink.cloud.sdk.servers.services.domain.group.filters.GroupFilter;
import com.centurylink.cloud.sdk.servers.services.domain.group.refs.Group;
import com.centurylink.cloud.sdk.servers.services.domain.server.filters.ServerFilter;
import com.centurylink.cloud.sdk.servers.services.domain.statistics.billing.BillingStatsEntry;
import com.centurylink.cloud.sdk.servers.services.domain.statistics.billing.Statistics;
import com.google.inject.Inject;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static com.centurylink.cloud.sdk.tests.TestGroups.LONG_RUNNING;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.eq;
import static com.centurylink.cloud.sdk.tests.TestGroups.INTEGRATION;

@Test(groups = {INTEGRATION, LONG_RUNNING})
@SuppressWarnings("unchecked")
public class BillingStatsEngineTest extends AbstractServersSdkTest {

    private final static String DEFAULT_GROUP_NAME = Group.DEFAULT_GROUP;
    private final static String SUB_GROUP_NAME = "st-gp";

    private final static String FIRST_SERVER_ID = "de1altdmd-srv189";
    private final static String SECOND_SERVER_ID = "de1altdmd-srv190";

    @Inject
    StatisticsService statisticsService;

    @Inject
    GroupService groupService;

    @Inject @Spy
    ServerService serverService;

    @Inject @Spy
    ServerClient serverClient;

    private Group group = Group
        .refByName()
        .dataCenter(DataCenter.DE_FRANKFURT)
        .name(DEFAULT_GROUP_NAME);

    private GroupMetadata groupMetadata;

    @BeforeMethod
    public void setUpFixtures() {
        groupMetadata = groupService.findByRef(group);

        List<ServerMetadata> serverMetadataList = mockFindServersResult();

        Mockito
            .doReturn(fromJson("billing_stats.json", ClientBillingStats.class))
                .when(serverClient).getGroupBillingStats(eq(groupMetadata.getId()));

        Mockito
            .doReturn(serverMetadataList.get(0))
                .when(serverService).findByRef(eq(serverMetadataList.get(0).asRefById()));

        Mockito
            .doReturn(serverMetadataList.get(1))
                .when(serverService).findByRef(eq(serverMetadataList.get(1).asRefById()));

        Mockito
            .doReturn(serverMetadataList)
                .when(serverService).find(anyObject());

    }

    private List<ServerMetadata> mockFindServersResult() {
        List<ServerMetadata> serverMetadataList = new ArrayList<>();

        serverMetadataList.add(createServerMetadata(FIRST_SERVER_ID));
        serverMetadataList.add(createServerMetadata(SECOND_SERVER_ID));

        return serverMetadataList;
    }

    private ServerMetadata createServerMetadata(String serverId) {
        return new ServerMetadata() {{
            setId(serverId);
            setName(serverId.toUpperCase());
            setGroupId(groupMetadata.getId());
            setIsTemplate(false);
            setLocationId(DataCenter.DE_FRANKFURT.getId());
            setOsType("CentOS 5 64-bit");
            setOs("centOS5_64Bit");
            setStatus("active");
            setType("standard");
            setStorageType("premium");
            setChangeInfo(new ChangeInfo() {{
                setCreatedBy("idrabenia.altd");
                setCreatedDate("2015-04-23T11:41:22Z");
                setModifiedBy("idrabenia.altd");
                setModifiedDate("2015-04-23T11:48:40Z");
            }});
            setDetails(new Details() {{
                setCpu(1);
                setDiskCount(4);
                setMemoryMB(3072);
                setPowerState("started");
                setInMaintenanceMode(false);
            }});
        }};
    }

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
        assertEquals(entry.getStatistics().getMonthlyEstimate(), BigDecimal.valueOf(44.53));
        assertEquals(entry.getStatistics().getMonthToDate(), BigDecimal.valueOf(5.57));
        assertEquals(entry.getStatistics().getCurrentHour(), BigDecimal.valueOf(0.1286));
    }

    private void testStatsByServer() {
        testStatsByDataCenters();

        List<BillingStatsEntry> statsByServer = statisticsService
            .billingStats()
            .forServers(new ServerFilter().nameContains("md-srv"))
            .groupByServer();

        assertNotNull(statsByServer);
        assertEquals(statsByServer.size(), 2);

        BillingStatsEntry<ServerMetadata> entry1 = statsByServer.get(0);
        BillingStatsEntry<ServerMetadata> entry2 = statsByServer.get(1);

        assertEquals(entry1.getEntity().getId(), FIRST_SERVER_ID);
        assertEquals(entry1.getStatistics().getTemplateCost(), BigDecimal.valueOf(0.0));
        assertEquals(entry1.getStatistics().getArchiveCost(), BigDecimal.valueOf(0.0));
        assertEquals(entry1.getStatistics().getMonthlyEstimate(), BigDecimal.valueOf(20.6));
        assertEquals(entry1.getStatistics().getMonthToDate(), BigDecimal.valueOf(4.88));
        assertEquals(entry1.getStatistics().getCurrentHour(), BigDecimal.valueOf(0.0519));

        assertEquals(entry2.getEntity().getId(), SECOND_SERVER_ID);
        assertEquals(entry2.getStatistics().getTemplateCost(), BigDecimal.valueOf(0.0));
        assertEquals(entry2.getStatistics().getArchiveCost(), BigDecimal.valueOf(0.0));
        assertEquals(entry2.getStatistics().getMonthlyEstimate(), BigDecimal.valueOf(23.93));
        assertEquals(entry2.getStatistics().getMonthToDate(), BigDecimal.valueOf(0.69));
        assertEquals(entry2.getStatistics().getCurrentHour(), BigDecimal.valueOf(0.0767));
    }

    private void testStatsByGroup() {
        testStatsByServer();

        List<BillingStatsEntry> statsByGroup = statisticsService
            .billingStats()
            .forGroups(new GroupFilter().groups(group))
            .aggregateSubItems()
            .groupByGroup();

        assertNotNull(statsByGroup);
        assertEquals(statsByGroup.size(), 2);

        BillingStatsEntry<GroupMetadata> entry1 = statsByGroup.get(0);
        BillingStatsEntry<GroupMetadata> entry2 = statsByGroup.get(1);

        assertEquals(entry1.getEntity().getName(), DEFAULT_GROUP_NAME);
        assertEquals(entry1.getStatistics().getTemplateCost(), BigDecimal.valueOf(0.0));
        assertEquals(entry1.getStatistics().getArchiveCost(), BigDecimal.valueOf(0.0));
        assertEquals(entry1.getStatistics().getMonthlyEstimate(), BigDecimal.valueOf(71.36));
        assertEquals(entry1.getStatistics().getMonthToDate(), BigDecimal.valueOf(16.68));
        assertEquals(entry1.getStatistics().getCurrentHour(), BigDecimal.valueOf(0.1805));

        assertEquals(entry2.getEntity().getName(), SUB_GROUP_NAME);
        assertEquals(entry2.getStatistics().getTemplateCost(), BigDecimal.ZERO);
        assertEquals(entry2.getStatistics().getArchiveCost(), BigDecimal.ZERO);
        assertEquals(entry2.getStatistics().getMonthlyEstimate(), BigDecimal.ZERO);
        assertEquals(entry2.getStatistics().getMonthToDate(), BigDecimal.ZERO);
        assertEquals(entry2.getStatistics().getCurrentHour(), BigDecimal.ZERO);
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
        assertEquals(summarize.getMonthlyEstimate(), BigDecimal.valueOf(71.36));
        assertEquals(summarize.getMonthToDate(), BigDecimal.valueOf(16.68));
        assertEquals(summarize.getCurrentHour(), BigDecimal.valueOf(0.1805));
    }

    @Test
    public void testBillingStatsEngine() throws Exception {
        testSummarize();
    }
}