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

package com.centurylink.cloud.sdk.server.services.dsl.servers;

import com.centurylink.cloud.sdk.base.services.dsl.domain.datacenters.refs.DataCenter;
import com.centurylink.cloud.sdk.server.services.client.domain.server.metadata.ServerMetadata;
import com.centurylink.cloud.sdk.server.services.dsl.ServerService;
import com.centurylink.cloud.sdk.server.services.dsl.domain.group.refs.Group;
import com.centurylink.cloud.sdk.server.services.dsl.domain.server.CloneServerConfig;
import com.centurylink.cloud.sdk.server.services.dsl.domain.server.CreateServerConfig;
import com.centurylink.cloud.sdk.server.services.dsl.domain.server.Machine;
import com.centurylink.cloud.sdk.server.services.dsl.domain.server.refs.Server;
import com.centurylink.cloud.sdk.server.services.dsl.domain.template.filters.os.CpuArchitecture;
import com.centurylink.cloud.sdk.server.services.dsl.domain.template.refs.Template;

import static com.centurylink.cloud.sdk.base.services.dsl.domain.datacenters.refs.DataCenter.DE_FRANKFURT;
import static com.centurylink.cloud.sdk.base.services.dsl.domain.datacenters.refs.DataCenter.US_CENTRAL_SALT_LAKE_CITY;
import static com.centurylink.cloud.sdk.server.services.dsl.domain.group.refs.Group.DEFAULT_GROUP;
import static com.centurylink.cloud.sdk.server.services.dsl.domain.server.ServerType.STANDARD;
import static com.centurylink.cloud.sdk.server.services.dsl.domain.template.filters.os.OsType.CENTOS;

/**
 * @author ilya.drabenia
 */
public class TestServerSupport {

    public static final String PASSWD = "1qa@WS3ed";
    private final ServerService serverService;

    public TestServerSupport(ServerService serverService) {
        this.serverService = serverService;
    }

    public static CreateServerConfig anyServerConfig() {
        return new CreateServerConfig()
            .name("ALTRS1")
            .type(STANDARD)

            .group(Group.refByName()
                .name(DEFAULT_GROUP)
                .dataCenter(DE_FRANKFURT)
            )

            .machine(new Machine()
                .cpuCount(1)
                .ram(2)
            )

            .template(Template.refByOs()
                .dataCenter(US_CENTRAL_SALT_LAKE_CITY)
                .type(CENTOS)
                .version("6")
                .architecture(CpuArchitecture.x86_64)
            );
    }

    public static CloneServerConfig getCloneServerConfig(Server server) {
        CloneServerConfig config = (CloneServerConfig) new CloneServerConfig()
            .name("CLNSV")
            .type(STANDARD)
            .group(Group.refByName()
                    .name(DEFAULT_GROUP)
                    .dataCenter(DE_FRANKFURT)
            )
            .machine(
                new Machine()
                    .cpuCount(1)
                    .ram(2)
            );

        config.setServer(server);

        return config;
    }

    public ServerMetadata createAnyServer() {
        return
            serverService.create(new CreateServerConfig()
                .name("ALTRS1")
                .type(STANDARD)
                .password(PASSWD)

                .group(Group.refByName()
                    .dataCenter(DataCenter.refById(DE_FRANKFURT.getId()))
                    .name(DEFAULT_GROUP)
                )

                .machine(new Machine()
                    .cpuCount(1)
                    .ram(2)
                )

                .template(Template.refByOs()
                    .dataCenter(DE_FRANKFURT)
                    .type(CENTOS)
                    .version("6")
                    .architecture(CpuArchitecture.x86_64)
                )
            )

            .waitUntilComplete()
            .getResult();
    }

    public void deleteServer(Server newServer) {
        serverService
            .delete(newServer)
            .waitUntilComplete();
    }
}
