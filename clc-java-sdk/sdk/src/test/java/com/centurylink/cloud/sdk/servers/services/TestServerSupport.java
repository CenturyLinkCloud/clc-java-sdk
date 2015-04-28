package com.centurylink.cloud.sdk.servers.services;

import com.centurylink.cloud.sdk.core.commons.services.domain.datacenters.refs.DataCenter;
import com.centurylink.cloud.sdk.servers.client.domain.server.metadata.ServerMetadata;
import com.centurylink.cloud.sdk.servers.services.domain.group.refs.Group;
import com.centurylink.cloud.sdk.servers.services.domain.server.CreateServerConfig;
import com.centurylink.cloud.sdk.servers.services.domain.server.Machine;
import com.centurylink.cloud.sdk.servers.services.domain.server.refs.Server;
import com.centurylink.cloud.sdk.servers.services.domain.template.Template;

import static com.centurylink.cloud.sdk.core.commons.services.domain.datacenters.refs.DataCenter.DE_FRANKFURT;
import static com.centurylink.cloud.sdk.core.commons.services.domain.datacenters.refs.DataCenter.US_CENTRAL_SALT_LAKE_CITY;
import static com.centurylink.cloud.sdk.servers.services.domain.group.refs.Group.DEFAULT_GROUP;
import static com.centurylink.cloud.sdk.servers.services.domain.template.filters.os.CpuArchitecture.x86_64;
import static com.centurylink.cloud.sdk.servers.services.domain.template.filters.os.OsType.CENTOS;
import static com.centurylink.cloud.sdk.servers.services.domain.server.ServerType.STANDARD;

/**
 * @author ilya.drabenia
 */
public class TestServerSupport {
    public static final String PASSWORD = "1qa@WS3ed";
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
                .architecture(x86_64)
            );
    }

    public ServerMetadata createAnyServer() {
        return
            serverService.create(new CreateServerConfig()
                .name("ALTRS1")
                .type(STANDARD)
                .password(PASSWORD)

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
                    .architecture(x86_64)
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
