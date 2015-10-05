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
import com.centurylink.cloud.sdk.loadbalancer.services.client.domain.LoadBalancerRequest;
import com.centurylink.cloud.sdk.loadbalancer.services.dsl.domain.LoadBalancerMetadata;

import javax.ws.rs.core.GenericType;
import java.util.List;

import static javax.ws.rs.client.Entity.entity;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON_TYPE;

public class LoadBalancerClient extends AuthenticatedSdkHttpClient {

    public LoadBalancerClient(BearerAuthentication authFilter, SdkConfiguration config) {
        super(authFilter, config);
    }

    public List<LoadBalancerMetadata> getLoadBalancers(String dataCenterId) {
        List<LoadBalancerMetadata> metadataList =
            client("/sharedLoadBalancers/{accountAlias}/{dataCenterId}")
                .resolveTemplate("dataCenterId", dataCenterId)
                    .request()
                    .get(new GenericType<List<LoadBalancerMetadata>>(){});

        metadataList.forEach(metadata -> setAdditionalParams(metadata, dataCenterId));

        return metadataList;
    }

    public LoadBalancerMetadata getLoadBalancer(String dataCenterId, String loadBalancerId) {
        LoadBalancerMetadata metadata =
            client("/sharedLoadBalancers/{accountAlias}/{dataCenterId}/{loadBalancerId}")
                .resolveTemplate("dataCenterId", dataCenterId)
                .resolveTemplate("loadBalancerId", loadBalancerId)
                    .request()
                    .get(LoadBalancerMetadata.class);

        setAdditionalParams(metadata, dataCenterId);

        return metadata;
    }

    public LoadBalancerMetadata create(String dataCenterId, LoadBalancerRequest request) {
        LoadBalancerMetadata metadata =
            client("/sharedLoadBalancers/{accountAlias}/{dataCenterId}")
                .resolveTemplate("dataCenterId", dataCenterId)
                .request()
                .post(entity(request, APPLICATION_JSON_TYPE))
                .readEntity(LoadBalancerMetadata.class);

        setAdditionalParams(metadata, dataCenterId);

        return metadata;
    }

    public void delete(String dataCenterId, String loadBalancerId) {
        client("/sharedLoadBalancers/{accountAlias}/{dataCenterId}/{loadBalancerId}")
            .resolveTemplate("dataCenterId", dataCenterId)
            .resolveTemplate("loadBalancerId", loadBalancerId)
            .request()
            .delete();
    }

    public void update(String dataCenterId, String loadBalancerId, LoadBalancerRequest request) {
        client("/sharedLoadBalancers/{accountAlias}/{dataCenterId}/{loadBalancerId}")
            .resolveTemplate("dataCenterId", dataCenterId)
            .resolveTemplate("loadBalancerId", loadBalancerId)
            .request()
            .put(entity(request, APPLICATION_JSON_TYPE));
    }

    private void setAdditionalParams(LoadBalancerMetadata metadata, String dataCenterId) {
        metadata.setDataCenterId(dataCenterId);
    }

}
