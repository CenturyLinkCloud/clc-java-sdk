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

package com.centurylink.cloud.sdk.network.services.dsl;

import com.centurylink.cloud.sdk.base.services.client.DataCentersClient;
import com.centurylink.cloud.sdk.base.services.client.domain.datacenters.deployment.capabilities.NetworkMetadata;
import com.centurylink.cloud.sdk.base.services.dsl.DataCenterService;
import com.centurylink.cloud.sdk.base.services.dsl.domain.datacenters.refs.DataCenter;
import com.centurylink.cloud.sdk.core.exceptions.ReferenceNotSupportedException;
import com.centurylink.cloud.sdk.network.services.dsl.domain.refs.IdNetworkRef;
import com.centurylink.cloud.sdk.network.services.dsl.domain.refs.NetworkRef;
import com.google.inject.Inject;

import java.util.List;

/**
 * @author ilya.drabenia
 */
public class NetworkService {
    private final DataCentersClient dataCentersClient;
    private final DataCenterService dataCentersService;

    @Inject
    public NetworkService(DataCentersClient dataCentersClient, DataCenterService dataCentersService) {
        this.dataCentersClient = dataCentersClient;
        this.dataCentersService = dataCentersService;
    }

    public NetworkMetadata findByRef(NetworkRef networkRef) {
        if (networkRef.is(IdNetworkRef.class)) {
            return
                dataCentersClient
                    .getDeploymentCapabilities(dataCenterId(networkRef.getDataCenter()))
                    .findNetworkById(networkRef.as(IdNetworkRef.class).getId());
        } else {
            throw new ReferenceNotSupportedException(networkRef.getClass());
        }
    }

    public List<NetworkMetadata> findByDataCenter(DataCenter dataCenter) {
        return
            dataCentersClient
                .getDeploymentCapabilities(dataCenterId(dataCenter))
                .getDeployableNetworks();
    }

    private String dataCenterId(DataCenter dataCenter) {
        return dataCentersService
            .findByRef(dataCenter)
            .getId();
    }
}
