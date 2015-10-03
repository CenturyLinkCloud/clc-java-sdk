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

package com.centurylink.cloud.sdk.policy.services.dsl.antiaffinity.search;

import com.centurylink.cloud.sdk.base.services.dsl.domain.datacenters.refs.DataCenter;
import com.centurylink.cloud.sdk.core.injector.Inject;
import com.centurylink.cloud.sdk.policy.services.AbstractPoliciesSdkTest;
import com.centurylink.cloud.sdk.policy.services.client.domain.AntiAffinityPolicyMetadata;
import com.centurylink.cloud.sdk.policy.services.dsl.PolicyService;
import com.centurylink.cloud.sdk.policy.services.dsl.domain.filters.AntiAffinityPolicyFilter;
import com.centurylink.cloud.sdk.tests.recorded.WireMockMixin;
import org.testng.annotations.Test;

import java.util.List;

import static com.centurylink.cloud.sdk.tests.TestGroups.RECORDED;

/**
 * @author Aliaksandr Krasitski
 */
@Test(groups = {RECORDED})
public class SearchAntiAffinityPolicyTest extends AbstractPoliciesSdkTest implements WireMockMixin {

    @Inject
    PolicyService policyService;

    @Test
    public void testFindByDataCenter() {
        List<AntiAffinityPolicyMetadata> policies =
            policyService.antiAffinity().find(new AntiAffinityPolicyFilter().dataCenters(DataCenter.DE_FRANKFURT));

        policies.forEach(policy -> assertEquals(
            DataCenter.DE_FRANKFURT.getId().toUpperCase(),
            policy.getLocation().toUpperCase())
        );
    }

    @Test
    public void testFindByName() {
        List<AntiAffinityPolicyMetadata> policies =
            policyService.antiAffinity().find(new AntiAffinityPolicyFilter().nameContains("PoLiCy"));

        policies.forEach(policy -> assertTrue(
                policy.getName().toUpperCase().contains("PoLiCy".toUpperCase()))
        );
    }

    @Test
    public void testFindAllPolicies() {
        List<AntiAffinityPolicyMetadata> policies =
            policyService.antiAffinity().find(new AntiAffinityPolicyFilter());

        assert policies.size() > 0;
    }
}
