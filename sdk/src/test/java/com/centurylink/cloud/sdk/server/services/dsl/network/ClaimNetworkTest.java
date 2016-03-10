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

import com.centurylink.cloud.sdk.base.services.dsl.domain.datacenters.refs.DataCenter;
import com.centurylink.cloud.sdk.core.injector.Inject;
import com.centurylink.cloud.sdk.server.services.AbstractServersSdkTest;
import com.centurylink.cloud.sdk.server.services.client.domain.network.NetworkMetadata;
import com.centurylink.cloud.sdk.server.services.dsl.NetworkService;
import com.centurylink.cloud.sdk.server.services.dsl.domain.network.filters.NetworkFilter;
import com.centurylink.cloud.sdk.tests.recorded.WireMockFileSource;
import com.centurylink.cloud.sdk.tests.recorded.WireMockMixin;
import org.testng.annotations.Test;

import java.util.List;

import static com.centurylink.cloud.sdk.tests.TestGroups.RECORDED;

@Test(groups = {RECORDED})
public class ClaimNetworkTest extends AbstractServersSdkTest implements WireMockMixin {

    @Inject
    private NetworkService networkService;

    @Test
    @WireMockFileSource("claim/before")
    public void testBeforeClaim() {
        List<NetworkMetadata> metadataList = networkService.find(
                new NetworkFilter().dataCenters(DataCenter.US_EAST_STERLING)
        );

        assertNotNull(metadataList);
        assertEquals(metadataList.size(), 1);
    }

    @Test
    @WireMockFileSource("claim/after")
    public void testClaim() {
        networkService
            .claim(new DataCenter[]{DataCenter.US_EAST_STERLING})
            .waitUntilComplete();

        List<NetworkMetadata> metadataList = networkService.find(
            new NetworkFilter().dataCenters(DataCenter.US_EAST_STERLING)
        );

        assertNotNull(metadataList);
        assertEquals(metadataList.size(), 2);
    }
}
