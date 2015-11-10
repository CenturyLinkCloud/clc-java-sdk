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
import com.centurylink.cloud.sdk.policy.services.client.domain.AlertPolicyMetadata;
import com.centurylink.cloud.sdk.policy.services.client.domain.AlertPolicyRequest;
import com.centurylink.cloud.sdk.policy.services.client.domain.AntiAffinityPolicyMetadata;
import com.centurylink.cloud.sdk.policy.services.client.domain.AntiAffinityPolicyRequest;
import com.centurylink.cloud.sdk.policy.services.client.domain.PolicyListResponse;

import javax.ws.rs.core.GenericType;
import java.util.List;

import static javax.ws.rs.client.Entity.entity;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON_TYPE;

/**
 * @author Aliaksandr Krasitski
 */
public class PolicyClient extends AuthenticatedSdkHttpClient {
    private static final String POLICY_ID = "policyId";
    private static final String ANTI_AFFINITY_POLICY_URL = "/antiAffinityPolicies/{accountAlias}";
    private static final String ANTI_AFFINITY_POLICY_WITH_ID_URL = ANTI_AFFINITY_POLICY_URL + "/{policyId}";
    private static final String ALERT_POLICY_URL = "/alertPolicies/{accountAlias}";
    private static final String ALERT_POLICY_WITH_ID_URL = ALERT_POLICY_URL + "/{policyId}";

    public PolicyClient(BearerAuthentication authFilter, SdkConfiguration config) {
        super(authFilter, config);
    }

    public AntiAffinityPolicyMetadata createAntiAffinityPolicy(AntiAffinityPolicyRequest policyRequest) {
        return
            client(ANTI_AFFINITY_POLICY_URL)
                .request()
                .post(entity(policyRequest, APPLICATION_JSON_TYPE))
                .readEntity(AntiAffinityPolicyMetadata.class);
    }

    public AntiAffinityPolicyMetadata modifyAntiAffinityPolicy(String policyId, AntiAffinityPolicyRequest policyRequest) {
        return
            client(ANTI_AFFINITY_POLICY_WITH_ID_URL)
                .resolveTemplate(POLICY_ID, policyId)
                .request()
                .put(entity(policyRequest, APPLICATION_JSON_TYPE))
                .readEntity(AntiAffinityPolicyMetadata.class);
    }

    public AntiAffinityPolicyMetadata getAntiAffinityPolicy(String policyId) {
        return
            client(ANTI_AFFINITY_POLICY_WITH_ID_URL)
                .resolveTemplate(POLICY_ID, policyId)
                .request()
                .get(AntiAffinityPolicyMetadata.class);
    }

    public List<AntiAffinityPolicyMetadata> getAntiAffinityPolicies() {
        return
            client(ANTI_AFFINITY_POLICY_URL)
                .request()
                .get(new GenericType<PolicyListResponse<AntiAffinityPolicyMetadata>>() {})
                .getItems();
    }

    public void deleteAntiAffinityPolicy(String policyId) {
        client(ANTI_AFFINITY_POLICY_WITH_ID_URL)
            .resolveTemplate(POLICY_ID, policyId)
            .request()
            .delete();
    }


    public AlertPolicyMetadata createAlertPolicy(AlertPolicyRequest policyRequest) {
        return
            client(ALERT_POLICY_URL)
                .request()
                .post(entity(policyRequest, APPLICATION_JSON_TYPE))
                .readEntity(AlertPolicyMetadata.class);
    }

    public AlertPolicyMetadata modifyAlertPolicy(String policyId, AlertPolicyRequest policyRequest) {
        return
            client(ALERT_POLICY_WITH_ID_URL)
                .resolveTemplate(POLICY_ID, policyId)
                .request()
                .put(entity(policyRequest, APPLICATION_JSON_TYPE))
                .readEntity(AlertPolicyMetadata.class);
    }

    public AlertPolicyMetadata getAlertPolicy(String policyId) {
        return
            client(ALERT_POLICY_WITH_ID_URL)
                .resolveTemplate(POLICY_ID, policyId)
                .request()
                .get(AlertPolicyMetadata.class);
    }

    public List<AlertPolicyMetadata> getAlertPolicies() {
        return
            client(ALERT_POLICY_URL)
                .request()
                .get(new GenericType<PolicyListResponse<AlertPolicyMetadata>>() {})
                .getItems();
    }

    public void deleteAlertPolicy(String policyId) {
        client(ALERT_POLICY_WITH_ID_URL)
            .resolveTemplate(POLICY_ID, policyId)
            .request()
            .delete();
    }
}