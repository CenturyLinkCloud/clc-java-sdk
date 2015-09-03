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

package com.centurylink.cloud.sdk.server.services.dsl.servers.modify;

import com.centurylink.cloud.sdk.server.services.AbstractServersSdkTest;
import com.centurylink.cloud.sdk.server.services.client.domain.server.metadata.ServerMetadata;
import com.centurylink.cloud.sdk.server.services.dsl.ServerService;
import com.centurylink.cloud.sdk.server.services.dsl.domain.network.AddNetworkConfig;
import com.centurylink.cloud.sdk.server.services.dsl.domain.network.refs.Network;
import com.centurylink.cloud.sdk.server.services.dsl.domain.server.refs.Server;
import com.centurylink.cloud.sdk.tests.recorded.WireMockFileSource;
import com.centurylink.cloud.sdk.tests.recorded.WireMockMixin;
import com.google.inject.Inject;
import org.testng.annotations.Test;

import static com.centurylink.cloud.sdk.tests.TestGroups.RECORDED;

//@Test(groups = {RECORDED})
public class SecondaryNetworkTest extends AbstractServersSdkTest implements WireMockMixin {

    private Server serverRef = Server.refById("de1altdweb598");

    @Inject
    ServerService serverService;

    ServerMetadata serverMetadata;

    @Test
    @WireMockFileSource("secondary-network/add")
    public void testAddSecondaryNetwork() {
        serverService.addSecondaryNetwork(
            serverRef.asFilter(),
            new AddNetworkConfig().network(Network.refById("fbc1f6c7dd0241dfa22bafa05244da00"))
        ).waitUntilComplete();
    }

    @Test
    @WireMockFileSource("secondary-network/add-assert")
    public void testAddSecondaryNetworkAssert() {
        serverMetadata = serverService.findByRef(serverRef);

        assert serverMetadata.getDetails().getSecondaryIPAddresses().size() == 1;
    }

    @Test
    @WireMockFileSource("secondary-network/remove")
    public void testRemoveSecondaryNetwork() {
        serverService.removeSecondaryNetworks(
            serverRef.asFilter()
        ).waitUntilComplete();
    }

    @Test
    @WireMockFileSource("secondary-network/remove-assert")
    public void testRemoveSecondaryNetworkAssert() {
        serverMetadata = serverService.findByRef(serverRef);

        assert serverMetadata.getDetails().getSecondaryIPAddresses().size() == 0;
    }

}
