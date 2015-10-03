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

package com.centurylink.cloud.sdk.loadbalancer.services.client;

import com.centurylink.cloud.sdk.core.auth.services.BearerAuthentication;
import com.centurylink.cloud.sdk.core.client.AuthenticatedSdkHttpClient;
import com.centurylink.cloud.sdk.core.config.SdkConfiguration;
import com.centurylink.cloud.sdk.core.injector.Inject;
import com.centurylink.cloud.sdk.loadbalancer.services.client.domain.LoadBalancerPoolRequest;
import com.centurylink.cloud.sdk.loadbalancer.services.dsl.domain.LoadBalancerPoolMetadata;

import javax.ws.rs.core.GenericType;
import java.util.List;

import static javax.ws.rs.client.Entity.entity;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON_TYPE;

public class LoadBalancerPoolClient extends AuthenticatedSdkHttpClient {

    @Inject
    public LoadBalancerPoolClient(BearerAuthentication authFilter, SdkConfiguration config) {
        super(authFilter, config);
    }

    public List<LoadBalancerPoolMetadata> getLoadBalancerPools(String dataCenterId, String loadBalancerId) {
        List<LoadBalancerPoolMetadata> metadataList =
            client("/sharedLoadBalancers/{accountAlias}/{dataCenterId}/{loadBalancerId}/pools")
                .resolveTemplate("dataCenterId", dataCenterId)
                .resolveTemplate("loadBalancerId", loadBalancerId)
                .request()
                .get(new GenericType<List<LoadBalancerPoolMetadata>>(){});

        metadataList.forEach(metadata -> setAdditionalParams(metadata, dataCenterId, loadBalancerId));

        return metadataList;
    }

    public LoadBalancerPoolMetadata getLoadBalancerPool(String dataCenterId, String loadBalancerId, String poolId) {
        LoadBalancerPoolMetadata metadata =
            client("/sharedLoadBalancers/{accountAlias}/{dataCenterId}/{loadBalancerId}/pools/{poolId}")
                .resolveTemplate("dataCenterId", dataCenterId)
                .resolveTemplate("loadBalancerId", loadBalancerId)
                .resolveTemplate("poolId", poolId)
                .request()
                .get(LoadBalancerPoolMetadata.class);

        setAdditionalParams(metadata, dataCenterId, loadBalancerId);

        return metadata;
    }

    public LoadBalancerPoolMetadata create(String dataCenterId, String loadBalancerId, LoadBalancerPoolRequest request) {
        LoadBalancerPoolMetadata metadata =
                client("/sharedLoadBalancers/{accountAlias}/{dataCenterId}/{loadBalancerId}/pools")
                    .resolveTemplate("dataCenterId", dataCenterId)
                    .resolveTemplate("loadBalancerId", loadBalancerId)
                    .request()
                    .post(entity(request, APPLICATION_JSON_TYPE))
                    .readEntity(LoadBalancerPoolMetadata.class);

        setAdditionalParams(metadata, dataCenterId, loadBalancerId);

        return metadata;
    }

    public void delete(String dataCenterId, String loadBalancerId, String loadBalancerPoolId) {
        client("/sharedLoadBalancers/{accountAlias}/{dataCenterId}/{loadBalancerId}/pools/{loadBalancerPoolId}")
            .resolveTemplate("dataCenterId", dataCenterId)
            .resolveTemplate("loadBalancerId", loadBalancerId)
            .resolveTemplate("loadBalancerPoolId", loadBalancerPoolId)
            .request()
            .delete();
    }

    public void update(
            String dataCenterId,
            String loadBalancerId,
            String loadBalancerPoolId,
            LoadBalancerPoolRequest request
    ) {
        client("/sharedLoadBalancers/{accountAlias}/{dataCenterId}/{loadBalancerId}/pools/{loadBalancerPoolId}")
                .resolveTemplate("dataCenterId", dataCenterId)
                .resolveTemplate("loadBalancerId", loadBalancerId)
                .resolveTemplate("loadBalancerPoolId", loadBalancerPoolId)
                .request()
                .put(entity(request, APPLICATION_JSON_TYPE));
    }

    private void setAdditionalParams(LoadBalancerPoolMetadata metadata, String dataCenterId, String loadBalancerId) {
        metadata.setDataCenterId(dataCenterId);
        metadata.setLoadBalancerId(loadBalancerId);
    }

}
