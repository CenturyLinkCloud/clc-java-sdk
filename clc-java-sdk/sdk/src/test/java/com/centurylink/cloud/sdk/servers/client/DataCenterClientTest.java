package com.centurylink.cloud.sdk.servers.client;

import com.centurylink.cloud.sdk.core.datacenters.client.DataCentersClient;
import com.centurylink.cloud.sdk.servers.AbstractServersSdkTest;
import com.centurylink.cloud.sdk.core.datacenters.client.domain.GetDataCenterResponse;
import com.centurylink.cloud.sdk.core.datacenters.client.domain.deployment.capacilities.GetDeploymentCapabilitiesResponse;
import com.google.inject.Inject;
import org.testng.annotations.Test;

import static com.centurylink.cloud.sdk.core.datacenters.services.domain.datacenter.DataCenters.CA_VANCOUVER;
import static com.centurylink.cloud.sdk.core.datacenters.services.domain.datacenter.DataCenters.DE_FRANKFURT;

/**
 * @author ilya.drabenia
 */
public class DataCenterClientTest extends AbstractServersSdkTest {

    @Inject
    private DataCentersClient client;

    @Test
    public void getDataCenterTest() {
        GetDataCenterResponse result = client.getDataCenter(DE_FRANKFURT.getId());

        assert result.getId() != null;
    }

    @Test
    public void getDeploymentCapabilitiesTest() {
        GetDeploymentCapabilitiesResponse deployment =
                client.getDataCenterDeploymentCapabilities(CA_VANCOUVER.getId());

        assert deployment.getTemplates().size() > 0;
    }

}
