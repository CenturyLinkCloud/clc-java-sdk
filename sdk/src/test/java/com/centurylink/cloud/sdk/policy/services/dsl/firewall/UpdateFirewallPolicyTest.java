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

import com.centurylink.cloud.sdk.policy.services.AbstractFirewallPolicySdkTest;
import com.centurylink.cloud.sdk.policy.services.dsl.domain.firewall.FirewallPolicyConfig;
import com.centurylink.cloud.sdk.policy.services.dsl.domain.firewall.FirewallPolicyMetadata;
import com.centurylink.cloud.sdk.policy.services.dsl.domain.firewall.refs.FirewallPolicy;
import com.centurylink.cloud.sdk.tests.recorded.WireMockFileSource;
import com.centurylink.cloud.sdk.tests.recorded.WireMockMixin;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

import static com.centurylink.cloud.sdk.tests.TestGroups.RECORDED;

@Test(groups = {RECORDED})
@WireMockFileSource("update")
public class UpdateFirewallPolicyTest extends AbstractFirewallPolicySdkTest implements WireMockMixin {

    private FirewallPolicy firewallPolicy;

    @Test
    public void testUpdate() {

        firewallPolicy = createFirewallPolicy();

        String port = "udp/8080";
        String destination = "10.110.37.12/32";
        String source1 = "10.110.37.12/32";
        String source2 = "10.110.37.13/32";

        List<String> sourceList = new ArrayList<>();
        sourceList.add(source1);
        sourceList.add(source2);

        List<String> destinationList = new ArrayList<>();
        destinationList.add(destination);

        List<String> portList = new ArrayList<>();
        portList.add(port);

        firewallPolicyService
            .update(
                firewallPolicy,
                new FirewallPolicyConfig()
                    .enabled(false)
                    .source(sourceList)
                    .destination(destinationList)
                    .ports(portList)
            )
            .waitUntilComplete()
            .getResult();

        FirewallPolicyMetadata metadata = firewallPolicyService.findByRef(firewallPolicy);

        assertNotNull(metadata);
        assertFalse(metadata.isEnabled());
        assertEquals(metadata.getPorts().size(), 1);
        assertEquals(metadata.getPorts().get(0), port);
        assertEquals(metadata.getSource().size(), 2);
        assertEquals(metadata.getSource().get(0), source1);
        assertEquals(metadata.getSource().get(1), source2);
        assertEquals(metadata.getDestination().size(), 1);
        assertEquals(metadata.getDestination().get(0), destination);
    }

    @AfterMethod
    public void deleteBalancer() {
        firewallPolicyService.delete(firewallPolicy);
    }
}
