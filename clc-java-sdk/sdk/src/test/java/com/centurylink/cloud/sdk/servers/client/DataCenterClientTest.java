package com.centurylink.cloud.sdk.servers.client;

import com.centurylink.cloud.sdk.commons.client.DataCentersClient;
import com.centurylink.cloud.sdk.commons.client.domain.datacenters.DataCenterMetadata;
import com.centurylink.cloud.sdk.commons.client.domain.datacenters.deployment.capabilities.DatacenterDeploymentCapabilitiesMetadata;
import com.centurylink.cloud.sdk.servers.AbstractServersSdkTest;
import com.google.inject.Inject;
import org.testng.annotations.Test;

import static com.centurylink.cloud.sdk.tests.TestGroups.INTEGRATION;
import static com.centurylink.cloud.sdk.commons.services.domain.datacenters.refs.DataCenter.CA_VANCOUVER;
import static com.centurylink.cloud.sdk.commons.services.domain.datacenters.refs.DataCenter.DE_FRANKFURT;

/**
 * @author ilya.drabenia
 */
@Test(groups = INTEGRATION)
public class DataCenterClientTest extends AbstractServersSdkTest {

    @Inject
    private DataCentersClient client;

    @Test
    public void getDataCenterTest() {
        DataCenterMetadata result = client.getDataCenter(DE_FRANKFURT.getId());

        assert result.getId() != null;
    }

    @Test
    public void getDeploymentCapabilitiesTest() {
        DatacenterDeploymentCapabilitiesMetadata deployment =
                client.getDeploymentCapabilities(CA_VANCOUVER.getId());

        assert deployment.getTemplates().size() > 0;
    }

}
