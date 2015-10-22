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

package com.centurylink.cloud.sdk.server.services.dsl.servers.create;

import com.centurylink.cloud.sdk.core.injector.Inject;
import com.centurylink.cloud.sdk.policy.services.dsl.AutoscalePolicyService;
import com.centurylink.cloud.sdk.policy.services.client.domain.autoscale.AutoscalePolicyMetadata;
import com.centurylink.cloud.sdk.policy.services.dsl.domain.autoscale.refs.AutoscalePolicy;
import com.centurylink.cloud.sdk.server.services.AbstractServersSdkTest;
import com.centurylink.cloud.sdk.server.services.client.domain.server.metadata.ServerMetadata;
import com.centurylink.cloud.sdk.server.services.dsl.ServerService;
import com.centurylink.cloud.sdk.server.services.dsl.domain.group.refs.Group;
import com.centurylink.cloud.sdk.server.services.dsl.domain.server.Machine;
import com.centurylink.cloud.sdk.server.services.dsl.domain.server.refs.Server;
import com.centurylink.cloud.sdk.server.services.dsl.domain.template.filters.os.CpuArchitecture;
import com.centurylink.cloud.sdk.server.services.dsl.domain.template.filters.os.OsType;
import com.centurylink.cloud.sdk.server.services.dsl.domain.template.refs.Template;
import com.centurylink.cloud.sdk.server.services.dsl.servers.TestServerSupport;
import com.centurylink.cloud.sdk.tests.recorded.WireMockFileSource;
import com.centurylink.cloud.sdk.tests.recorded.WireMockMixin;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

import static com.centurylink.cloud.sdk.base.services.dsl.domain.datacenters.refs.DataCenter.CA_TORONTO_2;
import static com.centurylink.cloud.sdk.core.util.Strings.isNullOrEmpty;
import static com.centurylink.cloud.sdk.tests.TestGroups.RECORDED;

/**
* @author Aliaksandr Krasitski
*/
@Test(groups = {RECORDED})
public class CreateWithAutoscalePolicyTest extends AbstractServersSdkTest implements WireMockMixin {

    @Inject
    AutoscalePolicyService autoscalePolicyService;

    @Inject
    ServerService serverService;

    ServerMetadata server;

    @Test
    @WireMockFileSource("autoscale")
    public void testCreateWithAutoscalePolicy() {
        String policyName = "My Policy";

        server =
            serverService.create(TestServerSupport.anyServerConfig()
                .name("ASP")
                .template(Template.refByOs()
                    .dataCenter(CA_TORONTO_2)
                    .type(OsType.RHEL)
                    .edition("6")
                    .architecture(CpuArchitecture.x86_64)
                )

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

        assert !isNullOrEmpty(server.getId());

        ServerMetadata metadata = serverService.findByRef(Server.refById(server.getId()));
        assert metadata.getDetails().getAutoscalePolicy() != null;

        AutoscalePolicyMetadata autoscalePolicy =
            autoscalePolicyService.getAutoscalePolicyOnServer(server.asRefById());
        assert autoscalePolicy != null;

        assertEquals(metadata.getDetails().getAutoscalePolicy().getId(), autoscalePolicy.getId());

        assertEquals(autoscalePolicy.getName(), policyName);
    }

    @AfterMethod
    public void deleteServer() {
        serverService.delete(server.asRefById());
    }

}
