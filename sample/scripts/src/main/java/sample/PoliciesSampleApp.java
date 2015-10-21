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

package sample;

import com.centurylink.cloud.sdk.ClcSdk;
import com.centurylink.cloud.sdk.base.services.dsl.domain.datacenters.refs.DataCenter;
import com.centurylink.cloud.sdk.core.auth.services.domain.credentials.DefaultCredentialsProvider;
import com.centurylink.cloud.sdk.policy.services.client.domain.ActionSettingsEmailMetadata;
import com.centurylink.cloud.sdk.policy.services.client.domain.AlertPolicyMetadata;
import com.centurylink.cloud.sdk.policy.services.client.domain.AntiAffinityPolicyMetadata;
import com.centurylink.cloud.sdk.policy.services.dsl.PolicyService;
import com.centurylink.cloud.sdk.policy.services.dsl.domain.ActionSettingsEmail;
import com.centurylink.cloud.sdk.policy.services.dsl.domain.AlertAction;
import com.centurylink.cloud.sdk.policy.services.dsl.domain.AlertPolicyConfig;
import com.centurylink.cloud.sdk.policy.services.dsl.domain.AlertTrigger;
import com.centurylink.cloud.sdk.policy.services.dsl.domain.AlertTriggerMetric;
import com.centurylink.cloud.sdk.policy.services.dsl.domain.AntiAffinityPolicyConfig;
import com.centurylink.cloud.sdk.policy.services.dsl.domain.filters.AlertPolicyFilter;
import com.centurylink.cloud.sdk.policy.services.dsl.domain.filters.AntiAffinityPolicyFilter;
import com.centurylink.cloud.sdk.policy.services.dsl.domain.refs.AlertPolicy;
import com.centurylink.cloud.sdk.policy.services.dsl.domain.refs.AntiAffinityPolicy;
import com.centurylink.cloud.sdk.server.services.client.domain.server.metadata.ServerMetadata;
import com.centurylink.cloud.sdk.server.services.dsl.ServerService;
import com.centurylink.cloud.sdk.server.services.dsl.domain.group.refs.Group;
import com.centurylink.cloud.sdk.server.services.dsl.domain.server.CreateServerConfig;
import com.centurylink.cloud.sdk.server.services.dsl.domain.server.Machine;
import com.centurylink.cloud.sdk.server.services.dsl.domain.server.filters.ServerFilter;
import com.centurylink.cloud.sdk.server.services.dsl.domain.server.refs.Server;
import com.centurylink.cloud.sdk.server.services.dsl.domain.template.refs.Template;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.List;

import static com.centurylink.cloud.sdk.server.services.dsl.domain.server.ServerType.HYPERSCALE;
import static com.centurylink.cloud.sdk.server.services.dsl.domain.template.filters.os.CpuArchitecture.x86_64;
import static com.centurylink.cloud.sdk.server.services.dsl.domain.template.filters.os.OsType.CENTOS;
import static sample.SamplesTestsConstants.SAMPLES;

public class PoliciesSampleApp extends Assert {

    private ServerService serverService;
    private PolicyService policyService;
    private Server server;
    private AntiAffinityPolicy antiAffinityPolicy;
    private AlertPolicy alertPolicy;

    private String antiAffinityPolicyName = "Sample New York Anti-Affinity Policy";
    private String newAntiAffinityPolicyName = "Sample New York Anti-Affinity Policy New";
    private String[] newRecipients = new String[]{"sampleuser1@samplecompany.com", "sampleuser2@samplecompany.com"};

    private int newDuration = 10;
    private float newThreshold = 95f;

    public PoliciesSampleApp() {
        ClcSdk sdk = new ClcSdk(
            new DefaultCredentialsProvider()
        );

        serverService = sdk.serverService();
        policyService = sdk.policyService();
    }

