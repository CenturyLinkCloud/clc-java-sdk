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
import com.centurylink.cloud.sdk.server.services.dsl.domain.network.refs.Network;
import com.centurylink.cloud.sdk.tests.recorded.WireMockFileSource;
import com.centurylink.cloud.sdk.tests.recorded.WireMockMixin;
import org.testng.annotations.Test;

import java.util.List;

import static com.centurylink.cloud.sdk.tests.TestGroups.RECORDED;

@Test(groups = {RECORDED})
@WireMockFileSource("search")
public class SearchNetworkTest extends AbstractServersSdkTest implements WireMockMixin {

    @Inject
    private NetworkService networkService;

    @Test
    public void testFindAll() {

        List<NetworkMetadata> metadataList = networkService.find(
                new NetworkFilter()
        );

        assertNotNull(metadataList);
        assertEquals(metadataList.size(), 14);
    }

    @Test
    public void testFindByRefId() {
        String id = "809297928f1d4224b9b9927f745bc082";

        NetworkMetadata metadata = networkService.findByRef(Network.refById(id));

        assertEquals(metadata.getId(), id);
    }

    @Test
    public void testFindByRefName() {
        String name = "vlan_719_10.56.119";

        NetworkMetadata metadata = networkService.findByRef(
            Network.refByName().name(name).dataCenter(DataCenter.CA_TORONTO_1)
        );

        assertEquals(metadata.getName(), name);
    }

    @Test
    public void testFindByRefGateway() {
        String gateway = "10.50.48.1";

        NetworkMetadata metadata = networkService.findByRef(
            Network.refByGateway(DataCenter.CA_VANCOUVER, gateway)
        );

        assertEquals(metadata.getGateway(), gateway);
    }

    @Test
    public void testFindByDataCenter() {

        List<NetworkMetadata> metadataList = networkService.find(
                new NetworkFilter().dataCenters(DataCenter.US_EAST_STERLING)
        );

        assertNotNull(metadataList);
        assertEquals(metadataList.size(), 2);
        assertEquals(metadataList.get(0).getDataCenterId(), DataCenter.US_EAST_STERLING.getId());
        assertEquals(metadataList.get(1).getDataCenterId(), DataCenter.US_EAST_STERLING.getId());
    }

    @Test
    public void testFindById() {
        String id = "09518bea62ad42beac3b318d25287c84";

        NetworkMetadata networkMetadata = networkService.findByRef(
            Network.refById(id)
        );

        assertNotNull(networkMetadata);
        assertEquals(networkMetadata.getId(), id);
    }

    @Test
    public void testFindByNameAndDataCenter() {
        String name = "vlan";

        List<NetworkMetadata> metadataList = networkService.find(
            new NetworkFilter()
                .dataCenters(DataCenter.US_EAST_STERLING)
                .nameContains(name)
        );

        assertNotNull(metadataList);
        assertEquals(metadataList.size(), 2);
        assertTrue(metadataList.get(0).getName().contains(name));
        assertTrue(metadataList.get(1).getName().contains(name));
    }
}
