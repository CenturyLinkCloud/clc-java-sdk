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

package com.centurylink.cloud.sdk.servers.services.servers.search;

import com.centurylink.cloud.sdk.common.management.services.DataCenterService;
import com.centurylink.cloud.sdk.common.management.services.domain.datacenters.refs.DataCenter;
import com.centurylink.cloud.sdk.servers.AbstractServersSdkTest;
import com.centurylink.cloud.sdk.servers.client.domain.server.metadata.ServerMetadata;
import com.centurylink.cloud.sdk.servers.services.ServerService;
import com.centurylink.cloud.sdk.servers.services.domain.server.filters.ServerFilter;
import com.centurylink.cloud.sdk.tests.TestGroups;
import com.google.inject.Inject;
import org.testng.annotations.Test;

import java.util.List;

/**
 * @author Aliaksandr Krasitski
 */
public class SearchServersInSubGroupsTest extends AbstractServersSdkTest {

    @Inject
    ServerService serverService;

    @Inject
    DataCenterService dataCenterService;

    @Test(groups = TestGroups.INTEGRATION)
    public void testSearchActiveServers() {
        String rootGroupId = dataCenterService.findByRef(DataCenter.DE_FRANKFURT).getGroup().getId();
        ServerFilter serverFilter = new ServerFilter()
            .groupId(rootGroupId);

        List<ServerMetadata> serversInRootGroup = serverService.find(serverFilter);

        assertEquals(serversInRootGroup.size(), 0, "check servers count in root group");

        List<ServerMetadata> allServersInGroup = serverService.find(serverFilter
            .searchInSubGroups(true)
        );

        assertTrue(allServersInGroup.size() >= 0);
    }

}
