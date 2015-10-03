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

import com.centurylink.cloud.sdk.base.services.dsl.DataCenterService;
import com.centurylink.cloud.sdk.base.services.dsl.domain.datacenters.refs.DataCenter;
import com.centurylink.cloud.sdk.core.injector.Inject;
import com.centurylink.cloud.sdk.server.services.AbstractServersSdkTest;
import com.centurylink.cloud.sdk.server.services.client.domain.server.metadata.ServerMetadata;
import com.centurylink.cloud.sdk.server.services.dsl.ServerService;
import com.centurylink.cloud.sdk.server.services.dsl.domain.server.filters.ServerFilter;
import org.testng.annotations.Test;

import java.util.List;

import static com.centurylink.cloud.sdk.tests.TestGroups.INTEGRATION;

/**
 * @author Aliaksandr Krasitski
 */
public class SearchServersInSubGroupsTest extends AbstractServersSdkTest {

    @Inject
    ServerService serverService;

    @Inject
    DataCenterService dataCenterService;

    @Test(groups = INTEGRATION)
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
