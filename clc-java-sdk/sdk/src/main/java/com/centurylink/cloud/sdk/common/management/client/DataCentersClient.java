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

package com.centurylink.cloud.sdk.common.management.client;

import com.centurylink.cloud.sdk.common.management.client.domain.datacenters.DataCenterMetadata;
import com.centurylink.cloud.sdk.common.management.client.domain.datacenters.GetDataCenterListResponse;
import com.centurylink.cloud.sdk.common.management.client.domain.datacenters.deployment.capabilities.DatacenterDeploymentCapabilitiesMetadata;
import com.centurylink.cloud.sdk.core.auth.services.BearerAuthentication;
import com.centurylink.cloud.sdk.core.client.AuthenticatedSdkHttpClient;
import com.centurylink.cloud.sdk.core.config.SdkConfiguration;
import com.google.inject.Inject;

/**
 * @author ilya.drabenia
 */
public class DataCentersClient extends AuthenticatedSdkHttpClient {

    @Inject
    public DataCentersClient(BearerAuthentication authFilter, SdkConfiguration config) {
        super(authFilter, config);
    }

    public DatacenterDeploymentCapabilitiesMetadata getDeploymentCapabilities(String dataCenterId) {
        return
            client("/datacenters/{accountAlias}/{dataCenterId}/deploymentCapabilities")
                .resolveTemplate("dataCenterId", dataCenterId)
                .request().get(DatacenterDeploymentCapabilitiesMetadata.class);
    }

    public DataCenterMetadata getDataCenter(String dataCenterId) {
        return
            client("/datacenters/{accountAlias}/{dataCenterId}")
                .queryParam("groupLinks", true)
                .resolveTemplate("dataCenterId", dataCenterId)
                .request().get(DataCenterMetadata.class);
    }

    // TODO: need to implement memoization of this method with acceptable expiration time
    public GetDataCenterListResponse findAllDataCenters() {
        return
            client("/datacenters/{accountAlias}?groupLinks=true")
                .request()
                .get(GetDataCenterListResponse.class);
    }

}
