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

package com.centurylink.cloud.sdk.firewallpolicy.services.dsl;

import com.centurylink.cloud.sdk.base.services.dsl.domain.datacenters.refs.DataCenter;
import com.centurylink.cloud.sdk.firewallpolicy.services.AbstractFirewallPolicySdkTest;
import com.centurylink.cloud.sdk.firewallpolicy.services.dsl.domain.FirewallPolicyMetadata;
import com.centurylink.cloud.sdk.firewallpolicy.services.dsl.domain.refs.FirewallPolicy;
import com.centurylink.cloud.sdk.tests.recorded.WireMockFileSource;
import com.centurylink.cloud.sdk.tests.recorded.WireMockMixin;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

import static com.centurylink.cloud.sdk.tests.TestGroups.RECORDED;

@Test(groups = {RECORDED})
@WireMockFileSource("create")
public class CreateFirewallPolicyTest extends AbstractFirewallPolicySdkTest implements WireMockMixin{

    private FirewallPolicy firewallPolicy;

    @Test
    public void testCreate() {
        firewallPolicy = createFirewallPolicy();

        FirewallPolicyMetadata metadata = firewallPolicyService.findByRef(firewallPolicy);

        assertNotNull(metadata);
        assertTrue(metadata.isEnabled());
        assertEquals(metadata.getPorts().size(), 1);
        assertEquals(metadata.getSource().size(), 3);
        assertEquals(metadata.getDestination().size(), 2);
        assertEquals(metadata.getDataCenterId(), DataCenter.DE_FRANKFURT.getId());
        assertEquals(authentication.getAccountAlias().toLowerCase(), metadata.getDestinationAccount());
    }

    @AfterMethod
    public void deleteBalancer() {
        firewallPolicyService.delete(firewallPolicy);
    }
}
