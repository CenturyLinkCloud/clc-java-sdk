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

package com.centurylink.cloud.sdk.policy.services.dsl.firewall;

import com.centurylink.cloud.sdk.base.services.dsl.domain.datacenters.refs.DataCenter;
import com.centurylink.cloud.sdk.policy.services.AbstractFirewallPolicySdkTest;
import com.centurylink.cloud.sdk.policy.services.dsl.domain.firewall.FirewallPolicyMetadata;
import com.centurylink.cloud.sdk.policy.services.dsl.domain.firewall.filter.FirewallPolicyFilter;
import com.centurylink.cloud.sdk.policy.services.dsl.domain.firewall.refs.FirewallPolicy;
import com.centurylink.cloud.sdk.tests.recorded.WireMockFileSource;
import com.centurylink.cloud.sdk.tests.recorded.WireMockMixin;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

import static com.centurylink.cloud.sdk.tests.TestGroups.RECORDED;

@Test(groups = {RECORDED})
@WireMockFileSource("search")
public class SearchFirewallPolicyTest extends AbstractFirewallPolicySdkTest implements WireMockMixin {

    private List<FirewallPolicy> firewallPolicyList = new ArrayList<>();

    @Test
    public void testSearchFirewallPolicies() {
        firewallPolicyList.add(createFirewallPolicy());
        firewallPolicyList.add(createFirewallPolicy());

        List<FirewallPolicyMetadata> firewallPolicyMetadataList = firewallPolicyService.find(
                new FirewallPolicyFilter().dataCenters(DataCenter.DE_FRANKFURT)
        );

        assertNotNull(firewallPolicyMetadataList);
        assertEquals(firewallPolicyMetadataList.size(), 2);
        assertEquals(firewallPolicyMetadataList.get(0).getDataCenterId(), DataCenter.DE_FRANKFURT.getId());
        assertEquals(firewallPolicyMetadataList.get(1).getDataCenterId(), DataCenter.DE_FRANKFURT.getId());

        FirewallPolicyMetadata firewallPolicy1Metadata = firewallPolicyService.findByRef(firewallPolicyList.get(0));

        assertNotNull(firewallPolicy1Metadata);
        assertEquals(firewallPolicy1Metadata.getDataCenterId(), DataCenter.DE_FRANKFURT.getId());
    }

    @AfterMethod
    public void deleteFirewallPolicies() {
        firewallPolicyService.delete(firewallPolicyList);
    }
}
