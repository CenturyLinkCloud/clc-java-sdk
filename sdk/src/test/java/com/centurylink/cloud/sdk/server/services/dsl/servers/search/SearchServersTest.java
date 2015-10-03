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

package com.centurylink.cloud.sdk.server.services.dsl.servers.search;

import com.centurylink.cloud.sdk.base.services.client.DataCentersClient;
import com.centurylink.cloud.sdk.base.services.client.domain.datacenters.GetDataCenterListResponse;
import com.centurylink.cloud.sdk.base.services.dsl.domain.datacenters.refs.DataCenter;
import com.centurylink.cloud.sdk.core.injector.Inject;
import com.centurylink.cloud.sdk.core.services.filter.Filter;
import com.centurylink.cloud.sdk.server.services.AbstractServersSdkTest;
import com.centurylink.cloud.sdk.server.services.client.ServerClient;
import com.centurylink.cloud.sdk.server.services.client.domain.group.GroupMetadata;
import com.centurylink.cloud.sdk.server.services.client.domain.server.metadata.ServerMetadata;
import com.centurylink.cloud.sdk.server.services.dsl.ServerService;
import com.centurylink.cloud.sdk.server.services.dsl.domain.server.PowerState;
import com.centurylink.cloud.sdk.server.services.dsl.domain.server.ServerStatus;
import com.centurylink.cloud.sdk.server.services.dsl.domain.server.filters.ServerFilter;
import org.mockito.Mock;
import org.mockito.Mockito;
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

    @Inject @Mock
    DataCentersClient dataCentersClient;

    @Inject @Mock
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
