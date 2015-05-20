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

package com.centurylink.cloud.sdk.servers.services.servers.create;

import com.centurylink.cloud.sdk.servers.AbstractServersSdkTest;
import com.centurylink.cloud.sdk.servers.client.domain.server.metadata.ServerMetadata;
import com.centurylink.cloud.sdk.servers.services.ServerService;
import com.centurylink.cloud.sdk.servers.services.domain.group.refs.Group;
import com.centurylink.cloud.sdk.servers.services.domain.template.refs.Template;
import com.google.inject.Inject;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

import static com.centurylink.cloud.sdk.common.management.services.domain.datacenters.refs.DataCenter.US_EAST_STERLING;
import static com.centurylink.cloud.sdk.servers.services.domain.group.refs.Group.DEFAULT_GROUP;
import static com.centurylink.cloud.sdk.servers.services.domain.template.filters.os.CpuArchitecture.x86_64;
import static com.centurylink.cloud.sdk.servers.services.domain.template.filters.os.OsType.RHEL;
import static com.centurylink.cloud.sdk.servers.services.servers.TestServerSupport.anyServerConfig;
import static com.centurylink.cloud.sdk.tests.TestGroups.INTEGRATION;
import static com.centurylink.cloud.sdk.tests.TestGroups.LONG_RUNNING;
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
            serverService.create(anyServerConfig()
                .name("CMOS")
                .template(Template.refByOs()
                    .dataCenter(US_EAST_STERLING)
                    .type(RHEL)
                    .edition("6")
                    .architecture(x86_64)
                )
                .managedOs()
                .group(Group.refByName()
                    .name(DEFAULT_GROUP)
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
