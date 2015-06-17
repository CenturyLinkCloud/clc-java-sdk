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

package com.centurylink.cloud.sdk.networks.services;

import com.centurylink.cloud.sdk.common.management.client.DataCentersClient;
import com.centurylink.cloud.sdk.common.management.client.domain.datacenters.DataCenterMetadata;
import com.centurylink.cloud.sdk.common.management.client.domain.datacenters.GetDataCenterListResponse;
import com.centurylink.cloud.sdk.common.management.client.domain.datacenters.deployment.capabilities.DatacenterDeploymentCapabilitiesMetadata;
import com.centurylink.cloud.sdk.common.management.client.domain.datacenters.deployment.capabilities.NetworkMetadata;
import com.centurylink.cloud.sdk.core.auth.AuthModule;
import com.centurylink.cloud.sdk.networks.AbstractNetworksSdkTest;
import com.centurylink.cloud.sdk.networks.NetworksModule;
import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.google.inject.Module;
import com.google.inject.util.Modules;
import org.mockito.stubbing.OngoingStubbing;
import org.testng.annotations.Test;

import java.util.List;

import static java.util.Arrays.asList;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class NetworkServiceTest extends AbstractNetworksSdkTest {

    @Inject
    NetworkService networkService;

    @Inject
    DataCentersClient dataCentersClient;

    @Override
    protected List<Module> modules() {
        return list(new AuthModule(), Modules.override(new NetworksModule()).with(new AbstractModule() {
            @Override
            protected void configure() {
                bind(DataCentersClient.class).toInstance(mock(DataCentersClient.class));
            }
        }));
    }

    @Test
    public void testFindNetworksByDataCenter() {
        mockDataCenters();
        mockNetworks(asList(
            new NetworkMetadata()
                .name("first_network")
                .networkId("1"),
            new NetworkMetadata()
                .name("second_network")
                .networkId("2")
        ));

        List<NetworkMetadata> networks = networkService.findByDataCenter(com.centurylink.cloud.sdk.common.management.services.domain.datacenters.refs.DataCenter.CA_TORONTO_2);

        assert networks.size() == 2;
        assert networks.get(0).getName().equals("first_network");
        assert networks.get(1).getNetworkId().equals("2");
    }

    private OngoingStubbing<DatacenterDeploymentCapabilitiesMetadata> mockNetworks(List<NetworkMetadata> networks) {
        return when(dataCentersClient.getDeploymentCapabilities(anyString()))
            .thenReturn(new DatacenterDeploymentCapabilitiesMetadata()
                .deployableNetworks(networks)
            );
    }

    private void mockDataCenters() {
        when(dataCentersClient.findAllDataCenters())
            .thenReturn(new GetDataCenterListResponse(
                asList(new DataCenterMetadata("ca3", "Toronto_2")))
            );
    }

}