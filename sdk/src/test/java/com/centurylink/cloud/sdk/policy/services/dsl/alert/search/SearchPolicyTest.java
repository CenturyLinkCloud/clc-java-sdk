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

package com.centurylink.cloud.sdk.policy.services.dsl.alert.search;

import com.centurylink.cloud.sdk.core.injector.Inject;
import com.centurylink.cloud.sdk.policy.services.AbstractPoliciesSdkTest;
import com.centurylink.cloud.sdk.policy.services.client.domain.AlertPolicyMetadata;
import com.centurylink.cloud.sdk.policy.services.client.domain.AlertTriggerMetadata;
import com.centurylink.cloud.sdk.policy.services.dsl.PolicyService;
import com.centurylink.cloud.sdk.policy.services.dsl.domain.AlertTriggerMetric;
import com.centurylink.cloud.sdk.policy.services.dsl.domain.filters.AlertPolicyFilter;
import com.centurylink.cloud.sdk.policy.services.dsl.domain.refs.AlertPolicy;
import com.centurylink.cloud.sdk.tests.recorded.WireMockMixin;
import org.testng.annotations.Test;

import java.util.List;

import static com.centurylink.cloud.sdk.tests.TestGroups.RECORDED;

/**
 * @author Aliaksandr Krasitski
 */
@Test(groups = {RECORDED})
public class SearchPolicyTest extends AbstractPoliciesSdkTest implements WireMockMixin {

    @Inject
    PolicyService policyService;

    @Test
    public void testFindByRef() {
        String policyName = "New Policy Name";

        AlertPolicyMetadata policy = policyService.alert().findByRef(AlertPolicy.refByName(policyName));

        assertEquals(policy.getName(), policyName);


        String id = "953586c2bd604848ab1947e8debec72c";

        policy = policyService.alert().findByRef(AlertPolicy.refById(id));

        assertEquals(policy.getId(), id);
    }

    @Test
    public void testFindByName() {
        List<AlertPolicyMetadata> policies =
            policyService.alert().find(new AlertPolicyFilter().nameContains("PoLiCy"));

        policies.forEach(policy -> assertTrue(
                policy.getName().toUpperCase().contains("PoLiCy".toUpperCase()))
        );
    }

    @Test
    public void testFindByPredicate() {
        List<AlertPolicyMetadata> policies =
            policyService.alert().find(
                new AlertPolicyFilter()
                    .where(ap -> ap.getTriggers().get(0).getMetric().equals(AlertTriggerMetric.CPU.name()) &&
                        ap.getTriggers().get(0).getThreshold() > 20f)
            );

        policies.forEach(policy -> {
                AlertTriggerMetadata trigger = policy.getTriggers().get(0);
                assertTrue(trigger.getMetric().equalsIgnoreCase(AlertTriggerMetric.CPU.name()));
                assertTrue(trigger.getThreshold() > 20f);
            }
        );
    }

    @Test
    public void testFindAllPolicies() {
        List<AlertPolicyMetadata> policies =
            policyService.alert().find(new AlertPolicyFilter());

        assert policies.size() > 0;
    }
}