    @BeforeClass(groups = {SAMPLES})
    public void init() {
        clearAll();

        int initDuration = 5;
        float initThreshold = 52f;

        alertPolicy = policyService.alert().create(
            new AlertPolicyConfig()
                .name("Sample Alert Policy")
                .action(new AlertAction()
                    .settings(new ActionSettingsEmail("sampleuser@samplecompany.com")))
                .triggers(
                    new AlertTrigger().duration(initDuration).metric(AlertTriggerMetric.CPU).threshold(initThreshold)
                )
        ).waitUntilComplete().getResult();

        antiAffinityPolicy = policyService.antiAffinity().create(
            new AntiAffinityPolicyConfig()
                .dataCenter(DataCenter.US_EAST_NEW_YORK)
                .name(antiAffinityPolicyName)
        ).waitUntilComplete().getResult();

        server = serverService.create(new CreateServerConfig()
            .name("plc")
            .group(Group.refByName(DataCenter.US_EAST_NEW_YORK, Group.DEFAULT_GROUP))
            .description("Sample server for policies")
            .type(HYPERSCALE)
            .machine(new Machine()
                .cpuCount(1)
                .ram(1)
                .antiAffinityPolicy(antiAffinityPolicy)
            )
            .template(Template.refByOs()
                .dataCenter(DataCenter.US_EAST_NEW_YORK)
                .type(CENTOS)
                .version("6")
                .architecture(x86_64)
            )
            .timeToLive(
                ZonedDateTime.now().plusHours(2)
            )
        )
        .waitUntilComplete().getResult();
    }

    @AfterClass(groups = {SAMPLES})
    public void deletePolicies() {
        clearAll();
    }

    private void clearAll() {
        policyService.alert().delete(new AlertPolicyFilter()).waitUntilComplete();
        policyService.antiAffinity().delete(new AntiAffinityPolicyFilter()).waitUntilComplete();

        serverService.delete(new ServerFilter().dataCenters(DataCenter.US_EAST_NEW_YORK)).waitUntilComplete();
    }


    @Test(groups = {SAMPLES})
    public void checkThatServerCreatedWithPolicy() {
        assertAntiAffinityPolicy(antiAffinityPolicyName);
    }

    @Test(groups = {SAMPLES})
    public void modifyPolicies() {
        policyService.alert().modify(alertPolicy,
            new AlertPolicyConfig()
                .action(new AlertAction()
                    .settings(new ActionSettingsEmail(newRecipients)))
                .trigger(
                    new AlertTrigger().duration(newDuration).metric(AlertTriggerMetric.MEMORY).threshold(newThreshold)
                )
        ).waitUntilComplete();

        policyService.antiAffinity().modify(antiAffinityPolicy,
            new AntiAffinityPolicyConfig()
                .name(newAntiAffinityPolicyName)
        ).waitUntilComplete();
    }

    @Test(groups = {SAMPLES})
    public void modifyPoliciesCheck() {
        assertAntiAffinityPolicy(newAntiAffinityPolicyName);

        AlertPolicyMetadata alertPolicyMetadata = policyService.alert().findByRef(alertPolicy);
        List<String> recipients = ((ActionSettingsEmailMetadata)alertPolicyMetadata.getActions().get(0).getSettings())
            .getRecipients();

        assertEquals(
            recipients,
            Arrays.asList(newRecipients)
        );

        assertEquals(
            alertPolicyMetadata.getTriggers().get(0).getThreshold(),
            newThreshold
        );

        assertEquals(
            alertPolicyMetadata.getTriggers().get(0).getDuration(),
            "00:"+ newDuration + ":00"
        );

        assertEquals(
            alertPolicyMetadata.getTriggers().get(0).getMetric(),
            AlertTriggerMetric.MEMORY.name().toLowerCase()
        );
    }

    private void assertAntiAffinityPolicy(String policyName) {
        ServerMetadata serverMetadata = serverService.findByRef(server);

        AntiAffinityPolicyMetadata serverPolicyMetadata = serverMetadata.getDetails().getAntiAffinityPolicy();

        AntiAffinityPolicyMetadata policyMetadata = policyService.antiAffinity().findByRef(antiAffinityPolicy);

        assert serverPolicyMetadata.getName().equals(policyMetadata.getName());
        assert serverPolicyMetadata.getId().equals(policyMetadata.getId());
        assert serverMetadata.getLocationId().equals(policyMetadata.getLocation());

        assert serverPolicyMetadata.getName().equals(policyName);
    }
}
