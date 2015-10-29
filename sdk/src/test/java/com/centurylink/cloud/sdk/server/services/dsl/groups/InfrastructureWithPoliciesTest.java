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

package com.centurylink.cloud.sdk.server.services.dsl.groups;

import com.centurylink.cloud.sdk.base.services.dsl.domain.datacenters.refs.DataCenter;
import com.centurylink.cloud.sdk.core.injector.Inject;
import com.centurylink.cloud.sdk.policy.services.dsl.domain.ActionSettingsEmail;
import com.centurylink.cloud.sdk.policy.services.dsl.domain.AlertAction;
import com.centurylink.cloud.sdk.policy.services.dsl.domain.AlertPolicyConfig;
import com.centurylink.cloud.sdk.policy.services.dsl.domain.AlertTrigger;
import com.centurylink.cloud.sdk.policy.services.dsl.domain.AlertTriggerMetric;
import com.centurylink.cloud.sdk.policy.services.dsl.domain.AntiAffinityPolicyConfig;
import com.centurylink.cloud.sdk.policy.services.dsl.domain.refs.AntiAffinityPolicy;
import com.centurylink.cloud.sdk.server.services.AbstractServersSdkTest;
import com.centurylink.cloud.sdk.server.services.client.domain.group.GroupMetadata;
import com.centurylink.cloud.sdk.server.services.client.domain.server.metadata.ServerMetadata;
import com.centurylink.cloud.sdk.server.services.dsl.GroupService;
import com.centurylink.cloud.sdk.server.services.dsl.ServerService;
import com.centurylink.cloud.sdk.server.services.dsl.domain.InfrastructureConfig;
import com.centurylink.cloud.sdk.server.services.dsl.domain.group.GroupHierarchyConfig;
import com.centurylink.cloud.sdk.server.services.dsl.domain.group.filters.GroupFilter;
import com.centurylink.cloud.sdk.server.services.dsl.domain.group.refs.Group;
import com.centurylink.cloud.sdk.server.services.dsl.domain.server.CreateServerConfig;
import com.centurylink.cloud.sdk.server.services.dsl.domain.server.Machine;
import com.centurylink.cloud.sdk.server.services.dsl.domain.server.ServerType;
import com.centurylink.cloud.sdk.server.services.dsl.domain.server.filters.ServerFilter;
import com.centurylink.cloud.sdk.server.services.dsl.domain.template.filters.os.CpuArchitecture;
import com.centurylink.cloud.sdk.server.services.dsl.domain.template.filters.os.OsType;
import com.centurylink.cloud.sdk.server.services.dsl.domain.template.refs.Template;
import com.centurylink.cloud.sdk.tests.recorded.WireMockFileSource;
import com.centurylink.cloud.sdk.tests.recorded.WireMockMixin;
import org.testng.annotations.Test;

import java.time.ZonedDateTime;
import java.util.List;

import static com.centurylink.cloud.sdk.base.services.dsl.domain.datacenters.refs.DataCenter.US_EAST_STERLING;
import static com.centurylink.cloud.sdk.server.services.dsl.domain.group.GroupHierarchyConfig.group;
import static com.centurylink.cloud.sdk.tests.TestGroups.RECORDED;

/**
 * @author Aliaksandr Krasitski
 */
@Test(groups = {RECORDED})
public class InfrastructureWithPoliciesTest extends AbstractServersSdkTest implements WireMockMixin {

    @Inject
    GroupService groupService;

    @Inject
    ServerService serverService;

    String antiAffinityPolicyName = "test anti-affinity";

    private String name(String value) {
        return value;
    }

    @Test
    @WireMockFileSource("infrastructure-policies")
    public void testInfrastructure() throws Exception {
        defineInfrastructure(
            initConfig(
                US_EAST_STERLING
            )
        );

        checkPolicies();
    }

    private CreateServerConfig buildServerConfig(String name, String antiAffinityPolicyName) {

        return new CreateServerConfig()
            .name(name)
            .description(name)
            .type(ServerType.HYPERSCALE)
            .machine(new Machine()
                .cpuCount(1)
                .ram(1)
                .antiAffinityPolicy(
                    AntiAffinityPolicy
                        .refByName()
                        .name(antiAffinityPolicyName)
                )
            )
            .template(Template.refByOs()
                .dataCenter(US_EAST_STERLING)
                .type(OsType.CENTOS)
                .version("6")
                .architecture(CpuArchitecture.x86_64)
            );
    }

    private InfrastructureConfig initConfig(DataCenter... dataCenters) {

        return new InfrastructureConfig()
            .dataCenters(dataCenters)
            .alertPolicies(
                new AlertPolicyConfig()
                    .name("db space policy")
                    .action(
                        new AlertAction()
                            .settings(
                                new ActionSettingsEmail("test-db@abc.com")
                        )
                    )
                    .trigger(
                        new AlertTrigger()
                            .metric(AlertTriggerMetric.DISK)
                            .threshold(80f)
                            .duration(15)
                    ),
                new AlertPolicyConfig()
                    .name("cfg policy")
                    .action(
                        new AlertAction().settings(
                            new ActionSettingsEmail("test-cfg@abc.com")
                        )
                    )
                    .trigger(
                        new AlertTrigger()
                            .metric(AlertTriggerMetric.CPU)
                            .threshold(75f)
                            .duration(5)
                    )
            )
            .antiAffinityPolicies(
                new AntiAffinityPolicyConfig()
                    .name(antiAffinityPolicyName)
            )
            .subitems(new GroupHierarchyConfig()
                .name(name("Application3"))
                .subitems(
                    group(name("Config")).subitems(
                        buildServerConfig("cfg1", antiAffinityPolicyName),
                        buildServerConfig("cfg2", antiAffinityPolicyName),
                        buildServerConfig("cfg3", antiAffinityPolicyName)
                    ),
                    group(name("Replica")).subitems(
                        buildServerConfig("rs1", antiAffinityPolicyName),
                        buildServerConfig("rs2", antiAffinityPolicyName),
                        buildServerConfig("rs3", antiAffinityPolicyName)
                    )
                ));
    }

    private List<GroupMetadata> defineInfrastructure(InfrastructureConfig... config) {
        List<Group> groups = groupService
            .defineInfrastructure(config)
            .waitUntilComplete()
            .getResult();

        return groupService.find(
            new GroupFilter().groups(
                groups.toArray(
                    new Group[groups.size()]
                )
            )
        );
    }

    private void checkPolicies() {
        List<ServerMetadata> servers = serverService.find(
            new ServerFilter()
                .groupNames("Application3")
                .dataCenters(US_EAST_STERLING)
                .searchInSubGroups(true)
        );

        servers.forEach(
            server -> assertEquals(
                server.getDetails().getAntiAffinityPolicy().getName(),
                antiAffinityPolicyName
            )
        );
    }

}
