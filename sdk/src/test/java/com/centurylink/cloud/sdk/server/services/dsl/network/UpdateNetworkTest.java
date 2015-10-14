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

package com.centurylink.cloud.sdk.server.services.dsl.network;

import com.centurylink.cloud.sdk.core.injector.Inject;
import com.centurylink.cloud.sdk.server.services.AbstractServersSdkTest;
import com.centurylink.cloud.sdk.server.services.client.domain.network.NetworkMetadata;
import com.centurylink.cloud.sdk.server.services.dsl.NetworkService;
import com.centurylink.cloud.sdk.server.services.dsl.domain.network.UpdateNetworkConfig;
import com.centurylink.cloud.sdk.server.services.dsl.domain.network.filters.NetworkFilter;
import com.centurylink.cloud.sdk.server.services.dsl.domain.network.refs.Network;
import com.centurylink.cloud.sdk.tests.recorded.WireMockFileSource;
import com.centurylink.cloud.sdk.tests.recorded.WireMockMixin;
import org.testng.annotations.Test;

import static com.centurylink.cloud.sdk.tests.TestGroups.RECORDED;

@Test(groups = {RECORDED})
@WireMockFileSource("update")
public class UpdateNetworkTest extends AbstractServersSdkTest implements WireMockMixin {

    @Inject
    private NetworkService networkService;

    @Test
    public void testUpdate() {
        String id = "09518bea62ad42beac3b318d25287c84";
        String name = "vlan_upd_2820_10.127.220";
        String description = "vlan_desc_upd_2820_10.127.220";

        networkService.update(
            new NetworkFilter()
                .id(id),
            new UpdateNetworkConfig()
                .name(name)
                .description(description)

        );

        NetworkMetadata networkMetadata = networkService.findByRef(
            Network.refById(id)
        );

        assertNotNull(networkMetadata);
        assertEquals(networkMetadata.getId(), id);
        assertEquals(networkMetadata.getName(), name);
        assertEquals(networkMetadata.getDescription(), description);
    }
}
