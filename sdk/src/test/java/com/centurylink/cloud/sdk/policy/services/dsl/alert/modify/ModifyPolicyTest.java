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

package com.centurylink.cloud.sdk.policy.services.dsl.alert.modify;

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
import com.centurylink.cloud.sdk.policy.services.dsl.domain.filters.AlertPolicyFilter;
import com.centurylink.cloud.sdk.policy.services.dsl.domain.refs.AlertPolicy;
import com.centurylink.cloud.sdk.tests.recorded.WireMockFileSource;
import com.centurylink.cloud.sdk.tests.recorded.WireMockMixin;
import com.google.inject.Inject;
import org.testng.annotations.AfterClass;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.List;

import static com.centurylink.cloud.sdk.tests.TestGroups.RECORDED;

/**
 * @author Aliaksandr Krasitski
 */
@Test(groups = RECORDED)
public class ModifyPolicyTest extends AbstractPoliciesSdkTest implements WireMockMixin {
    @Inject
    PolicyService policyService;

    AlertPolicy policy;
    AlertPolicyFilter filter;


    private String newPolicyName = "New Policy Name";
    private String recipient = "test@test.com";
    private String[] newRecipients = new String[]{"test1@test.com", "test2@test.com"};
    private AlertTriggerMetric metric = AlertTriggerMetric.MEMORY;

    @Test
    @WireMockFileSource("create")
    public void createPolicy() {
        policy =
            policyService.alert().create(
                new AlertPolicyConfig()
                    .name("My policy")
                    .action(new AlertAction()
                        .settings(new ActionSettingsEmail(recipient)))
                    .triggers(
                        new AlertTrigger().duration(5).metric(metric).threshold(99.0f)
                    )
                )
                .waitUntilComplete()
                .getResult();

        filter = policy.asFilter();
    }

    @Test
    @WireMockFileSource("update-name")
    public void testModifyName() throws Exception {
        policyService.alert()
            .modify(filter,
                new AlertPolicyConfig()
                    .name(newPolicyName))
            .waitUntilComplete()
            .getResult();
    }

    @Test
    @WireMockFileSource("update-name-assert")
    public void testModifyNameAssert() throws Exception {
        AlertPolicyMetadata metadata = policyService.alert().findByRef(policy);

        assertEquals(metadata.getName(), newPolicyName);

        AlertActionMetadata action = metadata.getActions().get(0);
        assertEquals(action.getAction(), "email");
        assertEquals(((ActionSettingsEmailMetadata)action.getSettings()).getRecipients().get(0), recipient);

        List<AlertTriggerMetadata> triggers = metadata.getTriggers();

        assertEquals(triggers.size(), 1);
        assertEquals(triggers.get(0).getDuration(), "00:05:00");
        assertEquals(triggers.get(0).getThreshold(), 95.0f);
    }

    @Test
    @WireMockFileSource("update-fields")
    public void testModifyOtherProperties() throws Exception {
        policyService.alert()
            .modify(filter,
                new AlertPolicyConfig()
                    .action(new AlertAction().settings(new ActionSettingsEmail(newRecipients)))
                    .trigger(new AlertTrigger().duration(10).metric(AlertTriggerMetric.DISK).threshold(40f))
            )
            .waitUntilComplete()
            .getResult();
    }

    @Test
    @WireMockFileSource("update-fields-assert")
    public void testModifyOtherPropertiesAssert() throws Exception {
        AlertPolicyMetadata metadata = policyService.alert().findByRef(policy);

        assertEquals(metadata.getName(), newPolicyName);

        AlertActionMetadata action = metadata.getActions().get(0);
        assertEquals(action.getAction(), "email");
        assertEquals(((ActionSettingsEmailMetadata)action.getSettings()).getRecipients(), Arrays.asList(newRecipients));

        List<AlertTriggerMetadata> triggers = metadata.getTriggers();

        assertEquals(triggers.size(), 1);
        assertEquals(triggers.get(0).getDuration(), "00:10:00");
        assertEquals(triggers.get(0).getThreshold(), 40.0f);
    }

    @AfterClass
    public void deletePolicy() {
        policyService.alert().delete(policy.asFilter());
    }
}
