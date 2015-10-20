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
import com.centurylink.cloud.sdk.policy.services.dsl.domain.refs.AntiAffinityPolicy;
import com.centurylink.cloud.sdk.server.services.AbstractServersSdkTest;
import com.centurylink.cloud.sdk.server.services.client.domain.server.metadata.ServerMetadata;
import com.centurylink.cloud.sdk.server.services.dsl.ServerService;
import com.centurylink.cloud.sdk.server.services.dsl.domain.group.refs.Group;
import com.centurylink.cloud.sdk.server.services.dsl.domain.server.Machine;
import com.centurylink.cloud.sdk.server.services.dsl.domain.server.ServerType;
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
import static com.centurylink.cloud.sdk.tests.TestGroups.RECORDED;
import static com.centurylink.cloud.sdk.core.util.Strings.isNullOrEmpty;

/**
 * @author Aliaksandr Krasitski
 */
@Test(groups = {RECORDED})
public class CreateWithAntiAffinityPolicyTest extends AbstractServersSdkTest implements WireMockMixin {

    @Inject
    ServerService serverService;

    ServerMetadata server;

    @Test
    @WireMockFileSource("anti-affinity")
    public void testCreateWithAntiAffinityPolicy() {
        server =
            serverService.create(TestServerSupport.anyServerConfig()
                .name("AAP")
                .template(Template.refByOs()
                    .dataCenter(CA_TORONTO_2)
                    .type(OsType.RHEL)
                    .edition("6")
                    .architecture(CpuArchitecture.x86_64)
                )
                .type(ServerType.HYPERSCALE)

                .machine(new Machine()
                    .cpuCount(1)
                    .ram(2)
                    .antiAffinityPolicy(AntiAffinityPolicy.refByName()
                        .name("Policy CA3")
                        .dataCenter(CA_TORONTO_2)
                    )
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

        assertEquals(metadata.getDetails().getAntiAffinityPolicy().getName(), "Policy CA3");
    }

    @AfterMethod
    public void deleteServer() {
        serverService.delete(server.asRefById());
    }

}
