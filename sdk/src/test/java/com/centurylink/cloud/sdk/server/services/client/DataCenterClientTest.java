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

package com.centurylink.cloud.sdk.server.services.client;

import com.centurylink.cloud.sdk.base.services.client.DataCentersClient;
import com.centurylink.cloud.sdk.base.services.client.domain.datacenters.DataCenterMetadata;
import com.centurylink.cloud.sdk.base.services.client.domain.datacenters.deployment.capabilities.DatacenterDeploymentCapabilitiesMetadata;
import com.centurylink.cloud.sdk.server.services.AbstractServersSdkTest;
import com.google.inject.Inject;
import org.testng.annotations.Test;

import static com.centurylink.cloud.sdk.base.services.dsl.domain.datacenters.refs.DataCenter.CA_VANCOUVER;
import static com.centurylink.cloud.sdk.base.services.dsl.domain.datacenters.refs.DataCenter.DE_FRANKFURT;
import static com.centurylink.cloud.sdk.tests.TestGroups.INTEGRATION;

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
