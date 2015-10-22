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

package com.centurylink.cloud.sdk.policy.services.dsl.autoscale;

import com.centurylink.cloud.sdk.policy.services.AbstractAutoscalePolicySdkTest;
import com.centurylink.cloud.sdk.policy.services.dsl.domain.autoscale.AutoscalePolicyMetadata;
import com.centurylink.cloud.sdk.policy.services.dsl.domain.autoscale.refs.AutoscalePolicy;
import com.centurylink.cloud.sdk.server.services.dsl.domain.server.refs.Server;
import com.centurylink.cloud.sdk.tests.recorded.WireMockFileSource;
import com.centurylink.cloud.sdk.tests.recorded.WireMockMixin;
import org.testng.annotations.Test;

import static com.centurylink.cloud.sdk.tests.TestGroups.RECORDED;

@Test(groups = {RECORDED})
@WireMockFileSource("set")
public class SetAutoscalePolicyTest extends AbstractAutoscalePolicySdkTest implements WireMockMixin {

    @Test
    public void testSetPolicy() {
        String policyId = "02c9a0551e494c0fa6ede693268c0216";
        String serverId = "gb1altdsrv101";

        AutoscalePolicy autoscalePolicy = AutoscalePolicy.refById(policyId);
        Server server = Server.refById(serverId);

        autoscalePolicyService.setAutoscalePolicyOnServer(autoscalePolicy, server.asFilter());

        AutoscalePolicyMetadata metadata = autoscalePolicyService.getAutoscalePolicyOnServer(server);

        assertNotNull(metadata);
        assertEquals(metadata.getId(), policyId);
    }
}
