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

package com.centurylink.cloud.sdk.server.services.dsl.servers.clone;

import com.centurylink.cloud.sdk.server.services.AbstractServersSdkTest;
import com.centurylink.cloud.sdk.server.services.client.domain.server.metadata.ServerMetadata;
import com.centurylink.cloud.sdk.server.services.dsl.ServerService;
import com.centurylink.cloud.sdk.server.services.dsl.domain.server.CloneServerConfig;
import com.centurylink.cloud.sdk.server.services.dsl.domain.server.refs.Server;
import com.centurylink.cloud.sdk.server.services.dsl.servers.TestServerSupport;
import com.centurylink.cloud.sdk.tests.recorded.WireMockFileSource;
import com.centurylink.cloud.sdk.tests.recorded.WireMockMixin;
import com.google.inject.Inject;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

import static com.centurylink.cloud.sdk.tests.TestGroups.RECORDED;
import static com.centurylink.cloud.sdk.base.services.dsl.domain.datacenters.refs.DataCenter.DE_FRANKFURT;

@Test(groups = {RECORDED})
@WireMockFileSource
public class CloneServerTest extends AbstractServersSdkTest implements WireMockMixin {

    @Inject
    ServerService serverService;

    ServerMetadata serverMetadata;

    @Test
    public void testCloneSever() {
        Server server = Server.refById("de1altdcln04");

        CloneServerConfig config = TestServerSupport.getCloneServerConfig(server);

        serverMetadata = serverService.clone(config)
            .waitUntilComplete()
            .getResult();

        assertNotNull(serverMetadata);
        assertNotNull(serverMetadata.getId());
        assertEquals(serverMetadata.getLocationId().toLowerCase(), DE_FRANKFURT.getId());
        assertEquals(serverMetadata.getOsType(), "CentOS 6 64-bit");
        assertEquals(serverMetadata.getType(), config.getType().getCode());
        assertTrue(serverMetadata.getName().contains(config.getName()));
    }

    @AfterMethod
    public void deleteServer() {
        serverService.delete(serverMetadata.asRefById().asFilter());
    }

}
