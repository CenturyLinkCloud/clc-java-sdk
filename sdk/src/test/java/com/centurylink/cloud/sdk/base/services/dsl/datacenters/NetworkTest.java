package com.centurylink.cloud.sdk.base.services.dsl.datacenters;

import com.centurylink.cloud.sdk.base.services.BaseModule;
import com.centurylink.cloud.sdk.base.services.client.DataCentersClient;
import com.centurylink.cloud.sdk.base.services.client.domain.datacenters.DataCenterMetadata;
import com.centurylink.cloud.sdk.base.services.client.domain.datacenters.GetDataCenterListResponse;
import com.centurylink.cloud.sdk.base.services.client.domain.datacenters.deployment.capabilities.DatacenterDeploymentCapabilitiesMetadata;
import com.centurylink.cloud.sdk.base.services.client.domain.datacenters.deployment.capabilities.NetworkMetadata;
import com.centurylink.cloud.sdk.base.services.dsl.DataCenterService;
import com.centurylink.cloud.sdk.base.services.dsl.domain.datacenters.refs.DataCenter;
import com.centurylink.cloud.sdk.core.auth.AuthModule;
import com.centurylink.cloud.sdk.tests.AbstractSdkTest;
import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.google.inject.Module;
import com.google.inject.util.Modules;
import org.mockito.stubbing.OngoingStubbing;
import org.testng.annotations.Test;

import java.util.List;

import static com.centurylink.cloud.sdk.tests.TestGroups.INTEGRATION;
import static java.util.Arrays.asList;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author aliaksandr.krasitski
 */
@Test(groups = INTEGRATION)
public class NetworkTest extends AbstractSdkTest {

    @Inject
    DataCenterService dataCenterService;

    @Inject
    DataCentersClient dataCenterClient;

    @Override
    protected List<Module> modules() {
        return list(new AuthModule(), Modules.override(new BaseModule()).with(new AbstractModule() {
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

        List<NetworkMetadata> networks = dataCenterService.findNetworkByDataCenter(DataCenter.CA_TORONTO_2);

        assertEquals(networks.size(), 2);
        assertEquals(networks.get(0).getName(), "first_network");
        assertEquals(networks.get(1).getNetworkId(), "2");
    }

    private OngoingStubbing<DatacenterDeploymentCapabilitiesMetadata> mockNetworks(List<NetworkMetadata> networks) {
        return when(dataCenterClient.getDeploymentCapabilities(anyObject()))
            .thenReturn(new DatacenterDeploymentCapabilitiesMetadata()
                    .deployableNetworks(networks)
            );
    }

    private void mockDataCenters() {
        when(dataCenterClient.findAllDataCenters())
            .thenReturn(new GetDataCenterListResponse(
                    asList(new DataCenterMetadata("ca3", "Toronto_2")))
            );
    }
}
