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

package com.centurylink.cloud.sdk.server.services.dsl.servers.modify;

import com.centurylink.cloud.sdk.core.injector.Inject;
import com.centurylink.cloud.sdk.policy.services.dsl.AutoscalePolicyService;
import com.centurylink.cloud.sdk.policy.services.dsl.domain.autoscale.AutoscalePolicyMetadata;
import com.centurylink.cloud.sdk.policy.services.dsl.domain.autoscale.refs.AutoscalePolicy;
import com.centurylink.cloud.sdk.server.services.AbstractServersSdkTest;
import com.centurylink.cloud.sdk.server.services.client.domain.server.metadata.ServerMetadata;
import com.centurylink.cloud.sdk.server.services.dsl.ServerService;
import com.centurylink.cloud.sdk.server.services.dsl.domain.group.refs.Group;
import com.centurylink.cloud.sdk.server.services.dsl.domain.server.Machine;
import com.centurylink.cloud.sdk.server.services.dsl.domain.server.ModifyServerConfig;
import com.centurylink.cloud.sdk.server.services.dsl.domain.server.refs.Server;
import com.centurylink.cloud.sdk.server.services.dsl.servers.TestServerSupport;
import com.centurylink.cloud.sdk.tests.recorded.WireMockFileSource;
import com.centurylink.cloud.sdk.tests.recorded.WireMockMixin;
import org.testng.annotations.Test;

import static com.centurylink.cloud.sdk.base.services.dsl.domain.datacenters.refs.DataCenter.CA_TORONTO_2;
import static com.centurylink.cloud.sdk.tests.TestGroups.RECORDED;

/**
* @author Aliaksandr Krasitski
*/
@Test(groups = {RECORDED})
public class ModifyWithAutoscalePolicyTest extends AbstractServersSdkTest implements WireMockMixin {

    @Inject
    AutoscalePolicyService autoscalePolicyService;

    @Inject
    ServerService serverService;

    ServerMetadata server;

    @Test
    @WireMockFileSource("autoscale/create")
    public void testCreateServer() {
        String policyName = "Autoscale Policy 1";
        server =
            serverService.create(TestServerSupport.anyServerConfig()
                .name("ASP")
                .machine(new Machine()
                    .cpuCount(1)
                    .ram(2)
                    .autoscalePolicy(AutoscalePolicy.refByName(policyName))
                )
                .group(Group.refByName()
                    .name(Group.DEFAULT_GROUP)
                    .dataCenter(CA_TORONTO_2)
                )
            )
            .waitUntilComplete()
            .getResult();

        ServerMetadata metadata = serverService.findByRef(Server.refById(server.getId()));
        assert metadata.getDetails().getAutoscalePolicy() != null;
    }

    @Test
    @WireMockFileSource("autoscale/update")
    public void testModifyWithAutoscalePolicy() {
        String newPolicyName = "Autoscale Policy 2";

        serverService.modify(
            server.asRefById(),
            new ModifyServerConfig().machineConfig(
                new Machine()
                    .autoscalePolicy(AutoscalePolicy.refByName(newPolicyName))
                    .cpuCount(2)
            )
        )
        .waitUntilComplete();

        ServerMetadata metadata = serverService.findByRef(server.asRefById());
        assert metadata.getDetails().getAutoscalePolicy() != null;

        AutoscalePolicyMetadata autoscalePolicy = autoscalePolicyService.getAutoscalePolicyOnServer(server.asRefById());
        assert autoscalePolicy != null;

        assertEquals(metadata.getDetails().getAutoscalePolicy().getId(), autoscalePolicy.getId());

        assertEquals(autoscalePolicy.getName(), newPolicyName);
    }

}
