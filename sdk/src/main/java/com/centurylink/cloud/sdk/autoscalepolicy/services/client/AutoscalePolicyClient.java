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

package com.centurylink.cloud.sdk.autoscalepolicy.services.client;

import com.centurylink.cloud.sdk.autoscalepolicy.services.client.domain.SetAutoscalePolicyRequest;
import com.centurylink.cloud.sdk.autoscalepolicy.services.dsl.domain.AutoscalePolicyMetadata;
import com.centurylink.cloud.sdk.core.auth.services.BearerAuthentication;
import com.centurylink.cloud.sdk.core.client.AuthenticatedSdkHttpClient;
import com.centurylink.cloud.sdk.core.client.errors.ClcHttpClientException;
import com.centurylink.cloud.sdk.core.config.SdkConfiguration;

import javax.ws.rs.core.GenericType;
import java.util.List;

import static javax.ws.rs.client.Entity.entity;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON_TYPE;

public class AutoscalePolicyClient extends AuthenticatedSdkHttpClient {

    public AutoscalePolicyClient(BearerAuthentication authFilter, SdkConfiguration config) {
        super(authFilter, config);
    }

    public List<AutoscalePolicyMetadata> getAutoscalePolicies() {
        return
            client("/autoscalePolicies/{accountAlias}")
                .request()
                .get(new GenericType<List<AutoscalePolicyMetadata>>() {});
    }

    public AutoscalePolicyMetadata getAutoscalePolicy(String policyId) {
        return
            client("/autoscalePolicies/{accountAlias}/{policyId}")
                .resolveTemplate("policyId", policyId)
                .request()
                .get(AutoscalePolicyMetadata.class);
    }

    public void setAutoscalePolicyOnServer(String serverId, SetAutoscalePolicyRequest request) {
        client("/servers/{accountAlias}/{serverId}/cpuAutoscalePolicy")
            .resolveTemplate("serverId", serverId)
            .request()
            .put(entity(request, APPLICATION_JSON_TYPE));
    }

    public AutoscalePolicyMetadata getAutoscalePolicyOnServer(String serverId) {
        try {
            AutoscalePolicyMetadata metadata = client("/servers/{accountAlias}/{serverId}/cpuAutoscalePolicy")
                .resolveTemplate("serverId", serverId)
                .request()
                .get(AutoscalePolicyMetadata.class);

            if (metadata != null && metadata.getId() != null) {
                metadata = getAutoscalePolicy(metadata.getId());
            }

            return metadata;
        } catch (ClcHttpClientException e) {
            return null;
        }
    }

    public void removeAutoscalePolicyOnServer(String serverId) {
        client("/servers/{accountAlias}/{serverId}/cpuAutoscalePolicy")
            .resolveTemplate("serverId", serverId)
            .request()
            .delete();
    }

}
