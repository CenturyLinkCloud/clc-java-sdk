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

import com.centurylink.cloud.sdk.policy.services.AbstractPoliciesSdkTest;
import com.centurylink.cloud.sdk.policy.services.client.domain.AlertPolicyMetadata;
import com.centurylink.cloud.sdk.policy.services.client.domain.AlertTriggerMetadata;
import com.centurylink.cloud.sdk.policy.services.dsl.PolicyService;
import com.centurylink.cloud.sdk.policy.services.dsl.domain.AlertTriggerMetric;
import com.centurylink.cloud.sdk.policy.services.dsl.domain.filters.AlertPolicyFilter;
import com.centurylink.cloud.sdk.tests.recorded.WireMockMixin;
import org.testng.annotations.Test;

import javax.inject.Inject;
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
                    .where(ap -> ap.getTriggers().get(0).getMetric().equals(AlertTriggerMetric.CPU) &&
                        ap.getTriggers().get(0).getThreshold() > 20f)
            );

        policies.forEach(policy -> {
                AlertTriggerMetadata trigger = policy.getTriggers().get(0);
                assertTrue(trigger.getMetric().equals(AlertTriggerMetric.CPU.name().toLowerCase()));
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
