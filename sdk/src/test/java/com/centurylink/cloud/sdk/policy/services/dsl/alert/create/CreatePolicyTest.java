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

package com.centurylink.cloud.sdk.policy.services.dsl.alert.create;

import com.centurylink.cloud.sdk.core.injector.Inject;
import com.centurylink.cloud.sdk.policy.services.AbstractPoliciesSdkTest;
import com.centurylink.cloud.sdk.policy.services.client.domain.ActionSettingsEmailMetadata;
import com.centurylink.cloud.sdk.policy.services.client.domain.AlertActionMetadata;
import com.centurylink.cloud.sdk.policy.services.client.domain.AlertPolicyMetadata;
import com.centurylink.cloud.sdk.policy.services.client.domain.AlertTriggerMetadata;
import com.centurylink.cloud.sdk.policy.services.dsl.PolicyService;
import com.centurylink.cloud.sdk.policy.services.dsl.domain.ActionSettingsEmail;
import com.centurylink.cloud.sdk.policy.services.dsl.domain.AlertAction;
import com.centurylink.cloud.sdk.policy.services.dsl.domain.AlertPolicyConfig;
import com.centurylink.cloud.sdk.policy.services.dsl.domain.AlertTrigger;
import com.centurylink.cloud.sdk.policy.services.dsl.domain.AlertTriggerMetric;
import com.centurylink.cloud.sdk.policy.services.dsl.domain.refs.AlertPolicy;
import com.centurylink.cloud.sdk.tests.recorded.WireMockMixin;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

import java.util.List;

import static com.centurylink.cloud.sdk.tests.TestGroups.RECORDED;
import static com.centurylink.cloud.sdk.core.util.Strings.isNullOrEmpty;

/**
 * @author Aliaksandr Krasitski
 */
@Test(groups = {RECORDED})
public class CreatePolicyTest extends AbstractPoliciesSdkTest implements WireMockMixin {

    @Inject
    PolicyService policyService;

    AlertPolicy policy;

    @Test
    public void testCreate() throws Exception {
        String policyName = "My policy";
        String recipient = "user@company.com";

        policy =
            policyService.alert().create(
                new AlertPolicyConfig()
                    .name(policyName)
                    .action(new AlertAction()
                        .settings(new ActionSettingsEmail(recipient)))
                    .triggers(
                        new AlertTrigger().duration(5).metric(AlertTriggerMetric.CPU).threshold(52.0f)
                    )
                )
                .waitUntilComplete()
                .getResult();

        AlertPolicyMetadata metadata = policyService.alert().findByRef(policy);

        assert !isNullOrEmpty(metadata.getId());
        assertEquals(metadata.getName(), policyName);

        AlertActionMetadata action = metadata.getActions().get(0);
        assertEquals(action.getAction(), "email");
        assertEquals(((ActionSettingsEmailMetadata)action.getSettings()).getRecipients().get(0), recipient);

        List<AlertTriggerMetadata> triggers = metadata.getTriggers();


        assertEquals(triggers.size(), 1);

        assertEquals(triggers.get(0).getDuration(), "00:05:00");
        assertEquals(triggers.get(0).getThreshold(), 50.0f);

    }

    @AfterMethod
    public void deletePolicy() {
        policyService.alert().delete(policy.asFilter());
    }

}

