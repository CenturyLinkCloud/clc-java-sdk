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

import com.centurylink.cloud.sdk.core.injector.Inject;
import com.centurylink.cloud.sdk.server.services.AbstractServersSdkTest;
import com.centurylink.cloud.sdk.server.services.client.domain.server.metadata.ServerMetadata;
import com.centurylink.cloud.sdk.server.services.dsl.ServerService;
import com.centurylink.cloud.sdk.server.services.dsl.domain.server.CloneServerConfig;
import com.centurylink.cloud.sdk.server.services.dsl.domain.server.refs.Server;
import com.centurylink.cloud.sdk.server.services.dsl.servers.TestServerSupport;
import com.centurylink.cloud.sdk.tests.recorded.WireMockMixin;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

import static com.centurylink.cloud.sdk.base.services.dsl.domain.datacenters.refs.DataCenter.DE_FRANKFURT;
import static com.centurylink.cloud.sdk.tests.TestGroups.RECORDED;

@Test(groups = {RECORDED})
public class CloneServerTest extends AbstractServersSdkTest implements WireMockMixin {

    @Inject
    ServerService serverService;

    Server server;

    @Test
    public void testCloneSever() {

        CloneServerConfig config = TestServerSupport.getCloneServerConfig(Server.refById("de1altdcln04"));

        server = serverService.clone(config)
            .waitUntilComplete()
            .getResult();

        ServerMetadata serverMetadata = serverService.findByRef(server);

        assertNotNull(serverMetadata);
        assertNotNull(serverMetadata.getId());
        assertEquals(serverMetadata.getLocationId().toLowerCase(), DE_FRANKFURT.getId());
        assertEquals(serverMetadata.getOsType(), "CentOS 6 64-bit");
        assertEquals(serverMetadata.getType(), config.getType().getCode());
        assertTrue(serverMetadata.getName().contains(config.getName()));
    }

    @AfterMethod
    public void deleteServer() {
        serverService.delete(server.asFilter());
    }

}
