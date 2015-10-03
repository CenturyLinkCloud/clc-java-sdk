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

package com.centurylink.cloud.sdk.policy.services.dsl.antiaffinity.modify;

import com.centurylink.cloud.sdk.base.services.dsl.domain.datacenters.refs.DataCenter;
import com.centurylink.cloud.sdk.core.injector.Inject;
import com.centurylink.cloud.sdk.policy.services.AbstractPoliciesSdkTest;
import com.centurylink.cloud.sdk.policy.services.client.domain.AntiAffinityPolicyMetadata;
import com.centurylink.cloud.sdk.policy.services.dsl.PolicyService;
import com.centurylink.cloud.sdk.policy.services.dsl.domain.AntiAffinityPolicyConfig;
import com.centurylink.cloud.sdk.policy.services.dsl.domain.filters.AntiAffinityPolicyFilter;
import com.centurylink.cloud.sdk.policy.services.dsl.domain.refs.AntiAffinityPolicy;
import com.centurylink.cloud.sdk.tests.recorded.WireMockFileSource;
import com.centurylink.cloud.sdk.tests.recorded.WireMockMixin;
import org.testng.annotations.Test;

import static com.centurylink.cloud.sdk.tests.TestGroups.RECORDED;
import static com.google.common.base.Strings.isNullOrEmpty;

/**
 * @author Aliaksandr Krasitski
 */
@Test(groups = {RECORDED})
public class ModifyAntiAffinityPolicyTest extends AbstractPoliciesSdkTest implements WireMockMixin {
    @Inject
    PolicyService policyService;

    AntiAffinityPolicy policy;
    AntiAffinityPolicyFilter filter;

    @Test
    @WireMockFileSource("created")
    public void createPolicy() {
        String policyName = "My policy";

        policy =
            policyService.antiAffinity().create(
                new AntiAffinityPolicyConfig()
                    .name(policyName)
                    .dataCenter(DataCenter.CA_TORONTO_1)
                )
                .waitUntilComplete()
                .getResult();

        AntiAffinityPolicyMetadata metadata = policyService.antiAffinity().findByRef(policy);

        assert !isNullOrEmpty(metadata.getId());
        assertEquals(metadata.getName(), policyName);
        assertEquals(metadata.getLocation().toUpperCase(), DataCenter.CA_TORONTO_1.getId().toUpperCase());

        filter = policy.asFilter();
    }

    @Test
    @WireMockFileSource("updated")
    public void testModify() throws Exception {
        String newPolicyName = "New Policy Name";

        policyService.antiAffinity()
            .modify(filter, new AntiAffinityPolicyConfig().name(newPolicyName))
            .waitUntilComplete()
            .getResult();

        AntiAffinityPolicyMetadata metadata = policyService.antiAffinity().findByRef(policy);

        assertEquals(metadata.getName(), newPolicyName);

        deletePolicy();
    }

    private void deletePolicy() {
        policyService.antiAffinity().delete(policy.asFilter());
    }
}
