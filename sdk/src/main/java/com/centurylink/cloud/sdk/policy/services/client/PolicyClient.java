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

package com.centurylink.cloud.sdk.policy.services.client;

import com.centurylink.cloud.sdk.core.auth.services.BearerAuthentication;
import com.centurylink.cloud.sdk.core.client.AuthenticatedSdkHttpClient;
import com.centurylink.cloud.sdk.core.config.SdkConfiguration;
import com.centurylink.cloud.sdk.policy.services.client.domain.AntiAffinityPoliciesResponse;
import com.centurylink.cloud.sdk.policy.services.client.domain.AntiAffinityPolicyMetadata;
import com.centurylink.cloud.sdk.policy.services.client.domain.AntiAffinityPolicyRequest;
import com.google.inject.Inject;

import java.util.List;

import static javax.ws.rs.client.Entity.entity;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON_TYPE;

/**
 * @author Aliaksandr Krasitski
 */
public class PolicyClient extends AuthenticatedSdkHttpClient {

    @Inject
    public PolicyClient(BearerAuthentication authFilter, SdkConfiguration config) {
        super(authFilter, config);
    }

    public AntiAffinityPolicyMetadata createAntiAffinityPolicy(AntiAffinityPolicyRequest policyRequest) {
        return
            client("/antiAffinityPolicies/{accountAlias}")
                .request()
                .post(entity(policyRequest, APPLICATION_JSON_TYPE))
                .readEntity(AntiAffinityPolicyMetadata.class);
    }

    public AntiAffinityPolicyMetadata modifyAntiAffinityPolicy(String policyId, AntiAffinityPolicyRequest policyRequest) {
        return
            client("/antiAffinityPolicies/{accountAlias}/{policyId}")
                .resolveTemplate("policyId", policyId)
                .request()
                .put(entity(policyRequest, APPLICATION_JSON_TYPE))
                .readEntity(AntiAffinityPolicyMetadata.class);
    }

    public AntiAffinityPolicyMetadata getAntiAffinityPolicy(String policyId) {
        return
            client("/antiAffinityPolicies/{accountAlias}/{policyId}")
                .resolveTemplate("policyId", policyId)
                .request()
                .get(AntiAffinityPolicyMetadata.class);
    }

    public List<AntiAffinityPolicyMetadata> getAntiAffinityPolicies() {
        return
            client("/antiAffinityPolicies/{accountAlias}")
                .request()
                .get(AntiAffinityPoliciesResponse.class)
                .getItems();
    }

    public void deleteAntiAffinityPolicy(String policyId) {
        client("/antiAffinityPolicies/{accountAlias}/{policyId}")
            .resolveTemplate("policyId", policyId)
            .request()
            .delete();
    }
}