package com.centurylink.cloud.sdk.servers.services.servers.search;

import com.centurylink.cloud.sdk.common.management.client.DataCentersClient;
import com.centurylink.cloud.sdk.common.management.client.domain.datacenters.GetDataCenterListResponse;
import com.centurylink.cloud.sdk.common.management.services.domain.datacenters.refs.DataCenter;
import com.centurylink.cloud.sdk.core.services.filter.Filter;
import com.centurylink.cloud.sdk.servers.AbstractServersSdkTest;
import com.centurylink.cloud.sdk.servers.client.ServerClient;
import com.centurylink.cloud.sdk.servers.client.domain.group.GroupMetadata;
import com.centurylink.cloud.sdk.servers.client.domain.server.metadata.ServerMetadata;
import com.centurylink.cloud.sdk.servers.services.ServerService;
import com.centurylink.cloud.sdk.servers.services.domain.server.PowerState;
import com.centurylink.cloud.sdk.servers.services.domain.server.ServerStatus;
import com.centurylink.cloud.sdk.servers.services.domain.server.filters.ServerFilter;
import com.google.inject.Inject;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.List;

import static com.centurylink.cloud.sdk.core.function.Streams.map;
import static com.google.common.collect.Sets.newHashSet;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.eq;

/**
 * @author Ilya Drabenia
 */
public class SearchServersTest extends AbstractServersSdkTest {
    static final String CA2_ROOT_ID = "8c8a2e29f1f24bbaa347c5b2c3d4b791";
    static final String IL1_ROOT_ID = "6949dfcbcb0c459289d91821b04f6eab";

    @Inject
    ServerService serverService;

    @Inject @Spy
    DataCentersClient dataCentersClient;

    @Inject @Spy
    ServerClient serverClient;

    @BeforeMethod
    public void setUpFixtures() {
        Mockito
            .doReturn(fromJson("data_centers_list.json", GetDataCenterListResponse.class))
            .when(dataCentersClient).findAllDataCenters();

        Mockito
            .doReturn(fromJson("ca2_root_group.json", GroupMetadata.class))
            .when(serverClient).getGroup(eq(CA2_ROOT_ID), anyBoolean());

        Mockito
            .doReturn(fromJson("il1_root_group.json", GroupMetadata.class))
            .when(serverClient).getGroup(eq(IL1_ROOT_ID), anyBoolean());
    }

    @Test
    public void testSearchActiveServers() {
        List<ServerMetadata> results = serverService.find(new ServerFilter()
            .status(ServerStatus.ARCHIVED)
        );

        assertEquals(results.size(), 1);
        assertEquals(results.get(0).getName(), "CA2ALTDARCHIV01");
    }

    @Test
    public void testSearchServerInGroup1() {
        List<ServerMetadata> results = serverService.find(new ServerFilter()
            .groupNameContains("Group1")
        );

        assertEquals(results.size(), 3);
        assertThatResultContains(results, "CA2ALTDCENT201", "CA2ALTDCENTS101", "IL1ALTDDEB101");
    }

    @Test
    public void testOrOperation() {
        List<ServerMetadata> results = serverService.find(Filter.or(
            new ServerFilter()
                .dataCenters(DataCenter.CA_TORONTO_1)
                .id("CA2ALTDCENT201"),

            new ServerFilter()
                .dataCenters(DataCenter.US_CENTRAL_CHICAGO)
                .id("IL1ALTDDEB101")
        ));

        assertThatResultContains(results, "CA2ALTDCENT201", "IL1ALTDDEB101");
    }

    @Test
    public void testAndOperation() {
        List<ServerMetadata> results = serverService.find(Filter.and(
            new ServerFilter()
                .dataCenters(DataCenter.CA_TORONTO_1)
                .id("CA2ALTDCENT201", "CA2ALTDCENTS101"),

            new ServerFilter()
                .dataCenters(DataCenter.CA_TORONTO_1)
                .id("CA2ALTDCENTS101", "CA2ALTDARCHIV01")
        ));

        assertThatResultContains(results, "CA2ALTDCENTS101");
    }

    @Test
    public void testSearchByNameSubstring() {
        List<ServerMetadata> results = serverService.find(new ServerFilter()
            .dataCenters(DataCenter.CA_TORONTO_1)
            .nameContains("CENT", "ARCHIV")
        );

        assertThatResultContains(results, "CA2ALTDCENTS101", "CA2ALTDCENT201", "CA2ALTDARCHIV01");
    }

    @Test
    public void testSearchByDescription() {
        List<ServerMetadata> results = serverService.find(new ServerFilter()
            .dataCenters(DataCenter.CA_TORONTO_1)
            .descriptionContains("CENTos")
        );

        assertThatResultContains(results, "CA2ALTDCENTS101", "CA2ALTDCENT201");
    }

    @Test
    public void testSearchByPowerState() {
        List<ServerMetadata> results = serverService.find(new ServerFilter()
            .dataCenters(DataCenter.CA_TORONTO_1)
            .powerStates(PowerState.STARTED)
        );

        assertThatResultContains(results, "CA2ALTDCENTS101", "CA2ALTDCENT201", "CA2ALTDDEB201");
    }

    private void assertThatResultContains(List<ServerMetadata> servers, String... serverIds) {
        assertEquals(
            newHashSet(map(servers, ServerMetadata::getName)),
            newHashSet(serverIds)
        );
    }

}
