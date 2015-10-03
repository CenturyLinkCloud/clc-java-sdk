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
import com.centurylink.cloud.sdk.server.services.AbstractServersSdkTest;
import com.centurylink.cloud.sdk.server.services.client.domain.server.metadata.ServerMetadata;
import com.centurylink.cloud.sdk.server.services.dsl.ServerService;
import com.centurylink.cloud.sdk.server.services.dsl.domain.group.refs.Group;
import com.centurylink.cloud.sdk.server.services.dsl.domain.template.filters.os.CpuArchitecture;
import com.centurylink.cloud.sdk.server.services.dsl.domain.template.filters.os.OsType;
import com.centurylink.cloud.sdk.server.services.dsl.domain.template.refs.Template;
import com.centurylink.cloud.sdk.server.services.dsl.servers.TestServerSupport;

import static com.centurylink.cloud.sdk.base.services.dsl.domain.datacenters.refs.DataCenter.US_EAST_STERLING;
import static com.google.common.base.Strings.isNullOrEmpty;

/**
 * @author Ilya Drabenia
 */
//@Test(groups = {INTEGRATION, LONG_RUNNING})
public class CreateWithManagedOsTest extends AbstractServersSdkTest {

    @Inject
    ServerService serverService;

    ServerMetadata server;

//    @Test
    public void testCreateWithManagedOS() {
        server =
            serverService.create(TestServerSupport.anyServerConfig()
                .name("CMOS")
                .template(Template.refByOs()
                    .dataCenter(US_EAST_STERLING)
                    .type(OsType.RHEL)
                    .edition("6")
                    .architecture(CpuArchitecture.x86_64)
                )
                .managedOs()
                .group(Group.refByName()
                    .name(Group.DEFAULT_GROUP)
                    .dataCenter(US_EAST_STERLING)
                )
            )
            .waitUntilComplete()
            .getResult();

        assert !isNullOrEmpty(server.getId());
    }

//    @AfterMethod
    public void deleteServer() {
        serverService.delete(server.asRefById());
    }

}
