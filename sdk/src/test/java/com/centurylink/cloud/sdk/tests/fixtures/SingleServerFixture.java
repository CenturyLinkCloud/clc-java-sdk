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

package com.centurylink.cloud.sdk.tests.fixtures;

import com.centurylink.cloud.sdk.ClcSdk;
import com.centurylink.cloud.sdk.base.services.dsl.domain.datacenters.refs.DataCenter;
import com.centurylink.cloud.sdk.server.services.client.domain.server.metadata.ServerMetadata;
import com.centurylink.cloud.sdk.server.services.dsl.ServerService;
import com.centurylink.cloud.sdk.server.services.dsl.domain.group.refs.Group;
import com.centurylink.cloud.sdk.server.services.dsl.domain.server.CreateServerConfig;
import com.centurylink.cloud.sdk.server.services.dsl.domain.server.DiskConfig;
import com.centurylink.cloud.sdk.server.services.dsl.domain.server.DiskType;
import com.centurylink.cloud.sdk.server.services.dsl.domain.server.Machine;
import com.centurylink.cloud.sdk.server.services.dsl.domain.server.NetworkConfig;
import com.centurylink.cloud.sdk.server.services.dsl.domain.server.refs.Server;
import com.centurylink.cloud.sdk.server.services.dsl.domain.template.refs.Template;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import java.time.ZonedDateTime;

import static com.centurylink.cloud.sdk.server.services.dsl.domain.group.refs.Group.DEFAULT_GROUP;
import static com.centurylink.cloud.sdk.server.services.dsl.domain.server.ServerType.STANDARD;
import static com.centurylink.cloud.sdk.server.services.dsl.domain.template.filters.os.CpuArchitecture.x86_64;
import static com.centurylink.cloud.sdk.server.services.dsl.domain.template.filters.os.OsType.CENTOS;
import static com.centurylink.cloud.sdk.tests.TestGroups.INTEGRATION;
import static com.centurylink.cloud.sdk.tests.TestGroups.LONG_RUNNING;
import static org.testng.Assert.assertEquals;

/**
 * @author Ilya Drabenia
 */
@Test(groups = {INTEGRATION, LONG_RUNNING})
public class SingleServerFixture {
    private static volatile SingleServerFixture instance;

    private final ServerService serverService = new ClcSdk().serverService();
    private volatile Server server;

    public static Server server() {
        return instance.getServer();
    }

    private Server getServer() {
        assertThatServerProperlyStarted(loadMetadataOf(server));

        return server;
    }

    @BeforeSuite(groups = {LONG_RUNNING})
    public void createServer() {
        instance = this;

        server =
            serverService
                .create(new CreateServerConfig()
                    .name("TCRT")
                    .type(STANDARD)
                    .group(Group.refByName()
                        .name(DEFAULT_GROUP)
                        .dataCenter(DataCenter.refByName("FranKfUrt"))
                    )
                    .timeToLive(ZonedDateTime.now().plusDays(1))
                    .machine(new Machine()
                        .cpuCount(1)
                        .ram(3)
                        .disk(new DiskConfig()
                            .type(DiskType.RAW)
                            .size(14)
                        )
                    )
                    .template(Template.refByOs()
                        .dataCenter(DataCenter.DE_FRANKFURT)
                        .type(CENTOS)
                        .version("6")
                        .architecture(x86_64)
                    )
                    .network(new NetworkConfig()
                        .primaryDns("172.17.1.26")
                        .secondaryDns("172.17.1.27")
                    )
                )
                .waitUntilComplete()
                .getResult();

        assertThatServerProperlyStarted(
            loadMetadataOf(server)
        );
    }

    @AfterSuite(groups = {LONG_RUNNING})
    public void deleteServer() {
        ServerMetadata serverStateBeforeDelete = loadMetadataOf(server);

        serverService.delete(server);

        assertThatServerProperlyStarted(serverStateBeforeDelete);
    }

    private ServerMetadata loadMetadataOf(Server curServer) {
        return serverService.findByRef(curServer);
    }

    @Test(enabled = false) // it's assert
    public void assertThatServerProperlyStarted(ServerMetadata metadata) {
        assertEquals(metadata.getLocationId().toUpperCase(), "DE1");

        if (metadata.getDetails() != null) {
            assertEquals(metadata.getDetails().getPowerState(), "started");
        }

        assertEquals(metadata.getStatus(), "active");
    }

}
