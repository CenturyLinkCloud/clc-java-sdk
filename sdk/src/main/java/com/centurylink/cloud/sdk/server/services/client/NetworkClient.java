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

import com.centurylink.cloud.sdk.core.auth.services.BearerAuthentication;
import com.centurylink.cloud.sdk.core.client.AuthenticatedSdkHttpClient;
import com.centurylink.cloud.sdk.core.client.domain.NetworkLink;
import com.centurylink.cloud.sdk.core.config.SdkConfiguration;
import com.centurylink.cloud.sdk.server.services.client.domain.network.NetworkMetadata;
import com.centurylink.cloud.sdk.server.services.client.domain.network.UpdateNetworkRequest;

import javax.ws.rs.core.GenericType;
import java.util.HashMap;
import java.util.List;

import static javax.ws.rs.client.Entity.entity;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON_TYPE;

public class NetworkClient extends AuthenticatedSdkHttpClient {

    private static final String DATACENTER_ID = "dataCenterId";

    public NetworkClient(BearerAuthentication authFilter, SdkConfiguration config) {
        super(authFilter, config);
    }

    public List<NetworkMetadata> getNetworks(String dataCenterId) {
        List<NetworkMetadata> metadataList =
            experimentalClient("/networks/{accountAlias}/{dataCenterId}")
                .resolveTemplate(DATACENTER_ID, dataCenterId)
                .request()
                .get(new GenericType<List<NetworkMetadata>>() {});

        metadataList.forEach(metadata -> setDatacenter(metadata, dataCenterId));

        return metadataList;
    }

    public NetworkLink claim(String dataCenterId) {
        return
            experimentalClient("/networks/{accountAlias}/{dataCenterId}/claim")
                .resolveTemplate(DATACENTER_ID, dataCenterId)
                .request()
                .post(entity(new HashMap<>(), APPLICATION_JSON_TYPE))
                .readEntity(NetworkLink.class);
    }

    public void release(String networkId, String dataCenterId) {
        experimentalClient("/networks/{accountAlias}/{dataCenterId}/{networkId}/release")
            .resolveTemplate(DATACENTER_ID, dataCenterId)
            .resolveTemplate("networkId", networkId)
            .request()
            .post(entity(new HashMap<>(), APPLICATION_JSON_TYPE));
    }

    public void update(String dataCenterId, String networkId, UpdateNetworkRequest request) {
        experimentalClient("/networks/{accountAlias}/{dataCenterId}/{networkId}")
            .resolveTemplate(DATACENTER_ID, dataCenterId)
            .resolveTemplate("networkId", networkId)
            .request()
            .put(entity(request, APPLICATION_JSON_TYPE));
    }

    private void setDatacenter(NetworkMetadata metadata, String dataCenterId) {
        metadata.setDataCenterId(dataCenterId);
    }
}