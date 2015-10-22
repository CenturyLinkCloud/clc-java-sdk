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
import com.centurylink.cloud.sdk.policy.services.client.domain.firewall.FirewallPolicyRequest;
import com.centurylink.cloud.sdk.policy.services.dsl.domain.firewall.FirewallPolicyMetadata;

import javax.ws.rs.core.GenericType;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static javax.ws.rs.client.Entity.entity;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON_TYPE;

public class FirewallPolicyClient extends AuthenticatedSdkHttpClient {
    private static final String DATACENTER_ID = "dataCenterId";
    private static final String POLICY_ID = "firewallPolicyId";

    public FirewallPolicyClient(BearerAuthentication authFilter, SdkConfiguration config) {
        super(authFilter, config);
    }

    public List<FirewallPolicyMetadata> getFirewallPolicies(String dataCenterId, String destinationAccountAlias) {
        List<FirewallPolicyMetadata> metadataList =
            experimentalClient("/firewallPolicies/{accountAlias}/{dataCenterId}")
                .resolveTemplate(DATACENTER_ID, dataCenterId)
                    .queryParam("destinationAccountAlias", destinationAccountAlias)
                    .request()
                    .get(new GenericType<List<FirewallPolicyMetadata>>() {
                    });

        metadataList.forEach(metadata -> addAdditionalParams(metadata, dataCenterId));

        return metadataList;
    }

    public FirewallPolicyMetadata getFirewallPolicy(String dataCenterId, String firewallPolicyId) {
        FirewallPolicyMetadata metadata =
            experimentalClient("/firewallPolicies/{accountAlias}/{dataCenterId}/{firewallPolicyId}")
                .resolveTemplate(DATACENTER_ID, dataCenterId)
                .resolveTemplate(POLICY_ID, firewallPolicyId)
                .request()
                .get(FirewallPolicyMetadata.class);

        addAdditionalParams(metadata, dataCenterId);

        return metadata;
    }

    public FirewallPolicyMetadata create(String dataCenterId, FirewallPolicyRequest request) {
        FirewallPolicyMetadata metadata =
            experimentalClient("/firewallPolicies/{accountAlias}/{dataCenterId}")
                .resolveTemplate(DATACENTER_ID, dataCenterId)
                .request()
                .post(entity(request, APPLICATION_JSON_TYPE))
                .readEntity(FirewallPolicyMetadata.class);

        addAdditionalParams(metadata, dataCenterId);

        return metadata;
    }

    public void update(String dataCenterId, String firewallPolicyId, FirewallPolicyRequest request) {
        experimentalClient("/firewallPolicies/{accountAlias}/{dataCenterId}/{firewallPolicyId}")
            .resolveTemplate(DATACENTER_ID, dataCenterId)
            .resolveTemplate(POLICY_ID, firewallPolicyId)
            .request()
            .put(entity(request, APPLICATION_JSON_TYPE));
    }

    public void delete(String dataCenterId, String firewallPolicyId) {
        experimentalClient("/firewallPolicies/{accountAlias}/{dataCenterId}/{firewallPolicyId}")
            .resolveTemplate(DATACENTER_ID, dataCenterId)
            .resolveTemplate(POLICY_ID, firewallPolicyId)
            .request()
            .delete();
    }

    private void addAdditionalParams(FirewallPolicyMetadata metadata, String dataCenterId) {
        metadata.setDataCenterId(dataCenterId);

        if (metadata.getId() == null && metadata.getLinks().size() != 0) {
            String href = metadata.getLinks().get(0).getHref();

            /* extract id from href */
            Matcher matcher = Pattern
                .compile(".*/([0-9a-f]*)")
                .matcher(href);

            if (matcher.find()) {
                metadata.setId(matcher.group(1));
            }
        }
    }

}
