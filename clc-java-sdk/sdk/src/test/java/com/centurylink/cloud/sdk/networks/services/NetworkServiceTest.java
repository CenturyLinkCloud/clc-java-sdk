package com.centurylink.cloud.sdk.networks.services;

import com.centurylink.cloud.sdk.core.datacenters.client.domain.deployment.capacilities.NetworkMetadata;
import com.centurylink.cloud.sdk.core.datacenters.services.domain.DataCenters;
import com.centurylink.cloud.sdk.networks.AbstractNetworksSdkTest;
import com.google.inject.Inject;
import org.testng.annotations.Test;

import java.util.List;

public class NetworkServiceTest extends AbstractNetworksSdkTest {

    @Inject
    NetworkService networkService;

    @Test
    public void testFindNetworksByDataCenter() {
        List<NetworkMetadata> networks = networkService.findByDataCenter(DataCenters.CA_TORONTO_2);

        // TODO: for assertion need to implement allocation operation
    }

}