package com.centurylink.cloud.sdk.networks.services;

import com.centurylink.cloud.sdk.core.auth.AuthModule;
import com.centurylink.cloud.sdk.core.datacenters.client.DataCentersClient;
import com.centurylink.cloud.sdk.core.datacenters.client.domain.DataCenterMetadata;
import com.centurylink.cloud.sdk.core.datacenters.client.domain.GetDataCenterListResponse;
import com.centurylink.cloud.sdk.core.datacenters.client.domain.deployment.capacilities.GetDeploymentCapabilitiesResponse;
import com.centurylink.cloud.sdk.core.datacenters.client.domain.deployment.capacilities.NetworkMetadata;
import com.centurylink.cloud.sdk.core.datacenters.services.domain.DataCenters;
import com.centurylink.cloud.sdk.networks.AbstractNetworksSdkTest;
import com.centurylink.cloud.sdk.servers.ServersModule;
import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.google.inject.Module;
import com.google.inject.util.Modules;
import org.mockito.stubbing.OngoingStubbing;
import org.testng.annotations.Test;

import java.util.List;

import static java.util.Arrays.asList;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

public class NetworkServiceTest extends AbstractNetworksSdkTest {

    @Inject
    NetworkService networkService;

    @Inject
    DataCentersClient dataCentersClient;

    @Override
    protected List<Module> modules() {
        return list(new AuthModule(), Modules.override(new ServersModule()).with(new AbstractModule() {
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

        List<NetworkMetadata> networks = networkService.findByDataCenter(DataCenters.CA_TORONTO_2);

        assert networks.size() == 2;
        assert networks.get(0).getName().equals("first_network");
        assert networks.get(1).getNetworkId().equals("2");
    }

    private OngoingStubbing<GetDeploymentCapabilitiesResponse> mockNetworks(List<NetworkMetadata> networks) {
        return when(dataCentersClient.getDataCenterDeploymentCapabilities(anyString()))
            .thenReturn(new GetDeploymentCapabilitiesResponse()
                .deployableNetworks(networks)
            );
    }

    private void mockDataCenters() {
        when(dataCentersClient.findAllDataCenters())
            .thenReturn(new GetDataCenterListResponse(
                asList(new DataCenterMetadata("CA3", "Toronto_2")))
            );
    }

}